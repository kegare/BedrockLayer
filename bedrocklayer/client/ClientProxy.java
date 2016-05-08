package bedrocklayer.client;

import bedrocklayer.client.config.CycleIntegerEntry;
import bedrocklayer.core.CommonProxy;
import bedrocklayer.core.Config;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientProxy extends CommonProxy
{
	@Override
	public void initializeConfigEntries()
	{
		Config.cycleInteger = CycleIntegerEntry.class;
	}
}