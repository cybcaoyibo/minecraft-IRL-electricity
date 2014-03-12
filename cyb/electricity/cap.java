package cyb.electricity;

public class cap
{
	//0: uF
	//1: mF
	//2: F
	static byte unitlevel(double d)
	{
		if(d >= 1)
			return 2;
		if(d >= 0.001)
			return 1;
		return 0;
	}
	
	static double unitconv(double d, int lvl)
	{
		if(lvl == 2)
			return d;
		if(lvl == 1)
			return d * 1000.;
		if(lvl == 0)
			return d * 1000000.;
		throw new RuntimeException("unknown unit");
	}
	
	static String unitstr(int lvl)
	{
		if(lvl == 2)
			return "F";
		if(lvl == 1)
			return "mF";
		if(lvl == 0)
			return "μF";
		throw new RuntimeException("unknown unit");
	}
	
	static String uconv(double d)
	{
		int lvl = unitlevel(d);
		if(Math.abs(Math.floor(unitconv(d, lvl)) - unitconv(d, lvl)) < 0.01)
			return (int)unitconv(d, lvl) + unitstr(lvl);
		return Math.round(unitconv(d, lvl) * 100.) / 100. + unitstr(lvl);
	}
	
	static int texind()
	{
		return 60;
	}
}
