package cyb.electricity;

import net.minecraft.tileentity.TileEntity;

public interface component
{
	int num_nodes();
	int dir_nodes(int id);
	boolean unknown_current();
	double get_current_factor(int i, int u);
	double get_current_factor(int i);
	double get_voltage_factor(int u);
	double get_current_constant(int i);
	double get_voltage_constant();
	double get_voltage_factor();
	TileEntity get_tile();
	void set_voltage(int id, double u);
	void set_current(double i);
	void begin_iterate();
	boolean step_iterate();
	//return true if this component tend to connect all adjacent component and the dir_nodes is invalid when placing a block around the component.
	boolean is_wire();
	void on_add(circuit_manager cm);
	void on_remove(circuit_manager cm);
	public void end_iterate();
}
