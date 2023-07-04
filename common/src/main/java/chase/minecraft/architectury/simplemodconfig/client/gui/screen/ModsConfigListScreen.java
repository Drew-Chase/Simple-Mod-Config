package chase.minecraft.architectury.simplemodconfig.client.gui.screen;

import chase.minecraft.architectury.simplemodconfig.client.gui.component.ConfigListComponent;
import chase.minecraft.architectury.simplemodconfig.client.gui.component.ModConfigListComponent;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import chase.minecraft.architectury.simplemodconfig.handlers.LoadedConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

import static chase.minecraft.architectury.simplemodconfig.client.gui.GUIFactory.createButton;
import static chase.minecraft.architectury.simplemodconfig.client.gui.GUIFactory.createTextBox;

public class ModsConfigListScreen extends Screen
{
	@Nullable
	private Screen parent;
	private @Nullable ConfigListComponent configListComponent;
	private String loadedConfigName = "";
	private EditBox searchBox;
	private ModConfigListComponent modList;
	
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
		modList = addRenderableWidget(new ModConfigListComponent(this));
		addRenderableWidget(createButton((150 / 2) - 50, height - 25, 100, 20, CommonComponents.GUI_DONE, button ->
		{
			if (configListComponent != null)
			{
				configListComponent.save();
			}
			assert minecraft != null;
			minecraft.setScreen(parent);
		}));
		searchBox = addRenderableWidget(createTextBox(Minecraft.getInstance().font, 5, 5, 140, 20, Component.empty()));
		if (LoadedConfigs.getInstance().size() > 0)
		{
			Optional<Map.Entry<String, ConfigHandler<?>>> first = LoadedConfigs.getInstance().get().stream().findFirst();
			if (first.isPresent())
			{
				Map.Entry<String, ConfigHandler<?>> element = first.get();
				load(element.getKey(), element.getValue());
			}
		}
	}
	
	public void load(String name, ConfigHandler<?> configHandler)
	{
		if (configListComponent != null)
		{
			configListComponent.save();
		}
		loadedConfigName = name;
		configListComponent = new ConfigListComponent(configHandler, width, height, 30, height, 155, 30);
	}
	
	public boolean isLoaded(String name)
	{
		return this.loadedConfigName.equals(name);
	}
	
	@Override
	public void render(GuiGraphics graphics, int i, int j, float f)
	{
		if (searchBox.getValue().isEmpty())
		{
			searchBox.setSuggestion("Search");
		} else
		{
			searchBox.setSuggestion("");
		}
		super.renderDirtBackground(graphics);
		if (configListComponent != null)
		{
			configListComponent.render(graphics, i, j, f, width, height, 30, height, 155);
			graphics.drawCenteredString(font, loadedConfigName, ((width + 150) / 2), font.lineHeight, 0xff_ff_ff);
		}
		super.render(graphics, i, j, f);
	}
	
	@Override
	public boolean mouseClicked(double d, double e, int i)
	{
		if (configListComponent != null)
		{
			configListComponent.mouseClicked(d, e, i);
		}
		return super.mouseClicked(d, e, i);
	}
	
	@Override
	public boolean charTyped(char c, int i)
	{
		if (searchBox.isFocused())
		{
			modList.search(searchBox.getValue());
		}
		if (configListComponent != null)
		{
			configListComponent.charTyped(c, i);
		}
		return super.charTyped(c, i);
	}
	
	@Override
	public boolean keyPressed(int i, int j, int k)
	{
		if (searchBox.isFocused())
		{
			modList.search(searchBox.getValue());
		}
		if (configListComponent != null)
		{
			configListComponent.keyPressed(i, j, k);
		}
		return super.keyPressed(i, j, k);
	}
	
	@Override
	public boolean keyReleased(int i, int j, int k)
	{
		if (searchBox.isFocused())
		{
			modList.search(searchBox.getValue());
		}
		if (configListComponent != null)
		{
			configListComponent.keyReleased(i, j, k);
		}
		return super.keyReleased(i, j, k);
	}
	
	@Override
	public boolean mouseScrolled(double d, double e, double f)
	{
		if (configListComponent != null && configListComponent.isMouseOver(d, e))
		{
			configListComponent.mouseScrolled(d, e, f);
		}
		return super.mouseScrolled(d, e, f);
	}
	
	@Override
	public boolean mouseDragged(double d, double e, int i, double f, double g)
	{
		if (configListComponent != null)
		{
			configListComponent.mouseDragged(d, e, i, f, g);
		}
		return super.mouseDragged(d, e, i, f, g);
	}
}
