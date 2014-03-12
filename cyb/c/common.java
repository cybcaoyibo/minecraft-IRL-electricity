package cyb.c;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

//公用函数库 by CYB

public class common
{
	public static void drop_items(World world, int x, int y, int z)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (!(tileEntity instanceof IInventory))
        	return;
        IInventory inventory = (IInventory)tileEntity;
        for(int i = 0; i < inventory.getSizeInventory(); i++)
        {
        	ItemStack item = inventory.getStackInSlot(i);
        	if (item != null && item.stackSize > 0)
        	{
        		EntityItem entityItem = new EntityItem(world,
        				x + .5, y + .5, z + .5,
        				new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));
        		if (item.hasTagCompound())
        			entityItem.getEntityItem().setTagCompound((NBTTagCompound) item.getTagCompound().copy());
        		entityItem.motionX = 0;
        		entityItem.motionY = .2;
        		entityItem.motionZ = 0;
        		entityItem.delayBeforeCanPickup = 20;
        		world.spawnEntityInWorld(entityItem);
        		item.stackSize = 0;
        	}
        }
	}
	
	public static ItemStack decrStackSize(IInventory inv, int var1, int var2)
	{
		ItemStack st = inv.getStackInSlot(var1);
		if(st != null)
		{
			if(st.stackSize < var2)
				inv.setInventorySlotContents(var1, null);
			else
			{
				ItemStack orig = st;
				st = st.splitStack(var2);
				if(orig.stackSize == 0)
					inv.setInventorySlotContents(var1, null);
			}
		}
		return st;
	}
	
	public static ItemStack getStackInSlotOnClosing(IInventory inv, int var1)
	{
		ItemStack st = inv.getStackInSlot(var1);
		if(st != null)
			inv.setInventorySlotContents(var1, null);
		return st;
	}
	
	public static void setInventorySlotContents(IInventory inv, ItemStack[] is, int var1, ItemStack var2)
	{
		is[var1] = var2;
		if(var2 != null && var2.stackSize > inv.getInventoryStackLimit())
			var2.stackSize = inv.getInventoryStackLimit();
	}

	public static int getInventoryStackLimit()
	{
		return 64;
	}

	public static ItemStack getStackInSlot(ItemStack[] is, int var1)
	{
		return is[var1];
	}

	public static boolean isUseableByPlayer(TileEntity te, EntityPlayer var1)
	{
		return !te.worldObj.isRemote &&
				te.worldObj.getBlockTileEntity(te.xCoord, te.yCoord, te.zCoord) == te &&
				var1.getDistanceSq(te.xCoord + .5, te.yCoord + .5, te.zCoord + .5) < 64;
	}
	
	public static int getSizeInventory(ItemStack[] is)
	{
		return is.length;
	}
	
	public static List<Slot> container_add_player(Container pt, InventoryPlayer inv_player)
	{
		List<Slot> slots = new ArrayList<Slot>();
		for (int i = 0; i < 3; i++)
		{
			for (int j = 0; j < 9; j++) {
				slots.add(new Slot(inv_player, j + i * 9 + 9,
						8 + j * 18, 84 + i * 18));
			}
		}
		for (int i = 0; i < 9; i++) {
			slots.add(new Slot(inv_player, i, 8 + i * 18, 142));
		}
		return slots;
	}
	
	public static class tex_entry
	{
		public String tex;
		public int tid;
	}
	
	public static tex_entry liquid_tex(int itemid, int dmg)
	{
		tex_entry ent = new tex_entry();
		if(itemid < Block.blocksList.length && Block.blocksList[itemid] != null)
		{
			ent.tex = Block.blocksList[itemid].getTextureFile();
			ent.tid = Block.blocksList[itemid].getBlockTextureFromSideAndMetadata(1, dmg);
		}
		else
		{
			if(Item.itemsList[itemid] != null)
			{
				ent.tex = Item.itemsList[itemid].getTextureFile();
				ent.tid = Item.itemsList[itemid].getIconFromDamage(dmg);
			}
			else
				return null;
		}
		return ent;
	}
	
	public static void send_to_crafters(List crafters, byte[] data, String modid)
	{
    	for(int i = 0; i < crafters.size(); i++)
    	{
    		ICrafting c = (ICrafting)crafters.get(i);
    		if(!(c instanceof Player))
    			continue;
    		Packet250CustomPayload packet = new Packet250CustomPayload(modid, data);
        	PacketDispatcher.sendPacketToPlayer(packet, (Player)c);
    	}
	}
	
	public static void err_updcraft(ilog logger, Container cont)
	{
		logger.log(Level.WARNING, "error during update crafting: " + cont.getClass().getName());
	}
	
	public static void err_null_te(ilog logger, Class<?> cr)
	{
		logger.log(Level.WARNING, "tile entity isn't a instance of " + cr.getName() + " (is null)");
	}
	
	public static void err_inva_te(ilog logger, TileEntity g, Class<?> cr)
	{
		logger.log(Level.WARNING, "tile entity isn't a instance of " + cr.getName() + " (is " + g.getClass().getName() + ")");
	}
	
	public static void err_cliupdte(ilog logger, TileEntity te)
	{
		logger.log(Level.WARNING, "error occured during updte(" + te.getClass().getName() + ")(client side)");
	}
	
	public static void err_desc(ilog logger, TileEntity te)
	{
		logger.log(Level.WARNING, "error during update tile: " + te.getClass().getName());
	}
	
	public static int max4(double a, double b, double c, double d)
	{
		if(b > a && b > c && b > d)
			return 1;
		if(c > a && c > b && c > d)
			return 2;
		if(d > a && d > b && d > c)
			return 3;
		return 0;
	}
}
