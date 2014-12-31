/*
 * BedrockLayer
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Translation, or MMPL_J.
 */

package com.kegare.bedrocklayer.handler;

import java.util.Iterator;

import net.minecraft.block.state.IBlockState;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.config.Configuration;

import com.kegare.bedrocklayer.api.IBedrockLayerAPI;
import com.kegare.bedrocklayer.core.Config;
import com.kegare.bedrocklayer.core.FlattenEntry;

public class BedrockLayerAPIHandler implements IBedrockLayerAPI
{
	@Override
	public Configuration getConfig()
	{
		return Config.config;
	}

	@Override
	public void registerFlatten(int dim, int min, int max, IBlockState filler)
	{
		BedrockEventHooks.flattenEntries.add(new FlattenEntry(dim, min, max, filler));
	}

	@Override
	public void registerFlatten(int dim, int min, int max, IBlockState filler, String configName, boolean configDefault)
	{
		BedrockEventHooks.flattenEntries.add(new FlattenEntry(dim, min, max, filler).setConfig(configName, configDefault));
	}

	@Override
	public boolean unregisterFlatten(int dim)
	{
		boolean flag = false;

		for (Iterator<FlattenEntry> iterator = BedrockEventHooks.flattenEntries.iterator(); iterator.hasNext();)
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

		for (FlattenEntry entry : BedrockEventHooks.flattenEntries)
		{
			if (entry.flatten(chunk) && !flag)
			{
				flag = true;
			}
		}

		return flag;
	}
}