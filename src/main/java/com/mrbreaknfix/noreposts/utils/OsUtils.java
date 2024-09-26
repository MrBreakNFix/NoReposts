package com.mrbreaknfix.noreposts.utils;

public class OsUtils {

    private static final String OS = System.getProperty("os.name").toLowerCase();

    /**
     * Get the operating system name.
     *
     * @return the operating system name
     */
    public static String getOs() {
        if (isWindows()) {
            return "Windows";
        } else if (isMac()) {
            return "macOS";
        } else if (isLinux()) {
            return "Linux";
        } else {
            return OS;
        }
    }

    /**
     * Check if the operating system is Windows.
     *
     * @return true if the operating system is Windows, false otherwise
     */
    public static boolean isWindows() {
        return OS.contains("win");
    }

    /**
     * Check if the operating system is macOS.
     *
     * @return true if the operating system is macOS, or other Apple operating systems, false otherwise
     */
    public static boolean isMac() {
        return OS.contains("mac") || OS.contains("darwin") || OS.contains("osx") || OS.contains("os x");
    }

    /**
     * Check if the operating system is Linux.
     *
     * @return true if the operating system is Linux, false otherwise
     */
    public static boolean isLinux() {
        return OS.contains("nux") || OS.contains("nix");
    }
}