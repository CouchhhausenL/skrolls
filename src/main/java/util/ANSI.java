package main.java.util;

// Only put non-jline-supported codes here, otherwise use Terminal.puts(...)
public final class ANSI {
    private ANSI() {
    }

    // 24-bit RGB escapes; arguments are the red, green and blue channels
    public static final String SET_FOREGROUND = "\033[38;2;%d;%d;%dm";
    public static final String SET_BACKGROUND = "\033[48;2;%d;%d;%dm";
    // SGR extended mouse reporting (1006) gives coordinates larger than 223
    public static final String ENABLE_SGR = "\033[?1006h";
    public static final String DISABLE_SGR = "\033[?1006l";
}
