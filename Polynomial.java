public class Polynomial {
	
	double[] coefficients;
	public Polynomial() {
		this.coefficients = new double[1];
	}
	public Polynomial(double[] args) {
		this.coefficients = args;
	}
	
	public Polynomial add(Polynomial x) {
		int a = this.coefficients.length;
		int b = x.coefficients.length;
		
		int size = Math.max(a, b);
		double[] ans = new double[size];
		for (int i = 0; i < size; i++) {
			if (i < a) ans[i] += this.coefficients[i];
			if (i < b) ans[i] += x.coefficients[i];
		}
		return new Polynomial(ans);
	}
	
	public double evaluate(double x) {
		double ans = 0;
		for (int i = 0; i < this.coefficients.length; i++) {
			ans += this.coefficients[i] * Math.pow(x, i);
		}
		return ans;
	}
	
	public boolean hasRoot(double x) {
		return evaluate(x) == 0;
	}
	
}