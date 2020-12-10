import java.util.ArrayList;
import java.util.LinkedList;
import java.util.NoSuchElementException;

// Name: Holly Qian
// Email: haqian@wisc.edu
// Team: BE
// Role: Backend 2
// TA: Brianna Cochran
// Lecturer: Gary Dahl
// Notes to Grader:
// Necessary Files: GraphADT.java, CS400Graph.java
public class AdventureTime extends CS400Graph<Data> {

    // field containing all the Data object events that are part of this story (Changed from Path
    // object)
    private ArrayList<Data> eventsInStory;
    public Data currentEvent;


    public AdventureTime() {
        createStory();
        currentEvent = new Data(0, "You wake up in a dark room.", false);
        eventsInStory = new ArrayList<>();
        eventsInStory.add(currentEvent);

    }

    /**
     * 
     * Obtains all of the next possible events from the currentEvent
     * 
     * @return an ArrayList of Strings containing all the next possible events in the story
     */
    public ArrayList<String> getNextPossibleEvents() {
        ArrayList<String> events = new ArrayList<String>();
        Vertex currentVertex = vertices.get(currentEvent);
        LinkedList<Edge> edges = currentVertex.edgesLeaving;
        for (int i = 0; i < edges.size(); i++) {
            events.add(edges.get(i).target.data.event);
        }
        return events;
    }

    /**
     * 
     * Constructs an ArrayList of all nodes that were traversed throughout the specific 
     * path that the user picked 
     * 
     * @return an ArrayList of Data nodes that were visited throughout the story 
     */
    public ArrayList<Data> getEventsInStory() {
        return eventsInStory;
    }

    /**
     * 
     * Based on the button pressed (1, 2, or 3), advances to the next event in the story
     * and updates currentEvent and eventsInStory to the current node
     * 
     * 
     * @param buttonPressed int value which user clicks 
     * @throws NoSuchElementException when a button is pressed with no event
     * associated with it 
     */
    public void nextEvent(int buttonPressed) throws NoSuchElementException {
        if (currentEvent.isEnd) {
            throw new NoSuchElementException("The current event is an ending.");
        }
        --buttonPressed;
        Vertex currentVertex = vertices.get(currentEvent);
        LinkedList<Edge> edges = currentVertex.edgesLeaving;
        try {
            if (edges.get(buttonPressed).target.data != null) {
                currentEvent = edges.get(buttonPressed).target.data;
                eventsInStory.add(currentEvent);
            }
        } catch (IndexOutOfBoundsException e) {
            throw new NoSuchElementException("Invalid button pressed.");
        }

    }

