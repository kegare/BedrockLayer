/*
 * BedrockLayer
 *
 * Copyright (c) 2014 kegare
 * https://github.com/kegare
 *
 * This mod is distributed under the terms of the Minecraft Mod Public License Japanese Translation, or MMPL_J.
 */

package com.kegare.bedrocklayer.client.config;

import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;

import com.google.common.collect.Lists;
import com.kegare.bedrocklayer.core.BedrockLayer;
import com.kegare.bedrocklayer.core.Config;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BedrockConfigGui extends GuiConfig
{
	public BedrockConfigGui(GuiScreen parent)
	{
		super(parent, getConfigElements(), BedrockLayer.MODID, false, false, I18n.format(BedrockLayer.CONFIG_LANG + "title"));
	}

	private static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = Lists.newArrayList();

		for (String category : Config.config.getCategoryNames())
		{
			list.addAll(new ConfigElement(Config.config.getCategory(category)).getChildElements());
		}

		return list;
	}
}