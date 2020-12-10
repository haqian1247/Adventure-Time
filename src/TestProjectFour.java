import java.util.ArrayList;
import java.util.NoSuchElementException;
import org.junit.Test;
import static org.junit.Assert.*;

// Name: Aidan Pierre-Louis
// Email: apierrelouis@wisc.edu
// Team: BE
// Role: Test Engineer 1
// TA: Brianna Cochran
// Lecturer: Gary Dahl
// Notes to Grader:
//Necessary Files: Main.java, AdventureTime.java, Data.java, CS400Graph.java GraphADT.java
public class TestProjectFour {

	/**
	 * Tests the functionality of the Data class' methods
	 */
	@Test
	public void testData() {
		System.out.println("Testing Data class");
		// testSave
		{
			// create an arraylist of integer strings
			ArrayList<String> inputs = new ArrayList<String>();
			for (int i = 0; i < 5; i++) {
				inputs.add(Integer.toString(i));
			}
			// tries to save arraylist to a new or existing 'story.csv' file
			Data data = new Data(0, "test", false);
			try {
				data.save(inputs);
			} catch (Exception e) {
				fail("Failed save.");
				e.printStackTrace();
			}
			System.out.println("\ttestSave completed.");
		}
		// testLoad
		{
			// creates an empty arraylist to be filled with data from csv file from first
			// test
			Data data = new Data(0, "test", false);
			ArrayList<String> list;
			// tries loading the data from the csv file into the arraylist
			try {
				list = data.load();
				// initializes strings with the expected and actual values from testSave
				String expected = new String("0,01,12,23,34");
				String actual = new String("");
				for (int i = 0; i < list.size(); i++) {
					if (i != list.size() - 1)
						actual += list.get(i) + ",";
					actual += list.get(i);
				}
				// compares the values at each index to the expected
				assertEquals("Failed load. Data mismatch.", expected, actual);
			} catch (Exception e) {
				fail("Failed load. Load error.");
				e.printStackTrace();
			}
			System.out.println("\ttestLoad completed.");
		}
		// testPrintStory
		{
			// instantiates a new data object to call printStory method
			Data data = new Data(0, "test", false);
			String actual = data.printStory();
			String expected = new String("0 1 2 3 4");
			// compares the string returned from the printStory method
			// to the expected data values from the csv from testSave and testLoad
			assertEquals("Failed printStory test.", expected, actual);
			System.out.println("\ttestPrintStory completed.");
		}
	}

