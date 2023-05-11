package chase.minecraft.architectury.simplemodconfig.client.gui.component;

import chase.minecraft.architectury.simplemodconfig.client.gui.screen.ModsConfigListScreen;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import chase.minecraft.architectury.simplemodconfig.handlers.LoadedConfigs;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class ModConfigListComponent extends ContainerObjectSelectionList<ModConfigListComponent.Entry>
{
	private final ModsConfigListScreen parent;
	
	public ModConfigListComponent(ModsConfigListScreen parent)
	{
		super(Minecraft.getInstance(), 150, parent.height, 30, parent.height - 32, 30);
		this.parent = parent;
		search("");
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
	
	public void search(String query)
	{
		clearEntries();
		LoadedConfigs.getInstance().get().forEach((item) ->
		{
			if (query.isEmpty() || item.getKey().toLowerCase().contains(query.toLowerCase()))
			{
				addEntry(new LoadedModEntry(item.getKey(), item.getValue()));
			}
		});
		refreshEntries();
	}
	
	@Environment(EnvType.CLIENT)
	public static abstract class Entry extends ContainerObjectSelectionList.Entry<Entry>
	{
		public abstract void refreshEntry();
	}
	
	public class LoadedModEntry extends Entry
	{
		private final ConfigHandler<?> value;
		private final String name;
		private boolean isHovering = false;
		
		public LoadedModEntry(String name, ConfigHandler<?> value)
		{
			this.name = name;
			this.value = value;
		}
		
		@Override
		public void render(@NotNull PoseStack poseStack, int x, int y, int uk, int widgetWidth, int widgetHeight, int mouseX, int mouseY, boolean isHovering, float partialTicks)
		{
			int parentWidth = ModConfigListComponent.this.width;
			this.isHovering = isHovering;
			if (isHovering || ModConfigListComponent.this.parent.isLoaded(name))
			{
				RenderSystem.setShaderColor(0f, 0f, 0f, .5f);
				fill(poseStack, x, y, x + parentWidth, y + widgetHeight, 0xFF_FF_FF_FF);
				RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
			}
			
			// Render Label
			ModConfigListComponent.this.minecraft.font.draw(poseStack, this.name, 5, y + minecraft.font.lineHeight, 0xFF_FF_FF);
		}
		
		@Override
		public boolean mouseClicked(double x, double y, int mouseButton)
		{
			if (isHovering && mouseButton == InputConstants.MOUSE_BUTTON_LEFT && !ModConfigListComponent.this.parent.isLoaded(name))
			{
				ModConfigListComponent.this.parent.load(this.name, this.value);
				Minecraft.getInstance().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f));
			}
			return super.mouseClicked(x, y, mouseButton);
		}
		
		@Override
		public @NotNull List<? extends NarratableEntry> narratables()
		{
			return new ArrayList<>();
		}
		
		@Override
		public @NotNull List<? extends GuiEventListener> children()
		{
			return new ArrayList<>();
		}
		
		@Override
		public void refreshEntry()
		{
		
		}
	}
}
