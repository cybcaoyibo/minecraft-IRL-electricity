package cyb.electricity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cyb.c.common;
import ic2.api.IWrenchable;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityFurnace;
import net.minecraftforge.common.ForgeDirection;
import net.minecraftforge.common.ISidedInventory;
import net.minecraftforge.liquids.ITankContainer;

public class tile_ce extends TileEntity implements component, ISidedInventory, current
{
	public static String nm = "cyb2.tile_ce";
	int facing = 0;
	ItemStack fuel_slot;
	int max_burn_time = 0;
	int remain_burn_time = 0;
	double power = 0;
	static double max_power = 1000.;
	public double next_generator_current = 0;
	public double generator_current = 0;
	public boolean is_burning_for_render = false;
	public boolean flickr_for_render = false;
	public int flickr_timer_for_render = 0;
	public double voltage_for_render = 0;
	public boolean wire_XP = false;
	public boolean wire_XN = false;
	public boolean wire_YP = false;
	public boolean wire_YN = false;
	public boolean wire_ZP = false;
	public boolean wire_ZN = false;
	static double wire_r = .001;
	static double volt_r = 1000000000.;
	public double wire_volt[] = new double[6];
	public double wire_volt_r[] = new double[6];
	public int value_for_render = -1;
	public double real_value = 0;
	public byte rid = -1;
	public double cval = 0;
	public double cap_prev_i = 0;
	public double cap_prev_si = 0;
	public double cap_now_i = 0;
	int get_meta()
	{
		return worldObj.getBlockMetadata(xCoord, yCoord, zCoord);
	}
	
	public void writeToNBT(NBTTagCompound root)
	{
		super.writeToNBT(root);
		NBTTagCompound cyb = new NBTTagCompound();
		cyb.setByte("m", (byte)get_meta());
		switch(get_meta())
		{
		case 1:
			cyb.setByte("f", (byte)facing);
			if(fuel_slot != null)
			{
				NBTTagCompound fuel_nbt = new NBTTagCompound();
				fuel_slot.writeToNBT(fuel_nbt);
				cyb.setTag("u", fuel_nbt);
			}
			cyb.setInteger("t", remain_burn_time);
			if(remain_burn_time > 0)
				cyb.setInteger("b", max_burn_time);
			cyb.setDouble("e", power);
			break;
		case 2:
			cyb.setBoolean("XP", wire_XP);
			cyb.setBoolean("XN", wire_XN);
			cyb.setBoolean("YP", wire_YP);
			cyb.setBoolean("YN", wire_YN);
			cyb.setBoolean("ZP", wire_ZP);
			cyb.setBoolean("ZN", wire_ZN);
			break;
		case 3:
		case 4:
			cyb.setByte("f", (byte)facing);
			break;
		case 5:
			cyb.setByte("f", (byte)facing);
			cyb.setByte("r", rid);
			break;
		case 6:
			cyb.setByte("f", (byte)facing);
			cyb.setDouble("c", cval);
			cyb.setDouble("i", cap_prev_i);
			cyb.setDouble("s", cap_prev_si);
			break;
		}
		root.setTag("cyb", cyb);
	}
	
	boolean test_comp(int x, int y, int z, int dir)
	{
		if(!(worldObj.getBlockTileEntity(x, y, z) instanceof component))
			return false;
		component cmp = (component)worldObj.getBlockTileEntity(x, y, z);
		if(cmp.is_wire())
			return true;
		for(int i = 0; i < cmp.num_nodes(); i++)
			if(cmp.dir_nodes(i) == dir)
				return true;
		return false;
	}
	
	public void readFromNBT(NBTTagCompound root)
	{
		super.readFromNBT(root);
		NBTTagCompound cyb = (NBTTagCompound)root.getTag("cyb");
		int meta = cyb.getByte("m");
		switch(meta)
		{
		case 1:
			facing = cyb.getByte("f");
			power = cyb.getDouble("e");
			remain_burn_time = cyb.getInteger("t");
			if(remain_burn_time > 0)
				max_burn_time = cyb.getInteger("b");
			if(cyb.hasKey("u"))
			{
				NBTTagCompound fuel_nbt = (NBTTagCompound)cyb.getTag("u");
				fuel_slot = ItemStack.loadItemStackFromNBT(fuel_nbt);
			}
			else
				fuel_slot = null;
			break;
		case 2:
			wire_XP = cyb.getBoolean("XP");
			wire_XN = cyb.getBoolean("XN");
			wire_YP = cyb.getBoolean("YP");
			wire_YN = cyb.getBoolean("YN");
			wire_ZP = cyb.getBoolean("ZP");
			wire_ZN = cyb.getBoolean("ZN");
			break;
		case 3:
		case 4:
			facing = cyb.getByte("f");
			break;
		case 5:
			facing = cyb.getByte("f");
			rid = cyb.getByte("r");
			break;
		case 6:
			facing = cyb.getByte("f");
			cval = cyb.getDouble("c");
			cap_prev_i = cyb.getDouble("i");
			cap_prev_si = cyb.getDouble("s");
			break;
		}
	}

