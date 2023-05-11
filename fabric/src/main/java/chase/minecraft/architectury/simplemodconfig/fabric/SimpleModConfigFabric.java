package chase.minecraft.architectury.simplemodconfig.fabric;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import net.fabricmc.api.ModInitializer;

public class SimpleModConfigFabric implements ModInitializer
{
	@Override
	public void onInitialize()
	{
		SimpleModConfig.init();
	}
}