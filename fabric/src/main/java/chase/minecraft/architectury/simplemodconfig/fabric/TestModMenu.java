package chase.minecraft.architectury.simplemodconfig.fabric;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import chase.minecraft.architectury.simplemodconfig.client.gui.screen.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.architectury.platform.Platform;

public class TestModMenu implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<?> getModConfigScreenFactory()
	{
		if (Platform.isDevelopmentEnvironment())
		{
			return (ConfigScreenFactory<ConfigScreen>) screen -> new ConfigScreen(SimpleModConfig.builder.configHandler, screen);
		}
		return ModMenuApi.super.getModConfigScreenFactory();
	}
}
