package com.mrbreaknfix.noreposts.utils;

import net.fabricmc.loader.api.metadata.ModMetadata;

import static com.mrbreaknfix.noreposts.NoReposts.IncompliantMods;

public class IncompliantMod {
    private ModMetadata metadata;
    private REASON reason;

    public IncompliantMod(ModMetadata metadata, REASON reason) {
        this.metadata = metadata;
        this.reason = reason;
    }

    public ModMetadata getMetadata() {
        return metadata;
    }

    public REASON getReason() {
        return reason;
    }



    public static void addIncompliantMod(IncompliantMod metadata) {
        // check if already exists
        if (IncompliantMods != null) {
            for (IncompliantMod mod : IncompliantMods) {
                if (mod.getMetadata().getId().equals(mod.getMetadata().getId())) {
                    return;
                }
            }
        }

        // add to array
        if (IncompliantMods == null) {
            IncompliantMods = new IncompliantMod[]{metadata};
        } else {
            IncompliantMod[] temp = new IncompliantMod[IncompliantMods.length + 1];
            System.arraycopy(IncompliantMods, 0, temp, 0, IncompliantMods.length);
            temp[temp.length - 1] = metadata;
            IncompliantMods = temp;
        }
    }
}
