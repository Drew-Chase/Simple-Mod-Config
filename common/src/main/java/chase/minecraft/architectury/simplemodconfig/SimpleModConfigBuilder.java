package chase.minecraft.architectury.simplemodconfig;

import chase.minecraft.architectury.simplemodconfig.client.gui.screen.ConfigScreen;
import chase.minecraft.architectury.simplemodconfig.handlers.CommandHandler;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import chase.minecraft.architectury.simplemodconfig.handlers.LoadedConfigs;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.event.events.common.CommandRegistrationEvent;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class SimpleModConfigBuilder
{
	
	@Nullable
	private CommandHandler cmdHandler = null;
	public final ConfigHandler<?> configHandler;
	public final String modDisplayName;
	
	public SimpleModConfigBuilder(ConfigHandler<?> configHandler, String modDisplayName)
	{
		this.configHandler = configHandler;
		configHandler.setDisplayName(Component.literal(modDisplayName));
		this.modDisplayName = modDisplayName;
		LoadedConfigs.getInstance().add(modDisplayName, configHandler);
	}
	
	/**
	 * adds a command to the server
	 *
	 * @param name the name of the command - ex: /{name}
	 * @return The Simple Mod Config Builder
	 */
	public SimpleModConfigBuilder withCommand(String name)
	{
		return withCommand(name, Component.literal(name.replaceAll("-", " ").toUpperCase()));
	}
	
	/**
	 * adds a command to the server
	 *
	 * @param name        the name of the command - ex: /{name}
	 * @param displayName the display name of the command, this is shown when listing config items
	 * @return The Simple Mod Config Builder
	 */
	public SimpleModConfigBuilder withCommand(String name, Component displayName)
	{
		return withCommand(name, displayName, 4);
	}
	
	/**
	 * @param name            the name of the command - ex: /{name}
	 * @param displayName     the display name of the command, this is shown when listing config items
	 * @param permissionLevel the permission level of the command, default is 4 or operators only
	 * @return The Simple Mod Config Builder
	 */
	public SimpleModConfigBuilder withCommand(String name, Component displayName, int permissionLevel)
	{
		CommandRegistrationEvent.EVENT.register((dispatcher, registry, selection) ->
		{
			cmdHandler = new CommandHandler(name, displayName, configHandler);
			cmdHandler.register(dispatcher, permissionLevel);
		});
		return this;
	}
	
	/**
	 * Creates a keybinding to open config screen
	 *
	 * @param key             The keycode of the default key - See InputConstants
	 * @param controlCategory The display name of the mod, this is used for the controls category
	 * @return The Simple Mod Config Builder
	 */
	@Environment(EnvType.CLIENT)
	public SimpleModConfigBuilder withKey(int key, String controlCategory)
	{
		KeyMapping mapping = new KeyMapping("simplemodconfig.openconfig", key, controlCategory);
		KeyMappingRegistry.register(mapping);
		ClientTickEvent.CLIENT_POST.register(instance ->
		{
			if (mapping.consumeClick())
			{
				instance.setScreen(new ConfigScreen(configHandler, instance.screen));
			}
		});
		return this;
	}
}
