/*
 * BedrockLayer
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Translation, or MMPL_J.
 */

package com.kegare.bedrocklayer.core;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.StatCollector;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

public class FlattenEntry
{
	public int dim;
	public int minHeight;
	public int maxHeight;
	public IBlockState filler;

	public String configName;
	public boolean configDefault;

	public FlattenEntry(int dim, int min, int max, IBlockState filler)
	{
		this.dim = dim;
		this.minHeight = min;
		this.maxHeight = max;
		this.filler = filler;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(dim, minHeight, maxHeight, filler);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof FlattenEntry)
		{
			FlattenEntry entry = (FlattenEntry)obj;

			return dim == entry.dim && minHeight == entry.minHeight && maxHeight == entry.maxHeight && filler == entry.filler;
		}

		return false;
	}

	public FlattenEntry setConfig(String name, boolean defaultValue)
	{
		configName = name;
		configDefault = defaultValue;

		return this;
	}

	public Property getConfigProperty(Configuration config)
	{
		if (Strings.isNullOrEmpty(configName))
		{
			return null;
		}

		Property prop = config.get("bedrocklayer", configName, configDefault);
		prop.setLanguageKey(BedrockLayer.CONFIG_LANG + prop.getName());
		prop.comment = StatCollector.translateToLocal(prop.getLanguageKey() + ".tooltip");
		prop.comment += " [default: " + prop.getDefault() + "]";

		return prop;
	}

	public boolean flatten(Chunk chunk)
	{
		if (chunk.getWorld().provider.getDimensionId() != dim)
		{
			return false;
		}

		for (int x = 0; x < 16; ++x)
		{
			for (int z = 0; z < 16; ++z)
			{
				for (int y = minHeight; y < maxHeight; ++y)
				{
					if (chunk.getBlock(x, y, z) == Blocks.bedrock)
					{
						chunk.setBlockState(new BlockPos(x, y, z), filler);
					}
				}
			}
		}

		return true;
	}
}