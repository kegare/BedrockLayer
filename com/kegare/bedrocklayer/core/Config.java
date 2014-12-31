/*
 * BedrockLayer
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Translation, or MMPL_J.
 */

package com.kegare.bedrocklayer.core;

import java.io.File;
import java.util.List;

import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.common.Loader;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;
import com.kegare.bedrocklayer.handler.BedrockEventHooks;
import com.kegare.bedrocklayer.util.BedrockLog;

public class Config
{
	public static Configuration config;

	public static int flattenType;
	public static boolean useLayeredCache;

	public static Class<? extends IConfigEntry> cycleInteger;

	public static void syncConfig()
	{
		if (config == null)
		{
			File file = new File(Loader.instance().getConfigDir(), "BedrockLayer.cfg");
			config = new Configuration(file);

			try
			{
				config.load();
			}
			catch (Exception e)
			{
				File dest = new File(file.getParentFile(), file.getName() + ".bak");

				if (dest.exists())
				{
					dest.delete();
				}

				file.renameTo(dest);

				BedrockLog.log(Level.ERROR, e, "A critical error occured reading the " + file.getName() + " file, defaults will be used - the invalid file is backed up at " + dest.getName());
			}
		}

		String category = Configuration.CATEGORY_GENERAL;
		Property prop;
		List<String> propOrder = Lists.newArrayList();

		prop = config.get(category, "flattenType", 0);
		prop.setMinValue(0).setMaxValue(1).setLanguageKey(BedrockLayer.CONFIG_LANG + category + "." + prop.getName()).setConfigEntryClass(cycleInteger);
		prop.comment = StatCollector.translateToLocal(prop.getLanguageKey() + ".tooltip");
		prop.comment += " [range: " + prop.getMinValue() + " ~ " + prop.getMaxValue() + ", default: " + prop.getDefault() + "]";
		propOrder.add(prop.getName());
		flattenType = MathHelper.clamp_int(prop.getInt(flattenType), Integer.parseInt(prop.getMinValue()), Integer.parseInt(prop.getMaxValue()));

		if (flattenType < 0 || flattenType > 1)
		{
			flattenType = 0;

			prop.set(flattenType);
		}

		prop = config.get(category, "useLayeredCache", true);
		prop.setLanguageKey(BedrockLayer.CONFIG_LANG + category + "." + prop.getName());
		prop.comment = StatCollector.translateToLocal(prop.getLanguageKey() + ".tooltip");
		prop.comment += " [default: " + prop.getDefault() + "]";
		propOrder.add(prop.getName());
		useLayeredCache = prop.getBoolean(useLayeredCache);

		config.setCategoryPropertyOrder(category, propOrder);

		category = "bedrocklayer";
		propOrder = Lists.newArrayList();

		for (FlattenEntry entry : BedrockEventHooks.flattenEntries)
		{
			prop = entry.getConfigProperty(config);

			if (prop != null)
			{
				propOrder.add(prop.getName());

				System.out.println(prop.getName());
			}
		}

		config.setCategoryPropertyOrder(category, propOrder);

		if (config.hasChanged())
		{
			config.save();
		}
	}
}