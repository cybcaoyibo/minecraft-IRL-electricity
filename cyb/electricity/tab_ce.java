package cyb.electricity;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class tab_ce extends CreativeTabs
{
	public static String nm = "cyb2.tab_ce";
	public Item itm = Item.redstoneRepeater;
	public tab_ce()
	{
		super(nm);
	}

    public ItemStack getIconItemStack()
    {
        return new ItemStack(itm, 1);
    }
}
