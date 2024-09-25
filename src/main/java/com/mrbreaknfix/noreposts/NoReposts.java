package com.mrbreaknfix.noreposts;

import com.mrbreaknfix.noreposts.utils.ADSCheck;
import com.mrbreaknfix.noreposts.utils.OsUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.nio.file.Path;


public class NoReposts implements PreLaunchEntrypoint {
    public static final String[] origins = {
            ".modrinth.*",
            ".*9minecraft.*",
            // any github download that does NOT contain Coderx-Gamer
            "^(?=.*github\\.com)(?!.*github\\.com/Coderx-Gamer/ui-utils).*$",
            ".*discord.*",
            ".*curseforge.*",
            ".*planetminecraft.*",
            ".*minecraftforum.*",
            ".*minecraftdl.*",
    };

    @Override
    public void onPreLaunch() {
        System.out.println("Checking mod integrity...");

        FabricLoader.getInstance().getAllMods().stream()
//                .map(ModContainer::getMetadata, ModContainer::getPath)
            .map(ModContainer::getMetadata)
            .forEach(metadata -> {
                String modId = metadata.getId();
                ModContainer mod = FabricLoader.getInstance().getModContainer(modId).isPresent() ? FabricLoader.getInstance().getModContainer(modId).get() : null;
                if (mod == null) {
                    System.out.println("Mod " + metadata.getName() + " is not present in the mod container.");
                    return;
                }

                try {
                    Path modPath = mod.getOrigin().getPaths().get(0);
//                    System.out.println("Mod " + metadata.getName() + " has full path: " + modPath);

                    // Windows checks:
                    if (OsUtils.isWindows()) {

                        // Matches sites
                            try {
                                String fromSite = ADSCheck.getMatchingOriginFromRegex(modPath.toFile(), origins);
                                if (fromSite == null) {
                                    System.out.println("Mod " + metadata.getName() + " was not downloaded from a listed site. Under normal conditions this message should not be visible.");
                                    return;
                                }
                                // check if the regex matches the 9minecraft one
                                if (fromSite.matches(origins[0])) {
                                    System.out.println("Mod " + metadata.getName() + " was downloaded from the correct site! " + fromSite);
                                } else if (fromSite.matches(origins[1])) {
                                    System.out.println("Mod " + metadata.getName() + " was downloaded from a 9minecraft site: " + fromSite);
                                } else if (fromSite.matches(origins[2])) {
                                    System.out.println("Mod " + metadata.getName() + " was downloaded from a github site which is not Coderx-Gamer's: " + fromSite);
                                } else {
                                    System.out.println("Mod " + metadata.getName() + " was downloaded from a blacklisted site: " + fromSite);
                                }


                            } catch (IllegalArgumentException e) {
                                // Does not have MoTW, user could have unblocked file, or just default mod, or not downloaded from internet. Fails silently.
                            }
                    }

                } catch (Exception e) {
                    System.out.println("Error checking mod " + metadata.getName() + ": " + e.getMessage());
                }
            });
    }
}
