import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.util.InputUtil;

public class ExampleClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		Example.builder.withKey(InputUtil.GLFW_KEY_N, "example-config");
	}
}
