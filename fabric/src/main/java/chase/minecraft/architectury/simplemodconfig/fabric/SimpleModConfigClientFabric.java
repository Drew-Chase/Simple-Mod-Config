package chase.minecraft.architectury.simplemodconfig.fabric;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import net.fabricmc.api.ClientModInitializer;

public class SimpleModConfigClientFabric implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		SimpleModConfig.initClient();
	}
	
}
