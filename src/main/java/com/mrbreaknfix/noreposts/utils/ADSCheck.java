package com.mrbreaknfix.noreposts.utils;

import java.io.File;

public class ADSCheck {
    /**
     * On windows systems, checks if the file was downloaded from a specific site, using regex
     * @param file the file to check
     * @param regex regex of the sites to check for
     * @return true if the file was downloaded from a site that matches the regex, false otherwise
     *
     * <p>
     *     Example usage denying anything that contains "9minecraft"
     *     <pre>
     *         {@code
     *         ADSCheck.isFileFrom(file, ".*9minecraft.*");
     *         }
     *         </pre>
     *    </p>
     */
    public static String matchFileOriginWithRegex(File file, String... regex) throws IllegalArgumentException {
        if (!file.exists()) {
            throw new IllegalArgumentException("File doesn't exist.");
        }

        MoTW motw = MoTW.of(file);

        String referrerUrl = motw != null ? motw.getReferrerUrl() : null;
        String hostUrl = motw != null ? motw.getHostUrl() : null;

        // Check if the referrer URL or host URL matches the regex of any of the repost sites
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