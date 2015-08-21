import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Scanner;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSubmitInput;
import com.gargoylesoftware.htmlunit.html.HtmlTextInput;

public class Collect {
    public static int fromSeatNumber = 23000;
    public static int toSeatNumber = 23593;
    public static int step = 1;
    public static boolean verbose = true;

    public static boolean skipInvalid = true;

    public static WebClient webClient;
    public static HtmlPage page;
    public static HtmlForm form;
    public static HtmlTextInput txtSeatNumber;
    public static HtmlSubmitInput btnSubmit;

    public static boolean[] checked = new boolean[100000];

    public static File resultsFolder;
    public static File invalidFolder;
    public static File failedFolder;

    public static void main(String[] args) throws Exception {
	getInputFromUser();

	initialize();

	retryFailed();
	collectResults(fromSeatNumber, toSeatNumber, step, verbose);

	System.out.println("************Done************");
	System.out.println(howManyChecked());
    }

    static void initialize() throws FailingHttpStatusCodeException, MalformedURLException, IOException {
	loadSaveFolders();

	loadSavedFiles();
	loadInvalids();

	// Set up form variables
	webClient = new WebClient();
	page = webClient.getPage("http://engasu.net/Spring/Results.aspx");
	form = page.getForms().get(0);
	txtSeatNumber = form.getInputByName("txtID");
	btnSubmit = form.getInputByName("btnDisplayResults");
    }

    public static void loadSaveFolders() {
	resultsFolder = new File("data/results");
	invalidFolder = new File("data/invalid");
	failedFolder = new File("data/failed");

	resultsFolder.mkdirs();
	invalidFolder.mkdirs();
	failedFolder.mkdirs();
    }

    private static void getInputFromUser() {
	Scanner in = new Scanner(System.in);
	System.out.print("Enter Start Number: ");
	fromSeatNumber = in.nextInt();

	System.out.print("Enter End Number: ");
	toSeatNumber = in.nextInt();

	System.out.print("Enter Step: ");
	step = in.nextInt();

	in.close();
    }

    public static void collectResult(int seatNumber, boolean verbose) throws Exception {

	if (verbose)
	    System.out.print("" + seatNumber + ": ");

	Natega natega = null;

	try {
	    natega = getNatega(seatNumber);

	    if (natega != null) {
		// if (verbose)
		System.out.print(natega.getName() + " got " + natega.getTotal() + " (" + natega.getPercentage() + "%)");

		File nategaFile = new File(resultsFolder.getAbsolutePath() + "/" + seatNumber + ".json");
		JSONHelper.saveJSONObject(nategaFile, natega.toJSON());
		checked[seatNumber] = true;
		unMarkFailed(seatNumber);
	    } else {
		saveInvalid(seatNumber);
		checked[seatNumber] = true;
		unMarkFailed(seatNumber);
		if (verbose)
		    System.out.print("Not available");
	    }

	    if (verbose)
		System.out.println();

	} catch (IOException e) {
	    markFailed(seatNumber);
	}
    }

    private static void markFailed(int seatNumber) throws IOException {
	String newFileName = failedFolder.getAbsolutePath() + "/" + seatNumber;
	File newFile = new File(newFileName);
	if (!newFile.exists())
	    newFile.createNewFile();
    }

    private static void unMarkFailed(int seatNumber) {
	String newFileName = failedFolder.getAbsolutePath() + "/" + seatNumber;
	File newFile = new File(newFileName);
	if (newFile.exists())
	    newFile.delete();
    }

    public static void collectResults(int fromSeatNumber, int toSeatNumber, int step, boolean verbose)
	    throws Exception {

	for (int seatNumber = fromSeatNumber; seatNumber < toSeatNumber; seatNumber += step) {
	    if (checked[seatNumber])
		continue;
	    collectResult(seatNumber, verbose);
	}
    }

    private static void saveInvalid(int seatNumber) {
	try {
	    File file = new File(invalidFolder.getAbsolutePath() + "/" + seatNumber);
	    if (!file.exists())
		file.createNewFile();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private static void loadInvalids() {
	try {
	    for (File file : invalidFolder.listFiles()) {
		int x = Integer.valueOf(file.getName());
		if (skipInvalid) {
		    checked[x] = true;
		    unMarkFailed(x);
		}
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    private static void loadSavedFiles() {
	for (File f : resultsFolder.listFiles()) {
	    String filename = f.getName();
	    int dot = filename.indexOf(".json");
	    if (dot != -1) {
		int x = Integer.valueOf(filename.substring(0, dot));
		checked[x] = true;
		unMarkFailed(x);
	    }
	}
    }

    public static Natega getNatega(int seatNumber) throws IOException {
	txtSeatNumber.setValueAttribute(Integer.toString(seatNumber));
	HtmlPage resultPage = btnSubmit.click();
	return Natega.fromXML(resultPage.asXml());
    }

    public static int howManyChecked() {
	int c = 0;
	for (int i = 0; i < checked.length; i++) {
	    if (checked[i])
		c++;
	}
	return c;
    }

    static void retryFailed() {
	for (File file : failedFolder.listFiles()) {
	    int seatNumber = Integer.valueOf(file.getName());
	    try {
		collectResult(seatNumber, true);
	    } catch (Exception e) {
	    }
	}
    }

    public static ArrayList<Natega> getNategasAsList() {
	ArrayList<Natega> array = new ArrayList<>();
	for (File file : resultsFolder.listFiles()) {
	    array.add(new Natega(JSONHelper.loadJSONObject(file)));
	}
	return array;
    }

    public static ArrayList<Natega> getNategasAsList(int fromSN, int toSN) {
	ArrayList<Natega> array = new ArrayList<>();
	for (File file : resultsFolder.listFiles()) {
	    String fname = file.getName();
	    int cur = Integer.valueOf(fname.substring(0, fname.length() - 5));
	    if (cur < fromSN || cur >= toSN)
		continue;
	    array.add(new Natega(JSONHelper.loadJSONObject(file)));
	}
	return array;
    }
}
