package chase.minecraft.architectury.simplemodconfig.forge;

import dev.architectury.platform.forge.EventBuses;
import chase.minecraft.architectury.simplemodconfig.SimpleModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SimpleModConfig.MOD_ID)
public class SimpleModConfigForge {
    public SimpleModConfigForge() {
		// Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(SimpleModConfig.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
            SimpleModConfig.init();
    }
}