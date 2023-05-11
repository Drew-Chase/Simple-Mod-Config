package chase.minecraft.architectury.simplemodconfig.forge;

import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = SimpleModConfig.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class SimpleModConfigClientForge
{
	@SubscribeEvent
	static void onClientSetup(final FMLClientSetupEvent event)
	{
		SimpleModConfig.initClient();
	}
	
}
