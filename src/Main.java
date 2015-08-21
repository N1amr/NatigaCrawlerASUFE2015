import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
	Scanner in = new Scanner(System.in);

	System.out.print("Enter first seat number: ");
	int fromSN = in.nextInt();

	System.out.print("Enter last seat number: ");
	int toSN = in.nextInt();

	try {
	    Collect.initialize();
	    Collect.retryFailed();
	    Collect.collectResults(fromSN, toSN + 1, 1, true);
	    Analyse.saveCSV(fromSN, toSN + 1);
	} catch (Exception e) {
	    e.printStackTrace();
	} finally {
	    in.close();
	}
    }
}
