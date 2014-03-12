package cyb.electricity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;

import cyb.electricity.dimension_manager.connector;

public class circuit_manager
{
	public boolean dirty = true;
	class int_pair
	{
		int a = 0, b = 0;
	}
	public static double r_wire = 0.001;
	public static double r_air = 1. / 0.;
	public static double timestep = 1. / 20.;
	public ArrayList<connector> cns;
	public double[][] matrix;
	//public right_hand[] rhs;
	public int[] shift_info;
	//public int[] shift_info_i;
	public ArrayList<component> cms = new ArrayList<component>();
	public ArrayList<ArrayList<Integer>> ns = new ArrayList<ArrayList<Integer>>();
	public HashMap<Integer, Integer> vs = new HashMap<Integer, Integer>();
	public ArrayList<int_pair> invalids = new ArrayList<int_pair>();
	int size_valid_ns;
	int size_invalid_ns;
	int size_vs;
	
	void on_del()
	{
		Iterator it = cms.iterator();
		while(it.hasNext())
			((component)it.next()).on_remove(this);
	}
	
	int push_cms(component c)
	{
		int id = cms.indexOf(c);
		if(id == -1)
		{
			cms.add(c);
			c.on_add(this);
			return cms.size() - 1;
		}
		return id;
	}
	
	void extend(ArrayList<?> list, int size)
	{
		while(list.size() < size)
			list.add(null);
	}
	
	void build_matrix()
	{
		int mat_size = size_invalid_ns + size_vs - 1;
		//rhs = new right_hand[mat_size];
		//for(int i = 0; i < mat_size; i++)
		//	rhs[i] = new right_hand();
		matrix = new double[mat_size][];
		for(int i = 0; i < mat_size; i++)
		{
			matrix[i] = new double[mat_size];
			for(int j = 0; j < mat_size; j++)
				matrix[i][j] = 0;
		}
		for(int i = 0; i < cns.size(); i++)
		{
			connector cn = cns.get(i);
			if(cn.mA > 0)
			{
				if(cn.mB > 0)
					matrix[cn.mA - 1][cn.mB - 1] += 1. / r_wire;
				matrix[cn.mA - 1][cn.mA - 1] -= 1. / r_wire;
			}
			if(cn.mB > 0)
			{
				if(cn.mA > 0)
					matrix[cn.mB - 1][cn.mA - 1] += 1. / r_wire;
				matrix[cn.mB - 1][cn.mB - 1] -= 1. / r_wire;
			}
		}
		for(int i = 0; i < cms.size(); i++)
		{
			component cm = cms.get(i);
			for(int j = 0; j < cm.num_nodes(); j++)
			{
				int cur = ns.get(i).get(j);
				if(cur == 0)
					continue;
				for(int k = 0; k < cm.num_nodes(); k++)
				{
					int vol = ns.get(i).get(k);
					if(vol == 0)
						continue;
					matrix[cur - 1][vol - 1] += cm.get_current_factor(j, k);
				}
			}
		}
		for(int i = 0; i < invalids.size(); i++)
		{
			int_pair p = invalids.get(i);
			int in = ns.get(p.a).get(p.b);
			if(in > 0)
				matrix[in - 1][in - 1] -= 1. / r_air;
		}
		Iterator it = vs.entrySet().iterator();
		while(it.hasNext())
		{
			Entry<Integer, Integer> en = (Entry<Integer, Integer>)it.next();
			if(en.getValue() + size_invalid_ns == 0) continue;
			component cm = cms.get(en.getKey());
			for(int i = 0; i < cm.num_nodes(); i++)
			{
				int n = ns.get(en.getKey()).get(i);
				if(n == 0)
					continue;
				matrix[n - 1][en.getValue() + size_invalid_ns - 1] += cm.get_current_factor(i);
				matrix[en.getValue() + size_invalid_ns - 1][n - 1] += cm.get_voltage_factor(i);
				matrix[en.getValue() + size_invalid_ns - 1][en.getValue() + size_invalid_ns - 1] = cm.get_voltage_factor();
			}
		}
		shift_info = math.factor(matrix);
	}
	
