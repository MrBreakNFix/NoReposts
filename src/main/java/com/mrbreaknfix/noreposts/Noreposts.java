package com.mrbreaknfix.noreposts;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.fabricmc.loader.api.FabricLoader;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;


import java.util.List;


public class Noreposts implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        System.out.println("Checking mod integrity...");
        List<String> SkipedMods = List.of("minecraft");

        FabricLoader.getInstance().getAllMods().stream()
//                .map(ModContainer::getMetadata, ModContainer::getPath)
            .map(ModContainer::getMetadata)
            .forEach(metadata -> {
                String modId = metadata.getId();
                ModContainer mod = FabricLoader.getInstance().getModContainer(modId).get();

                // Skip pre checked mods if their id is in the list
                if (SkipedMods.contains(modId)) {
                    System.out.println("Skipping " + metadata.getName() + ", as it has previously been checked");
                } else {
                    System.out.println("Checking " + metadata.getName());
                    try {
                        List<String> paths = new ArrayList<>();
                        // add all paths to a list
                        mod.getOrigin().getPaths().forEach(path -> paths.add(path.toString()));

                        // split the .jar file path to get the mod name
                        String[] splitPath = paths.get(0).split("/");
                        String modName = splitPath[splitPath.length - 1];
//                        System.out.println("Mod " + metadata.getName() + " has real name " + modName);

                        if (modName.toLowerCase().contains("UI-Utils-Mod-Fabric-1.21.1.jar".toLowerCase())) {
                            System.out.println("Mod " + metadata.getName() + " is a repost, deleting...");
                            // delete the mod
                            File file = new File(paths.getFirst());
                            file.delete();

                            // stop the game
//                            System.exit(0);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });
    }
}
