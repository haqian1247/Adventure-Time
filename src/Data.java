import java.util.ArrayList;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

// --== CS400 File Header Information ==--
// Name: Zari Dehdashti
// Email: zdehdashti@wisc.edu
// Team: BE
// TA: Brianna Cochran
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

public class Data implements Comparable<Data> {
	private static File csv;
	protected int iD;
	protected String event;
	protected boolean isEnd;

	/**
	 * the constructor that initializes the csv file
	 */
	public Data(int iD, String event, boolean isEnd) {
		this.iD = iD;
		csv = new File("story.csv");
		this.event = event;
		this.isEnd = isEnd;
	}

	/**
	 * saves the inputs into the csv file
	 * 
	 * @param inputsInOrder the arraylist of user selections
	 * @throws IOException throws exceptions if filewriter does not work
	 */
	public static void save(ArrayList<String> inputsInOrder) throws IOException {
		boolean start = true;

		FileWriter filewrite = new FileWriter(csv);
		for (String str : inputsInOrder) {
			if (start == true) {
				filewrite.write(str);
				start = false;
			} else {
				filewrite.write(",");
				filewrite.write(str);
			}
		}
		filewrite.flush();
		filewrite.close();
	}

	/**
	 * loads the contents of the csv into an arraylist
	 * 
	 * @return an arraylist of the contents of the csv (all the user's story
	 *         choices)
	 * @throws Exception throws if filereader or bufferedreader do not work
	 */
	protected static ArrayList<String> load() throws Exception {
		ArrayList<String> list;
		FileReader fileRead = new FileReader(csv);
		BufferedReader buff = Files.newBufferedReader(Paths.get("story.csv"));
		list = buff.lines().map(line -> line.split(",")).flatMap(Arrays::stream)
				.collect(Collectors.toCollection(ArrayList::new));
		buff.close();
		fileRead.close();

		return list;
	}

	;

	/**
	 * converts the outputs of load into a string of the entire story line
	 * 
	 * @return a string containing the chosen story lines
	 */
	public String printStory() {
		String output = "";
		boolean start = true;
		ArrayList<String> list = null;
		try {
			list = load();
		} catch (Exception e) {
			System.out.println("Load has failed.");
			e.printStackTrace();
		}
		for (String str : list) {
			if (start == true) { // if this is the first line passed in
				output = output + str;
				start = false;
			} else {
				output = output + " " + str;
				// add this after the first story line has been passed in
			}
		}
		return output;
	}

	/**
	 * returns the string event
	 * 
	 * @return a string containing the chosen story lines
	 */
	@Override
	public String toString() {
		return this.event;
	}

	/**
	 * sets the event for the data object
	 * 
	 * @param event the string from that story snippet
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	/**
	 * sets the iD for the data object
	 * 
	 * @param iD the int that the object will use as an identifier
	 */
	public void setID(int iD) {
		this.iD = iD;
	}

	/**
	 * Returns the unique id as the hashcode
	 * 
	 * @return the id of the event
	 */
	public int hashCode() {
		return this.iD;
	}

	/**
	 * Compares two data objects using iD
	 * 
	 * @param other Data object
	 * @return negative if this.iD is less than other.iD, and Positive if this.iD is
	 *         more than other.iD, and 0 if they are equal
	 */
	@Override
	public int compareTo(Data other) {
		return this.iD - other.iD;
	}

	/**
	 * compares if two data objects are the same
	 */
	@Override
	public boolean equals(Object other) {
		return (this.iD - other.hashCode()) == 0;
	}
}
