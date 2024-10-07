package com.mrbreaknfix.noreposts.utils;

import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;

public class Config {
    private String officialOrigin;
    private String[] allowedOrigins;
    private String[] blacklistedOrigins;
    private String[] disallowedNames;
    private ModMetadata meta;
    private String incorrectOriginMessage;
    private String blacklistedOriginMessage;
    private String incorrectNameMessage;

    //todo: noreposts:icon
    public Config(ModMetadata metadata) {
        this.meta = metadata;

        CustomValue officialOriginValue = metadata.getCustomValue("noreposts:officialOrigin");
        if (officialOriginValue != null) {
            this.officialOrigin = officialOriginValue.getAsString();
        } else {
            this.officialOrigin = null;
        }

        if (this.officialOrigin == null) {
            this.officialOrigin = "https://github.com/";
        }


        this.incorrectOriginMessage = "Uh oh! Looks like " + meta.getName() + " has been downloaded from an unofficial source, many reposted mods can have malware added to them!\n You can download the official mod at: " + officialOrigin;
        this.blacklistedOriginMessage = "Uh oh! Looks like " + meta.getName() + " has been downloaded from a blacklisted source, many mod reposting websites are frowned upon for hosting malware, violation of licenses, and outdated versions\n You can download the official mod at: " + officialOrigin;
        this.incorrectNameMessage = "Uh oh! Looks like " + meta.getName() + " could have been downloaded from an unofficial source, many reposted mods can have malware added to them!\n You can download the official mod at: " + officialOrigin;



        CustomValue allowedOriginsValue = metadata.getCustomValue("noreposts:allowedOriginsB64Reg");
        if (allowedOriginsValue != null) {
            String[] allowedOriginsArray = new String[allowedOriginsValue.getAsArray().size()];
            for (int i = 0; i < allowedOriginsValue.getAsArray().size(); i++) {
                allowedOriginsArray[i] = allowedOriginsValue.getAsArray().get(i).getAsString();
            }
            // decode base 64
            for (int i = 0; i < allowedOriginsArray.length; i++) {
                allowedOriginsArray[i] = new String(java.util.Base64.getDecoder().decode(allowedOriginsArray[i]));
            }
            this.allowedOrigins = allowedOriginsArray;
        } else {
            this.allowedOrigins = new String[0];
        }

        CustomValue disallowedNamesValue = metadata.getCustomValue("noreposts:disallowedNamesB64Reg");
        if (disallowedNamesValue != null) {
            String[] disallowedNamesArray = new String[disallowedNamesValue.getAsArray().size()];
            for (int i = 0; i < disallowedNamesValue.getAsArray().size(); i++) {
                disallowedNamesArray[i] = disallowedNamesValue.getAsArray().get(i).getAsString();
            }
            // decode base 64
            for (int i = 0; i < disallowedNamesArray.length; i++) {
                disallowedNamesArray[i] = new String(java.util.Base64.getDecoder().decode(disallowedNamesArray[i]));
            }
            this.disallowedNames = disallowedNamesArray;
        } else {
            this.disallowedNames = new String[0];
        }

        CustomValue blacklistedOriginsValue = metadata.getCustomValue("noreposts:blacklistedOriginsB64Reg");
        if (blacklistedOriginsValue != null) {
            String[] blacklistedOriginsArray = new String[blacklistedOriginsValue.getAsArray().size()];
            for (int i = 0; i < blacklistedOriginsValue.getAsArray().size(); i++) {
                blacklistedOriginsArray[i] = blacklistedOriginsValue.getAsArray().get(i).getAsString();
            }
            // decode base 64
            for (int i = 0; i < blacklistedOriginsArray.length; i++) {
                blacklistedOriginsArray[i] = new String(java.util.Base64.getDecoder().decode(blacklistedOriginsArray[i]));
            }
            this.blacklistedOrigins = blacklistedOriginsArray;
        } else {
            this.blacklistedOrigins = new String[0];
        }

        CustomValue incorrectOriginMessageValue = metadata.getCustomValue("noreposts:incorrectOriginMessage");
        if (incorrectOriginMessageValue != null) {
            // only set it if its longer than 0, else it will remain the default
            if (!incorrectOriginMessageValue.getAsString().isEmpty()) this.incorrectOriginMessage = incorrectOriginMessageValue.getAsString();
        }

        CustomValue blacklistedOriginMessageValue = metadata.getCustomValue("noreposts:blacklistedOriginMessage");
        if (blacklistedOriginMessageValue != null) {
            // only set it if its longer than 0, else it will remain the default
            if (!blacklistedOriginMessageValue.getAsString().isEmpty()) this.blacklistedOriginMessage = blacklistedOriginMessageValue.getAsString();
        }

        CustomValue incorrectNameMessageValue = metadata.getCustomValue("noreposts:incorrectNameMessage");
        if (incorrectNameMessageValue != null) {
            // only set it if its longer than 0, else it will remain the default
            if (!incorrectNameMessageValue.getAsString().isEmpty()) this.incorrectNameMessage = incorrectNameMessageValue.getAsString();
        }



    }

    public String getOfficialOrigin() {
        return officialOrigin;
    }

    public String[] getAllowedOrigins() {
        return allowedOrigins;
    }

    public String[] getDisallowedNames() {
        return disallowedNames;
    }

    public String[] getBlacklistedOrigins() {
        return blacklistedOrigins;
    }

    public String getIncorrectOriginMessage() {
        return incorrectOriginMessage;
    }

    public String getBlacklistedOriginMessage() {
        return blacklistedOriginMessage;
    }

    /**
     * Get the message for a given reason, if not present in config, default message will be returned
     * @param reason From REASON enum
     * @return The message to be displayed
     */
    public String getReasonMessage(REASON reason) {
        return switch (reason) {
            case INCORRECT_NAME -> incorrectNameMessage;
            case INCORRECT_ORIGIN -> incorrectOriginMessage;
            case BLACKLISTED_ORIGIN -> blacklistedOriginMessage;
        };
    }

    public String getIncorrectNameMessage() {
        return incorrectNameMessage;
    }

    public ModMetadata getMeta() {
        return meta;
    }
}