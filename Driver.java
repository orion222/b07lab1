import java.io.File;
import java.io.FileNotFoundException;
public class Driver {
	public static void main(String[] args) throws FileNotFoundException {
		
		File f = new File("poly.txt");
		Polynomial k = new Polynomial(f);
		System.out.println(k.evaluate(3));
		for (int i = 0; i < k.coefficients.length; i++) {
			System.out.println(k.coefficients[i] + " " + k.degrees[i]);
		}
		System.out.println();
		
		Polynomial p1 = new Polynomial(new double[] {1, 2, 3, 9}, new int[] {1, 2, 3, 0});
		Polynomial p2 = new Polynomial(new double[] {2, -1, 6}, new int[] {3, 1, 2});
		Polynomial s = p1.multiply(p2);
		
		for (int i = 0; i < s.coefficients.length; i++) {
			System.out.println(s.coefficients[i] + " " + s.degrees[i]);
		}
		System.out.println();
		
		Polynomial k1 = new Polynomial(new double[] {1}, new int[] {1});
		Polynomial k2 = new Polynomial(new double[] {-1}, new int[] {1});
		Polynomial m = p1.add(p2);
		for (int i = 0; i < m.coefficients.length; i++) {
			System.out.println(m.coefficients[i] + " " + m.degrees[i]);
		}
		
		
		
		System.out.println("N:");
		Polynomial n = s.multiply(k);
		for (int i = 0; i < n.coefficients.length; i++) {
			System.out.println(n.coefficients[i] + " " + n.degrees[i]);
		}
		n.saveToFile("poly.txt");
		System.out.println("s(0.1) = " + s.evaluate(0.1));
		if (s.hasRoot(-1))
			System.out.println("-1 is a root of s");
		else
			System.out.println("-1 is not a root of s");
	}
}
