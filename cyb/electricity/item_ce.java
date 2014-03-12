package cyb.electricity;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.util.List;
import java.util.logging.Level;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class item_ce extends Item
{
	public String nm = "cyb2.item_ce";
	
	public item_ce(int id)
	{
		super(id);
		this.setIconIndex(0);
		this.setHasSubtypes(true);
		this.setCreativeTab(cyb2.inst.tab_ce);
		this.setItemName(nm);
		this.setTextureFile(cyb2.img);
	}
	
    public void getSubItems(int id, CreativeTabs tab, List list)
    {
    	list.add(new ItemStack(id, 1, 1));
    	for(int i = 0; i < 45; i++)
    		list.add(new ItemStack(id, 1, i + 2));
    	for(int i = 1; i < 10; i++)
    	{
    		NBTTagCompound root = new NBTTagCompound();
    		root.setDouble("c", i / 1000. / 1000.);
    		ItemStack is = new ItemStack(id, 1, 47);
    		is.setTagInfo("cyb", root);
    		list.add(is);
    	}
    	for(int i = 1; i < 10; i++)
    	{
    		NBTTagCompound root = new NBTTagCompound();
    		root.setDouble("c", i * 10. / 1000. / 1000.);
    		ItemStack is = new ItemStack(id, 1, 47);
    		is.setTagInfo("cyb", root);
    		list.add(is);
    	}
    	for(int i = 1; i < 10; i++)
    	{
    		NBTTagCompound root = new NBTTagCompound();
    		root.setDouble("c", i * 100. / 1000. / 1000.);
    		ItemStack is = new ItemStack(id, 1, 47);
    		is.setTagInfo("cyb", root);
    		list.add(is);
    	}
    	for(int i = 1; i < 10; i++)
    	{
    		NBTTagCompound root = new NBTTagCompound();
    		root.setDouble("c", i / 1000.);
    		ItemStack is = new ItemStack(id, 1, 47);
    		is.setTagInfo("cyb", root);
    		list.add(is);
    	}
    	for(int i = 1; i < 10; i++)
    	{
    		NBTTagCompound root = new NBTTagCompound();
    		root.setDouble("c", i * 10. / 1000.);
    		ItemStack is = new ItemStack(id, 1, 47);
    		is.setTagInfo("cyb", root);
    		list.add(is);
    	}
    	for(int i = 1; i < 10; i++)
    	{
    		NBTTagCompound root = new NBTTagCompound();
    		root.setDouble("c", i * 100. / 1000.);
    		ItemStack is = new ItemStack(id, 1, 47);
    		is.setTagInfo("cyb", root);
    		list.add(is);
    	}
    	for(int i = 1; i <= 10; i++)
    	{
    		NBTTagCompound root = new NBTTagCompound();
    		root.setDouble("c", i);
    		ItemStack is = new ItemStack(id, 1, 47);
    		is.setTagInfo("cyb", root);
    		list.add(is);
    	}
    }
    
    public int getIconFromDamage(int dmg)
    {
    	if(dmg == 0)
    		return 242;
    	else if(dmg == 1)
    		return 2;
    	else if(2 <= dmg && dmg <= 46)
    		return res.texind(dmg - 2);
    	else if(dmg == 47)
    		return cap.texind();
        return this.iconIndex;
    }
    
    public String getItemNameIS(ItemStack is)
    {
    	if(2 <= is.getItemDamage() && is.getItemDamage() <= 46)
    		return iblk_ce.nm + "._5";
    	if(is.getItemDamage() == 47)
    	{
    		try
    		{
	    		NBTTagCompound root = is.getTagCompound();
	    		NBTTagCompound cyb = (NBTTagCompound)root.getTag("cyb");
	    		double c = cyb.getDouble("c");
	    		return iblk_ce.nm + "._7";
    		}
    		catch(Exception ex){return nm + "._0";}
    	}
    	if(is.getItemDamage() > 47)
    		return nm + "._0";
        return nm + "._" + is.getItemDamage();
    }
    
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
    	if(stack.getItemDamage() == 1)
    	{
        	if(FMLCommonHandler.instance().getEffectiveSide().isClient())
        		return false;
        	if(world.isRemote)
        		return false;
    		TileEntity tile = world.getBlockTileEntity(x, y, z);
    		if(!(tile instanceof current))
    			return true;
    		current cr = (current)tile;
    		if(!cr.can_current())
    			return true;
    		ByteArrayOutputStream stm = new ByteArrayOutputStream();
    		DataOutputStream out = new DataOutputStream(stm);
    		try
    		{
    			out.writeByte(2);
    			out.writeDouble(cr.get_current());
    			out.writeInt(stack.stackSize);
        		PacketDispatcher.sendPacketToPlayer(new Packet250CustomPayload(cyb2.id, stm.toByteArray()), (Player)player);
    		}
    		catch(Exception e){}
    		return true;
    	}
        return false;
    }
    
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean adv)
    {
		if(2 <= is.getItemDamage() && is.getItemDamage() <= 46)
			list.add(res.getval(is.getItemDamage() - 2) + "Ω");
		if(is.getItemDamage() == 47)
		{
    		try
    		{
	    		NBTTagCompound root = is.getTagCompound();
	    		NBTTagCompound cyb = (NBTTagCompound)root.getTag("cyb");
	    		double c = cyb.getDouble("c");
	    		list.add(cap.uconv(c));
    		}
    		catch(Exception ex){list.add("???");}
		}
    }
    
    public boolean onItemUse(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, World par3World, int par4, int par5, int par6, int par7, float par8, float par9, float par10)
    {
    	if(2 <= par1ItemStack.getItemDamage() && par1ItemStack.getItemDamage() <= 46)
    	{
	        int var11 = par3World.getBlockId(par4, par5, par6);
	        if (var11 == Block.snow.blockID)
	            par7 = 1;
	        else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID
	                && (Block.blocksList[var11] == null || !Block.blocksList[var11].isBlockReplaceable(par3World, par4, par5, par6)))
	        {
	            if (par7 == 0)
	                --par5;
	            if (par7 == 1)
	                ++par5;
	            if (par7 == 2)
	                --par6;
	            if (par7 == 3)
	                ++par6;
	            if (par7 == 4)
	                --par4;
	            if (par7 == 5)
	                ++par4;
	        }
	        if (par1ItemStack.stackSize == 0)
	            return false;
	        else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
	            return false;
	        else if (par5 == 255 && cyb2.inst.block_ce.blockMaterial.isSolid())
	            return false;
	        else if (par3World.canPlaceEntityOnSide(cyb2.inst.block_ce.blockID, par4, par5, par6, false, par7, par2EntityPlayer))
	        {
	            Block var12 = Block.blocksList[cyb2.inst.block_ce.blockID];
	            int var13 = /*this.getMetadata(par1ItemStack.getItemDamage())*/5;
	            int var14 = Block.blocksList[cyb2.inst.block_ce.blockID].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, var13);
	
	            if (placeBlockAt_resistor(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, var14))
	            {
	                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var12.stepSound.getPlaceSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
	                --par1ItemStack.stackSize;
	            }
	
	            return true;
	        }
	        else
	        {
	            return false;
	        }
    	}
    	if(par1ItemStack.getItemDamage() == 47)
    	{
    		double cval = 0;
    		boolean ok = false;
    		try
    		{
    			NBTTagCompound root = par1ItemStack.getTagCompound();
    			NBTTagCompound cyb = (NBTTagCompound)root.getTag("cyb");
    			cval = cyb.getDouble("c");
    			ok = true;
    		}catch(Exception ex){}
    		if(ok)
    		{
    	        int var11 = par3World.getBlockId(par4, par5, par6);
    	        if (var11 == Block.snow.blockID)
    	            par7 = 1;
    	        else if (var11 != Block.vine.blockID && var11 != Block.tallGrass.blockID && var11 != Block.deadBush.blockID
    	                && (Block.blocksList[var11] == null || !Block.blocksList[var11].isBlockReplaceable(par3World, par4, par5, par6)))
    	        {
    	            if (par7 == 0)
    	                --par5;
    	            if (par7 == 1)
    	                ++par5;
    	            if (par7 == 2)
    	                --par6;
    	            if (par7 == 3)
    	                ++par6;
    	            if (par7 == 4)
    	                --par4;
    	            if (par7 == 5)
    	                ++par4;
    	        }
    	        if (par1ItemStack.stackSize == 0)
    	            return false;
    	        else if (!par2EntityPlayer.canPlayerEdit(par4, par5, par6, par7, par1ItemStack))
    	            return false;
    	        else if (par5 == 255 && cyb2.inst.block_ce.blockMaterial.isSolid())
    	            return false;
    	        else if (par3World.canPlaceEntityOnSide(cyb2.inst.block_ce.blockID, par4, par5, par6, false, par7, par2EntityPlayer))
    	        {
    	            Block var12 = Block.blocksList[cyb2.inst.block_ce.blockID];
    	            int var13 = /*this.getMetadata(par1ItemStack.getItemDamage())*/6;
    	            int var14 = Block.blocksList[cyb2.inst.block_ce.blockID].onBlockPlaced(par3World, par4, par5, par6, par7, par8, par9, par10, var13);
    	
    	            if (placeBlockAt_capacitor(par1ItemStack, par2EntityPlayer, par3World, par4, par5, par6, par7, par8, par9, par10, var14, cval))
    	            {
    	                par3World.playSoundEffect((double)((float)par4 + 0.5F), (double)((float)par5 + 0.5F), (double)((float)par6 + 0.5F), var12.stepSound.getPlaceSound(), (var12.stepSound.getVolume() + 1.0F) / 2.0F, var12.stepSound.getPitch() * 0.8F);
    	                --par1ItemStack.stackSize;
    	            }
    	
    	            return true;
    	        }
    	        else
    	        {
    	            return false;
    	        }
    		}
    	}
    	return false;
    }
    public boolean placeBlockAt_resistor(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
       if (!world.setBlockAndMetadataWithNotify(x, y, z, cyb2.inst.block_ce.blockID, metadata))
    	   return false;
       if (world.getBlockId(x, y, z) == cyb2.inst.block_ce.blockID)
       {
           TileEntity tile = world.getBlockTileEntity(x, y, z);
           if(tile instanceof tile_ce)
           	((tile_ce)tile).rid = (byte) (stack.getItemDamage() - 2);
           else
           	cyb2.inst.log(Level.WARNING, "resistor-placing failed");
           Block.blocksList[cyb2.inst.block_ce.blockID].onBlockPlacedBy(world, x, y, z, player);
           Block.blocksList[cyb2.inst.block_ce.blockID].onPostBlockPlaced(world, x, y, z, metadata);
       }
       return true;
    }
    public boolean placeBlockAt_capacitor(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata, double cval)
    {
       if (!world.setBlockAndMetadataWithNotify(x, y, z, cyb2.inst.block_ce.blockID, metadata))
    	   return false;
       if (world.getBlockId(x, y, z) == cyb2.inst.block_ce.blockID)
       {
           TileEntity tile = world.getBlockTileEntity(x, y, z);
           if(tile instanceof tile_ce)
           	((tile_ce)tile).cval = cval;
           else
           	cyb2.inst.log(Level.WARNING, "capacitor-placing failed");
           Block.blocksList[cyb2.inst.block_ce.blockID].onBlockPlacedBy(world, x, y, z, player);
           Block.blocksList[cyb2.inst.block_ce.blockID].onPostBlockPlaced(world, x, y, z, metadata);
       }
       return true;
    }
}
