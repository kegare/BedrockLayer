/*
 * BedrockLayer
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Translation, or MMPL_J.
 */

package com.kegare.bedrocklayer.handler;

import java.util.Set;

import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.google.common.collect.Sets;
import com.kegare.bedrocklayer.core.BedrockLayer;
import com.kegare.bedrocklayer.core.Config;

public class BedrockEventHooks
{
	public static final BedrockEventHooks instance = new BedrockEventHooks();

	public static final Set<Long> layeredChunks = Sets.newHashSet();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent event)
	{
		if (event.modID.equals(BedrockLayer.MODID))
		{
			Config.syncConfig();
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChunkLoad(ChunkEvent.Load event)
	{
		World world = event.world;

		if (world.isRemote || Config.flattenType != 0)
		{
			return;
		}

		Chunk chunk = event.getChunk();
		long chunkSeed = ChunkCoordIntPair.chunkXZ2Int(chunk.xPosition, chunk.zPosition) ^ world.provider.getDimensionId();

		if (chunk.isLoaded() && (!Config.useLayeredCache || !layeredChunks.contains(chunkSeed)))
		{
			if (Config.overworld && world.provider.getDimensionId() == 0)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.setBlockState(new BlockPos(x, y, z), Blocks.stone.getDefaultState());
							}
						}
					}
				}
			}

			if ((Config.netherUpper || Config.netherLower) && world.provider.getDimensionId() == -1)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 126; Config.netherUpper && chunk.getBlock(x, 127, z) == Blocks.bedrock && y > 122; --y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.setBlockState(new BlockPos(x, y, z), Blocks.netherrack.getDefaultState());
							}
						}

						for (int y = 1; Config.netherLower && chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.setBlockState(new BlockPos(x, y, z), Blocks.netherrack.getDefaultState());
							}
						}
					}
				}
			}

			if (Config.useLayeredCache) layeredChunks.add(chunkSeed);
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onPrePopulateChunk(PopulateChunkEvent.Pre event)
	{
		World world = event.world;

		if (world.isRemote || Config.flattenType != 1)
		{
			return;
		}

		Chunk chunk = world.getChunkFromChunkCoords(event.chunkX, event.chunkZ);

		if (chunk.isLoaded())
		{
			if (Config.overworld && world.provider.getDimensionId() == 0)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 1; chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.setBlockState(new BlockPos(x, y, z), Blocks.stone.getDefaultState());
							}
						}
					}
				}
			}

			if ((Config.netherUpper || Config.netherLower) && world.provider.getDimensionId() == -1)
			{
				for (int x = 0; x < 16; ++x)
				{
					for (int z = 0; z < 16; ++z)
					{
						for (int y = 126; Config.netherUpper && chunk.getBlock(x, 127, z) == Blocks.bedrock && y > 122; --y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.setBlockState(new BlockPos(x, y, z), Blocks.netherrack.getDefaultState());
							}
						}

						for (int y = 1; Config.netherLower && chunk.getBlock(x, 0, z) == Blocks.bedrock && y < 5; ++y)
						{
							if (chunk.getBlock(x, y, z) == Blocks.bedrock)
							{
								chunk.setBlockState(new BlockPos(x, y, z), Blocks.netherrack.getDefaultState());
							}
						}
					}
				}
			}
		}
	}
}