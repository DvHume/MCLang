package org.dvhume.mclang.errors;

public class Ansi {

    public static final String RESET = "\033[0m";
    public static final String BOLD = "\033[1m";

    public static final String RED = "\033[31m";
    public static final String BLUE = "\033[34m";
    public static final String CYAN = "\033[36m";

    public static final String RED_BOLD = "\033[31;1m";
    public static final String BLUE_BOLD = "\033[34;1m";
    public static final String CYAN_BOLD = "\033[35;1m";

    public static String colorize(String text, String ansiCode, boolean enabled) {
        if (!enabled || text == null || text.isEmpty()) {
            return text;
        }
        return ansiCode + text + RESET;
    }

    public static String redBold(String text, boolean enabled) {
        return colorize(text, RED_BOLD, enabled);
    }

    public static String blueBold(String text, boolean enabled) {
        return colorize(text, BLUE_BOLD, enabled);
    }

    public static String cyanBold(String text, boolean enabled) {
        return colorize(text, CYAN_BOLD, enabled);
    }
}
