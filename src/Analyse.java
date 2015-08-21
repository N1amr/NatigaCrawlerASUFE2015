import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONObject;

public class Analyse {

    public static void main(String[] args) {
	Collect.loadSaveFolders();

	// searchByName("ÎØÇÈ");

	// calculateAverage();
	// calculateRank(1426);
    }

    public static void saveCSV(int fromSN, int toSN) {
	ArrayList<Natega> nategas = Collect.getNategasAsList(fromSN, toSN);

	System.out.println("Sorting...");
	Collections.sort(nategas);
	Collections.reverse(nategas);

	String note = "NOTE: There are only " + nategas.size() + " saved results";

	File file = new File("Ranks.csv");
	try {
	    if (!file.exists())
		file.createNewFile();

	    PrintWriter printWriter = new PrintWriter(file);
	    printWriter.println("Rank,Seat Number,Name,Score,Percentage");
	    for (int i = 0; i < nategas.size(); i++) {
		Natega natega = nategas.get(i);
		printWriter.print("" + (i + 1) + "," + natega.getSeatNumber() + "," + natega.getName() + ","
			+ natega.getTotal() + "," + natega.getPercentage() + "%");
		// System.out.println("" + (i + 1) + "," +
		// natega.getSeatNumber() + "," + natega.getName() + ","
		// + natega.getTotal() + "," + natega.getPercentage() + "%");
		if (i != 2)
		    printWriter.println();
		else
		    printWriter.println(",," + note);
	    }
	    printWriter.flush();
	    printWriter.close();

	    System.out.println("************************************************");
	    System.out.println("A .csv file was saved at:\n" + file.getAbsolutePath());
	    System.out.println(note);
	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    private static int savedResultsCount() {
	return Collect.resultsFolder.listFiles().length;
    }

    private static void searchByName(String string) {
	for (File resultFile : Collect.resultsFolder.listFiles()) {
	    JSONObject jsonObject = JSONHelper.loadJSONObject(resultFile);
	    Natega natega = new Natega(jsonObject);
	    if (natega.getName().contains(string)) {
		System.out.println(natega);
		System.out.println("****************************************************");
	    }
	}
	System.out.println("Finished");
    }

    private static void calculateAverage() {
	int count = 0;
	double sum = 0;
	for (File resultFile : Collect.resultsFolder.listFiles()) {
	    JSONObject jsonObject = JSONHelper.loadJSONObject(resultFile);
	    Natega natega = new Natega(jsonObject);
	    count++;
	    sum += natega.getTotal();
	}
	System.out.println("Average score is " + ((sum / count) / 4.1));
    }

    private static void calculateRank(float degree) {
	int above = 0;
	for (File resultFile : Collect.resultsFolder.listFiles()) {
	    JSONObject jsonObject = JSONHelper.loadJSONObject(resultFile);
	    Natega natega = new Natega(jsonObject);
	    if (natega.getTotal() >= degree)
		above++;
	}
	System.out.println("A student got " + degree + " is ranked #" + above);
    }
}
