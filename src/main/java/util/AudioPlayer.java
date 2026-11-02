package main.java.util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
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

    protected final Random RANDOM = new Random();
    protected final ExecutorService EXECUTOR;
    private final Map<String, List<String>> categories = new ConcurrentHashMap<>();
    private final Map<String, Clip> clipCache = new ConcurrentHashMap<>();

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
                getOrCreateClip(file);
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
                Clip clip = AudioSystem.getClip();
                clip.open(in);
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
