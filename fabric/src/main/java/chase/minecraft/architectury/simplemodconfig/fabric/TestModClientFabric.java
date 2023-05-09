package chase.minecraft.architectury.simplemodconfig.fabric;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import com.mojang.blaze3d.platform.InputConstants;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.architectury.platform.Platform;
import net.fabricmc.api.ClientModInitializer;

public class TestModClientFabric implements ClientModInitializer, ModMenuApi
{
	@Override
	public void onInitializeClient()
	{
		if (Platform.isDevelopmentEnvironment())
		{
			SimpleModConfig.builder
					.withKey(InputConstants.KEY_NUMPAD0, "simplemodconfig.category");
		}
	}
	
}
