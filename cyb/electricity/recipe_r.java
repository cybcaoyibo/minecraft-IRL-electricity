package cyb.electricity;

import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class recipe_r implements IRecipe
{
	ItemStack rst = null;
	
	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		boolean allsame = true;
		int prev = 0;
		int siz = inv.getSizeInventory();
		int am = 0;
		int cn = 0;
		for(int i = 0; i < siz; i++)
		{
			ItemStack is = inv.getStackInSlot(i);
			if(is == null)
				continue;
			if(is.stackSize == 0)
				continue;
			if(is.itemID != cyb2.inst.item_ce.itemID)
				return false;
			if(is.getItemDamage() < 2 || is.getItemDamage() > 46)
				return false;
			if(!res.isvalid(is.getItemDamage() - 2))
				return false;
			int now = res.getval(is.getItemDamage() - 2);
			if(allsame)
			{
				if(prev != now)
				{
					if(prev != 0)
						allsame = false;
					prev = now;
				}
			}
			am += now;
			cn++;
		}
		if(am == 0)
			return false;
		if(cn == 1)
		{
			if(am >= 10000 && am % 10000 == 0)
			{
				rst = new ItemStack(cyb2.inst.item_ce.itemID, 10, 28 + am / 10000);
				return true;
			}
			if(am >= 1000 && am % 1000 == 0)
			{
				rst = new ItemStack(cyb2.inst.item_ce.itemID, 10, 19 + am / 1000);
				return true;
			}
			if(am >= 100 && am % 100 == 0)
			{
				rst = new ItemStack(cyb2.inst.item_ce.itemID, 10, 10 + am / 100);
				return true;
			}
			if(am >= 10 && am % 10 == 0)
			{
				rst = new ItemStack(cyb2.inst.item_ce.itemID, 10, 1 + am / 10);
				return true;
			}
		}
		for(int i = 44; i >= 0; i--)
		{
			int sg = res.getval(i);
			if(allsame && sg == prev)
				continue;
			if(am >= sg && am % sg == 0)
				if(am / sg <= cyb2.inst.item_ce.getItemStackLimit())
				{
					rst = new ItemStack(cyb2.inst.item_ce.itemID, am / sg, i + 2);
					return true;
				}
		}
		return false;
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		if(rst == null) return null;
		return rst.copy();
	}

	@Override
	public int getRecipeSize()
	{
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return rst;
	}

}
