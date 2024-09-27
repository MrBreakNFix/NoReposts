package com.mrbreaknfix.noreposts.screens;

import com.mrbreaknfix.noreposts.NoReposts;
import com.mrbreaknfix.noreposts.utils.Config;
import com.mrbreaknfix.noreposts.utils.IncompliantMod;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.text.Text;

import static com.mrbreaknfix.noreposts.NoReposts.LOGGER;

public class IncompliantModsScreen extends Screen {
    public IncompliantModsScreen() {
        super(Text.of("Incompliant Mods"));
    }

    static MinecraftClient mc = MinecraftClient.getInstance();
    // set the current Y to top of the screen
    private static int currentY = 10;

    // Add a HIDE button
    public void init() {
        super.init();
        Text message = Text.of("Hide");
        ButtonWidget.PressAction onPress = (button) -> {
            LOGGER.info("Hiding popup");
            mc.setScreen(new TitleScreen(true));
        };
        int w = 50;

        // X: center of screen - half of button width, Y: bottom of screen - 30
        int x = this.width / 2 - w / 2;
        int y = this.height - 30;
        ButtonWidget hideButton = new ButtonWidget.Builder(message, onPress).width(w).position(x, y).build();

        hideButton.active = NoReposts.canHidePopup;
        this.addDrawableChild(hideButton);

        showIncompliantMods();
    }

    public void showIncompliantMods() {
        for (int i = 0; i < NoReposts.IncompliantMods.length; i++) {
            IncompliantMod mod = NoReposts.IncompliantMods[i];
            Config config = new Config(mod.getMetadata());

            // Draw mod name
            String message = mod.getMetadata().getName();
            TextWidget modName = new TextWidget(textRenderer.getWidth(message), currentY, Text.of(message), mc.textRenderer);
            this.addDrawableChild(modName).setPosition(this.width / 2 - modName.getWidth() / 2, currentY);
            currentY += textRenderer.fontHeight + 2;

            // Draw reason
            message = config.getReasonMessage(mod.getReason());
            TextWidget reason = new TextWidget(textRenderer.getWidth(message), currentY, Text.of(message), mc.textRenderer);
            this.addDrawableChild(reason).setPosition(this.width / 2 - reason.getWidth() / 2, currentY);
            currentY += textRenderer.fontHeight + 2;

            // Draw a link to the official mod
            message = "Download the official mod at: " + config.getOfficialOrigin();
            TextWidget downloadLink = new TextWidget(textRenderer.getWidth(message), currentY, Text.of(message), mc.textRenderer);
            this.addDrawableChild(downloadLink).setPosition(this.width / 2 - downloadLink.getWidth() / 2, currentY);
            currentY += textRenderer.fontHeight + 2;

        }
    }

}