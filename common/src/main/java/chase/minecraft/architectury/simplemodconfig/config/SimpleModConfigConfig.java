package chase.minecraft.architectury.simplemodconfig.config;

import chase.minecraft.architectury.simplemodconfig.annotation.SimpleConfig;

public class SimpleModConfigConfig
{
	@SimpleConfig(displayName = "Show Button", description = "If the config button should be shown on the title screen")
	public boolean ShowTitleScreenButton = true;
}