	boolean added = false;
	
	void explode()
	{
		if(cyb2.inst.e_explode)
			worldObj.setBlockAndMetadataWithNotify(xCoord, yCoord, zCoord, Block.lavaMoving.blockID, 6);
	}
	
	boolean isfire(int id)
	{
		return id == Block.fire.blockID || id == Block.lavaMoving.blockID || id == Block.lavaStill.blockID;
	}
	
	int d_exptime = 0;
	
	void d_explode()
	{
		d_exptime++;
		if(d_exptime > 3)
			explode();
	}
	
	void expfire()
	{
		if(!cyb2.inst.expfire)
			return;
		if(isfire(worldObj.getBlockId(xCoord - 1, yCoord, zCoord)))
			d_explode();
		if(isfire(worldObj.getBlockId(xCoord + 1, yCoord, zCoord)))
			d_explode();
		if(isfire(worldObj.getBlockId(xCoord, yCoord - 1, zCoord)))
			d_explode();
		if(isfire(worldObj.getBlockId(xCoord, yCoord + 1, zCoord)))
			d_explode();
		if(isfire(worldObj.getBlockId(xCoord, yCoord, zCoord - 1)))
			d_explode();
		if(isfire(worldObj.getBlockId(xCoord, yCoord, zCoord + 1)))
			d_explode();
	}
	
