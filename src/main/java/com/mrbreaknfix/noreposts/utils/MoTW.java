package com.mrbreaknfix.noreposts.utils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;

/**
 * Represents a Zone.Identifier Alternate Data Stream (ADS) in a file.
 * <p>
 *    "The Mark of the Web (MoTW) is an identifier used by Microsoft Windows to mark files downloaded from the Internet as potentially unsafe.
 *    It is implemented as an NTFS Zone.Identifier alternate data stream (ADS) containing an identifier element which indicates (with the "Mark") that a file saved on a computer could contain harmful or malicious content because it was downloaded from an external source ("the Web")."
 *    <a href="https://en.wikipedia.org/wiki/Mark_of_the_Web"> - Wikipedia</a>.
 *
 *    This class represents the Zone.Identifier ADS in a file, allowing you to get fields from the MotW object, including
 *    the ZoneId, HostUrl, and ReferrerUrl, if present
 */
public class MoTW {
    private String zoneId;
    private String hostUrl;
    private String referrerUrl;

    public MoTW(String zoneId, String hostUrl, String referrerUrl) {
        this.zoneId = zoneId;
        this.hostUrl = hostUrl;
        this.referrerUrl = referrerUrl;
    }

    public String getZoneId() {
        return zoneId;
    }

    public String getHostUrl() {
        return hostUrl;
    }

    public String getReferrerUrl() {
        return referrerUrl;
    }

    public static MoTW from(File file) throws IllegalArgumentException {
        if (!file.exists()) {
            throw new IllegalArgumentException("File doesn't exist.");
        }

        // Check if the Zone.Identifier ADS exists by trying to open it
        String adsPath = file.getAbsolutePath() + ":Zone.Identifier";
        File adsFile = new File(adsPath);

        if (!adsFile.exists()) {
            throw new IllegalArgumentException("No Zone.Identifier stream found.");
        }

        // Try to read the Zone.Identifier ADS using RandomAccessFile
        try (RandomAccessFile streamFile = new RandomAccessFile(adsPath, "r")) {
            String line;
            String zoneId = null;
            String hostUrl = null;
            String referrerUrl = null;

            while ((line = streamFile.readLine()) != null) {
                String content = new String(line.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                // set the ZoneId
                if (content.contains("[ZoneTransfer]")) {
                    zoneId = content;
                }
                // set the HostUrl
                if (content.startsWith("HostUrl=")) {
                    hostUrl = content.substring(8);
                }
                // set the ReferrerUrl
                if (content.startsWith("ReferrerUrl=")) {
                    referrerUrl = content.substring(12);
                }
//                System.out.println(content);
            }
            return new MoTW(zoneId, hostUrl, referrerUrl);
        } catch (IOException e) {
            System.out.println("Error reading the Zone.Identifier ADS: " + e.getMessage());
        }

        return null;

    }
}