package bedrocklayer.handler;

import java.util.Set;

import com.google.common.collect.Sets;

import bedrocklayer.core.BedrockLayer;
import bedrocklayer.core.Config;
import bedrocklayer.core.FlattenEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BedrockEventHooks
{
	public static final Set<FlattenEntry> ENTRIES = Sets.newLinkedHashSet();
	public static final Set<Long> LAYERED_CHUNKS = Sets.newHashSet();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
	{
		if (event.getModID().equals(BedrockLayer.MODID))
		{
			Config.syncConfig();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChunkLoad(ChunkEvent.Load event)
	{
		World world = event.getWorld();

		if (world.isRemote || Config.flattenType != 0)
		{
			return;
		}

		Chunk chunk = event.getChunk();
		long chunkSeed = ChunkPos.asLong(chunk.xPosition, chunk.zPosition) ^ world.provider.getDimension();

		if (chunk.isLoaded() && (!Config.useLayeredCache || !LAYERED_CHUNKS.contains(chunkSeed)))
		{
			for (FlattenEntry entry : ENTRIES)
			{
				Property prop = entry.getConfigProperty(Config.config);

				if (prop == null || !prop.isBooleanValue() || prop.getBoolean())
				{
					entry.flatten(chunk);
				}
			}

			if (Config.useLayeredCache)
			{
				LAYERED_CHUNKS.add(chunkSeed);
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPrePopulateChunk(PopulateChunkEvent.Pre event)
	{
		World world = event.getWorld();

		if (world.isRemote || Config.flattenType != 1)
		{
			return;
		}

		Chunk chunk = world.getChunkFromChunkCoords(event.getChunkX(), event.getChunkZ());

		if (chunk.isLoaded())
		{
			for (FlattenEntry entry : ENTRIES)
			{
				Property prop = entry.getConfigProperty(Config.config);

				if (prop == null || !prop.isBooleanValue() || prop.getBoolean())
				{
					entry.flatten(chunk);
				}
			}
		}
	}
}