	public void updateEntity()
	{
		super.updateEntity();

		if(FMLCommonHandler.instance().getEffectiveSide().isClient() || worldObj.isRemote)
		{
			if(get_meta() == 1)
			{
				flickr_timer_for_render++;
				if(flickr_timer_for_render >= 5)
				{
					flickr_for_render = !flickr_for_render;
					flickr_timer_for_render = 0;
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				}
			}
			return;
		}
		
		if(get_meta() >= 1 && get_meta() <= 5)
			expfire();
		if(get_meta() == 1)
		{
			generator_current = next_generator_current;
			next_generator_current = 0;
			if(!added)
			{
				if(facing >= 1 && facing <= 4)
				{
					added = true;
					cyb2.inst.gman.add_component(this);
				}
			}
			if(this.generator_current < -0.1)
				explode();
			this.power -= generator_current * get_voltage_constant() * circuit_manager.timestep;
			if(this.power < -0.1)
				explode();
			if(this.generator_current > 1)
				explode();
			if(this.power > this.max_power)
				this.power = this.max_power;
			if(this.power < 0)
				this.power = 0;
			
			
			if(this.remain_burn_time > 0)
			{
				this.power++;
				if(this.power > this.max_power)
					this.power = this.max_power;
				this.remain_burn_time--;
				if(this.remain_burn_time == 0)
					worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
			else
			{
				if(this.max_power - this.power >= 1)
				{
					if(TileEntityFurnace.isItemFuel(fuel_slot))
					{
						this.max_burn_time = TileEntityFurnace.getItemBurnTime(fuel_slot);
						this.remain_burn_time = this.max_burn_time;
						fuel_slot.stackSize--;
						if(fuel_slot.stackSize == 0)
							fuel_slot = fuel_slot.getItem().getContainerItemStack(fuel_slot);
						this.onInventoryChanged();
						worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
					}
				}
			}
			voltage_for_render = get_voltage_constant();
		}
		else if(get_meta() == 2)
		{
			boolean new_XP = false, new_XN = false, new_YP = false, new_YN = false, new_ZP = false, new_ZN = false;
			if(test_comp(xCoord + 1, yCoord, zCoord, 4))
				new_XP = true;
			if(test_comp(xCoord - 1, yCoord, zCoord, 5))
				new_XN = true;
			if(test_comp(xCoord, yCoord + 1, zCoord, 0))
				new_YP = true;
			if(test_comp(xCoord, yCoord - 1, zCoord, 1))
				new_YN = true;
			if(test_comp(xCoord, yCoord, zCoord + 1, 2))
				new_ZP = true;
			if(test_comp(xCoord, yCoord, zCoord - 1, 3))
				new_ZN = true;
			if(new_XP != wire_XP || new_XN != wire_XN || new_YP != wire_YP || new_YN != wire_YN || new_ZP != wire_ZP || new_ZN != wire_ZN)
			{
				wire_XP = new_XP;
				wire_XN = new_XN;
				wire_YP = new_YP;
				wire_YN = new_YN;
				wire_ZP = new_ZP;
				wire_ZN = new_ZN;
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				cyb2.inst.gman.get_dim(this.worldObj.getWorldInfo().getDimension()).dirty = true;
			}
			if(!added)
				if(num_nodes() >= 2)
				{
					added = true;
					cyb2.inst.gman.add_component(this);
				}else;
			else
				if(num_nodes() >= 2);
				else
				{
					added = false;
					cyb2.inst.gman.remove_component(this);
				}
			for(int i = 0; i < 6; i++)
				wire_volt_r[i] = wire_volt[i];
			for(int i = 0; i < 6; i++)
				wire_volt[i] = 0;
		}
		else if(get_meta() == 3 || get_meta() == 4)
		{
			if(!added)
			{
				if(facing >= 1 && facing <= 4)
				{
					added = true;
					cyb2.inst.gman.add_component(this);
				}
			}
			real_value = (wire_volt[0] - wire_volt[1]);
			int val_prev = value_for_render;
			if(get_meta() == 3)
			{
				real_value /= wire_r;
				if(real_value < 0)
					value_for_render = 0;
				else if(real_value >= 1)
					value_for_render = 7;
				else
					value_for_render = (int)(real_value * 8.);
			}
			else
			{
				if(real_value < 0)
					value_for_render = 0;
				else if(real_value >= 12)
					value_for_render = 7;
				else
					value_for_render = (int)(real_value * 8. / 12.);
			}
			if(value_for_render >= 8)
				value_for_render = 7;
			if(val_prev != value_for_render)
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			wire_volt[0] = wire_volt[1] = 0;
		}
		else if(get_meta() == 5)
		{
			if(res.isvalid(rid) && facing >= 1 && facing <= 3)
				if(!added)
				{
					cyb2.inst.gman.add_component(this);
					added = true;
				}
				else;
			else
				if(added)
				{
					cyb2.inst.gman.remove_component(this);
					added = false;
				}
				else;
			if(res.isvalid(rid))
				real_value = (wire_volt[0] - wire_volt[1]) / (double)res.getval(rid);
			else
				real_value = 0;
			wire_volt[0] = wire_volt[1] = 0;
		}
		else if(get_meta() == 6)
		{
			if(added && cval <= 0)
			{
				cyb2.inst.gman.remove_component(this);
				added = false;
			}
			else if(!added && cval > 0)
			{
				cyb2.inst.gman.add_component(this);
				added = true;
				wire_volt[0] = wire_volt[1] = 0;
			}
			if(added)
			{
				cap_now_i = (wire_volt[0] - wire_volt[1]) * 2. * cval / circuit_manager.timestep - cap_prev_si - cap_prev_i;
				cap_prev_si += cap_now_i + cap_prev_i;
				cap_prev_i = cap_now_i;
			}
		}
	}
	
	public void invalidate()
	{
		super.invalidate();
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		if(worldObj.isRemote)
			return;
		if(get_meta() >= 1 && get_meta() <= 6)
		{
			if(added)
			{
				added = false;
				cyb2.inst.gman.remove_component(this);
			}
		}
	}
	
    public Packet getDescriptionPacket()
    {
    	ByteArrayOutputStream stm = new ByteArrayOutputStream();
    	DataOutputStream out = new DataOutputStream(stm);
    	try
    	{
			out.writeByte(0);
	    	//out.writeInt(worldObj.getWorldInfo().getDimension());
	    	out.writeInt(xCoord);
	    	out.writeInt(yCoord);
	    	out.writeInt(zCoord);
			switch(get_meta())
			{
			case 1:
				out.writeByte(0);
				out.writeByte(facing);
				out.writeBoolean(remain_burn_time > 0);
				break;
			case 2:
				out.writeByte(1);
				out.writeBoolean(wire_XP);
				out.writeBoolean(wire_XN);
				out.writeBoolean(wire_YP);
				out.writeBoolean(wire_YN);
				out.writeBoolean(wire_ZP);
				out.writeBoolean(wire_ZN);
				break;
			case 3:
			case 4:
				out.writeByte(2);
				out.writeByte(facing);
				out.writeByte(value_for_render);
				break;
			case 5:
				out.writeByte(3);
				out.writeByte(facing);
				out.writeByte(rid);
				break;
			case 6:
				out.writeByte(4);
				out.writeByte(facing);
				break;
			default: return null;
			}
			return new Packet250CustomPayload(cyb2.id, stm.toByteArray());
		}
    	catch(IOException ex)
    	{
    		common.err_desc(cyb2.inst, this);
		}
    	return null;
    }

	@Override
	public int num_nodes()
	{
		if(get_meta() == 1 || get_meta() == 3 || get_meta() == 4)
			return 2;
		else if(get_meta() == 2)
			return (wire_XP ? 1 : 0) + (wire_XN ? 1 : 0) + (wire_YP ? 1 : 0) + (wire_YN ? 1 : 0) + (wire_ZP ? 1 : 0) + (wire_ZN ? 1 : 0);
		else if(get_meta() == 5)
			return 2;
		else if(get_meta() == 6)
			return 2;
		return 0;
	}

	@Override
	public int dir_nodes(int id)
	{
		if(get_meta() == 1 || get_meta() == 3 || get_meta() == 4 || get_meta() == 6)
		{
			switch(facing)
			{
			case 1:
				if(id == 0)
					return 2;
				else if(id == 1)
					return 3;
				return 0;
			case 2:
				if(id == 0)
					return 3;
				else if(id == 1)
					return 2;
				return 0;
			case 3:
				if(id == 0)
					return 5;
				else if(id == 1)
					return 4;
				return 0;
			case 4:
				if(id == 0)
					return 4;
				else if(id == 1)
					return 5;
				return 0;
			}
		}
		else if(get_meta() == 2)
		{
			ArrayList<Integer> al = new ArrayList<Integer>();
			if(wire_XP) al.add(5);
			if(wire_XN) al.add(4);
			if(wire_YP) al.add(1);
			if(wire_YN) al.add(0);
			if(wire_ZP) al.add(3);
			if(wire_ZN) al.add(2);
			return al.get(id);
		}
		else if(get_meta() == 5)
		{
			if(facing == 1)
				if(id == 0)
					return 5;
				else
					return 4;
			else if(facing == 2)
				if(id == 0)
					return 1;
				else
					return 0;
			else
				if(id == 0)
					return 3;
				else
					return 2;
		}
		return 0;
	}

	@Override
	public boolean unknown_current()
	{
		if(get_meta() == 1)
			return true;
		return false;
	}

	@Override
	public double get_current_factor(int i, int u)
	{
		if(get_meta() == 2)
			if(num_nodes() == 0)
				return 0;
			else if(num_nodes() == 1)
				return 0;
			else if(num_nodes() == 2)
				if(i == 0)
					if(u == 0)
						return -1. / wire_r;
					else
						return 1. / wire_r;
				else
					if(u == 0)
						return 1. / wire_r;
					else
						return -1. / wire_r;
			else
				if(i == 0)
					if(u == 0)
						return -1. / wire_r;
					else if(u == 1)
						return 1. / wire_r;
					else
						return 0;
				else if(i == num_nodes() - 1)
					if(u == num_nodes() - 1)
						return -1. / wire_r;
					else if(u == num_nodes() - 2)
						return 1. / wire_r;
					else
						return 0;
				else
					if(i == u)
						return -2. / wire_r;
					else if(i == u - 1)
						return 1. / wire_r;
					else if(i == u + 1)
						return 1. / wire_r;
					else
						return 0;
		else if(get_meta() == 3)
			if(i == u)
				return -1. / wire_r;
			else
				return 1. / wire_r;
		else if(get_meta() == 4)
			if(i == u)
				return -1. / volt_r;
			else
				return 1. / volt_r;
		else if(get_meta() == 5)
			if(i == u)
				return -1. / (double)res.getval(rid);
			else
				return 1. / (double)res.getval(rid);
		else if(get_meta() == 6)
			if(i == u)
				return circuit_manager.timestep / 2. / cval;
			else
				return -circuit_manager.timestep / 2. / cval;
		else
			return 0;
	}

	@Override
	public double get_current_factor(int i)
	{
		if(get_meta() == 1)
		{
			if(i == 0)
				return 1;
			else if(i == 1)
				return -1;
		}
		return 0;
	}

	@Override
	public double get_voltage_factor(int u)
	{
		if(get_meta() == 1)
		{
			if(u == 0)
				return 1;
			else if(u == 1)
				return -1;
		}
		return 0;
	}
	
	@Override
	public double get_voltage_factor()
	{
		return 0;
	}

	@Override
	public double get_current_constant(int i)
	{
		if(get_meta() == 6)
			if(i == 0)
				return -cap_prev_si - cap_prev_i;
			else
				return cap_prev_si + cap_prev_i;
		else
			return 0;
	}

	@Override
	public double get_voltage_constant()
	{
		if(get_meta() == 1)
			return Math.pow((power) / 1000., 2) * 12.;
		return 0;
	}

	@Override
	public TileEntity get_tile()
	{
		return this;
	}

	@Override
	public void set_voltage(int id, double u)
	{
		if(get_meta() == 2)
			wire_volt[dir_nodes(id)] = u;
		else if(get_meta() >= 3 && get_meta() <= 6)
			wire_volt[id] = u;
	}

	@Override
	public void set_current(double i)
	{
		if(get_meta() == 1)
			next_generator_current = i;
	}

	@Override
	public void begin_iterate()
	{
	}

	@Override
	public boolean step_iterate()
	{
		return false;
	}

	@Override
	public void end_iterate()
	{
	}

	@Override
	public int getSizeInventory()
	{
		if(get_meta() == 1)
			return 1;
		return 0;
	}

	@Override
	public ItemStack getStackInSlot(int var1)
	{
		if(get_meta() == 1)
			return fuel_slot;
		return null;
	}

	@Override
	public ItemStack decrStackSize(int var1, int var2)
	{
		return common.decrStackSize(this, var1, var2);
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int var1)
	{
		return common.getStackInSlotOnClosing(this, var1);
	}

	@Override
	public void setInventorySlotContents(int var1, ItemStack var2)
	{
		if(get_meta() == 1)
		{
			fuel_slot = var2;
			if(fuel_slot != null && fuel_slot.stackSize > this.getInventoryStackLimit())
				fuel_slot.stackSize = this.getInventoryStackLimit();
		}
	}

	@Override
	public String getInvName()
	{
		return nm;
	}

	@Override
	public int getInventoryStackLimit()
	{
		return common.getInventoryStackLimit();
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		if(get_meta() == 1 || get_meta() == 3 || get_meta() == 4)
			return common.isUseableByPlayer(this, player);
		return false;
	}

	@Override
	public void openChest()
	{
	}

	@Override
	public void closeChest()
	{
	}

	@Override
	public int getStartInventorySide(ForgeDirection side)
	{
		return 0;
	}

	@Override
	public int getSizeInventorySide(ForgeDirection side)
	{
		if(get_meta() == 1)
			return 1;
		return 0;
	}

	@Override
	public boolean is_wire()
	{
		if(get_meta() == 2)
			return true;
		return false;
	}

	@Override
	public boolean can_current()
	{
		if(get_meta() >= 1 && get_meta() <= 6)
			return true;
		return false;
	}

	double wire_current()
	{
		double[] crs = new double[num_nodes()];
		for(int i = 0; i < num_nodes(); i++)
			crs[i] = wire_volt_r[dir_nodes(i)];
		if(num_nodes() < 2)
			return 0;
		if(num_nodes() == 2)
			return Math.abs((crs[0] - crs[1])) / wire_r;
		double cur = 0;
		for(int i = 0; i < crs.length - 1; i++)
			cur = Math.max(cur, Math.abs(crs[i] - crs[i + 1]) / wire_r);
		return cur;
	}
	
	@Override
	public double get_current()
	{
		if(get_meta() == 1)
			return generator_current;
		if(get_meta() == 2)
			return wire_current();
		if(get_meta() == 3)
			return real_value;
		if(get_meta() == 4)
			return (wire_volt[0] - wire_volt[1]) / volt_r;
		if(get_meta() == 5)
			return Math.abs(real_value);
		if(get_meta() == 6)
			return Math.abs(cap_prev_i);
		return 0;
	}

	circuit_manager cm = null;
	
	@Override
	public void on_add(circuit_manager cm)
	{
		this.cm = cm;
	}

	@Override
	public void on_remove(circuit_manager cm)
	{
		this.cm = null;
	}
}
