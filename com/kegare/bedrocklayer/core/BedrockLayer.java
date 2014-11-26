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
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

import com.kegare.bedrocklayer.handler.BedrockEventHooks;

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