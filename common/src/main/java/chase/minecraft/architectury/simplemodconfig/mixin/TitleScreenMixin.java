package chase.minecraft.architectury.simplemodconfig.mixin;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import chase.minecraft.architectury.simplemodconfig.client.gui.screen.ModsConfigListScreen;
import chase.minecraft.architectury.simplemodconfig.config.SimpleModConfigConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static dev.architectury.hooks.client.screen.ScreenHooks.addRenderableWidget;

@Mixin(TitleScreen.class)
public class TitleScreenMixin
{
	
	@Inject(at = @At("TAIL"), method = "init")
	void init(CallbackInfo cb)
	{
		if (SimpleModConfig.configHandler.getConfig().ShowTitleScreenButton)
		{
			TitleScreen titleScreen = (TitleScreen) ((Object) this);
			int y = ((titleScreen.height / 4 + 48) + 72 + 12) - 25;
			int x = titleScreen.width / 2 + 104;
			ImageButton gear = new ImageButton(x, y, 20, 20, 0, 0, 20, SimpleModConfig.id("gui/config.png"), 32, 64, arg -> Minecraft.getInstance().setScreen(new ModsConfigListScreen(titleScreen)), Component.translatable("narrator.button.simplemodconfig"));
			addRenderableWidget(titleScreen, gear);
		}
	}
}
