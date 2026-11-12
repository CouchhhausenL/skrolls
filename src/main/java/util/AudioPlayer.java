package main.java.util;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

/**
 * Shared audio engine used by both MusicPlayer and SFXPlayer.
 * Caches loaded clips in memory for instant, zero-latency playback.
 */
public abstract class AudioPlayer {

    public static final boolean IS_CHEERPJ = "true".equalsIgnoreCase(System.getProperty("cheerpj"))
            || (new java.io.File("/app").isDirectory() && new java.io.File("/files").isDirectory());

    // Native CheerpJ bridge methods implemented via JavaScript in index.html
    public static native void playWebAudio(String path, byte[] audioData, float volume, boolean loop);

    public static native void stopWebAudio(String path);

    public static native void setWebAudioVolume(float volume);

    protected final Random RANDOM = new Random();
    protected final ExecutorService EXECUTOR;
    private final Map<String, List<String>> categories = new ConcurrentHashMap<>();
    private final Map<String, Clip> clipCache = new ConcurrentHashMap<>();
    private final Map<String, byte[]> resourceDataCache = new ConcurrentHashMap<>();

    private Clip currentClip;
    private float volume;

    protected AudioPlayer(String threadName, float defaultVolume) {
        this.volume = defaultVolume;
        EXECUTOR = Executors.newSingleThreadExecutor(r -> {
            Thread t = new Thread(r, threadName);
            t.setDaemon(true);
            return t;
        });
    }

    // -------------------------------------------------------------------------
    // Resource indexing & playback helpers
    // -------------------------------------------------------------------------

    public List<String> getCategory(String folder) {
        return categories.computeIfAbsent(folder, this::loadCategory);
    }

    public void playRandom(String folder, boolean loop) {
        String path = getRandomFromList(getCategory(folder));
        if (path != null) {
            play(path, loop);
        }
    }

    public void playRandom(String folder) {
        playRandom(folder, false);
    }

    /**
     * Preloads all audio clips in a category into memory on a background thread
     * so subsequent plays are completely instant.
     */
    public void preloadCategory(String folder) {
        EXECUTOR.submit(() -> {
            for (String file : getCategory(folder)) {
                if (IS_CHEERPJ) {
                    getOrCreateResourceData(file);
                } else {
                    getOrCreateClip(file);
                }
            }
        });
    }

    private byte[] getOrCreateResourceData(String path) {
        return resourceDataCache.computeIfAbsent(path, p -> {
            try (var in = getClass().getResourceAsStream(p)) {
                if (in == null) {
                    System.err.println("Audio resource not found: " + p);
                    return null;
                }
                return in.readAllBytes();
            } catch (Exception e) {
                System.err.println("Failed to read audio resource " + p + ": " + e.getMessage());
                return null;
            }
        });
    }

