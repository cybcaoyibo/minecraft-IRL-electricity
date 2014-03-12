package cyb.electricity;

import static net.minecraftforge.common.ForgeDirection.DOWN;
import static net.minecraftforge.common.ForgeDirection.UP;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cyb.c.common;

import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockHalfSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class block_ce extends BlockContainer
{
	public static String nm = "cyb2.block_ce";
	
	protected block_ce(int id)
	{
		super(id, Material.circuits);
		this.setHardness(.2f);
		this.setStepSound(soundMetalFootstep);
		this.setBlockName(nm);
		this.setCreativeTab(cyb2.inst.tab_ce);
		this.setTextureFile(cyb2.img);
	}
	
    public void getSubBlocks(int id, CreativeTabs tab, List list)
    {
    	list.add(new ItemStack(id, 1, 1));
    	list.add(new ItemStack(id, 1, 2));
    	list.add(new ItemStack(id, 1, 3));
    	list.add(new ItemStack(id, 1, 4));
    }

	@Override
	public TileEntity createNewTileEntity(World world)
	{
		return new tile_ce();
	}
	
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLiving player)
	{
		if(FMLCommonHandler.instance().getEffectiveSide().isClient())
			return;
		if(world.isRemote)
			return;
		int meta = world.getBlockMetadata(x, y, z);
		switch(meta)
		{
		case 1:
		case 3:
		case 4:
		{
			double dx = player.posX - x - .5;
			double dz = player.posZ - z - .5;
			int facing = cyb.c.common.max4(dx, -dx, dz, -dz) + 1;
			((tile_ce)world.getBlockTileEntity(x, y, z)).facing = facing;
			world.markBlockForUpdate(x, y, z);
			break;
		}
		case 5:
		{
			double xdir = Math.abs(player.posX - x - .5);
			double ydir = Math.abs(player.posY - y - .5);
			double zdir = Math.abs(player.posZ - z - .5);
			double nowdir = ydir;
			int dir = 2;
			if(xdir > nowdir)
			{
				nowdir = xdir;
				dir = 1;
			}
			if(zdir > nowdir)
				dir = 3;
			((tile_ce)world.getBlockTileEntity(x, y, z)).facing = dir;
			world.markBlockForUpdate(x, y, z);
			break;
		}
		case 6:
		{
			double xdir = Math.abs(player.posX - x - .5);
			double zdir = Math.abs(player.posZ - z - .5);
			int dir = common.max4(xdir, -xdir, zdir, -zdir) + 1;
			((tile_ce)world.getBlockTileEntity(x, y, z)).facing = dir;
			world.markBlockForUpdate(x, y, z);
			break;
		}
		}
	}
	
    public int getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
    	TileEntity te = world.getBlockTileEntity(x, y, z);
    	if(!(te instanceof tile_ce))
    		return 0;
    	tile_ce tile = (tile_ce)te;
    	int meta = world.getBlockMetadata(x, y, z);
    	if(meta == 1)
    	{
    		if(tile.facing >= 1 && tile.facing <= 4)
    		{
    			if(tile.facing == 1)
    			{
    				if(side == 5) return tile.is_burning_for_render ? (tile.flickr_for_render ? 249 : 250) : 248;
    				if(side == 3) return 243;
    				if(side == 2) return 242;
    			}
    			else if(tile.facing == 2)
    			{
    				if(side == 4) return tile.is_burning_for_render ? (tile.flickr_for_render ? 249 : 250) : 248;
    				if(side == 3) return 242;
    				if(side == 2) return 243;
    			}
    			else if(tile.facing == 3)
    			{
    				if(side == 3) return tile.is_burning_for_render ? (tile.flickr_for_render ? 249 : 250) : 248;
    				if(side == 4) return 243;
    				if(side == 5) return 242;
    			}
    			else
    			{
    				if(side == 2) return tile.is_burning_for_render ? (tile.flickr_for_render ? 249 : 250) : 248;
    				if(side == 4) return 242;
    				if(side == 5) return 243;
    			}
    			return 240;
    		}
    	}
    	else if(meta == 2)
    		return 64;
    	else if(meta == 3 || meta == 4)
    	{
    		if(tile.value_for_render < 0 || tile.value_for_render > 7)
    			return 0;
    		if(tile.facing >= 1 && tile.facing <= 4)
    		{
    			int fro = meta == 3 ? 252 : 220;
    			fro += tile.value_for_render % 4;
    			if(tile.value_for_render >= 4)
    				fro -= 16;
    			if(tile.facing == 1)
    			{
    				if(side == 5) return fro;
    				if(side == 3) return 243;
    				if(side == 2) return 242;
    			}
    			else if(tile.facing == 2)
    			{
    				if(side == 4) return fro;
    				if(side == 3) return 242;
    				if(side == 2) return 243;
    			}
    			else if(tile.facing == 3)
    			{
    				if(side == 3) return fro;
    				if(side == 4) return 243;
    				if(side == 5) return 242;
    			}
    			else
    			{
    				if(side == 2) return fro;
    				if(side == 4) return 242;
    				if(side == 5) return 243;
    			}
    			return 240;
    		}
    	}
    	else if(meta == 6)
    	{
    		if(tile.cval <= 0)
    			return 0;
    		if(tile.facing >= 1 && tile.facing <= 4)
    		{
    			if(side == 0)
    				return cap.texind();
    			if(side == 1)
    				return cap.texind();
    			if(tile.facing == 1)
    			{
    				if(side == 3) return 13;
    				if(side == 2) return 13;
    			}
    			else if(tile.facing == 2)
    			{
    				if(side == 3) return 13;
    				if(side == 2) return 13;
    			}
    			else if(tile.facing == 3)
    			{
    				if(side == 4) return 13;
    				if(side == 5) return 13;
    			}
    			else
    			{
    				if(side == 4) return 13;
    				if(side == 5) return 13;
    			}
    			return 66;
    		}
    	}
    	return 0;
    }
    
    public int getBlockTextureFromSideAndMetadata(int side, int meta)
    {
    	switch(meta)
    	{
    	case 1:
    		switch(side)
    		{
    		case 5:
    		case 0:
    		case 1:
    			return 240;
    		case 4:
    			return 248;
    		case 2:
    			return 243;
    		case 3:
    			return 242;
    		}
    		return 0;
    	case 2:
    		return 52;
    	case 3:
    	case 4:
    		switch(side)
    		{
    		case 5:
    		case 0:
    		case 1:
    			return 240;
    		case 4:
    			return meta == 3 ? 252 : 220;
    		case 2:
    			return 243;
    		case 3:
    			return 242;
    		}
    		return 0;
    	case 5:
    		return 65;
    	case 6:
    		return cap.texind();
    	}
    	return 0;
    }
	
	@Override
    public void breakBlock(World world, int x, int y, int z, int id, int meta)
	{
		if(meta == 1)
			cyb.c.common.drop_items(world, x, y, z);
		if(meta == 6)
		{
			TileEntity tile = world.getBlockTileEntity(x, y, z);
			if(!(tile instanceof tile_ce))
				return;
			tile_ce te = (tile_ce)tile;
			if(te.cval <= 0)
				return;
			ItemStack is = new ItemStack(cyb2.inst.item_ce.itemID, 1, 47);
			NBTTagCompound cyb = new NBTTagCompound();
			cyb.setDouble("c", te.cval);
			is.setTagInfo("cyb", cyb);
    		EntityItem entityItem = new EntityItem(world,
    				x + .5, y + .5, z + .5,
    				is);
    		entityItem.getEntityItem().setTagCompound((NBTTagCompound) is.getTagCompound().copy());
    		entityItem.motionX = 0;
    		entityItem.motionY = .2;
    		entityItem.motionZ = 0;
    		entityItem.delayBeforeCanPickup = 20;
    		world.spawnEntityInWorld(entityItem);
		}
        super.breakBlock(world, x, y, z, id, meta);
	}
	
	@Override
    public boolean onBlockActivated(World world, int x, int y, int z,
    		EntityPlayer player, int idk, float what, float these, float are)
	{
		TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
		if (tileEntity == null || player.isSneaking())
			return false;
		if(!(tileEntity instanceof tile_ce))
			return false;
		if(((tile_ce)tileEntity).get_meta() == 2)
			return false;
		if(((tile_ce)tileEntity).get_meta() >= 1 && ((tile_ce)tileEntity).get_meta() <= 4)
		{
			if(world.isRemote)
				return true;
			if(FMLCommonHandler.instance().getEffectiveSide().isClient())
				return true;
			player.openGui(cyb2.inst, 0, world, x, y, z);
			return true;
		}
		else if(((tile_ce)tileEntity).get_meta() == 5)
		{
			if(world.isRemote)
				return true;
			if(FMLCommonHandler.instance().getEffectiveSide().isClient())
				return true;
			if(!res.isvalid(((tile_ce)tileEntity).rid))
				return true;
			ByteArrayOutputStream stm = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stm);
			try
			{
				out.writeByte(4);
				out.writeDouble(res.getval(((tile_ce)tileEntity).rid));
				PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload(cyb2.id, stm.toByteArray()), (Player)player);
			}
			catch(Exception ex){}
			return true;
		}
		else if(((tile_ce)tileEntity).get_meta() == 6)
		{
			if(world.isRemote)
				return true;
			if(FMLCommonHandler.instance().getEffectiveSide().isClient())
				return true;
			if(((tile_ce)tileEntity).cval <= 0)
				return true;
			ByteArrayOutputStream stm = new ByteArrayOutputStream();
			DataOutputStream out = new DataOutputStream(stm);
			try
			{
				out.writeByte(5);
				out.writeDouble(((tile_ce)tileEntity).cval);
				PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload(cyb2.id, stm.toByteArray()), (Player)player);
			}
			catch(Exception ex){}
			return true;
		}
		return false;
	}
	
	@Override
    public int idDropped(int meta, Random ran, int f)
    {
    	if(1 <= meta && meta <= 4)
    		return this.blockID;
    	return 0;
    }
	
	@Override
    public int damageDropped(int meta)
    {
    	if(1 <= meta && meta <= 4)
    		return meta;
    	return 0;
    }
    
    @Override
    public int getRenderType()
    {
        return cyb2.inst.render_ce.rid;
    }
    
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }
    
    @Override
    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
    	TileEntity te = world.getBlockTileEntity(x, y, z);
    	if(!(te instanceof tile_ce))
    		return false;
    	if(((tile_ce)te).get_meta() == 1)
    		return true;
    	return false;
    }
    
    @Override
    public void addCollidingBlockToList(World world, int x, int y, int z, AxisAlignedBB aabb, List list, Entity entity)
    {
    	TileEntity tile = world.getBlockTileEntity(x, y, z);
    	if(!(tile instanceof tile_ce))
    		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    	else if(((tile_ce)tile).get_meta() == 1)
    		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    	else if(((tile_ce)tile).get_meta() == 2)
    	{
    		this.setBlockBounds(6.f / 16.f, 6.f / 16.f, 6.f / 16.f, 10.f / 16.f, 10.f / 16.f, 10.f / 16.f);
    		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    		tile_ce te = (tile_ce)tile;
    		if(te.wire_XN)
    		{
        		this.setBlockBounds(0, 6.f / 16.f, 6.f / 16.f, 6.f / 16.f, 10.f / 16.f, 10.f / 16.f);
        		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    		}
    		if(te.wire_XP)
    		{
        		this.setBlockBounds(10.f / 16.f, 6.f / 16.f, 6.f / 16.f, 1.f, 10.f / 16.f, 10.f / 16.f);
        		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    		}
    		if(te.wire_YN)
    		{
        		this.setBlockBounds(6.f / 16.f, 0, 6.f / 16.f, 10.f / 16.f, 6.f / 16.f, 10.f / 16.f);
        		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    		}
    		if(te.wire_YP)
    		{
        		this.setBlockBounds(6.f / 16.f, 10.f / 16.f, 6.f / 16.f, 10.f / 16.f, 1.f, 10.f / 16.f);
        		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    		}
    		if(te.wire_ZN)
    		{
        		this.setBlockBounds(6.f / 16.f, 0.f, 6.f / 16.f, 10.f / 16.f, 10.f / 16.f, 6.f / 16.f);
        		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    		}
    		if(te.wire_ZP)
    		{
        		this.setBlockBounds(6.f / 16.f, 0.f, 10.f / 16.f, 10.f / 16.f, 10.f / 16.f, 1.f);
        		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    		}
    		this.setBlockBounds(0, 0, 0, 1, 1, 1);
    	}
    	else if(((tile_ce)tile).get_meta() == 5)
    	{
    		box bx = get_box(world, x, y, z);
    		this.setBlockBounds(bx.minX, bx.minY, bx.minZ, bx.maxX, bx.maxY, bx.maxZ);
    		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    		this.setBlockBounds(0, 0, 0, 1, 1, 1);
    	}
    	else
    		super.addCollidingBlockToList(world, x, y, z, aabb, list, entity);
    }
    
    class box
    {
    	public float minX, minY, minZ, maxX, maxY, maxZ;
    	public box(float minX, float minY, float minZ, float maxX, float maxY, float maxZ)
    	{
    		this.minX = minX;
    		this.minY = minY;
    		this.minZ = minZ;
    		this.maxX = maxX;
    		this.maxY = maxY;
    		this.maxZ = maxZ;
    	}
    }
    
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World world, int x, int y, int z)
    {
    	box b = get_box(world, x, y, z);
    	return AxisAlignedBB.getAABBPool().addOrModifyAABBInPool(x + b.minX, y + b.minY, z + b.minZ, x + b.maxX, y + b.maxY, z + b.maxZ);
    }
    
    public box get_box(World world, int x, int y, int z)
    {
    	TileEntity tile = world.getBlockTileEntity(x, y, z);
    	if(!(tile instanceof tile_ce))
    		return new box(0, 0, 0, 1, 1, 1);
    	tile_ce te = (tile_ce)tile;
    	if(te.get_meta() == 2)
    	{
    		float minX = 6.f / 16.f;
    		float minY = 6.f / 16.f;
    		float minZ = 6.f / 16.f;
    		float maxX = 10.f / 16.f;
    		float maxY = 10.f / 16.f;
    		float maxZ = 10.f / 16.f;
    		if(te.wire_XN)
    			minX = 0;
    		if(te.wire_XP)
    			maxX = 1;
    		if(te.wire_YN)
    			minY = 0;
    		if(te.wire_YP)
    			maxY = 1;
    		if(te.wire_ZN)
    			minZ = 0;
    		if(te.wire_ZP)
    			maxZ = 1;
    		return new box(minX, minY, minZ, maxX, maxY, maxZ);
    	}
    	else if(te.get_meta() == 5)
    	{
    		if(te.facing == 1)
    			return new box(0.f, 5.f / 16.f, 5.f / 16.f, 1.f, 11.f / 16.f, 11.f / 16.f);
    		else if(te.facing == 2)
    			return new box(5.f / 16.f, 0.f, 5.f / 16.f, 11.f / 16.f, 1.f, 11.f / 16.f);
    		else if(te.facing == 3)
    			return new box(5.f / 16.f, 5.f / 16.f, 0.f, 11.f / 16.f, 11.f / 16.f, 1.f);
    		else
    			return new box(0, 0, 0, 1, 1, 1);
    	}
    	else
    		return new box(0, 0, 0, 1, 1, 1);
    }
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int fac)
    {
    	TileEntity tile = world.getBlockTileEntity(x, y, z);
    	if(!(tile instanceof tile_ce))
    		return true;
    	tile_ce te = (tile_ce)tile;
    	if(te.get_meta() == 1)
    		return super.shouldSideBeRendered(world, x, y, z, fac);
    	else if(te.get_meta() == 2)
    		return true;
    	else if(te.get_meta() == 3)
    		return super.shouldSideBeRendered(world, x, y, z, fac);
    	else if(te.get_meta() == 4)
    		return super.shouldSideBeRendered(world, x, y, z, fac);
    	return true;
    }
    
    
    public int idPicked(World world, int x, int y, int z)
    {
    	TileEntity tile = world.getBlockTileEntity(x, y, z);
    	if(!(tile instanceof tile_ce))
    		return 0;
    	tile_ce te = (tile_ce)tile;
    	if(0 <= te.get_meta() && te.get_meta() <= 4)
    		return this.blockID;
    	if(te.get_meta() == 5)
    		if(res.isvalid(te.rid))
    			return cyb2.inst.item_ce.itemID;
    	return 0;
    }

    public int getDamageValue(World world, int x, int y, int z)
    {
    	TileEntity tile = world.getBlockTileEntity(x, y, z);
    	if(!(tile instanceof tile_ce))
    		return 0;
    	tile_ce te = (tile_ce)tile;
    	if(0 <= te.get_meta() && te.get_meta() <= 4)
    		return te.get_meta();
    	if(te.get_meta() == 5)
    		if(res.isvalid(te.rid))
    			return te.rid + 2;
    	return 0;
    }
}
