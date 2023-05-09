package chase.minecraft.architectury.simplemodconfig.client.gui.component;

import chase.minecraft.architectury.simplemodconfig.annotation.SimpleConfig;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.*;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

import static chase.minecraft.architectury.simplemodconfig.client.gui.GUIFactory.*;

@Environment(EnvType.CLIENT)
public class ConfigListComponent extends ContainerObjectSelectionList<ConfigListComponent.Entry>
{
	private final ConfigHandler<?> configHandler;
	private final Screen parent;
	
	public ConfigListComponent(Screen parent, ConfigHandler<?> configHandler)
	{
		super(Minecraft.getInstance(), parent.width, parent.height + 15, 30, parent.height - 32, 30);
		this.configHandler = configHandler;
		this.parent = parent;
		clearEntries();
		configHandler.getAllSorted().forEach((key, value) -> addEntry(new ConfigEntry(key, value)));
		refreshEntries();
	}
	
	@Override
	protected int getScrollbarPosition()
	{
		return width - 10;
	}
	
	@Override
	public int getRowWidth()
	{
		return width - 25;
	}
	
	public void refreshEntries()
	{
		children().forEach(Entry::refreshEntry);
	}
	
	public void save()
	{
		children().forEach(Entry::save);
		configHandler.save();
	}
	
	public void resetEntries()
	{
		children().forEach(Entry::reset);
	}
	
	@Environment(EnvType.CLIENT)
	public static abstract class Entry extends ContainerObjectSelectionList.Entry<Entry>
	{
		public abstract void refreshEntry();
		
		public abstract void save();
		
		public abstract void reset();
	}
	
	public class ConfigEntry extends Entry
	{
		private final String name;
		private final Button resetButton;
		private AbstractWidget inputWidget = null;
		private Object value;
		private final Component displayName;
		private final SimpleConfig options;
		
		public ConfigEntry(String name, Object value)
		{
			options = ConfigListComponent.this.configHandler.getConfigOptions(name);
			assert options != null;
			this.name = name;
			this.value = value;
			this.displayName = getDisplayName();
			resetButton = createButton(0, 0, 50, 20, Component.translatable("controls.reset"), button ->
			{
				reset();
			});
			if (value instanceof Boolean)
			{
				inputWidget = createCycleButton(Component.literal(name), 0, 0, 100, 20, value.toString().toUpperCase(), new String[]{"TRUE", "FALSE"}, Component.empty(), val ->
				{
					this.value = Boolean.valueOf(val);
					if (inputWidget != null)
						inputWidget.setTooltip(Tooltip.create(ConfigListComponent.this.configHandler.getTooltip(name)));
				}, Component::literal);
			} else if (value instanceof Number number)
			{
				inputWidget = createNumbersTextBox(ConfigListComponent.this.minecraft.font, 0, 0, 100, 20, Component.literal(name), number.getClass());
				((EditBox) inputWidget).setValue(value.toString());
			} else
			{
				String[] items = options.options();
				if (items.length > 0)
				{
					inputWidget = createCycleButton(Component.literal(name), 0, 0, 100, 20, value.toString(), items, Component.empty(), val ->
					{
						this.value = val;
						if (inputWidget != null)
							inputWidget.setTooltip(Tooltip.create(ConfigListComponent.this.configHandler.getTooltip(name)));
					}, Component::literal);
				} else
				{
					inputWidget = createTextBox(ConfigListComponent.this.minecraft.font, 0, 0, 100, 20, Component.literal(name));
					((EditBox) inputWidget).setValue(value.toString());
				}
			}
			inputWidget.setTooltip(Tooltip.create(ConfigListComponent.this.configHandler.getTooltip(name)));
		}
		
