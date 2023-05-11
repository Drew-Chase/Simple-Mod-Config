package chase.minecraft.architectury.simplemodconfig.fabric;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import chase.minecraft.architectury.simplemodconfig.client.gui.screen.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class SimpleModMenuModMenu implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		return (ConfigScreenFactory<ConfigScreen>) screen -> new ConfigScreen(SimpleModConfig.builder.configHandler, screen);
	}
}
