import java.io.*;
import java.util.*;
public class Polynomial {
	
	double[] coefficients;
	int[] degrees;
	public Polynomial() {
		this.coefficients = null;
		this.degrees = null;
	}
	public Polynomial(double[] coefficients, int[] degrees) {
		this.coefficients = coefficients;
		this.degrees = degrees;
	}
	
	public Polynomial(File f) {
		
		try {
			BufferedReader sc = new BufferedReader(new FileReader(f));
			char[] s = sc.readLine().toCharArray();
			int size = 0;
			for (char i: s) if (i == '+' || i == '-') size++;
			
			double[] c = new double[size + 1];
			int[] d = new int[size + 1];

			int pos = 0;
			String num = "";
			boolean deg = false;
			for (int i = 0; i < s.length; i++) {
				if (s[i] == '+' || s[i] == '-') {
					if (!deg) {
						d[pos] = 0;
						c[pos] = Double.parseDouble(num);
					}
					else if (num.equals("")) {
						d[pos] = 1;
					}
					else d[pos] = Integer.parseInt(num);
					
					deg = false;
					num = ""+s[i];
					pos++;
				}
				else if (s[i] == 'x') {
					c[pos] = Double.parseDouble(num);
					num = "";
					deg = true;
				}
				else num += s[i];
			}
			if (deg && num != "") d[size] = Integer.parseInt(num);
			else if (num != "") c[size] = Double.parseDouble(num);
			
			if (s.length == 0) {
				this.coefficients = null;
				this.degrees = null;
			}
			else {
				this.coefficients = c;
				this.degrees = d;			
			}

			sc.close();
		}
		catch (IOException e){
			System.out.println(e);
		}
	}
	
	public void saveToFile(String fname) {
		try {
			File f = new File(fname);
			PrintWriter sc = new PrintWriter(new FileWriter(f));
			String out = "";
			
			for (int i = 0; i < coefficients.length; i++) {
				if (coefficients[i] > 0) out += '+';
				
				out += coefficients[i];
				out += 'x';
				if (degrees[i] > 1) out += degrees[i];
			}
			sc.write(out.substring(1));
			sc.close();
			
		}
		catch (IOException e){
			System.out.println(e);
		}
	}
	
	public Polynomial add(Polynomial p) {
		
		if (this.coefficients == null) return new Polynomial(p.coefficients, p.degrees);
		else if (p.coefficients == null) return new Polynomial(this.coefficients, this.degrees);
		
		int size = 0;
		
		// find number of terms
		// assume the max degree polynomial < 1e8
		int[] freq = new int[(int)1e8];
		for (int i = 0; i < degrees.length; i++) {
			if (freq[degrees[i]] == 0) {
				size++;
				freq[degrees[i]] = 1;
			}
		}
		for (int i = 0; i < p.degrees.length; i++) {
			if (freq[p.degrees[i]] == 0) {
				size++;
				freq[p.degrees[i]] = 1;
			}
		}
		
		int[] ansdegree = new int[size];
		double[] ansco = new double[size];
		
		for (int i = 0; i < degrees.length; i++) {
			ansdegree[i] = degrees[i];
			ansco[i] = coefficients[i];
		}
		int pos = degrees.length;
		boolean found = false;
		
		// terms that cancel out
		int zeros = 0;
		for (int i = 0; i < p.degrees.length; i++) {
			for (int x = 0; x < pos; x++) {
				if (ansdegree[x] == p.degrees[i]) {
					ansco[x] += p.coefficients[i];
					if (ansco[x] == 0) zeros++;
					found = true;
					break;
				}
			}
			if (!found) {
				ansdegree[pos] = p.degrees[i];
				ansco[pos] = p.coefficients[i];
				pos++;
			}
			found = false;
		}
		
		double[] retco = new double[size - zeros];
		int[] retdeg = new int[size - zeros];
		int zerod = 0;
		for (int i = 0; i < size; i++) {
			if (ansco[i] != 0) {
				retco[i - zerod] = ansco[i];
				retdeg[i - zerod] = ansdegree[i];
			}
			else zerod++;
		}
		if (size - zeros == 0) return new Polynomial();
		return new Polynomial(retco, retdeg);
		
	}
	
	public Polynomial multiply(Polynomial p) {
		
		double[] co1 = this.coefficients;
		double[] co2 = p.coefficients;
		
		int[] deg1 = this.degrees;
		int[] deg2 = p.degrees;
		
		if (co1 == null || co2 == null) return new Polynomial();
		
		double[] ansco = new double[co1.length * co2.length];
		int[] ansdeg = new int[deg1.length * deg2.length];
		
		int terms = 0;
		boolean[] vis = new boolean[(int)1e9];
		for (int i = 0; i < co1.length; i++) {
			for (int x = 0; x < co2.length; x++) {
				ansco[i*co2.length + x] = co1[i] * co2[x];
				ansdeg[i*co2.length + x] = deg1[i] + deg2[x];
				if (!vis[deg1[i] + deg2[x]]) {
					terms++;
					vis[deg1[i] + deg2[x]] = true;
				}
			}
		}
		
		int[] retdeg = new int[terms];
		double[] retco = new double[terms];
		int pos = 0;
		for (int i = 0; i < ansdeg.length; i++) {
			boolean found = false;
			for (int x = 0; x < pos; x++) {
				if (retdeg[x] == ansdeg[i]) {
					retco[x] += ansco[i];
					found = true;
					break;
				}
			}
			if (!found) {
				retdeg[pos] = ansdeg[i];
				retco[pos] = ansco[i];
				pos++;
			}
		}
		
		return new Polynomial(retco, retdeg);
	}
	
	public double evaluate(double x) {
		double ans = 0;
		for (int i = 0; i < this.coefficients.length; i++) {
			ans += this.coefficients[i] * Math.pow(x, this.degrees[i]);
		}
		return ans;
	}
	
	public boolean hasRoot(double x) {
		return evaluate(x) == 0;
	}
	
}