		@Override
		public void render(@NotNull PoseStack poseStack, int x, int y, int uk, int widgetWidth, int widgetHeight, int mouseX, int mouseY, boolean isHovering, float partialTicks)
		{
			update();
			int parentWidth = ConfigListComponent.this.width;
			if (isHovering)
			{
				RenderSystem.setShaderColor(0f, 0f, 0f, .5f);
				fill(poseStack, x, y, x + parentWidth, y + widgetHeight, 0xFF_FF_FF_FF);
				RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
			}
			
			// Render Label
			ConfigListComponent.this.minecraft.font.draw(poseStack, this.displayName, 20, y + minecraft.font.lineHeight, 0xFF_FF_FF);
			
			// Render Buttons
			int buttonPadding = 4;
			resetButton.setX(widgetWidth - resetButton.getWidth() + buttonPadding);
			resetButton.setY(y + (widgetHeight / 2) - (resetButton.getHeight() / 2));
			resetButton.render(poseStack, mouseX, mouseY, partialTicks);
			resetButton.active = !Objects.requireNonNull(ConfigListComponent.this.configHandler.getInitial(name)).toString().equalsIgnoreCase(value.toString());
			resetButton.setFocused(false);
			
			inputWidget.setX(resetButton.getX() - inputWidget.getWidth() - buttonPadding);
			inputWidget.setY(resetButton.getY());
			inputWidget.render(poseStack, mouseX, mouseY, partialTicks);
		}
		
		@Override
		public @NotNull List<? extends NarratableEntry> narratables()
		{
			return ImmutableList.of(resetButton, inputWidget);
		}
		
		@Override
		public @NotNull List<? extends GuiEventListener> children()
		{
			return ImmutableList.of(resetButton, inputWidget);
		}
		
		public void save()
		{
			update();
			ConfigListComponent.this.configHandler.set(name, value);
		}
		
		private void update()
		{
			if (value instanceof Boolean && inputWidget instanceof CycleButton cycleButton)
			{
				value = Boolean.valueOf(cycleButton.getValue().toString());
			} else if (value instanceof Number && inputWidget instanceof EditBox editBox)
			{
				
				if (editBox.getValue().isEmpty())
				{
					if (!editBox.isFocused())
						reset();
				} else
				{
					if (value instanceof Number)
					{
						if (!editBox.isFocused())
						{
							double number = Double.parseDouble(editBox.getValue());
							if (number > options.max() || number < options.min())
							{
								reset();
							}
						}
						if (value instanceof Integer)
						{
							value = (int) Double.parseDouble(editBox.getValue());
						} else if (value instanceof Float)
						{
							value = Float.parseFloat(editBox.getValue());
						} else if (value instanceof Double)
						{
							value = Double.parseDouble(editBox.getValue());
						} else if (value instanceof Long)
						{
							value = (long) Double.parseDouble(editBox.getValue());
						}
					}
				}
			} else if (value instanceof String)
			{
				if (inputWidget instanceof CycleButton cycleButton)
				{
					value = cycleButton.getValue().toString();
				} else if (inputWidget instanceof EditBox editBox)
				{
					value = editBox.getValue();
				}
			}
		}
		
		public void reset()
		{
			Object initial = ConfigListComponent.this.configHandler.getInitial(name);
			assert initial != null;
			if (inputWidget instanceof CycleButton cycleButton)
			{
				if (value instanceof Boolean)
				{
					cycleButton.setValue(initial.toString().toUpperCase());
				} else
				{
					cycleButton.setValue(initial.toString());
				}
			} else if (inputWidget instanceof EditBox editBox)
			{
				editBox.setValue(initial.toString());
			}
		}
		
		public void refreshEntry()
		{
			value = ConfigListComponent.this.configHandler.get(name);
		}
		
		private Component getDisplayName()
		{
			
			SimpleConfig options = ConfigListComponent.this.configHandler.getConfigOptions(name);
			assert options != null;
			if (!options.displayName().isEmpty())
				return Component.literal(options.displayName());
			StringBuilder builder = new StringBuilder();
			for (int i = 0; i < name.length(); i++)
			{
				char c = name.charAt(i);
				if (Character.isUpperCase(c))
				{
					builder.append(' ');
				}
				builder.append(c);
			}
			return Component.literal(builder.toString().trim());
		}
	}
}
