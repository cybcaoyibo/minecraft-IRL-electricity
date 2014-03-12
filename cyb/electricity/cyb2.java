package cyb.electricity;

import java.util.EnumSet;
import java.util.logging.Level;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.Configuration;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.Event;
import net.minecraftforge.event.IEventListener;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.Init;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.PostInit;
import cpw.mods.fml.common.Mod.PreInit;
import cpw.mods.fml.common.Mod.ServerStarted;
import cpw.mods.fml.common.Mod.ServerStopped;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.NetworkMod.SidedPacketHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;
import cyb.c.ilog;


@Mod(modid = "cyb2", name = "CYB's real electricity", version = "3", dependencies = "after:cyb1;after:IC2;after:GregTech_Addon;after:BuildCraft|Core;after:RedPowerCore;after:ThermalExpansion;after:Railcraft")
@NetworkMod(clientSideRequired = true, serverSideRequired = false, clientPacketHandlerSpec = @SidedPacketHandler(channels = { "cyb2" }, packetHandler = handler_c.class), serverPacketHandlerSpec = @SidedPacketHandler(channels = { "cyb2" }, packetHandler = handler_s.class))
public class cyb2 implements ITickHandler, ilog
{
	public static String nm = "CYB's real electricity";
	public static String id = "cyb2";
	@Instance("cyb2")
	public static cyb2 inst;
	public tab_ce tab_ce;
	public int item_ce_id;
	public int block_ce_id;
	public static String img = "/cyb/electricity/1.fw.png";
	public item_ce item_ce;
	public block_ce block_ce;
	public render_ce render_ce;
	public boolean e_explode;
	public boolean expfire;

	@PreInit
	public void pre_init(FMLPreInitializationEvent ev)
	{
		Configuration config = new Configuration(
				ev.getSuggestedConfigurationFile());
		config.load();
		tab_ce = new tab_ce();
		item_ce_id = config.getItem("item_ce", 5491).getInt();
		item_ce = new item_ce(item_ce_id);
		block_ce_id = config.getBlock("block_ce", 736).getInt();
		block_ce = new block_ce(block_ce_id);
		tab_ce.itm = item_ce;
		e_explode = config.get("configuration", "explode", true).getBoolean(true);
		expfire = config.get("configuration", "expfire", false).getBoolean(false);
		config.save();
	}

