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

import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import com.kegare.bedrocklayer.handler.BedrockEventHooks;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

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
	public void preInit(FMLPreInitializationEvent event)
	{
		proxy.initializeConfigEntries();
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