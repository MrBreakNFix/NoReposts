package com.mrbreaknfix.noreposts;

import com.mrbreaknfix.noreposts.utils.ADSCheck;
import com.mrbreaknfix.noreposts.utils.OsUtils;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.metadata.ModOrigin;

import java.nio.file.Path;
import java.util.List;


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
                    System.out.println("Mod " + metadata.getName() + " is not present in the mod container. You should not be able to see this message under normal circumstances.");
                    return;
                }


                try {
                    // Skips nested mods
                    if (mod.getOrigin().getKind() == ModOrigin.Kind.NESTED) return;

                    Path modPath = mod.getOrigin().getPaths().getFirst();

                    // Get file name
                    String fileName = modPath.getFileName().toString();
                    System.out.println("Checking mod " + metadata.getName() + " with file name: " + fileName);

                    // Windows checks:
                    if (OsUtils.isWindows()) {

                        // Matches sites
                            try {
                                List<String> fromSites = ADSCheck.getSites(modPath.toFile());
                                // check if the regex matches the 9minecraft one
                                for (String fromSite : fromSites) {
                                    if (fromSite.matches(origins[0])) {
                                        System.out.println("Mod " + metadata.getName() + " was downloaded from the correct site! " + fromSite);
                                    } else if (fromSite.matches(origins[1])) {
                                        System.out.println("Mod " + metadata.getName() + " was downloaded from a 9minecraft site: " + fromSite);
                                    } else if (fromSite.matches(origins[2])) {
                                        System.out.println("Mod " + metadata.getName() + " was downloaded from a github site which is not Coderx-Gamer's: " + fromSite);
                                    } else {
                                        System.out.println("Mod " + metadata.getName() + " was downloaded from: " + fromSite);
                                    }
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
