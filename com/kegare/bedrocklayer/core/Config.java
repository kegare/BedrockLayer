/*
 * BedrockLayer
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License 1.0, or MMPL.
 * Please check the contents of the license located in http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package com.kegare.bedrocklayer.core;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

import com.kegare.bedrocklayer.util.BedrockLog;

import cpw.mods.fml.common.Loader;

public class Config
{
	public static boolean overworld;
	public static boolean netherUpper;
	public static boolean netherLower;
	public static boolean twilightforest;

	public static int flattenType = 0;
	public static boolean useLayeredCache = true;

	static void buildConfig()
	{
		File file = new File(Loader.instance().getConfigDir(), "BedrockLayer.cfg");
		Configuration config = new Configuration(file);

		try
		{
			config.load();

			config.addCustomCategoryComment
			(
				"bedrocklayer",
				"If multiplayer, server-side only."
			);
			config.addCustomCategoryComment
			(
				"advanced",
				"You don't need to change this category settings normally." + Configuration.NEW_LINE +
				"If multiplayer, server-side only."
			);

			String category = "bedrocklayer";
			Property prop;

			prop = config.get(category, "overworld", true);
			prop.comment = "Flatten uneven bedrock layers of the Overworld. [true/false]";
			overworld = prop.getBoolean(false);
			prop = config.get(category, "netherUpper", true);
			prop.comment = "Flatten uneven upper bedrock layers of the Nether. [true/false]";
			netherUpper = prop.getBoolean(false);
			prop = config.get(category, "netherLower", true);
			prop.comment = "Flatten uneven lower bedrock layers of the Nether. [true/false]";
			netherLower = prop.getBoolean(false);

			if (Loader.isModLoaded("TwilightForest"))
			{
				prop = config.get(category, "twilightforest", false);
				prop.comment = "Flatten uneven bedrock layers of the Twilight Forest. [true/false]";
				twilightforest = prop.getBoolean(false);
			}

			category = "advanced";

			prop = config.get(category, "flattenType", 0);
			prop.comment = "Specify the flatten type. [0-1]";
			prop.comment += Configuration.NEW_LINE;
			prop.comment += "0=Flatten uneven bedrock layers when loading chunk, ";
			prop.comment += "1=Flatten uneven bedrock layers when generating chunk";
			flattenType = prop.getInt();

			if (flattenType < 0 || flattenType > 1)
			{
				prop.set(0);
			}

			prop = config.get(category, "useLayeredCache", true);
			prop.comment = "Flatten more efficiently using layered chunks cache. [true/false]";
			useLayeredCache = prop.getBoolean(true);
		}
		catch (Exception e)
		{
			File dest = new File(file.getParentFile(), file.getName() + ".bak");

			if (dest.exists())
			{
				dest.delete();
			}

			file.renameTo(dest);

			BedrockLog.severe("A critical error occured reading the " + file.getName() + " file, defaults will be used - the invalid file is backed up at " + dest.getName(), e);
		}
		finally
		{
			if (config.hasChanged())
			{
				config.save();
			}
		}
	}
}