	/**
	 * Tests the functionality of the AdventureTime class' methods
	 * 
	 */
	@Test
	public void testAdventureTime() {
		System.out.println("Testing AdventureTime class");
		AdventureTime adventure = new AdventureTime();
		// testGetNextPossibleEvents
		{
			// create an arraylist of string values the method should
			// return for the current event of the adventure object (current event =
			// gardenStart)
			ArrayList<String> expected = new ArrayList<String>();
			expected.add("Look around");
			expected.add("Go back to sleep.");
			ArrayList<String> actual = adventure.getNextPossibleEvents();
			for (int i = 0; i < expected.size(); i++) {
				assertEquals("Failed printStory test. ArrayList values mismatch.", expected.get(i), actual.get(i));
			}
			System.out.println("\ttestGetNextPossibleEvents completed.");
		}
		// testNextEvent int & Data
		{
			adventure = new AdventureTime();
			// relies on getCurrentEvent's functionality
			// case 1: there is a next event at int 1
			try {
				adventure.nextEvent(1);
				assertEquals("Failed testNextEvent(int). Data mismatch.", adventure.getCurrentEvent().toString(),
						"Look around");
			} catch (NoSuchElementException e) {
				fail("Failed testNextEvent(int). Event exists but exception was still thrown.");
			}
			// case 2: there is no next event at 3
			try {
				adventure.nextEvent(3);
				fail("Failed testNextEvent(int). Exception was not thrown.");
			} catch (NoSuchElementException e) {
				assertEquals("Failed testNextEvent(int). Data mismatch.", e.getMessage(), "Invalid button pressed.");
			}
			// case 3: ending, there is no next event
			adventure = new AdventureTime();
			adventure.nextEvent(2);
			adventure.nextEvent(1);
			// checks to make sure the proper exception is thrown if event is an ending
			try {
				adventure.nextEvent(1);
				fail("Failed printStory test. No exception thrown for ending.");
			} catch (Exception e) {
				assertEquals("Failed printStory test. Wrong exception thrown for ending.",
						"The current event is an ending.", e.getMessage());
			}
			// case 4: next event of type Data exists
			adventure = new AdventureTime();
			Data expected = new Data(1, "Look around", false);
			// checks to see if nextEvent works without throwing an exception and if it
			// updates currentEvent
			try {
				adventure.nextEvent(expected);
				assertEquals("Failed testNextEvent(Data). Data mismatch.", expected.toString(),
						adventure.getCurrentEvent().toString());
			} catch (Exception e) {
				fail("Failed testNextEvent(Data). Exception was thrown.");
			}
			System.out.println("\ttestNextEvent completed.");
		}
		// testGetCurrentEvent
		{
			// checks to see if current event is gardenStart
			adventure = new AdventureTime();
			Data expected = new Data(0, "You are tending to your garden just outside your home. "
					+ "The weather is perfect, sunny, and warm. While you are filling your watering can, a frantic man approaches the fenceline. "
					+ "He says that his son has fallen into a well and he needs help getting him out.", false);
			;
			assertEquals("Failed testGetCurrentEvent. Data mismatch.", adventure.getCurrentEvent(), expected);
			System.out.println("\ttestGetCurrentEvent completed.");
		}
		// testIsStoryOver
		{
			// case 1: story is not over
			assertEquals("Failed testIsStoryOver. Story should not be over but returned true.", adventure.isStoryOver(),
					false);
			// case 2: story is not over
			adventure = new AdventureTime();
			adventure.nextEvent(2);
			adventure.nextEvent(1);
			assertEquals("Failed testIsStoryOver. Story should be over but returned false.", adventure.isStoryOver(),
					true);
			System.out.println("\ttestIsStoryOver completed.");
		}
		// testGetAllEndEvents
		{
			// initializes a new AdventureTime object to find all the end events for and
			// creates an arraylist of expected end events
			adventure = new AdventureTime();
			ArrayList<Data> expected = new ArrayList<Data>();
			expected.add(new Data(35,
					"You step back in hesitation.\nWho knows where this hole leads and whether or not the fall would "
							+ "be large enough to kill you. Is it really worth the risk?\nYour thoughts are interrupted as you feel someone breathing behind you."
							+ "\nYou freeze.\nHow did they even get in this room?\nYou feel a prick in your neck and lose consciouness.",
					true));
			expected.add(new Data(34,
					"Taking a deep breath, you back up and jump....\n ..... \n ...... \n You wake up in a dark room.",
					true));
			expected.add(new Data(30,
					"You pick up the scarf and wrap it around you neck.\nYou continue your trek forward.\nAs you walk, you notice something odd. Is the hallway getting smaller?"
							+ "\nThe walls are closing together. Suddenly, you hear footsteps coming towards you from behind.\nYou try to make out what is coming, but is unable to"
							+ " from the darkness.\nPanicked, you run down the narrow hallway, the ceilng also closing in.\nYou get down to your knees and crawl through the tight space, the footsteps appraoching."
							+ "\nYou feel your head jerk backwards as whoever is behind you grabs the scarf wrapped around your neck.\nYou're dragged backwards, before you feel a prick in your"
							+ " neck and lose consciousness. ",
					true));
			expected.add(new Data(26,
					"You slowly open the second door. The room is pitch black.\nHesitently, you step inside"
							+ " the room to get a better look.\nThe door slams behind you and locks.\nNothing happens for a minute, before the room is filled"
							+ " with a light.\nStartled, you riase your arms over your eyes to block out the blinding light.\nYou wake up in a white room."
							+ "\nYou're in a hospital.",
					true));
			expected.add(new Data(25,
					"You slowly open the first door.\nThe room is pitch black. Hesitently, you step inside the room "
							+ "to get a better look.\nThe door slams behind you and locks.\nYou desperately search the room to find something to help you "
							+ "escape, but find nothing.\nYou spend hours scavenging the room for something.\nYou find nothing.\nThe exhaustion "
							+ "finally catches up to you.\nYou collapse to the floor and close your eyes.\nYou wake up in a dark room.",
					true));
			expected.add(new Data(21,
					"You pick up the scalpel and hold it tightly before leaving the room.\nYou continue your trek forward. "
							+ "You don't know how much time passes, but you find nothing else in the hallway."
							+ "\nSuddenly, you hear footsteps rapidly approaching from behind.\nYou quickly turn around and point your scalpel towards whoever is coming."
							+ "\nIt is a man in a white coat, his face obscured by the darkness.\nThe wind is knocked out of you and they tackle you to the ground. "
							+ "\nYou raise your scalpel and stab the figure in the arm. They don't even flinch.\nThe figure raises their arm, in their hand a syringe.\nYou feel "
							+ "the needle prick and lose consciousness. ",
					true));
			expected.add(new Data(14,
					"You decide that it is best for you to continue down the hallway, as you are unsure if you're willing to take the risk"
							+ " of finding out what's on the other side of the door.\nYou continue your trek forward.\nSuddenly, you hear footsteps rapidly appraoching from "
							+ "behind you.\nYou feel a prick in your neck and lose consciousness.",
					true));
			expected.add(new Data(4,
					"You decide perhaps it is best to stay in the dark room.\nYou walk back to the middle of "
							+ "the room and lay down against the cold floor, before closing your eyes and falling back asleep.\nYou don't wake up.",
					true));
			// loads the actual arraylist with all the end events from the method and
			// compares them to make sure they are all there
			ArrayList<Data> actual = adventure.getAllEndEvents();
			for (int i = 0; i < expected.size(); i++) {
				assertEquals("Failed testGetAllEndEvents. Event returned did not match event expected.",
						expected.get(i), actual.get(i));
			}
			System.out.println("\ttestGetAllEndEvents completed.");
		}
		// testGetClosestEndDistance
		{
			adventure = new AdventureTime();
			adventure.nextEvent(1);
			adventure.nextEvent(1);
			// the nearest end is 2 vertices from the beginning 
			// checks to see if the method returns the correct value for this vertex
			int expected = 2;
			assertEquals("Failed testGetClosestEndDistance. ", expected, adventure.getClosestEndDistance());
			// nearest end should be 0 edgeweight
			adventure = new AdventureTime();
			adventure.nextEvent(2);
			adventure.nextEvent(1);
			expected = 0;
			assertEquals("Failed testGetClosestEndDistance. ", expected, adventure.getClosestEndDistance());
			System.out.println("\ttestGetClosestEndDistance completed.");
		}
	}

