package chase.minecraft.architectury.simplemodconfig.client.gui.screen;

import chase.minecraft.architectury.simplemodconfig.client.gui.component.ConfigListComponent;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import static chase.minecraft.architectury.simplemodconfig.client.gui.GUIFactory.createButton;

@Environment(EnvType.CLIENT)
public class ConfigScreen extends Screen
{
	@Nullable
	private final Screen parent;
	private final ConfigHandler<?> configHandler;
	private ConfigListComponent configListComponent;
	
	public ConfigScreen(ConfigHandler<?> configHandler, @Nullable Screen parent)
	{
		super(configHandler.getDisplayName());
		this.parent = parent;
		this.configHandler = configHandler;
	}
	
	@Override
	protected void init()
	{
		configListComponent = new ConfigListComponent(this, configHandler);
		addRenderableWidget(configListComponent);
		addRenderableWidget(createButton((width / 2) - 110, height - 25, 100, 20, Component.translatable("simplemodconfig.gui.reset.all"), button ->
		{
			configHandler.reset();
			configListComponent.refreshEntries();
			button.setFocused(false);
		}));
		addRenderableWidget(createButton((width / 2) + 10, height - 25, 100, 20, CommonComponents.GUI_DONE, button ->
		{
			configListComponent.save();
			assert minecraft != null;
			minecraft.setScreen(parent);
		}));
	}
	
	@Override
	public void render(@NotNull PoseStack poseStack, int x, int y, float partialTicks)
	{
		super.render(poseStack, x, y, partialTicks);
		drawCenteredString(poseStack, font, title.getString(), (width / 2) - (font.width(title.getString()) / 2), font.lineHeight, 0xff_ff_ff);
	}
}
