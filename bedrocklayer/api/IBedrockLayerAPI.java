package bedrocklayer.api;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;

public interface IBedrockLayerAPI
{
	public void registerFlatten(int dim, int min, int max, IBlockState filler);

	public void registerFlatten(int dim, int min, int max, IBlockState filler, String configName, boolean configDefault);

	public boolean unregisterFlatten(int dim);

	public boolean flatten(Chunk chunk);
}