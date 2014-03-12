package cyb.electricity;

public class res
{
	public static int texind(int d)
	{
		if(d % 9 < 3)
			return 3 + 16 * (d % 9) + (d / 9) * 2;
		if(d % 9 == 3)
			return 3 + 16 * 4 + (d / 9) * 2;
		if(d % 9 < 7)
			return 4 + 16 * (d % 9 - 4) + (d / 9) * 2;
		if(d % 9 == 7)
			return 4 + 16 * 4 + (d / 9) * 2;
		if(d % 9 == 8)
			return 55 + d / 9;
		return 0;
	}
	
	public static int getval(int d)
	{
		return (d % 9 + 1) * (int)Math.pow(10, d / 9);
	}
	
	public static boolean isvalid(int d)
	{
		return 0 <= d && d <= 44;
	}
}
