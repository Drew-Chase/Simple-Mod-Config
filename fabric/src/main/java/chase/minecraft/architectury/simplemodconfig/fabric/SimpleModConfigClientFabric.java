package chase.minecraft.architectury.simplemodconfig.fabric;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.platform.Platform;
import net.fabricmc.api.ClientModInitializer;

public class SimpleModConfigClientFabric implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		SimpleModConfig.initClient();
	}
	
}
