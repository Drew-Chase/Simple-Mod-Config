package chase.minecraft.architectury.simplemodconfig.client.gui.screen;

import chase.minecraft.architectury.simplemodconfig.client.gui.component.ConfigListComponent;
import chase.minecraft.architectury.simplemodconfig.client.gui.component.ModConfigListComponent;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import static chase.minecraft.architectury.simplemodconfig.client.gui.GUIFactory.createButton;

public class ModsConfigListScreen extends Screen
{
	@Nullable
	private Screen parent;
	private @Nullable ConfigListComponent configListComponent;
	private String loadedConfigName = "";
	
	public ModsConfigListScreen()
	{
		this(null);
	}
	
	public ModsConfigListScreen(Screen screen)
	{
		super(Component.translatable("simplemodconifg.title"));
	}
	
	@Override
	protected void init()
	{
		super.init();
		addRenderableWidget(new ModConfigListComponent(this));
		addRenderableWidget(createButton((150 / 2) - 50, height - 25, 100, 20, CommonComponents.GUI_DONE, button ->
		{
			minecraft.setScreen(parent);
		}));
	}
	
	public void load(String name, ConfigHandler<?> configHandler)
	{
		loadedConfigName = name;
		configListComponent = new ConfigListComponent(this, configHandler, width, height, 30, height , 155, 30);
	}
	public boolean isLoaded(String name)
	{
		return this.loadedConfigName.equals(name);
	}
	@Override
	public void render(PoseStack poseStack, int i, int j, float f)
	{
		super.renderDirtBackground(poseStack);
		if (configListComponent != null)
		{
			configListComponent.render(poseStack, i, j, f);
			drawCenteredString(poseStack, font, loadedConfigName, (width / 2) - (150), font.lineHeight, 0xff_ff_ff);
		}
		drawCenteredString(poseStack, font, title.getString(), (150 / 2), font.lineHeight, 0xff_ff_ff);
		super.render(poseStack, i, j, f);
	}
}
