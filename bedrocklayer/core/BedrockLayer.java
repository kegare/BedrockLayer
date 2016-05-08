package bedrocklayer.core;

import java.util.Map;

import bedrocklayer.api.BedrockLayerAPI;
import bedrocklayer.handler.BedrockEventHooks;
import bedrocklayer.handler.BedrockLayerAPIHandler;
import net.minecraft.init.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLConstructionEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLLoadCompleteEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkCheckHandler;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid = BedrockLayer.MODID, guiFactory = "bedrocklayer.client.config.BedrockGuiFactory")
public class BedrockLayer
{
	public static final String MODID = "bedrocklayer";

	@SidedProxy(modId = MODID, clientSide = "bedrocklayer.client.ClientProxy", serverSide = "bedrocklayer.core.CommonProxy")
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

		BedrockLayerAPI.registerFlatten(0, 1, 5, Blocks.stone.getDefaultState(), "overworld", true);
		BedrockLayerAPI.registerFlatten(-1, 1, 5, Blocks.netherrack.getDefaultState(), "netherLower", true);
		BedrockLayerAPI.registerFlatten(-1, 122, 127, Blocks.netherrack.getDefaultState(), "netherUpper", true);
	}

	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new BedrockEventHooks());
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