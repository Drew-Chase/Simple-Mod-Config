package chase.minecraft.architectury.simplemodconfig;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SimpleModConfig
{
	public static final String MOD_ID = "simple-mod-config";
	public static final Logger log = LogManager.getLogger(MOD_ID);
	
	public static void init()
	{
		log.info("Initializing Simple Mod Config");
	}
}