    /**
     *  Advances to the next event in the story given a specific event, and updates
     *  currentEvent and eventsInStory to the current node 
     * 
     * 
     * @param event next event expected in the story 
     * @throws NoSuchElementException if there is no edge connected between the
     * event and the currentEvent 
     */
    public void nextEvent(Data event) throws NoSuchElementException {
        boolean found = false;
        Vertex currentVertex = vertices.get(currentEvent);
        LinkedList<Edge> edges = currentVertex.edgesLeaving;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).target.data.equals(event)) {
                currentEvent = edges.get(i).target.data;
                eventsInStory.add(currentEvent);
                found = true;
            }
        }
        if (!found) {
            throw new NoSuchElementException("Invalid event.");
        }
    }

    // return the current event that the story is on
    public Data getCurrentEvent() {
        return currentEvent;
    }

    /**
     * If current event is an End event (leaf), returns true
     *
     * @return true if end event
     */
    public boolean isStoryOver() {
        return getCurrentEvent().isEnd;
    }

    // return an arraylist of all the possible Ending events (aka: anything with isEnd = true)
    public ArrayList<Data> getAllEndEvents() {
        ArrayList<Data> output = new ArrayList<>();
        for (Data d : vertices.keySet()) {
            if (d.isEnd)
                output.add(d);
        }
        return output;
    }

    // returns the smallest int value from the current node to the nearest end event
    public int getClosestEndDistance() {
        // if no start node is set yet, return -1
        if (getCurrentEvent() == null) {
            return -1;
        }

        int shortestEnd = Integer.MAX_VALUE;

        // for each possible end node, use djikstra's to find path, saving the shortest one found.
        for (Data end : getAllEndEvents()) {
            // skips nodes that can't be reached
            try {
                shortestEnd = Integer.min(getPathCost(getCurrentEvent(), end), shortestEnd);
            } catch (Exception e) {
            }
        }

        // if it messes up, make sure to return -1
        if (shortestEnd == Integer.MAX_VALUE) {
            return -1;
        } else {
            return shortestEnd;
        }
    }

    /**
     * 
     * Private helper method which creates the story that will be loaded into the game
     * 
     */
    private void createStory() {
        // START OF STORY
        Data darkRoomStart = new Data(0, "You wake up in a dark room.", false);
        insertVertex(darkRoomStart);
        Data lookAround = new Data(1, "Look around", false);
        insertVertex(lookAround);
        insertEdge(darkRoomStart, lookAround, 1);
        Data nextEvent = new Data(2,
            "You stand up and scan the room.\nDespite the darkness, you're able to make out "
                + "the cement walls surrounding you and a metal door; a small source of light coming from it.",
            false);
        insertVertex(nextEvent);
        insertEdge(lookAround, nextEvent, 1);
        Data secondOption = new Data(3, "Go back to sleep.", false);
        insertVertex(secondOption);
        insertEdge(darkRoomStart, secondOption, 1);
        // FIRST ENDING
        Data firstEnd = new Data(4,
            "You decide perhaps it is best to stay in the dark room.\nYou walk back to the middle of "
                + "the room and lay down against the cold floor, before closing your eyes and falling back asleep.\nYou don't wake up.",
            true);
        insertVertex(firstEnd);
        insertEdge(secondOption, firstEnd, 1);
        Data openDoor = new Data(5, "Open the door", false);
        insertVertex(openDoor);
        insertEdge(nextEvent, openDoor, 1);
        Data stayRoom = new Data(6, "Stay in the room", false);
        insertVertex(stayRoom);
        insertEdge(nextEvent, stayRoom, 1);
        insertEdge(stayRoom, firstEnd, 1);
        Data doorCont = new Data(7,
            "You walk up to the door and slowly open it before peeking to see what is on the other side.\n"
                + "There is a lightblub hanging eerily above you outside the door.\nExplains where the light was coming from.\n "
                + "You step outside the room and notice two separate hallways.\nYou squint to try to make out anything in the darkness"
                + " of the two hallways, but find nothing.",
            false);
        insertVertex(doorCont);
        insertEdge(openDoor, doorCont, 1);
        Data leftHallway = new Data(8, "Left hallway", false);
        Data rightHallway = new Data(9, "Right hallway", false);
        Data backRoom = new Data(10, "Go back to the room", false);
        insertVertex(leftHallway);
        insertVertex(rightHallway);
        insertVertex(backRoom);
        insertEdge(doorCont, leftHallway, 1);
        insertEdge(doorCont, rightHallway, 1);
        insertEdge(doorCont, backRoom, 1);
        insertEdge(backRoom, firstEnd, 1);
        // LEFT HALLWAY
        Data leftHallwayCont = new Data(11,
            "You decide that the left hallway looks more appealing to you, and begin to walk through the long corridor."
                + "\nYour left hand stays on the wall so you don't lose your way in the darkness.\nYou eventually come across as small light ahead.\nIntrigued, you"
                + " continue forward until you found the source: a room.",
            false);
        insertVertex(leftHallwayCont);
        insertEdge(leftHallway, leftHallwayCont, 1);
        Data leftDoor = new Data(12, "Go inside the room", false);
        Data keepGoing = new Data(13, "Continue down the hallway", false);
        Data endingTwo = new Data(14,
            "You decide that it is best for you to continue down the hallway, as you are unsure if you're willing to take the risk"
                + " of finding out what's on the other side of the door.\nYou continue your trek forward.\nSuddenly, you hear footsteps rapidly appraoching from "
                + "behind you.\nYou feel a prick in your neck and lose consciousness.",
            true);
        insertVertex(leftDoor);
        insertVertex(keepGoing);
        insertVertex(endingTwo);
        insertEdge(keepGoing, endingTwo, 1);
        insertEdge(leftHallwayCont, keepGoing, 1);
        insertEdge(leftHallwayCont, leftDoor, 1);
        // RIGHT HALLWAY
        Data rightHallway_2 = new Data(15,
            "You decide that the right hallway looks more appealing to you, and begin to walk through the long corridor."
                + "\nYour right hand stays on the wall so you don't lose your way through the darkness.\nEventually, your hand comes across a bump in the wall."
                + "\nYou lean forward so your face is only inches from the wall, trying to make out what you find.\nYour eyes make out a door. There is no light coming from inside.",
            false);
        insertVertex(rightHallway_2);
        insertEdge(rightHallway, rightHallway_2, 1);
        Data rightDoor = new Data(16, "Go inside the room", false);
        Data keepGoingRight = new Data(17, "Continue down the hallway", false);
        insertVertex(rightDoor);
        insertVertex(keepGoingRight);
        insertEdge(rightHallway_2, rightDoor, 1);
        insertEdge(rightHallway_2, keepGoingRight, 1);
        insertEdge(keepGoingRight, endingTwo, 1);
        // INSIDE ROOM LEFT
        Data goInsideLeft = new Data(18,
            "You deicde to see what's on the other side of the door.\nYour hand reaches for the knob before slowly turning it, only to be"
                + " blinded by the light coming from the other side.\nAfter a few moments, your eyes adjust and you are able to peer inside the labratory-like room."
                + " \nTables litter the floor and bright lights hang from the ceiling. You wonder what this room is for.\nYou decide to scavenge the room for anything that could be of"
                + " use to you, perhaps a weapon?\nAfter digging through every drawer and checking every corner, you eventually find two thing that you could make use of: "
                + "a syringe and a scalpel.\nYou think it is best if you only took one.",
            false);
        insertVertex(goInsideLeft);
        insertEdge(leftDoor, goInsideLeft, 1);
        Data scalpel = new Data(19, "Scalpel", false);
        Data syringe = new Data(20, "Syringe", false);
        insertVertex(scalpel);
        insertVertex(syringe);
        insertEdge(goInsideLeft, syringe, 1);
        insertEdge(goInsideLeft, scalpel, 1);
        Data scalpelCont = new Data(21,
            "You pick up the scalpel and hold it tightly before leaving the room.\nYou continue your trek forward. "
                + "You don't know how much time passes, but you find nothing else in the hallway."
                + "\nSuddenly, you hear footsteps rapidly approaching from behind.\nYou quickly turn around and point your scalpel towards whoever is coming."
                + "\nIt is a man in a white coat, his face obscured by the darkness.\nThe wind is knocked out of you and they tackle you to the ground. "
                + "\nYou raise your scalpel and stab the figure in the arm. They don't even flinch.\nThe figure raises their arm, in their hand a syringe.\nYou feel "
                + "the needle prick and lose consciousness. ",
            true);
        insertVertex(scalpelCont);
        insertEdge(scalpel, scalpelCont, 1);
        Data syringeCont = new Data(22,
            "You pick up the syringe and hold it tightly before leaving the room."
                + "\nYou continue your trek forward.\nYou don't know how much time passes, but you find "
                + "nothing else in the hallway.\nSuddenly, you hear footsteps rapidly approaching from behind.\nYou quickly turn around, the "
                + "syringe behind your back. There is a man in a white coat, his face obscured by the darkness.\nThe wind is knocked out of you"
                + " as he tackles you to the ground.\nQuickly, you lift up the syringe and stab the figure in the arm.\nThe figure stops moving."
                + "\nYou shove them off of you, their body falling limp to the floor.\nRelieved you brought the syringe, you stand back up and continue"
                + " down the corridor.\nYou finally reach the end of the hallway, where two doors stand in front of you.",
            false);
        insertVertex(syringeCont);
        insertEdge(syringe, syringeCont, 1);
        // ENDING OF LEFT HALLWAY
        Data firstDoor = new Data(23, "First Door", false);
        Data secondDoor = new Data(24, "Second door", false);
        insertVertex(firstDoor);
        insertVertex(secondDoor);
        insertEdge(syringeCont, firstDoor, 1);
        insertEdge(syringeCont, secondDoor, 1);
        Data firstDoorCont = new Data(25,
            "You slowly open the first door.\nThe room is pitch black. Hesitently, you step inside the room "
                + "to get a better look.\nThe door slams behind you and locks.\nYou desperately search the room to find something to help you "
                + "escape, but find nothing.\nYou spend hours scavenging the room for something.\nYou find nothing.\nThe exhaustion "
                + "finally catches up to you.\nYou collapse to the floor and close your eyes.\nYou wake up in a dark room.",
            true);
        insertVertex(firstDoorCont);
        insertEdge(firstDoor, firstDoorCont, 1);
        Data secondDoorCont = new Data(26,
            "You slowly open the second door. The room is pitch black.\nHesitently, you step inside"
                + " the room to get a better look.\nThe door slams behind you and locks.\nNothing happens for a minute, before the room is filled"
                + " with a light.\nStartled, you riase your arms over your eyes to block out the blinding light.\nYou wake up in a white room."
                + "\nYou're in a hospital.",
            true);
        insertVertex(secondDoorCont);
        insertEdge(secondDoor, secondDoorCont, 1);
        // INSIDE RIGHT ROOM
        Data goInsideRight = new Data(27,
            "You deicde to see what's on the other side of the door.\nYour hand reaches for the knob before slowly turning it and peering inside."
                + "\nSince there is no light source, it's hard to make out what's in the room.\nYou slowly creep inside and feel around.\nYou realize that the room"
                + " is very small and there is only enough room for you to stand inside it.\nYour hands feel around more until you come across something hard "
                + "and somethhing soft.\nYou pick up the two items and bring them closer to your face: one is a scarf and one is a ring.\nAh, this room is a closet."
                + "\nYou think it's best for you to take one of the items.",
            false);
        insertVertex(goInsideRight);
        insertEdge(rightDoor, goInsideRight, 1);
        Data scarf = new Data(28, "Scarf", false);
        Data ring = new Data(29, "Ring", false);
        insertVertex(scarf);
        insertVertex(ring);
        insertEdge(goInsideRight, scarf, 1);
        insertEdge(goInsideRight, ring, 1);
        Data scarfCont = new Data(30,
            "You pick up the scarf and wrap it around you neck.\nYou continue your trek forward.\nAs you walk, you notice something odd. Is the hallway getting smaller?"
                + "\nThe walls are closing together. Suddenly, you hear footsteps coming towards you from behind.\nYou try to make out what is coming, but is unable to"
                + " from the darkness.\nPanicked, you run down the narrow hallway, the ceilng also closing in.\nYou get down to your knees and crawl through the tight space, the footsteps appraoching."
                + "\nYou feel your head jerk backwards as whoever is behind you grabs the scarf wrapped around your neck.\nYou're dragged backwards, before you feel a prick in your"
                + " neck and lose consciousness. ",
            true);
        insertVertex(scarfCont);
        insertEdge(scarf, scarfCont, 1);
        // ENDING OF RIGHT PATH
        Data ringCont = new Data(31,
            "You pick up the ring and put it on your finger. It fits perfectly.\nYou continue your trek froward. As you walk, you notice"
                + " something odd.\nIs the hallway getting smaller? The walls are closing together around you.\nSuddenly, you hear footsteps coming from behind."
                + "You try to make out what's coming, but is unable to. Panicked, you run down the narrow hallway, the ceiling also closing in. "
                + "\nYou get down to your knees and crawl through the tight space, the footsteps approaching.\nYou come across a small hole-like exit.\nDesperately, "
                + "you force your way through the hole, leading you to another room on the other side.\nYou pause to listen for the footsteps, but hear only silence."
                + "\nYou look around the room and notice a door. You approach the door with caution before trying to open it, to no avail.\nYou notice a ring shaped hole"
                + " in the place of a lock.\nYou remove the ring and place it within the hole.\nIt fits perfectly. \nA loud 'click' echose throughout the room as the door"
                + " unlocks itself. The door cracks open.\nA cold burst of air speeds past you. You look down and see that the door leads to a large hole.\nYou "
                + "cannot see the bottom.",
            false);
        insertVertex(ringCont);
        insertEdge(ring, ringCont, 1);
        Data jump = new Data(32, "Jump", false);
        Data turnAway = new Data(33, "Turn away", false);
        insertVertex(jump);
        insertVertex(turnAway);
        insertEdge(ringCont, jump, 1);
        insertEdge(ringCont, turnAway, 1);
        Data jumpCont = new Data(34,
            "Taking a deep breath, you back up and jump....\n ..... \n ...... \n You wake up in a dark room.",
            true);
        insertVertex(jumpCont);
        insertEdge(jump, jumpCont, 1);
        Data turnAwayCont = new Data(35,
            "You step back in hesitation.\nWho knows where this hole leads and whether or not the fall would "
                + "be large enough to kill you. Is it really worth the risk?\nYour thoughts are interrupted as you feel someone breathing behind you."
                + "\nYou freeze.\nHow did they even get in this room?\nYou feel a prick in your neck and lose consciouness.",
            true);
        insertVertex(turnAwayCont);
        insertEdge(turnAway, turnAwayCont, 1);
    }

}
