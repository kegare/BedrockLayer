package bedrocklayer.core;

import java.io.File;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Lists;

import bedrocklayer.handler.BedrockEventHooks;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.config.GuiConfigEntries.IConfigEntry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;

public class Config
{
	public static Configuration config;

	public static int flattenType;
	public static boolean useLayeredCache;

	public static Class<? extends IConfigEntry> cycleInteger;

	public static final String LANG_KEY = "bedrocklayer.config.";

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

				FMLLog.log(Level.ERROR, e, "A critical error occured reading the " + file.getName() + " file, defaults will be used - the invalid file is backed up at " + dest.getName());
			}
		}

		String category = Configuration.CATEGORY_GENERAL;
		Property prop;
		int min, max;
		String comment;
		List<String> propOrder = Lists.newArrayList();

		prop = config.get(category, "flattenType", 0);
		min = 0;
		max = 1;
		prop.setMinValue(min).setMaxValue(max).setConfigEntryClass(cycleInteger);
		prop.setLanguageKey(LANG_KEY + category + "." + prop.getName());
		comment = I18n.translateToLocal(prop.getLanguageKey() + ".tooltip");
		comment += " [range: " + min + " ~ " + max + ", default: " + prop.getDefault() + "]";

		for (int i = min; i <= max; ++i)
		{
			comment += Configuration.NEW_LINE + i + ": " + I18n.translateToLocal(prop.getLanguageKey() + "." + i);

			if (i < max)
			{
				comment += ",";
			}
		}

		prop.setComment(comment);
		propOrder.add(prop.getName());
		flattenType = MathHelper.clamp_int(prop.getInt(flattenType), min, max);

		prop = config.get(category, "useLayeredCache", true);
		prop.setLanguageKey(LANG_KEY + category + "." + prop.getName());
		comment = I18n.translateToLocal(prop.getLanguageKey() + ".tooltip");
		comment += " [default: " + prop.getDefault() + "]";
		prop.setComment(comment);
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
			}
		}

		config.setCategoryPropertyOrder(category, propOrder);

		if (config.hasChanged())
		{
			config.save();
		}
	}
}