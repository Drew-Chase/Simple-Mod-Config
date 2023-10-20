import chase.minecraft.architectury.simplemodconfig.SimpleModConfigBuilder;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import net.fabricmc.api.ModInitializer;
import net.minecraft.text.Text;

public class Example implements ModInitializer
{
	
	public static SimpleModConfigBuilder builder;
	public static ConfigHandler<ExampleConfiguration> configHandler;
	
	
	@Override
	public void onInitialize()
	{
		initConfig();
	}
	
	public static void initConfig()
	{
		Text displayName = Text.of("Test Config");
		configHandler = new ConfigHandler<>("test-config", new ExampleConfiguration());
		builder = new SimpleModConfigBuilder(configHandler, displayName.getString())
				.withCommand("example-config", displayName);
	}
	
}
