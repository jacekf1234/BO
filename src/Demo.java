
public class Demo {
	public static void main(String[] args) {
		TSP problem = new TSP(512);
		long start = System.currentTimeMillis();
		problem.solve(90);
		System.out.println("Time spent: " + (System.currentTimeMillis() - start) + " ms.");
	}
}
