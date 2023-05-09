package chase.minecraft.architectury.simplemodconfig.fabric;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;

public class TestModClientFabric implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		SimpleModConfig.builder
				.withKey(InputConstants.KEY_NUMPAD0, "simplemodconfig.category");
	}
}
