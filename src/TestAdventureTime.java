import java.util.ArrayList;
import java.util.List;

public class TestAdventureTime extends CS400Graph {

    public static void main(String[] args) {
        System.out.println(test());
//        AdventureTime adventure = new AdventureTime();
//        System.out.println(adventure.currentEvent.event);
//        ArrayList<String> test = new ArrayList<String>();
//        test = adventure.getNextPossibleEvents();
//        for (int i = 0; i < test.size(); i++) {
//            System.out.println(test.get(i));
//        }
    }
    public static boolean test() {
        AdventureTime adventure = new AdventureTime();
        Vertex currentVertex = adventure.vertices.get( new Data(1, "Look around", false));
        List<Edge> edges = currentVertex.edgesLeaving;
        for (int i = 0; i < edges.size(); i++) {
            System.out.println(edges.get(i).target.data);
        }
        return true;
    }

}
