package org.dvhume.mclang.errors;

public final class Ansi {

    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";

    public static final String RED = "\033[31m";
    public static final String BLUE = "\033[34m";
    public static final String CYAN = "\033[36m";
    public static final String YELLOW = "\033[33m";

    public static final String RED_BOLD = "\033[31;1m";
    public static final String BLUE_BOLD = "\033[34;1m";
    public static final String CYAN_BOLD = "\033[36;1m";
    public static final String YELLOW_BOLD = "\033[33;1m";

    private static final boolean ANSI_SUPPORTED = checkAnsiSupport();

    private Ansi() {}

    public static boolean isSupported() {
        return ANSI_SUPPORTED;
    }

    private static boolean checkAnsiSupport() {
        String os = System.getProperty("os.name").toLowerCase();
        if (!os.contains("win")) {
            return true;
        }

        try {
            new ProcessBuilder("cmd", "/c", "echo " + RESET).inheritIO().start().waitFor();
            return true;
        } catch (Exception e) {
            String term = System.getenv("TERM");
            String wt = System.getenv("WT_SESSION");
            return (wt != null) || (term != null && !term.equals("dumb"));
        }
    }

    public static String colorize(String text, String ansiCode) {
        if (!ANSI_SUPPORTED || text == null || text.isEmpty()) {
            return text;
        }
        return ansiCode + text + RESET;
    }

    public static String red(String text) {
        return colorize(text, RED);
    }

    public static String redBold(String text) {
        return colorize(text, RED_BOLD);
    }

    public static String blueBold(String text) {
        return colorize(text, BLUE_BOLD);
    }

    public static String cyanBold(String text) {
        return colorize(text, CYAN_BOLD);
    }

    public static String yellowBold(String text) {
        return colorize(text, YELLOW_BOLD);
    }
}