	@Init
	public void init(FMLInitializationEvent ev)
	{
		LanguageRegistry.instance().addStringLocalization("itemGroup." + tab_ce.nm, "en_US", nm);
		LanguageRegistry.instance().addStringLocalization("itemGroup." + tab_ce.nm, "zh_CN", "CYB的真实电力");
		
		if(FMLCommonHandler.instance().getSide().isClient())
		{
			MinecraftForgeClient.preloadTexture(img);
			render_ce = new render_ce();
			render_ce.rid = RenderingRegistry.getNextAvailableRenderId();
			RenderingRegistry.registerBlockHandler(render_ce);
		}
		NetworkRegistry.instance().registerGuiHandler(this, new handler_g());
		GameRegistry.registerBlock(block_ce, iblk_ce.class, block_ce.nm);
		GameRegistry.registerItem(item_ce, item_ce.nm);
		GameRegistry.registerTileEntity(tile_ce.class, tile_ce.nm);
		TickRegistry.registerTickHandler(this, Side.SERVER);
		LanguageRegistry.instance().addStringLocalization(item_ce.nm + "._0.name", "en_US", "???");
		LanguageRegistry.instance().addStringLocalization(item_ce.nm + "._1.name", "en_US", "Ammeter");
		LanguageRegistry.instance().addStringLocalization(item_ce.nm + "._1.name", "zh_CN", "电流表");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._0.name", "en_US", "???");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._1.name", "en_US", "Generator");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._1.name", "zh_CN", "发电机");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._2.name", "en_US", "Wire");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._2.name", "zh_CN", "电线");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._3.name", "en_US", "Ammeter");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._3.name", "zh_CN", "电流表");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._4.name", "en_US", "Voltmeter");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._4.name", "zh_CN", "电压表");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._5.name", "en_US", "Resistor");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._5.name", "zh_CN", "电阻");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._6.name", "en_US", "Resistor");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._6.name", "zh_CN", "电阻");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._7.name", "en_US", "Capacitor");
		LanguageRegistry.instance().addStringLocalization(iblk_ce.nm + "._7.name", "zh_CN", "电容");
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".max_current", "en_US", "Max Current");
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".max_current", "zh_CN", "最大电流");
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".max_voltage", "en_US", "Max Voltage");
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".max_voltage", "zh_CN", "最大电压");
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".current", "en_US", "Current");
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".current", "zh_CN", "电流");
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".resistance", "en_US", "Resistance"); 
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".resistance", "zh_CN", "电阻"); 
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".capacitance", "en_US", "Capacitance");
		LanguageRegistry.instance().addStringLocalization(cyb2.id + ".capacitance", "zh_CN", "电容");
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(block_ce.blockID, 6, 2), new Object[]{"AAA", "BBB", "AAA", Character.valueOf('A'), new ItemStack(Block.cobblestone.blockID, 1, 0), Character.valueOf('B'), "ingotCopper"}));
		ItemStack copper_cable = ic2.api.Items.getItem("copperCableItem");
		if(copper_cable != null)
			CraftingManager.getInstance().getRecipeList().add(new ShapelessOreRecipe(new ItemStack(block_ce.blockID, 1, 2), new Object[]{copper_cable.copy(), new ItemStack(Block.cobblestone.blockID, 1, 0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(item_ce.itemID, 1, 1), new Object[]{"AAA", "ABA", "ACA", Character.valueOf('A'), new ItemStack(Block.stone.blockID, 1, 0), Character.valueOf('B'), new ItemStack(block_ce.blockID, 1, 2), Character.valueOf('C'), new ItemStack(Block.thinGlass, 1, 0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(block_ce.blockID, 1, 3), new Object[]{"CBC", "AAA", Character.valueOf('A'), new ItemStack(Block.cobblestone.blockID, 1, 0), Character.valueOf('B'), new ItemStack(item_ce.itemID, 1, 1), Character.valueOf('C'), "ingotTin"}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(block_ce.blockID, 1, 4), new Object[]{"ABA", "ACA", "ABA", Character.valueOf('A'), new ItemStack(Item.ingotIron.itemID, 1, 0), Character.valueOf('B'), new ItemStack(block_ce.blockID, 1, 2), Character.valueOf('C'), new ItemStack(block_ce.blockID, 1, 3)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(block_ce.blockID, 1, 1), new Object[]{"AAA", "BCD", "AAA", Character.valueOf('A'), "slabWood", Character.valueOf('B'), new ItemStack(block_ce.blockID, 1, 3), Character.valueOf('C'), new ItemStack(Block.stoneOvenIdle, 1, 0), Character.valueOf('D'), new ItemStack(block_ce.blockID, 1, 4)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(block_ce.blockID, 1, 1), new Object[]{"AAA", "BCD", "AAA", Character.valueOf('A'), "slabWood", Character.valueOf('B'), new ItemStack(block_ce.blockID, 1, 4), Character.valueOf('C'), new ItemStack(Block.stoneOvenIdle, 1, 0), Character.valueOf('D'), new ItemStack(block_ce.blockID, 1, 3)}));
		ItemStack iron_furnace = ic2.api.Items.getItem("ironFurnace");
		if(iron_furnace != null)
		{
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(block_ce.blockID, 1, 1), new Object[]{"AAA", "BCD", "AAA", Character.valueOf('A'), "slabWood", Character.valueOf('B'), new ItemStack(block_ce.blockID, 1, 3), Character.valueOf('C'), iron_furnace.copy(), Character.valueOf('D'), new ItemStack(block_ce.blockID, 1, 4)}));
			CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(block_ce.blockID, 1, 1), new Object[]{"AAA", "BCD", "AAA", Character.valueOf('A'), "slabWood", Character.valueOf('B'), new ItemStack(block_ce.blockID, 1, 4), Character.valueOf('C'), iron_furnace.copy(), Character.valueOf('D'), new ItemStack(block_ce.blockID, 1, 3)}));
		}
		CraftingManager.getInstance().getRecipeList().add(new recipe_r());
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(item_ce.itemID, 1, 20), new Object[]{"ABA", "BAB", "ABA", Character.valueOf('A'), new ItemStack(block_ce.blockID, 1, 2), Character.valueOf('B'), new ItemStack(Block.cobblestone.blockID, 1, 0)}));
		CraftingManager.getInstance().getRecipeList().add(new ShapedOreRecipe(new ItemStack(item_ce.itemID, 1, 20), new Object[]{"BAB", "AAA", "BAB", Character.valueOf('A'), new ItemStack(block_ce.blockID, 1, 2), Character.valueOf('B'), new ItemStack(Block.cobblestone.blockID, 1, 0)}));
	}

	@PostInit
	public void post_init(FMLPostInitializationEvent ev)
	{
	}

	public void log(Level lvl, String str)
	{
		FMLCommonHandler.instance().getFMLLogger()
				.log(lvl, "[" + nm + "] " + str);
	}
	
	public global_manager gman;
	
	@ServerStarted
	public void srv_start(FMLServerStartedEvent ev)
	{
		gman = new global_manager();
	}
	
	@ServerStopped
	public void srv_stop(FMLServerStoppedEvent ev)
	{
		gman.on_del();
		gman = null;
	}

	@Override
	public void tickStart(EnumSet<TickType> type, Object... data)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		if(type.contains(TickType.WORLD))
		{
			World w = (World)data[0];
			gman.get_dim(w.getWorldInfo().getDimension()).tick();
		}
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... data)
	{
	}

	@Override
	public EnumSet<TickType> ticks()
	{
		return EnumSet.of(TickType.WORLD);
	}

	@Override
	public String getLabel()
	{
		return id;
	}
}