    protected Clip getOrCreateClip(String path) {
        return clipCache.computeIfAbsent(path, p -> {
            try {
                URL resource = getClass().getResource(p);
                if (resource == null) {
                    System.err.println("Audio resource not found: " + p);
                    return null;
                }
                AudioInputStream in = AudioSystem.getAudioInputStream(resource);
                AudioFormat base = in.getFormat();
                AudioFormat target = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        44100.0f,
                        16,
                        2,
                        4,
                        44100.0f,
                        false
                );

                AudioInputStream stream1 = AudioSystem.getAudioInputStream(
                        new AudioFormat(
                                AudioFormat.Encoding.PCM_SIGNED,
                                base.getSampleRate(),
                                16,
                                base.getChannels(),
                                base.getChannels() * 2,
                                base.getSampleRate(),
                                false
                        ),
                        in
                );
                AudioInputStream pcmStream = AudioSystem.getAudioInputStream(target, stream1);
                DataLine.Info info = new DataLine.Info(Clip.class, target);

                Clip clip = null;
                if (AudioSystem.isLineSupported(info)) {
                    clip = (Clip) AudioSystem.getLine(info);
                } else {
                    for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
                        try {
                            Mixer mixer = AudioSystem.getMixer(mixerInfo);
                            if (mixer.isLineSupported(info)) {
                                clip = (Clip) mixer.getLine(info);
                                break;
                            }
                        } catch (Throwable ignored) {
                        }
                    }
                }
                if (clip == null) {
                    clip = AudioSystem.getClip();
                }

                clip.open(pcmStream);
                pcmStream.close();
                in.close();
                applyVolume(clip);
                return clip;
            } catch (Exception e) {
                System.err.println("Failed to load audio clip " + p + ": " + e.getMessage());
                return null;
            }
        });
    }

    protected List<String> loadCategory(String folder) {
        try {
            String path = folder.startsWith("/") ? folder : "/" + folder;
            String prefix = path.endsWith("/") ? path : path + "/";
            return listResources(path).stream()
                    .map(file -> prefix + file)
                    .toList();
        } catch (Exception e) {
            System.err.println("Failed to index audio folder " + folder + ": " + e.getMessage());
            return List.of();
        }
    }

    public List<String> listResources(String folder) throws IOException, URISyntaxException {
        URL url = getClass().getResource(folder);
        if (url == null) return List.of();

        URI uri = url.toURI();
        if ("jar".equals(uri.getScheme())) {
            try {
                return listWavs(FileSystems.getFileSystem(uri).getPath(folder));
            } catch (FileSystemNotFoundException e) {
                try (FileSystem fs = FileSystems.newFileSystem(uri, Map.of())) {
                    return listWavs(fs.getPath(folder));
                }
            }
        }
        return listWavs(Path.of(uri));
    }

    private List<String> listWavs(Path dir) throws IOException {
        try (Stream<Path> s = Files.list(dir)) {
            return s.map(p -> p.getFileName().toString())
                    .filter(n -> n.endsWith(".wav"))
                    .toList();
        }
    }

    protected String getRandomFromList(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        return list.get(RANDOM.nextInt(list.size()));
    }

    // -------------------------------------------------------------------------
    // Volume control
    // -------------------------------------------------------------------------

    public synchronized void setVolume(float newVolume) {
        volume = Math.clamp(newVolume, 0.0f, 1.0f);
        if (IS_CHEERPJ) {
            try {
                setWebAudioVolume(volume);
            } catch (Throwable ignored) {
            }
        }
        for (Clip clip : clipCache.values()) {
            applyVolume(clip);
        }
    }

    public synchronized void setVolume(int percentage) {
        setVolume(percentage / 100.0f);
    }

    public synchronized float getVolume() {
        return volume;
    }

    private void applyVolume(Clip clip) {
        if (clip == null || !clip.isOpen()) return;
        if (!clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) return;
        try {
            FloatControl gain = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = volume <= 0.0001f
                    ? gain.getMinimum()
                    : Math.clamp((float) (20.0 * Math.log10(volume)), gain.getMinimum(), gain.getMaximum());
            gain.setValue(dB);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------------------
    // Playback control
    // -------------------------------------------------------------------------

    public synchronized void stop() {
        if (IS_CHEERPJ) {
            try {
                stopWebAudio(null);
            } catch (Throwable ignored) {
            }
        }
        if (currentClip != null && currentClip.isRunning()) {
            currentClip.stop();
        }
        currentClip = null;
    }

    public synchronized boolean isPlaying() {
        return currentClip != null && currentClip.isRunning();
    }

    public void play(String path) {
        play(path, true);
    }

    public void play(String path, boolean loop) {
        if (path == null) return;

        if (IS_CHEERPJ) {
            EXECUTOR.submit(() -> {
                try {
                    byte[] data = getOrCreateResourceData(path);
                    if (data != null) {
                        playWebAudio(path, data, volume, loop);
                    }
                } catch (Throwable t) {
                    System.err.println("WebAudio bridge failed: " + t.getMessage());
                }
            });
            return;
        }

        Clip cached = clipCache.get(path);
        if (cached != null) {
            playClip(cached, loop);
            return;
        }

        EXECUTOR.submit(() -> {
            Clip clip = getOrCreateClip(path);
            if (clip != null) {
                playClip(clip, loop);
            }
        });
    }

    private synchronized void playClip(Clip clip, boolean loop) {
        if (loop) {
            if (currentClip != null && currentClip != clip && currentClip.isRunning()) {
                currentClip.stop();
            }
            currentClip = clip;
        }

        if (clip.isRunning()) {
            clip.stop();
        }
        applyVolume(clip);
        clip.setFramePosition(0);

        if (loop) {
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
            clip.start();
        }
    }
}
