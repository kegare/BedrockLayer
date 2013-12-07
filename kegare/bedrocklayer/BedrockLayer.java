package kegare.bedrocklayer;

import java.io.File;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.EventPriority;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
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
	private static boolean overworld;
	private static boolean netherUpper;
	private static boolean netherLower;

	private static boolean useLayeredCache = true;

	private static final Set<Long> layeredChunks = Sets.newHashSet();
	private static final Set<Long> netherLayeredChunks = Sets.newHashSet();

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		Configuration cfg = new Configuration(new File(event.getModConfigurationDirectory(), "BedrockLayer.cfg"));

		try
		{
			cfg.load();

			cfg.addCustomCategoryComment("bedrocklayer", "If multiplayer, server-side only.");
			cfg.addCustomCategoryComment("advanced", "You don't need to change this category settings normally.");

			overworld = cfg.get("bedrocklayer", "overworld", true, "Flatten uneven bedrock layers of the Overworld. [true/false]").getBoolean(true);
			netherUpper = cfg.get("bedrocklayer", "netherUpper", true, "Flatten uneven upper bedrock layers of the Nether. [true/false]").getBoolean(true);
			netherLower = cfg.get("bedrocklayer", "netherLower", true, "Flatten uneven lower bedrock layers of the Nether. [true/false]").getBoolean(true);

			useLayeredCache = cfg.get("advanced", "useLayeredCache", true, "Flatten more efficiently using layered chunks cache. [true/false] Note: If multiplayer, server-side only.").getBoolean(true);
		}
		finally
		{
			if (cfg.hasChanged())
			{
				cfg.save();
			}
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(this);
	}

	@ForgeSubscribe(priority = EventPriority.HIGHEST)
	public void onChunkLoad(ChunkEvent.Load event)
	{
		World world = event.world;
		Chunk chunk = event.getChunk();
		long chunkSeed = ChunkCoordIntPair.chunkXZ2Int(chunk.xPosition, chunk.zPosition);

		if (!world.isRemote && chunk.isChunkLoaded)
		{
			if (overworld && world.provider.dimensionId == 0 && (useLayeredCache ? !layeredChunks.contains(chunkSeed) : true))
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; chunk.getBlockID(x, 0, z) == Block.bedrock.blockID && y < 5; ++y)
						{
							if (chunk.getBlockID(x, y, z) == Block.bedrock.blockID)
							{
								chunk.setBlockIDWithMetadata(x, y, z, Block.stone.blockID, 0);
							}
						}
					}
				}

				if (useLayeredCache) layeredChunks.add(chunkSeed);
			}

			if ((netherUpper || netherLower) && world.provider.dimensionId == -1 && (useLayeredCache ? !netherLayeredChunks.contains(chunkSeed) : true))
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; netherUpper && chunk.getBlockID(x, 0, z) == Block.bedrock.blockID && y < 5; ++y)
						{
							if (chunk.getBlockID(x, y, z) == Block.bedrock.blockID)
							{
								chunk.setBlockIDWithMetadata(x, y, z, Block.netherrack.blockID, 0);
							}
						}

						for (int y = 126; netherLower && chunk.getBlockID(x, 127, z) == Block.bedrock.blockID && y > 122; --y)
						{
							if (chunk.getBlockID(x, y, z) == Block.bedrock.blockID)
							{
								chunk.setBlockIDWithMetadata(x, y, z, Block.netherrack.blockID, 0);
							}
						}
					}
				}

				if (useLayeredCache) netherLayeredChunks.add(chunkSeed);
			}
		}
	}

	@ForgeSubscribe(priority = EventPriority.HIGH)
	public void onWorldUnload(WorldEvent.Unload event)
	{
		World world = event.world;

		if (!world.isRemote && useLayeredCache)
		{
			if (overworld && world.provider.dimensionId == 0)
			{
				layeredChunks.clear();
			}

			if ((netherUpper || netherLower) && world.provider.dimensionId == -1)
			{
				netherLayeredChunks.clear();
			}
		}
	}
}