import chase.minecraft.architectury.simplemodconfig.SimpleModConfigBuilder;
import chase.minecraft.architectury.simplemodconfig.client.gui.screen.ConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

public class ExampleModmenu implements ModMenuApi
{
	@Override
	public ConfigScreenFactory<ConfigScreen> getModConfigScreenFactory()
	{
		return new ConfigScreenFactory<>()
		{
			@Override
			public ConfigScreen create(Screen screen)
			{
				return new ConfigScreen(Example.builder.configHandler, screen);
			}
		};
	}
}
