package bedrocklayer.handler;

import java.util.Iterator;

import bedrocklayer.api.IBedrockLayerAPI;
import bedrocklayer.core.FlattenEntry;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;

public class BedrockLayerAPIHandler implements IBedrockLayerAPI
{
	@Override
	public void registerFlatten(int dim, int min, int max, IBlockState filler)
	{
		BedrockEventHooks.ENTRIES.add(new FlattenEntry(dim, min, max, filler));
	}

	@Override
	public void registerFlatten(int dim, int min, int max, IBlockState filler, String configName, boolean configDefault)
	{
		BedrockEventHooks.ENTRIES.add(new FlattenEntry(dim, min, max, filler).setConfig(configName, configDefault));
	}

	@Override
	public boolean unregisterFlatten(int dim)
	{
		boolean flag = false;

		for (Iterator<FlattenEntry> iterator = BedrockEventHooks.ENTRIES.iterator(); iterator.hasNext();)
		{
			if (iterator.next().dim == dim)
			{
				iterator.remove();

				if (!flag)
				{
					flag = true;
				}
			}
		}

		return flag;
	}

	@Override
	public boolean flatten(Chunk chunk)
	{
		boolean flag = false;

		for (FlattenEntry entry : BedrockEventHooks.ENTRIES)
		{
			if (entry.flatten(chunk) && !flag)
			{
				flag = true;
			}
		}

		return flag;
	}
}