	/**
	 * Don't worry about all this it would not compile because of weird permissions
	 * with Main extending Application
	 */
	// @Test
	/*
	 * public void testMain() { System.out.println("Testing Main class"); Main main
	 * = new Main(); // testClear { main.clear(); // mutates main's fields for
	 * testing main.currentIteration = 1; main.nearestEnd = 1; main.currentText =
	 * "hi"; main.currentStory = "hello"; main.isDone = true; main.clear(); //
	 * checks to see if currentIteration is returned to 0 having been changed
	 * assertEquals("Failed testClear. currentIteration did not reset.",
	 * main.currentIteration, 0); // checks to see if nearestEnd is returned to 0
	 * having been changed
	 * assertEquals("Failed testClear. nearestEnd did not reset.", main.nearestEnd,
	 * 0); // checks to see if currentText is returned to "..." having been changed
	 * assertEquals("Failed testClear. currentText did not reset.",
	 * main.currentText,
	 * "Please click the A, B, or C button to start a new story or click Load Save"
	 * + " to load your most recently saved story. At any point, you may " +
	 * "click Save to save your current progress.\n"); // checks to see if
	 * currentStory is returned to "" having been changed
	 * assertEquals("Failed testClear. currentStory did not reset.",
	 * main.currentStory, ""); // checks to see if isDone is returned to false
	 * having been changed assertEquals("Failed testClear. isDone did not reset.",
	 * main.isDone, false); System.out.println("\ttestClear completed"); } //
	 * testAdvanceStory (relies on functionality of printOutcome and promptStory) {
	 * main.clear(); // advance the story from gardenStart to suggestHelpElsewhere
	 * main.advanceStory(0); main.advanceStory(1); // checks to see if the
	 * currentEvent has been changed to the expected event Data expected = new
	 * Data(3,
	 * "You continue to water your garden and try to forget about the desparate man."
	 * , true); Data actual = main.proj.getCurrentEvent();
	 * assertEquals("Failed testAdvanceStory. Story did not advance.", expected,
	 * actual); // tries to advance the story when it has already ended
	 * main.advanceStory(1); actual = main.proj.getCurrentEvent();
	 * assertEquals("Failed testAdvanceStory. Story advanced?", expected, actual);
	 * System.out.println("\ttestAdvanceStory completed"); } // testRandomStoryGen {
	 * main.randomStoryGen(); // tests to see if the story ended
	 * assertEquals("Failed testRandomStoryGen. Story has not ended.", true,
	 * main.isDone); // checks to see if the story does not have the starting text
	 * assertTrue(!main.currentText.
	 * equals("You are tending to your garden just outside your home. " +
	 * "The weather is perfect, sunny, and warm. While you are filling your watering can, a frantic man approaches the fenceline. "
	 * +
	 * "He says that his son has fallen into a well and he needs help getting him out."
	 * )); System.out.println("\ttestRandomStoryGen completed"); } // testSaveStory
	 * { // moves the story forward to a point where it can be saved main.clear();
	 * main.advanceStory(0); main.advanceStory(2); main.advanceStory(2); // saves
	 * story and then checks to see if the data saved is correct main.saveStory();
	 * try { ArrayList<String> list = new Data(0, "test", false).load(); //
	 * initializes strings with the expected and actual values from testSave String
	 * expected = "0,02,24,46,67"; String actual = ""; for (int i = 0; i <
	 * list.size(); i++) { if (i != list.size() - 1) actual += list.get(i) + ",";
	 * actual += list.get(i); } // compares the values at each index to the expected
	 * assertEquals("Failed saveStory. Data mismatch.", expected, actual); } catch
	 * (Exception e) { fail("Failed saveStory. Load error."); e.printStackTrace(); }
	 * System.out.println("\ttestSaveStory completed"); } // testLoadStory { //
	 * loads the story from testSaveStory main.loadStory(); // checks to make sure
	 * the correct text is in the current story assertEquals("",
	 * "Start of Story: \n" +
	 * "You are tending to your garden just outside your home. The weather is perfect, sunny, and warm. While you are filling your watering can, a frantic man approaches the fenceline. He says that his son has fallen into a well and he needs help getting him out.\n"
	 * + "Agree to help the man\n" +
	 * "You grab your old ladder from the shed and follow the man to the well. Upon arrival you find the young boy at the bottom and begin to lower the ladder down. The child is too scared to move...\n"
	 * + "Volunteer to go down and rescue the child yourself\n" +
	 * "You descend the ladder and pick up the boy in one arm. While you are climbing back up, the ladder begins to buckle. You are nearing the top and the man reaches out a hand to help pull you up. Just as your hands lock together the ladder gives out, causing the man, child, and yourself to plummet. \n"
	 * + "\n" +
	 * "You awaken and feel as though you are suffering a mild concussion. It appears the man broke the fall for the small boy and as a result the man is unconscious; the boy is awake and staring at you with a blank face, seeming to be in shock. After gathering your senses, you realize that you are in a relatively wide corridor, larger than the width of the well. There are also lit torches lining the walls, giving sufficient light. This corridor has two paths, left or right, and you decide to take one as you see no other options.\n"
	 * + "\n" + "Load Successful! Please continue your story.\n\n" +
	 * "Please choose a next step in the story.\n" +
	 * "A) Choose to take the right path\n" + "B) Choose to take the left path\n\n",
	 * main.currentText); System.out.println("\ttestLoadStory completed"); } }
	 */
	/**
	 * tests developed by Josh to see if the load and save methods are functional to
	 * be honest I don't really understand this code block's purpose but I'm
	 * deleting it just in case
	 * 
	 * @author Josh Parker
	 */
	public static void testSaveAndLoad() {
		// Test for the save and load methods
		ArrayList<String> test = new ArrayList<>();
		for (int i = 0; i < 70; i += 10) {
			test.add(Integer.toString(i));
		}
		try {
			Data.save(test);
		} catch (Exception e) {
			System.out.println("Failed Save");
			e.printStackTrace();
		}
		System.out.println("Save completed");
		ArrayList<String> testLoad = new ArrayList<>();
		testLoad.add("Bad");
		try {
			testLoad = Data.load();
		} catch (Exception f) {
			System.out.println("Load Failed");
			f.printStackTrace();
		}
		System.out.println("Supposedly worked");
		System.out.println(testLoad);
	}
}
