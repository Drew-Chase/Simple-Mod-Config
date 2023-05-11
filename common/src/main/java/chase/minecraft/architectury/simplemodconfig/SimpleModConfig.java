package chase.minecraft.architectury.simplemodconfig;

import chase.minecraft.architectury.simplemodconfig.config.SimpleModConfigConfig;
import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import chase.minecraft.architectury.simplemodconfig.test.TestConfig;
import dev.architectury.platform.Platform;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleModConfig
{
	public static final String MOD_ID = "simplemodconfig";
	public static final Logger log = LogManager.getLogger(MOD_ID);
	public static SimpleModConfigBuilder testBuilder;
	public static SimpleModConfigBuilder builder;
	
	public static ConfigHandler<SimpleModConfigConfig> configHandler = new ConfigHandler<>(MOD_ID, new SimpleModConfigConfig());
	
	public static void init()
	{
		log.info("Initializing Simple Mod Config");
		if (Platform.isDevelopmentEnvironment())
		{
			String displayName = "Test Config";
			testBuilder = new SimpleModConfigBuilder(new ConfigHandler<>("test-config", new TestConfig()), displayName)
					.withCommand("test-config", Component.literal(displayName));
		}
	}
	
	@Environment(EnvType.CLIENT)
	public static void initClient()
	{
		builder = new SimpleModConfigBuilder(configHandler, "Simple Mod Config");
	}
	
	public static ResourceLocation id(String id)
	{
		return new ResourceLocation(MOD_ID, id);
	}
}