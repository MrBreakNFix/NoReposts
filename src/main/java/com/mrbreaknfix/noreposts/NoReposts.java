package com.mrbreaknfix.noreposts;

import com.mrbreaknfix.noreposts.utils.*;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModOrigin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static com.mrbreaknfix.noreposts.utils.IncompliantMod.addIncompliantMod;


public class NoReposts implements PreLaunchEntrypoint {
    public static IncompliantMod[] IncompliantMods = null;
    public static Logger LOGGER = LoggerFactory.getLogger("NoReposts");
    public static boolean canHidePopup = true;
    public static boolean shouldShowPopup = false;
    //todo: Add a hide for this mod option on the popup.
    // add a menu showing all incompliant mods, their reasons, and a link to fix them

    @Override
    public void onPreLaunch() {
        LOGGER.info("Validating mods...");

        FabricLoader.getInstance().getAllMods().stream()
            .map(ModContainer::getMetadata)
            .forEach(metadata -> {
                String modId = metadata.getId();
                ModContainer mod = FabricLoader.getInstance().getModContainer(modId).isPresent() ? FabricLoader.getInstance().getModContainer(modId).get() : null;
                if (mod == null) {
                    LOGGER.error("Mod {} is not present in the mod container. You should not be able to see this message under normal circumstances.", metadata.getName());
                    return;
                }

                // Skips nested mods
                if (mod.getOrigin().getKind() == ModOrigin.Kind.NESTED) return;

                // Get mod path
                Path modPath = mod.getOrigin().getPaths().getFirst();

                // Read NoReposts config, if present
                Config config = new Config(metadata);


                if (OsUtils.isWindows()) {

                    // Matches sites
                    try {
                        List<String> fromSites = ADSCheck.getSites(modPath.toFile());
                        for (String fromSite : fromSites) {
                            if (fromSite.equals("about:internet")) {
                                LOGGER.warn(metadata.getName() + " has an unknown origin.");
                                break;
                            }

                            // Check if the origin is blacklisted (FIRST)
                            if (config.getBlacklistedOrigins() != null && config.getBlacklistedOrigins().length >= 1) {
                                for (String blacklistedOrigin : config.getBlacklistedOrigins()) {
                                    if (fromSite.matches(blacklistedOrigin)) {
                                        LOGGER.warn("Mod " + metadata.getName() + " was downloaded from a BLACKLISTED site: " + fromSite);
                                        LOGGER.debug("Developer information: " + Arrays.toString(config.getBlacklistedOrigins()));

                                        addIncompliantMod(new IncompliantMod(metadata, REASON.BLACKLISTED_ORIGIN));
                                        canHidePopup = false;
                                        shouldShowPopup = true;
                                        // skip checking for allowed origins, as it is blacklisted
                                        return;
                                    }
                                }
                            }

                            // Check if the origin matches the allowed origins
                            if (config.getAllowedOrigins() != null && config.getAllowedOrigins().length >= 1) {
                                for (String allowedOrigin : config.getAllowedOrigins()) {
                                    // if it is equal to "about:internet", then we skip it, assuming it was downloaded in incognito or brave, or some other methods
                                    // and pray that the user knows what they are doing
                                     if (fromSite.matches(allowedOrigin)) {
                                        LOGGER.debug(metadata.getName() + " was downloaded from an allowed site: " + fromSite);
                                        break;
                                    } else {
                                        LOGGER.warn("Mod " + metadata.getName() + " was not downloaded from an allowed site: " + fromSite);
                                        addIncompliantMod(new IncompliantMod(metadata, REASON.INCORRECT_ORIGIN));
                                        // we allow the popup to be hidden in this case, because its not blacklisted, but it is incorrect;
                                         // could have just been sending it to a friend or something
                                        shouldShowPopup = true;
                                    }
                                }
                            }




                        }
                    } catch (IllegalArgumentException e) {
                        // Does not have MoTW, user could have unblocked file, or just default mod, or not downloaded from internet. Fails silently.
                    }
                }

                // Check for incorrect file name
                if (config.getDisallowedNames() != null && config.getDisallowedNames().length > 0) {
                    String fileName = modPath.getFileName().toString();

//                    LOGGER.info("Checking mod " + metadata.getName() + " with disallowed names: " + Arrays.toString(config.getDisallowedNames()));

                    for (String disallowedName : config.getDisallowedNames()) {
                        // check regex
                        if (fileName.matches(disallowedName)) {
                            LOGGER.warn("Mod " + metadata.getName() + " has an incorrect name: " + fileName);
                            addIncompliantMod(new IncompliantMod(metadata, REASON.INCORRECT_NAME));
                            canHidePopup = false;
                            shouldShowPopup = true;
                        } else {
                            LOGGER.debug("Mod " + metadata.getName() + " has a correct name: " + fileName);
                        }
                    }
                }

            });

        if(!OsUtils.isWindows()) {
            LOGGER.warn("Origin check is not supported on " + OsUtils.getOs());
        }

        if (IncompliantMods != null) {
            // List all the mods that are incompliant
            for (IncompliantMod mod : IncompliantMods) {
                Config config = new Config(mod.getMetadata());

                // switch based on reason
                switch (mod.getReason()) {
                    case INCORRECT_NAME:
                        LOGGER.warn(config.getIncorrectNameMessage());
                        break;
                    case INCORRECT_ORIGIN:
                        LOGGER.warn(config.getIncorrectOriginMessage());
                        break;
                    case BLACKLISTED_ORIGIN:
                        LOGGER.warn(config.getBlacklistedOriginMessage());
                        break;
                }
            }
        } else {
            LOGGER.info("No incompliant mods found!");
        }
    }
}
