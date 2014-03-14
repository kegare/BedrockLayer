/*
 * BedrockLayer
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package com.kegare.bedrocklayer.handler;

import com.google.common.collect.Sets;
import com.kegare.bedrocklayer.core.Config;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.init.Blocks;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkEvent;

import java.util.Set;

public class BedrockEventHooks
{
	public static final Set<Long> layeredChunks = Sets.newHashSet();

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onChunkLoad(ChunkEvent.Load event)
	{
		World world = event.world;

		if (world.isRemote || Config.flattenType != 0)
		{
			return;
		}

		Chunk chunk = event.getChunk();
		long chunkSeed = ChunkCoordIntPair.chunkXZ2Int(chunk.xPosition, chunk.zPosition) ^ world.provider.dimensionId;

		if (!world.isRemote && chunk.isChunkLoaded && (!Config.useLayeredCache || !layeredChunks.contains(chunkSeed)))
		{
			if (Config.overworld && world.provider.dimensionId == 0)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.func_150807_a(x, y, z, Blocks.stone, 0);
							}
						}
					}
				}
			}

			if ((Config.netherUpper || Config.netherLower) && world.provider.dimensionId == -1)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 126; Config.netherUpper && chunk.getBlock(x, 127, z) == Blocks.bedrock && y > 122; --y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.func_150807_a(x, y, z, Blocks.netherrack, 0);
							}
						}

						for (int y = 1; Config.netherLower && chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.func_150807_a(x, y, z, Blocks.netherrack, 0);
							}
						}
					}
				}
			}


			if (Config.twilightforest && world.provider.getDimensionName().equals("Twilight Forest"))
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.func_150807_a(x, y, z, Blocks.stone, 0);
							}
						}
					}
				}
			}

			if (Config.useLayeredCache) layeredChunks.add(chunkSeed);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGH)
	public void onPostPopulateChunk(PopulateChunkEvent.Post event)
	{
		World world = event.world;

		if (world.isRemote || Config.flattenType != 1)
		{
			return;
		}

		Chunk chunk = world.getChunkFromChunkCoords(event.chunkX, event.chunkZ);

		if (!world.isRemote && chunk.isChunkLoaded)
		{
			if (Config.overworld && world.provider.dimensionId == 0)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.func_150807_a(x, y, z, Blocks.stone, 0);
							}
						}
					}
				}
			}

			if ((Config.netherUpper || Config.netherLower) && world.provider.dimensionId == -1)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 126; Config.netherUpper && chunk.getBlock(x, 127, z) == Blocks.bedrock && y > 122; --y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.func_150807_a(x, y, z, Blocks.netherrack, 0);
							}
						}

						for (int y = 1; Config.netherLower && chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.func_150807_a(x, y, z, Blocks.netherrack, 0);
							}
						}
					}
				}
			}

			if (Config.twilightforest && world.provider.getDimensionName().equals("Twilight Forest"))
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.func_150807_a(x, y, z, Blocks.stone, 0);
							}
						}
					}
				}
			}
		}
	}
}