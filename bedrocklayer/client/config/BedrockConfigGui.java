package bedrocklayer.client.config;

import java.util.List;

import com.google.common.collect.Lists;

import bedrocklayer.core.BedrockLayer;
import bedrocklayer.core.Config;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class BedrockConfigGui extends GuiConfig
{
	public BedrockConfigGui(GuiScreen parent)
	{
		super(parent, getConfigElements(), BedrockLayer.MODID, false, false, I18n.format(Config.LANG_KEY + "title"));
	}

	private static List<IConfigElement> getConfigElements()
	{
		List<IConfigElement> list = Lists.newArrayList();
		Configuration config = Config.config;

		for (String category : config.getCategoryNames())
		{
			list.addAll(new ConfigElement(config.getCategory(category)).getChildElements());
		}

		return list;
	}
}