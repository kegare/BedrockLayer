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

import net.minecraft.world.ChunkCoordIntPair;
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

import com.google.common.collect.Sets;
import com.kegare.bedrocklayer.api.BedrockLayerAPI;
import com.kegare.bedrocklayer.core.BedrockLayer;
import com.kegare.bedrocklayer.core.Config;
import com.kegare.bedrocklayer.core.FlattenEntry;

public class BedrockEventHooks
{
	public static final BedrockEventHooks instance = new BedrockEventHooks();

	public static final Set<FlattenEntry> flattenEntries = Sets.newLinkedHashSet();
	public static final Set<Long> layeredChunks = Sets.newHashSet();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onConfigChanged(OnConfigChangedEvent event)
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
			for (FlattenEntry entry : flattenEntries)
			{
				Property prop = entry.getConfigProperty(BedrockLayerAPI.getConfig());

				if (prop == null || !prop.isBooleanValue() || prop.getBoolean())
				{
					entry.flatten(chunk);
				}
			}

			if (Config.useLayeredCache)
			{
				layeredChunks.add(chunkSeed);
			}
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
			for (FlattenEntry entry : flattenEntries)
			{
				Property prop = entry.getConfigProperty(BedrockLayerAPI.getConfig());

				if (prop == null || !prop.isBooleanValue() || prop.getBoolean())
				{
					entry.flatten(chunk);
				}
			}
		}
	}
}