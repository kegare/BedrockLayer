/*
 * BedrockLayer
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Translation, or MMPL_J.
 */

package com.kegare.bedrocklayer.client;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import com.kegare.bedrocklayer.client.config.CycleIntegerEntry;
import com.kegare.bedrocklayer.core.CommonProxy;
import com.kegare.bedrocklayer.core.Config;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public void initializeConfigEntries()
	{
		Config.cycleInteger = CycleIntegerEntry.class;
	}
}
