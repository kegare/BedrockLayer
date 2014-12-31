/*
 * BedrockLayer
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Translation, or MMPL_J.
 */

package com.kegare.bedrocklayer.core;

import static com.kegare.bedrocklayer.core.BedrockLayer.*;

import java.io.File;
import java.util.Map;

import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.kegare.bedrocklayer.api.BedrockLayerAPI;
import com.kegare.bedrocklayer.handler.BedrockEventHooks;
import com.kegare.bedrocklayer.handler.BedrockLayerAPIHandler;

@Mod
(
	modid = MODID,
	guiFactory = MOD_PACKAGE + ".client.config.BedrockGuiFactory"
)
public class BedrockLayer
{
	public static final String
	MODID = "kegare.bedrocklayer",
	MOD_PACKAGE = "com.kegare.bedrocklayer",
	CONFIG_LANG = "bedrocklayer.config.";

	@SidedProxy(modId = MODID, clientSide = MOD_PACKAGE + ".client.ClientProxy", serverSide = MOD_PACKAGE + ".core.CommonProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void construct(FMLConstructionEvent event)
	{
		BedrockLayerAPI.instance = new BedrockLayerAPIHandler();
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.initializeConfigEntries();

		Config.syncConfig();

		BedrockLayerAPI.registerFlatten(0, 1, 5, Blocks.stone.getDefaultState(), "overworld", true);
		BedrockLayerAPI.registerFlatten(-1, 1, 5, Blocks.netherrack.getDefaultState(), "netherLower", true);
		BedrockLayerAPI.registerFlatten(-1, 122, 127, Blocks.netherrack.getDefaultState(), "netherUpper", true);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		FMLCommonHandler.instance().bus().register(BedrockEventHooks.instance);

		MinecraftForge.EVENT_BUS.register(BedrockEventHooks.instance);
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		if (Loader.isModLoaded("TwilightForest"))
		{
			File file = new File(Loader.instance().getConfigDir(), "TwilightForest.cfg");

			if (file.exists() && file.canRead())
			{
				Property prop = new Configuration(file).getCategory("dimension").get("dimensionID");

				if (prop != null && prop.isIntValue())
				{
					BedrockLayerAPI.registerFlatten(prop.getInt(), 1, 5, Blocks.stone.getDefaultState(), "twilightforest", false);
				}
			}
		}
	}

	@EventHandler
	public void loaded(FMLLoadCompleteEvent event)
	{
		Config.syncConfig();
	}

	@EventHandler
	public void onServerStarting(FMLServerStartingEvent event)
	{
		BedrockEventHooks.layeredChunks.clear();
	}

	@NetworkCheckHandler
	public boolean netCheckHandler(Map<String, String> mods, Side side)
	{
		return true;
	}
}