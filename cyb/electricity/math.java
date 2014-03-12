package cyb.electricity;

import java.util.ArrayList;
import java.util.logging.Level;

public class math
{
	public static double threshold = 0.00000000001;
	
	 public static  int[] factor(double a[][]) {
		   int n = a.length;
		   int[] ipvt = new int[n];
			double scaleFactors[];
			int i,j,k;

			scaleFactors = new double[n];
			
		        // divide each row by its largest element, keeping track of the
			// scaling factors
			for (i = 0; i != n; i++) { 
			    double largest = 0;
			    for (j = 0; j != n; j++) {
				double x = Math.abs(a[i][j]);
				if (x > largest)
				    largest = x;
			    }
			    // if all zeros, it's a singular matrix
			    if (largest == 0)
			    	throw new RuntimeException("can't solve circuit(singular matrix)");
			    scaleFactors[i] = 1.0/largest;
			}
			
		        // use Crout's method; loop through the columns
			for (j = 0; j != n; j++) {
			    
			    // calculate upper triangular elements for this column
			    for (i = 0; i != j; i++) {
				double q = a[i][j];
				for (k = 0; k != i; k++)
				    q -= a[i][k]*a[k][j];
				a[i][j] = q;
			    }

			    // calculate lower triangular elements for this column
			    double largest = 0;
			    int largestRow = -1;
			    for (i = j; i != n; i++) {
				double q = a[i][j];
				for (k = 0; k != j; k++)
				    q -= a[i][k]*a[k][j];
				a[i][j] = q;
				double x = Math.abs(q);
				if (x >= largest) {
				    largest = x;
				    largestRow = i;
				}
			    }
			    
			    // pivoting
			    if (j != largestRow) {
				double x;
				for (k = 0; k != n; k++) {
				    x = a[largestRow][k];
				    a[largestRow][k] = a[j][k];
				    a[j][k] = x;
				}
				scaleFactors[largestRow] = scaleFactors[j];
			    }

			    // keep track of row interchanges
			    ipvt[j] = largestRow;

			    // avoid zeros
			    if (a[j][j] == 0.0) {
			    	cyb2.inst.log(Level.INFO, "zero: " + j);
				a[j][j]=1e-18;
			    }

			    if (j != n-1) {
				double mult = 1.0/a[j][j];
				for (i = j+1; i != n; i++)
				    a[i][j] *= mult;
			    }
			}
			return ipvt;
		    }
	 
	 public static void solve(double a[][], int ipvt[], double b[]) {
		int n = a.length;
		 int i;

		// find first nonzero b element
		for (i = 0; i != n; i++) {
		    int row = ipvt[i];

		    double swap = b[row];
		    b[row] = b[i];
		    b[i] = swap;
		    if (swap != 0)
			break;
		}
		
		int bi = i++;
		for (; i < n; i++) {
		    int row = ipvt[i];
		    int j;
		    double tot = b[row];
		    
		    b[row] = b[i];
		    // forward substitution using the lower triangular matrix
		    for (j = bi; j < i; j++)
			tot -= a[i][j]*b[j];
		    b[i] = tot;
		}
		for (i = n-1; i >= 0; i--) {
		    double tot = b[i];
		    
		    // back-substitution using the upper triangular matrix
		    int j;
		    for (j = i+1; j != n; j++)
			tot -= a[i][j]*b[j];
		    b[i] = tot/a[i][i];
		}
	    }

	public static void shift(double[][] matrix, right_hand[] rhs, int r, int[] shift_info)
	{
		int dim = rhs.length;
		int j = r;
		for(int i = r + 1; i < dim; i++)
			if(Math.abs(matrix[j][r]) < Math.abs(matrix[i][r]))
				j = i;
		if(j == r)
			return;
		double[] mtmp = matrix[r];
		matrix[r] = matrix[j];
		matrix[j] = mtmp;
		right_hand tmp = rhs[r];
		rhs[r] = rhs[j];
		rhs[j] = tmp;
		int itmp = shift_info[r];
		shift_info[r] = shift_info[j];
		shift_info[j] = itmp;
	}
	
	public static int[] transform(double[][] matrix, right_hand[] rhs)
	{
		int dim = rhs.length;
		int[] shift_info = new int[dim];
		for(int i = 0; i < dim; i++)
			shift_info[i] = i;
		for(int i = 0; i < dim; i++)
		{
			shift(matrix, rhs, i, shift_info);
			double w = matrix[i][i];
			for(int k = i; k < dim; k++)
				matrix[i][k] /= w;
			rhs[i].ops.clear();
			rhs[i].ops.add(new right_hand.op_mul(1. / w));
			for(int j = i + 1; j < dim; j++)
			{
				if(Math.abs(matrix[j][i]) < threshold)
					continue;
				double w1 = matrix[j][i] / matrix[i][i];
				for(int k = i; k < dim; k++)
					matrix[j][k] -= matrix[i][k] * w1;
				rhs[j].ops.add(new right_hand.op_sub(rhs[i], w1));
			}
		}
		return shift_info;
	}
	
	public static double[] result(double[][] matrix, right_hand[] rhs)
	{
		int dim = rhs.length;
		double[] rights = new double[dim];
		for(int i = 0; i < dim; i++)
			rhs[i].solved = false;
		while(true)
		{
			boolean need = false;
			for(int i = 0; i < dim; i++)
				if(!rhs[i].solved)
				{
					need = true;
					break;
				}
			if(!need)
				break;
			for(int i = 0; i < dim; i++)
			{
				if(rhs[i].solved)
					continue;
				boolean ok = true;
				for(int j = 0; j < rhs[i].ops.size(); j++)
					if(rhs[i].ops.get(j) instanceof right_hand.op_sub)
						if(!((right_hand.op_sub)rhs[i].ops.get(j)).oth.solved)
						{
							ok = false;
							break;
						}
				if(!ok)
					continue;
				rhs[i].solved = true;
				rights[i] = rhs[i].v();
			}
		}
		
		for(int i = dim - 1; i >= 0; i--)
			for(int j = dim - 1; j > i; j--)
				rights[i] -= rights[j] * matrix[i][j];
		return rights;
	}
}
