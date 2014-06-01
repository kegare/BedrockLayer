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

import java.util.Map;

import net.minecraftforge.common.MinecraftForge;

import com.kegare.bedrocklayer.handler.BedrockEventHooks;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkCheckHandler;
import cpw.mods.fml.relauncher.Side;

@Mod(modid = "kegare.bedrocklayer")
public class BedrockLayer
{
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BedrockEventHooks());
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		Config.buildConfig();
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