package cyb.electricity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StringTranslate;

public class iblk_ce extends ItemBlock
{
	public static String nm = "cyb2.iblk_ce";
	public iblk_ce(int id)
	{
		super(id);
		this.setHasSubtypes(true);
		this.setItemName(nm);
		this.setIconIndex(0);
		this.setTextureFile(cyb2.img);
	}
	
    public String getItemNameIS(ItemStack is)
    {
    	if(is.getItemDamage() > 5)
    		return nm + "._0";
        return nm + "._" + is.getItemDamage();
    }
    
    public int getMetadata(int dmg)
    {
    	switch(dmg)
    	{
    	case 1:
    		return 1;
    	case 2:
    		return 2;
    	case 3:
    		return 3;
    	case 4:
    		return 4;
    	}
        return 0;
    }
    
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean adv)
    {
    	if(is.getItemDamage() == 1)
    	{
    		list.add(StringTranslate.getInstance().translateKey(cyb2.id + ".max_current") + ": 1A");
    		list.add(StringTranslate.getInstance().translateKey(cyb2.id + ".max_voltage") + ": 12V");
    	}
    }
}
