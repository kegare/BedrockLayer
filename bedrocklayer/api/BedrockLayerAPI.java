package bedrocklayer.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;

/**
 * NOTE: Do NOT access to this class fields.
 * You should use API from this class methods.
 */
public final class BedrockLayerAPI
{
	public static IBedrockLayerAPI instance;

	private BedrockLayerAPI() {}

	public static void registerFlatten(int dim, int min, int max, IBlockState filler)
	{
		if (instance != null)
		{
			instance.registerFlatten(dim, min, max, filler);
		}
	}

	public static void registerFlatten(int dim, int min, int max, IBlockState filler, String configName, boolean configDefault)
	{
		if (instance != null)
		{
			instance.registerFlatten(dim, min, max, filler, configName, configDefault);
		}
	}

	public static boolean unregisterFlatten(int dim)
	{
		return instance != null && instance.unregisterFlatten(dim);
	}

	public static void flatten(Chunk chunk)
	{
		if (instance != null)
		{
			instance.flatten(chunk);
		}
	}
}