	public circuit_manager(ArrayList<connector> cns)
	{
		this.cns = cns;
		int now_nid = 0;
		this.cms.clear();
		this.ns.clear();
		for(int i = 0; i < cns.size(); i++)
		{
			int A_id = push_cms(cns.get(i).A);
			int B_id = push_cms(cns.get(i).B);
			cns.get(i).mA = now_nid++;
			cns.get(i).mB = now_nid++;
			extend(ns, cms.size());
			ArrayList<Integer> A_nid = null;
			ArrayList<Integer> B_nid = null;
			A_nid = ns.get(A_id);
			B_nid = ns.get(B_id);
			if(A_nid == null)
			{
				A_nid = new ArrayList<Integer>();
				extend(A_nid, cns.get(i).A.num_nodes());
				for(int j = 0; j < cns.get(i).A.num_nodes(); j++)
					A_nid.set(j, -1);
				ns.set(A_id, A_nid);
			}
			if(B_nid == null)
			{
				B_nid = new ArrayList<Integer>();
				extend(B_nid, cns.get(i).B.num_nodes());
				for(int j = 0; j < cns.get(i).B.num_nodes(); j++)
					B_nid.set(j, -1);
				ns.set(B_id, B_nid);
			}
			A_nid.set(cns.get(i).nA, cns.get(i).mA);
			B_nid.set(cns.get(i).nB, cns.get(i).mB);
		}
		size_valid_ns = now_nid;
		invalids.clear();
		for(int i = 0; i < ns.size(); i++)
		{
			ArrayList<Integer> nds = ns.get(i);
			for(int j = 0; j < nds.size(); j++)
			{
				if(nds.get(j) == -1)
				{
					int_pair p = new int_pair();
					p.a = i;
					p.b = j;
					invalids.add(p);
					nds.set(j, now_nid++);
				}
			}
		}
		size_invalid_ns = now_nid;
		int now_vid = 0;
		this.vs.clear();
		for(int i = 0; i < cms.size(); i++)
		{
			if(cms.get(i).unknown_current())
				vs.put(i, now_vid++);
		}
		size_vs = now_vid;
	}
	
	public void tick()
	{
		if(dirty)
		{
			build_matrix();
			dirty = false;
		}
		for(int i = 0; i < cms.size(); i++)
			cms.get(i).begin_iterate();
		boolean need = true;
		while(need)
		{
			need = false;
			double[] rhs = new double[matrix.length];
			for(int i = 0; i < rhs.length; i++)
				rhs[i] = 0;
			for(int i = 0; i < cms.size(); i++)
			{
				component cm = cms.get(i);
				for(int j = 0; j < cm.num_nodes(); j++)
				{
					int n = ns.get(i).get(j);
					if(n == 0)
						continue;
					rhs[n - 1] += cm.get_current_constant(j);
				}
			}
			Iterator it = vs.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<Integer, Integer> en = (Entry<Integer, Integer>)it.next();
				if(en.getValue() + size_invalid_ns == 0) continue;
				component cm = cms.get(en.getKey());
				rhs[en.getValue() + size_invalid_ns - 1] += cm.get_voltage_constant();
			}
			math.solve(matrix, shift_info, rhs);
			for(int i = 0; i < ns.size(); i++)
			{
				for(int j = 0; j < ns.get(i).size(); j++)
				{
					int n =  ns.get(i).get(j);
					if(n == 0)
						continue;
					cms.get(i).set_voltage(j, rhs[n - 1]);
				}
			}
			it = vs.entrySet().iterator();
			while(it.hasNext())
			{
				Entry<Integer, Integer> en = (Entry<Integer, Integer>)it.next();
				if(en.getValue() + size_invalid_ns == 0) continue;
				cms.get(en.getKey()).set_current(rhs[en.getValue() + size_invalid_ns - 1]);
			}
			for(int i = 0; i < cms.size(); i++)
				if(cms.get(i).step_iterate())
					need = true;
		}
		for(int i = 0; i < cms.size(); i++)
			cms.get(i).end_iterate();
	}
}
