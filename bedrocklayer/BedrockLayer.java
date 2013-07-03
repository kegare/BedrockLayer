package bedrocklayer;

import net.minecraft.block.Block;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkMod;

@Mod
(
	modid = "bedrocklayer",
	name = "BedrockLayer",
	version = BedrockLayer.version,
	dependencies = "required-after:Forge"
)
@NetworkMod
(
	clientSideRequired = false,
	serverSideRequired = false
)
public class BedrockLayer
{
	static final String version = "1.0.3";

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		event.getModMetadata().version = version;
	}

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
						if (chunk.getBlockID(x, 0, z) == Block.bedrock.blockID)
						{
							for (int y = 1; y <= 5; ++y)
							{
								if (chunk.getBlockID(x, y, z) == Block.bedrock.blockID)
								{
									chunk.setBlockIDWithMetadata(x, y, z, Block.stone.blockID, 0);
								}
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
						if (chunk.getBlockID(x, 0, z) == Block.bedrock.blockID)
						{
							for (int y = 1; y <= 5; ++y)
							{
								if (chunk.getBlockID(x, y, z) == Block.bedrock.blockID)
								{
									chunk.setBlockIDWithMetadata(x, y, z, Block.netherrack.blockID, 0);
								}
							}
						}

						if (chunk.getBlockID(x, 128, z) == Block.bedrock.blockID)
						{
							for (int y = 1; y <= 5; ++y)
							{
								if (chunk.getBlockID(x, 128 - y, z) == Block.bedrock.blockID)
								{
									chunk.setBlockIDWithMetadata(x, 128 - y, z, Block.netherrack.blockID, 0);
								}
							}
						}
					}
				}
			}
		}
	}
}