package cyb.electricity;

import java.util.ArrayList;
import java.util.Iterator;

public class right_hand
{
	public static abstract class op
	{
		public abstract double apply(double v);
	}
	
	public static class op_mul extends op
	{
		double f;
		public op_mul(double f)
		{
			this.f = f;
		}
		
		public double apply(double v)
		{
			return v * f;
		}
	}
	
	public static class op_sub extends op
	{
		right_hand oth;
		double f;
		public op_sub(right_hand oth, double f)
		{
			this.oth = oth;
			this.f = f;
		}
		
		public double apply(double v)
		{
			return v - f * oth.v();
		}
	}
	
	public ArrayList<op> ops = new ArrayList<op>();
	
	public double n = 0;
	public boolean solved = false;
	
	public double v()
	{
		double v = n;
		Iterator<op> it = ops.iterator();
		while(it.hasNext())
			v = it.next().apply(v);
		return v;
	}
	
	public right_hand(double n)
	{
		this.n = n;
	}
	
	public right_hand()
	{
	}
}
