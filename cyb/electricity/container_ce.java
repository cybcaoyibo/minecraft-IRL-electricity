package cyb.electricity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import cyb.c.common;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.Packet250CustomPayload;

public class container_ce extends Container
{
	public tile_ce tile;
	
	public container_ce (InventoryPlayer player, tile_ce te)
	{
		tile = te;
		if(te.get_meta() == 1)
			addSlotToContainer(new Slot(te, 0, 14, 53));
		
		List<Slot> slots = cyb.c.common.container_add_player(this, player);
		Iterator<Slot> it = slots.iterator();
		while(it.hasNext())
			addSlotToContainer(it.next());
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return tile.isUseableByPlayer(player);
	}

    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int s)
    {
    	if(tile.get_meta() == 1)
    	{
	    	ItemStack st = null;
	    	Slot slot = (Slot)inventorySlots.get(s);
	    	if(slot != null && slot.getHasStack())
	    	{
	    		ItemStack slot_st = slot.getStack();
	    		st = slot_st.copy();
	    		if(s == 0)
	    			if(!this.mergeItemStack(slot_st, 1, 37, true))
	    				return null;
	    			else;
	    		else if(!this.mergeItemStack(slot_st, 0, 1, false))
	    			return null;
	    		if(slot_st.stackSize == 0)
	    			slot.putStack(null);
	    		else
	    			slot.onSlotChanged();
	    		if(slot_st.stackSize == st.stackSize)
	    			return null;
	    		slot.onPickupFromSlot(player, slot_st);
	    	}
	    	return st;
    	}
    	return null;
    }
    
    int max_burn_cached = 0;
    int burn_cached = 0;
    double v_cached = 0;
    double a_cached = 0;
    double e_cached = 0;
    
    @Override
    public void detectAndSendChanges()
    {
    	super.detectAndSendChanges();
    	if(tile.get_meta() == 1)
    	{
	    	if(max_burn_cached != tile.max_burn_time || burn_cached != tile.remain_burn_time || v_cached != tile.voltage_for_render || a_cached != tile.generator_current || e_cached != tile.power)
	    	{
	    		max_burn_cached = tile.max_burn_time;
	    		burn_cached = tile.remain_burn_time;
	    		v_cached = tile.voltage_for_render;
	    		a_cached = tile.generator_current;
	    		e_cached = tile.power;
	        	ByteArrayOutputStream stm = new ByteArrayOutputStream();
	        	DataOutputStream out = new DataOutputStream(stm);
	        	try
	        	{
	    			out.writeByte(1);
	    	    	out.writeInt(tile.xCoord);
	    	    	out.writeInt(tile.yCoord);
	    	    	out.writeInt(tile.zCoord);
	    	    	out.writeInt(max_burn_cached);
	    	    	out.writeInt(burn_cached);
	    	    	out.writeDouble(v_cached);
	    	    	out.writeDouble(a_cached);
	    	    	out.writeDouble(e_cached);
	    			common.send_to_crafters(crafters, stm.toByteArray(), cyb2.id);
	    		}
	        	catch(IOException ex)
	        	{
	        		common.err_updcraft(cyb2.inst, this);
	    		}
	    	}
    	}
    	else if(tile.get_meta() == 3 || tile.get_meta() == 4)
    	{
    		if(v_cached != tile.real_value)
    		{
    			v_cached = tile.real_value;
	        	ByteArrayOutputStream stm = new ByteArrayOutputStream();
	        	DataOutputStream out = new DataOutputStream(stm);
	        	try
	        	{
	    			out.writeByte(3);
	    	    	out.writeInt(tile.xCoord);
	    	    	out.writeInt(tile.yCoord);
	    	    	out.writeInt(tile.zCoord);
	    	    	out.writeDouble(v_cached);
	    			common.send_to_crafters(crafters, stm.toByteArray(), cyb2.id);
	    		}
	        	catch(IOException ex)
	        	{
	        		common.err_updcraft(cyb2.inst, this);
	    		}
    		}
    	}
    }
}
