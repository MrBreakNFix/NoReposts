package com.mrbreaknfix.noreposts.mixin;

import com.mrbreaknfix.noreposts.NoReposts;
import com.mrbreaknfix.noreposts.screens.IncompliantModsScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.AccessibilityOnboardingScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import static com.mrbreaknfix.noreposts.NoReposts.LOGGER;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Redirect(method = "setScreen", at = @At(value = "FIELD", target = "Lnet/minecraft/client/MinecraftClient;currentScreen:Lnet/minecraft/client/gui/screen/Screen;", opcode = Opcodes.PUTFIELD))
    private void injected(MinecraftClient instance, Screen screen) {
        if (screen instanceof TitleScreen || screen instanceof AccessibilityOnboardingScreen) {
            if(NoReposts.shouldShowPopup) {
                instance.setScreen(new IncompliantModsScreen());
                LOGGER.info("Showing popup");
            } else {
                LOGGER.info("Not showing popup");
                instance.currentScreen = screen;
            }
        } else {
            instance.currentScreen = screen;
        }
    }
}
