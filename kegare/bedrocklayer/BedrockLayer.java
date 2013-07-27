package kegare.bedrocklayer;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod
(
	modid = "kegare.bedrocklayer"
)
@NetworkMod
(
	clientSideRequired = false,
	serverSideRequired = false
)
public class BedrockLayer
{
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@ForgeSubscribe
	public void onChunkLoad(ChunkEvent.Load event)
	{
		World world = event.world;
		Chunk chunk = event.getChunk();

		if (!world.isRemote && chunk.isChunkLoaded)
		{
			if (world.provider.dimensionId == 0)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; y < 5; ++y)
						{
							if (chunk.getBlockID(x, y, z) == Block.bedrock.blockID)
							{
								chunk.setBlockIDWithMetadata(x, y, z, Block.stone.blockID, 0);
							}
						}
					}
				}
			}
			else if (world.provider.dimensionId == -1)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; y < 5; ++y)
						{
							if (chunk.getBlockID(x, y, z) == Block.bedrock.blockID)
							{
								chunk.setBlockIDWithMetadata(x, y, z, Block.netherrack.blockID, 0);
							}

							if (chunk.getBlockID(x, 127 - y, z) == Block.bedrock.blockID)
							{
								chunk.setBlockIDWithMetadata(x, 127 - y, z, Block.netherrack.blockID, 0);
							}
						}
					}
				}
			}
		}
	}
}