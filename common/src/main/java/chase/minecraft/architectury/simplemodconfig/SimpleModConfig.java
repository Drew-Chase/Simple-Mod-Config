package chase.minecraft.architectury.simplemodconfig;

import chase.minecraft.architectury.simplemodconfig.handlers.ConfigHandler;
import chase.minecraft.architectury.simplemodconfig.test.TestConfig;
import dev.architectury.platform.Platform;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleModConfig
{
	public static final String MOD_ID = "simple-mod-config";
	public static final Logger log = LogManager.getLogger(MOD_ID);
	public static SimpleModConfigBuilder builder;
	
	public static void init()
	{
		log.info("Initializing Simple Mod Config");
		if (Platform.isDevelopmentEnvironment())
		{
			Component displayName = Component.literal("Test Config");
			ConfigHandler<TestConfig> configHandler = new ConfigHandler<>("test-config", new TestConfig(), displayName);
			builder = new SimpleModConfigBuilder(configHandler)
					.withCommand("test-config", displayName);
		}
	}
}