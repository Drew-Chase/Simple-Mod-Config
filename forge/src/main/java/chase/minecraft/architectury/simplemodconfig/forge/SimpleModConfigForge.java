package chase.minecraft.architectury.simplemodconfig.forge;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimpleModConfig.MOD_ID)
public class SimpleModConfigForge
{
	public SimpleModConfigForge()
	{
		// Submit our event bus to let architectury register our content on the right time
		EventBuses.registerModEventBus(SimpleModConfig.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
		SimpleModConfig.init();
		
		
		if (Platform.isDevelopmentEnvironment())
		{
			SimpleModConfig.testBuilder
					.withKey(InputConstants.KEY_NUMPAD0, "simplemodconfig.category");
		}
	}
}