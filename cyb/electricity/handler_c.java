package cyb.electricity;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;

import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StringTranslate;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;
import cyb.c.common;

public class handler_c implements IPacketHandler
{
	@Override
	public void onPacketData(INetworkManager manager,
			Packet250CustomPayload packet, Player player)
	{
		DataInputStream data = new DataInputStream(new ByteArrayInputStream(packet.data));
		try
		{
			byte type = data.readByte();
			switch(type)
			{
			case 0:
			{
				//int dim = data.readInt();
				//if(FMLClientHandler.instance().getClient().theWorld.getWorldInfo().getDimension() != dim)
				//{
				//	cyb2.inst.log(Level.INFO, "update tile in DIM" + dim + " while player in DIM" + FMLClientHandler.instance().getClient().theWorld.getWorldInfo().getDimension());
				//	return;
				//}
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();
				type = data.readByte();
				TileEntity te = FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(x, y, z);
				if(te == null)
					cyb2.inst.log(Level.WARNING, "missing tile @" + x + ", " + y + ", " + z);
				else if(!(te instanceof tile_ce))
					cyb2.inst.log(Level.WARNING, "invalid tile @" + x + ", " + y + ", " + z + "(" + te.getClass().getName() + "), should be " + tile_ce.class.getName());
				else
				{
					tile_ce tile = (tile_ce)te;
					int meta = FMLClientHandler.instance().getClient().theWorld.getBlockMetadata(x, y, z);
					switch(type)
					{
					case 0:
						if(meta != 1)
							cyb2.inst.log(Level.WARNING, "invalid meta @" + x + ", " + y + ", " + z + "(" + meta + "), should be 1");
						else
						{
							((tile_ce)te).facing = data.readByte();
							((tile_ce)te).is_burning_for_render = data.readBoolean();
							FMLClientHandler.instance().getClient().theWorld.markBlockForUpdate(x, y, z);
						}
						break;
					case 1:
						if(meta != 2)
							cyb2.inst.log(Level.WARNING, "invalid meta @" + x + ", " + y + ", " + z + "(" + meta + "), should be 2");
						else
						{
							((tile_ce)te).wire_XP = data.readBoolean();
							((tile_ce)te).wire_XN = data.readBoolean();
							((tile_ce)te).wire_YP = data.readBoolean();
							((tile_ce)te).wire_YN = data.readBoolean();
							((tile_ce)te).wire_ZP = data.readBoolean();
							((tile_ce)te).wire_ZN = data.readBoolean();
							FMLClientHandler.instance().getClient().theWorld.markBlockForUpdate(x, y, z);
						}
						break;
					case 2:
						if(meta != 3 && meta != 4)
							cyb2.inst.log(Level.WARNING, "invalid meta @" + x + ", " + y + ", " + z + "(" + meta + "), should be 3 or 4");
						else
						{
							((tile_ce)te).facing = data.readByte();
							((tile_ce)te).value_for_render = data.readByte();
							FMLClientHandler.instance().getClient().theWorld.markBlockForUpdate(x, y, z);
						}
						break;
					case 3:
						if(meta != 5)
							cyb2.inst.log(Level.WARNING, "invalid meta @" + x + ", " + y + ", " + z + "(" + meta + "), should be 5");
						else
						{
							((tile_ce)te).facing = data.readByte();
							((tile_ce)te).rid = data.readByte();
							FMLClientHandler.instance().getClient().theWorld.markBlockForUpdate(x, y, z);
						}
						break;
					case 4:
						if(meta != 6)
							cyb2.inst.log(Level.WARNING, "invalid meta @" + x + ", " + y + ", " + z + "(" + meta + "), should be 6");
						else
						{
							((tile_ce)te).facing = data.readByte();
							FMLClientHandler.instance().getClient().theWorld.markBlockForUpdate(x, y, z);
						}
						break;
					default:
						cyb2.inst.log(Level.WARNING, "invalid tile type @" + x + ", " + y + ", " + z + "(" + type + ")");
					}
				}
			}
				break;
			case 1:
			{
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();
				TileEntity te = FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(x, y, z);
				if(te == null)
					cyb2.inst.log(Level.WARNING, "missing tile @" + x + ", " + y + ", " + z);
				else if(!(te instanceof tile_ce))
					cyb2.inst.log(Level.WARNING, "invalid tile @" + x + ", " + y + ", " + z + "(" + te.getClass().getName() + "), should be " + tile_ce.class.getName());
				else
				{
					tile_ce tile = (tile_ce)te;
					int meta = FMLClientHandler.instance().getClient().theWorld.getBlockMetadata(x, y, z);
					if(meta != 1)
						cyb2.inst.log(Level.WARNING, "invalid meta @" + x + ", " + y + ", " + z + "(" + meta + "), should be 1");
					else
					{
						((tile_ce)te).max_burn_time = data.readInt();
						((tile_ce)te).remain_burn_time = data.readInt();
						((tile_ce)te).voltage_for_render = data.readDouble();
						((tile_ce)te).generator_current = data.readDouble();
						((tile_ce)te).power = data.readDouble();
					}
				}
			}
				break;
			case 2:
			{
				double cur = data.readDouble();
				int count = data.readInt();
				try
				{
					String curstr = String.valueOf(Math.round(cur * 100) / 100.);
					for(int i = 0; i < count; i++)
						FMLClientHandler.instance().getClient().thePlayer.sendChatToPlayer(StringTranslate.getInstance().translateKey(cyb2.id + ".current") + ": " + curstr + "A");
				}catch(Exception e){}
			}
				break;
			case 3:
			{
				int x = data.readInt();
				int y = data.readInt();
				int z = data.readInt();
				TileEntity te = FMLClientHandler.instance().getClient().theWorld.getBlockTileEntity(x, y, z);
				if(te == null)
					cyb2.inst.log(Level.WARNING, "missing tile @" + x + ", " + y + ", " + z);
				else if(!(te instanceof tile_ce))
					cyb2.inst.log(Level.WARNING, "invalid tile @" + x + ", " + y + ", " + z + "(" + te.getClass().getName() + "), should be " + tile_ce.class.getName());
				else
				{
					tile_ce tile = (tile_ce)te;
					int meta = FMLClientHandler.instance().getClient().theWorld.getBlockMetadata(x, y, z);
					if(meta != 3 && meta != 4)
						cyb2.inst.log(Level.WARNING, "invalid meta @" + x + ", " + y + ", " + z + "(" + meta + "), should be 3 or 4");
					else
					{
						((tile_ce)te).real_value = data.readDouble();
					}
				}
				break;
			}
			case 4:
			{
				double r = data.readDouble();
				String rstr = String.valueOf(Math.round(r * 100.) / 100.);
				try
				{
					FMLClientHandler.instance().getClient().thePlayer.sendChatToPlayer(StringTranslate.getInstance().translateKey(cyb2.id + ".resistance") + ": " + rstr + "Ω");
				}catch(Exception ex){}
				break;
			}
			case 5:
			{
				double c = data.readDouble();
				String cstr = cap.uconv(c);
				try
				{
					FMLClientHandler.instance().getClient().thePlayer.sendChatToPlayer(StringTranslate.getInstance().translateKey(cyb2.id + ".capacitance") + ": " + cstr);
				}catch(Exception ex){}
				break;
			}
			default:
				cyb2.inst.log(Level.WARNING, "invalid type " + type);
			}
		}
		catch(IOException e)
		{
			cyb2.inst.log(Level.WARNING, "got a malformed packet!");
			e.printStackTrace();
		}
	}
}
