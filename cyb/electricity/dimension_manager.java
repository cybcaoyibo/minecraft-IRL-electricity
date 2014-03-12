package cyb.electricity;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import cyb.cyb1;

public class dimension_manager
{
	public ArrayList<circuit_manager> circuits = new ArrayList<circuit_manager>();
	public ArrayList<component> components = new ArrayList<component>();
	
	boolean dirty = false;
	
	void add_component(component cmp)
	{
		for(int i = 0; i < components.size(); i++)
		{
			if(components.get(i) == cmp)
			{
				cyb2.inst.log(Level.WARNING, cmp.getClass() + " already added.");
				return;
			}
		}
		components.add(cmp);
		dirty = true;
	}
	
	void remove_component(component cmp)
	{
		for(int i = 0; i < components.size(); i++)
		{
			if(components.get(i) == cmp)
			{
				components.remove(i);
				dirty = true;
				return;
			}
		}
		cyb2.inst.log(Level.WARNING, cmp.getClass() + " not added yet.");
		return;
	}

	class connector
	{
		public component A, B;
		public int nA, nB;
		public int mA, mB;
	}
	
	void split_circuit(List<connector> src, List<connector> dst, connector now)
	{
		src.remove(now);
		dst.add(now);
		while(true)
		{
			boolean brk = false;
			for(int i = 0; i < src.size(); i++)
			{
				if(src.get(i).A == now.A || src.get(i).A == now.B || src.get(i).B == now.B || src.get(i).B == now.A)
				{
					split_circuit(src, dst, src.get(i));
					brk = true;
					break;
				}
			}
			if(!brk)
				break;
		}
	}
	
	void on_del()
	{
		for(int i = 0; i < circuits.size(); i++)
			circuits.get(i).on_del();
		circuits.clear();
	}
	
	void tick()
	{
		if(dirty)
		{
			for(int i = 0; i < circuits.size(); i++)
				circuits.get(i).on_del();
			circuits.clear();
			
			ArrayList<connector> connections = new ArrayList<connector>();
			for(int i = 0; i < components.size(); i++)
			{
				component cmp = components.get(i);
				
				for(int j = 0; j < cmp.num_nodes(); j++)
				{
					int dir = cmp.dir_nodes(j);
					int x = cmp.get_tile().xCoord;
					int y = cmp.get_tile().yCoord;
					int z = cmp.get_tile().zCoord;
					switch(dir)
					{
					case 0: y--; break;
					case 1: y++; break;
					case 2: z--; break;
					case 3: z++; break;
					case 4: x--; break;
					case 5: x++; break;
					}
					if(cmp.get_tile().worldObj.getBlockTileEntity(x, y, z) instanceof component)
					{
						component oth = (component)cmp.get_tile().worldObj.getBlockTileEntity(x, y, z);
						if(!components.contains(oth))
							continue;
						int oppdir = (dir % 2 == 0) ? (dir + 1) : (dir - 1);
						int othnode = -1;
						for(int k = 0; k < oth.num_nodes(); k++)
						{
							if(oth.dir_nodes(k) == oppdir)
							{
								othnode = k;
								break;
							}
						}
						if(othnode == -1)
							continue;
						
						connector ctr = new connector();
						ctr.A = cmp;
						ctr.B = oth;
						ctr.nA = j;
						ctr.nB = othnode;
						connections.add(ctr);
					}
				}
			}
			
			ArrayList<connector> valids = new ArrayList<connector>();
			
			for(int i = 0; i < connections.size(); i++)
			{
				boolean ok = true;
				for(int j = 0; j < i; j++)
				{
					connector a = connections.get(i);
					connector b = connections.get(j);
					if(a.A == b.B && a.B == b.A)
					{
						ok = false;
						break;
					}
				}
				if(ok)
					valids.add(connections.get(i));
			}
			
			
			
			//cyb2.inst.log(Level.INFO, "---" + (connections.size() - valids.size()));
			
			connections = valids;
			
			//cyb2.inst.log(Level.INFO, "dim cms: " + components.size());
			
			while(connections.size() > 0)
			{
				ArrayList<connector> now = new ArrayList<connector>();
				split_circuit(connections, now, connections.get(0));
				circuits.add(new circuit_manager(now));
			}
			
			dirty = false;
		}
		for(int i = 0; i < circuits.size(); i++)
			circuits.get(i).tick();
	}
}
