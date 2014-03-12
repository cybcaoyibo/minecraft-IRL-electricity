package cyb.electricity;

import java.util.HashMap;
import java.util.Iterator;

public class global_manager
{
	public HashMap<Integer, dimension_manager> dims = new HashMap<Integer, dimension_manager>();
	
	dimension_manager get_dim(int dim)
	{
		if(dims.containsKey(dim))
			return dims.get(dim);
		dimension_manager neb = new dimension_manager();
		dims.put(dim, neb);
		return neb;
	}
	
	void add_component(component cmp)
	{
		get_dim(cmp.get_tile().worldObj.getWorldInfo().getDimension()).add_component(cmp);
	}
	
	void remove_component(component cmp)
	{
		get_dim(cmp.get_tile().worldObj.getWorldInfo().getDimension()).remove_component(cmp);
	}
	
	void tick()
	{
		Iterator it = dims.values().iterator();
		while(it.hasNext())
			((dimension_manager)it.next()).tick();
	}
	
	void on_del()
	{
		Iterator it = dims.values().iterator();
		while(it.hasNext())
			((dimension_manager)it.next()).on_del();
	}
}
