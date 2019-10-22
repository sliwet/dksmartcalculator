public class Function{
	public static Boolean contains(String name){
		switch (name){
			case "COS" : return true;
			case "SIN" : return true;
			case "TAN" : return true;
			case "ACOS": return true;
			case "ASIN": return true;
			case "ATAN": return true;
			case "SQRT": return true;
			case "E" : return true;
			case "LN"  : return true;
			case "LOG" : return true;
			case "LG"  : return true;
			case "POL" : return true;
			case "RAD" : return true;
			case "DEG" : return true;
		}
		return false;
	}

	public static double evaluate(String name,double [] x) throws Exception{
		switch (name){
			case "COS" : return cos(x[0]);
			case "SIN" : return sin(x[0]);
			case "TAN" : return tan(x[0]);
			case "ACOS": return acos(x[0]);
			case "ASIN": return asin(x[0]);
			case "ATAN": return atan(x[0]);
			case "SQRT": return Math.sqrt(x[0]);
			case "E" : return Math.exp(x[0]);
			case "LN"  : return Math.log(x[0]);
			case "LOG" : return Math.log10(x[0]);
			case "LG"  : return log2(x[0]);
			case "POL" : return pol(x);
			case "RAD" : return rad(x[0]);
			case "DEG" : return deg(x[0]);
		}
		throw new Exception(name + " is not one of function names\n");
	}

	public static double log2(double x){
		return Math.log(x)/Math.log(2.0);
	}

	public static double cos(double x){
		return Math.cos(x);
	}

	public static double sin(double x){
		return Math.sin(x);
	}

	public static double tan(double x){
		return Math.tan(x);
	}

	public static double acos(double x){
		return Math.acos(x);
	}

	public static double asin(double x){
		return Math.asin(x);
	}

	public static double atan(double x){
		return Math.atan(x);
	}

	public static double rad(double deg){
		return deg * (Math.PI / 180.0);
	}

	public static double deg(double rad){
		return rad * (180.0 / Math.PI);
	}

	public static double pol(double [] x){ // first element is x, coefs: x[1]: 0th order  ... x[n]: (n-1)th order
		double fval = 0.0;
        for(int i = (x.length-1);i > 0;i--) fval = fval * x[0] + x[i];
		return fval;
	}

	public static double factorial(double x){
		int xi = toInt(x);
		if(xi < 2) return 1.0;
		double retvalue = 1.0;
		for(int i = xi; i>1;i--) retvalue *= i;
		return retvalue;
	}

	public static int toInt(double x){
		int n = (int)x;
		if((x > 0) && (x - n > 0.5)) return ++n;
		if((x < 0) && (n - x > 0.5)) return --n;
		return n;
	}

    public static double[] stat(double xn,double [] stat0){
    	if(stat0 == null){
	        return (new double []{xn,0.0,xn,xn,1.1});
    	}
    	else if(stat0[4] < 1){
	        return (new double []{xn,0.0,xn,xn,1.1});
    	}
    	else{
	    	double anm1 = stat0[0],snm1 = stat0[1],min0 = stat0[2],max0 = stat0[3];
	    	int n = (int)(stat0[4]) + 1;
	    	double an = xn/n + (n-1)*anm1/n;
	    	double sn2 = (xn*xn + snm1*snm1*(n-2) + (n-1)*anm1*anm1 - n*an*an)/(n-1);
	    	double min = Math.min(xn,min0);
	    	double max = Math.max(xn,max0);
	        return (new double[]{an,Math.sqrt(sn2),min,max,(n + 0.1)});
    	}
    }

	public static double toDouble(String str) throws Exception{
		if(str.equals("PI")) return Math.PI;
		else return Double.parseDouble(str);
	}
}

