package bedrocklayer.core;

import com.google.common.base.Objects;
import com.google.common.base.Strings;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.storage.ExtendedBlockStorage;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class FlattenEntry
{
	public int dim;
	public int minHeight;
	public int maxHeight;
	public IBlockState filler;

	public String configName;
	public boolean configDefault;

	public FlattenEntry(int dim, int min, int max, IBlockState filler)
	{
		this.dim = dim;
		this.minHeight = min;
		this.maxHeight = max;
		this.filler = filler;
	}

	@Override
	public int hashCode()
	{
		return Objects.hashCode(dim, minHeight, maxHeight, filler);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (obj instanceof FlattenEntry)
		{
			FlattenEntry entry = (FlattenEntry)obj;

			return dim == entry.dim && minHeight == entry.minHeight && maxHeight == entry.maxHeight && filler == entry.filler;
		}

		return false;
	}

	public FlattenEntry setConfig(String name, boolean defaultValue)
	{
		configName = name;
		configDefault = defaultValue;

		return this;
	}

	public Property getConfigProperty(Configuration config)
	{
		if (Strings.isNullOrEmpty(configName))
		{
			return null;
		}

		Property prop = config.get("bedrocklayer", configName, configDefault);
		prop.setLanguageKey(Config.LANG_KEY + prop.getName());
		prop.setComment(I18n.translateToLocal(prop.getLanguageKey() + ".tooltip") + " [default: " + prop.getDefault() + "]");

		return prop;
	}

	public boolean flatten(final Chunk chunk)
	{
		World world = chunk.getWorld();

		if (world.provider.getDimension() != dim)
		{
			return false;
		}

		if (world instanceof WorldServer)
		{
			((WorldServer)world).addScheduledTask(
				new Runnable()
				{
					@Override
					public void run()
					{
						for (ExtendedBlockStorage storage : chunk.getBlockStorageArray())
						{
							if (storage != null)
							{
								for (int x = 0; x < 16; ++x)
								{
									for (int z = 0; z < 16; ++z)
									{
										for (int y = minHeight; y < maxHeight; ++y)
										{
											if (storage.get(x, y, z) == Blocks.BEDROCK.getDefaultState())
											{
												storage.set(x, y, z, filler);
											}
										}
									}
								}
							}
						}

						chunk.setModified(true);
					}
				}
			);
		}

		return true;
	}
}