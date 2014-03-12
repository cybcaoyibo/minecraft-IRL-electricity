package cyb.electricity;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

public class handler_g implements IGuiHandler
{

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z)
	{
		switch(id)
		{
		case 0:
			if(world.getBlockTileEntity(x, y, z) instanceof tile_ce)
				return new container_ce(player.inventory, (tile_ce)world.getBlockTileEntity(x, y, z));
			break;
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world,
			int x, int y, int z)
	{
		switch(id)
		{
		case 0:
			if(world.getBlockTileEntity(x, y, z) instanceof tile_ce)
				return new gui_ce(player.inventory, (tile_ce)world.getBlockTileEntity(x, y, z));
			break;
		}
		return null;
	}

}
