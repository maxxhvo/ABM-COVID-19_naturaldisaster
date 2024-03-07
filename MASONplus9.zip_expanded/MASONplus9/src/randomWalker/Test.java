package randomWalker;

public class Test {

	public static void main(String[] args) {
		double x = 2; //Math.random()*2;
		double y = 2;//Math.random()*2;
		final double h = Math.sqrt(x*x + y*y);
		final double xcos = x/h;
		double result1 = Math.atan2(x,y);
		double result2 = (y >= 0) ? Math.acos(xcos) :-Math.acos(xcos); //don't need to compute ycos
		System.out.println("x = "+x+" y = "+y+" result1 = "+result1+" result2 = "+result2);
	}

}
