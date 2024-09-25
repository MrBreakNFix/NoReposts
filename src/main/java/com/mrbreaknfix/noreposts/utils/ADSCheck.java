package com.mrbreaknfix.noreposts.utils;

import java.io.File;

public class ADSCheck {
    /**
     * On Windows systems, checks if the file was downloaded from a specific site, using regex
     * @param file the file to check
     * @param regex regex of the sites to check for
     * @return the site the file was downloaded from that matches the regex, or null if it wasn't downloaded from any of the sites
     *  <h2>Throws IllegalArgumentException if no ADS is present. Make sure to try/catch for no MoTW before implementing logic like in the examples</h2>
     * <p>
     *     Example usage denying anything that contains "9minecraft"
     *     <pre>{@code
     *         String blockedSite = ADSCheck.getMatchingOriginFromRegex(file, ".*9minecraft.*");
     *         if (blockedSite != null) {
     *              System.out.println("File was downloaded from a blocked site: " + blockedSite);
     *         }
     *     }</pre>
     *     Example usage allowing only GitHub downloads that do not contain your username
     *     <pre>{@code
     *     String yourUsername = "MrBreakNFix";
     *     String allowedSite = ADSCheck.getMatchingOriginFromRegex(file, "^(?=.*github\\.com)(?!.*github\\.com/" + yourUsername + ").*$");
     *     if (allowedSite != null) {
     *         System.out.println("File was downloaded from a allowed site: " + allowedSite);
     *     } else {
     *         System.out.println("File was not downloaded from a allowed site.");
     *     }
     *     }</pre>
     *
     *     Example usage that if ADS is present will only allow downloads from Modrinth, or your GitHub
     *     <pre>{@code
     *     String yourUsername = "MrBreakNFix";
     *     String[] allowedOrigins = {
     *         ".modrinth.*",
     *         "^(?=.*github\\.com)(?!.*github\\.com/" + yourUsername + ").*$"
     *         };
     *     String allowedSite = ADSCheck.getMatchingOriginFromRegex(file, allowedOrigins);
     *     if (allowedSite != null) {
     *         System.out.println("File was downloaded from a allowed site: " + allowedSite);
     *     } else {
     *         System.out.println("File was not downloaded from a allowed site.");
     *     }
     * </p>
     */
    public static String getMatchingOriginFromRegex(File file, String... regex) throws IllegalArgumentException {

        if (!file.exists()) {
            throw new IllegalArgumentException("File doesn't exist.");
        }

        MoTW motw = MoTW.from(file);

        String referrerUrl = motw != null ? motw.getReferrerUrl() : null;
        String hostUrl = motw != null ? motw.getHostUrl() : null;

        for (String matches : regex) {
            if (referrerUrl != null && referrerUrl.matches(matches)) {
                return referrerUrl;
            }

            if (hostUrl != null && hostUrl.matches(matches)) {
                return hostUrl;
            }
        }
        return null;

    }
}