import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

public class Graph {
    private HashMap<Integer, Arc> arcs;
    private HashMap<Integer, Node> nodes;
    private HashSet<Integer> transports, greenTrain, redTrain, greenReindeer, redReindeer;
    private int nextNodeID, bagStartID;
    private Node source, terminal;
    private ArrayList<HashMap<Integer, Arc>> toList;
    private PriorityQueue<Node> greatestHeight;
    private Queue<Node> activeNodes;

    public Graph() {
        arcs = new HashMap<Integer, Arc>();
        nodes = new HashMap<Integer, Node>();
        greenTrain = new HashSet<Integer>();
        redTrain = new HashSet<Integer>();
        greenReindeer = new HashSet<Integer>();
        redReindeer = new HashSet<Integer>();
        transports = new HashSet<Integer>();
        toList = new ArrayList<HashMap<Integer, Arc>>();
        greatestHeight = new PriorityQueue<Node>();

        source = new Node(0);
        toList.add(new HashMap<Integer, Arc>());
        terminal = new Node(1);
        toList.add(new HashMap<Integer, Arc>());
        nextNodeID = 2;
    }

    public int getMaxFlow() {
        //initialize preflow
        for (Arc arc : toList.get(0).values()) {
            arc.adjustFlow(arc.getCapacity());
            activeNodes.add(nodes.get(arc.getTo()));
        }
        source.setHeight(nodes.size());

        

        return 0;
    }


    // //Start of push relabel;
    // private void push(Arc arc) {
    //     Node fromNode = nodes.get(arc.getFrom());
    //     Node toNode = nodes.get(arc.getTo());
    //     int pushed = min(fromNode.getExcessFlow(), arc.getDeficit());
        
    //     toNode.increaseExcessFlow(pushed);
    //     fromNode.decreaseExcessFlow(pushed);
    //     arc.adjustFlow(pushed);        
    // }

    // //Might have an error!
    // private void relabel(Node node) { 
    //     HashMap<Integer, Arc> adjMap = toList.get(node.getID());
    //     Integer minAdjacentHeight = Integer.MAX_VALUE;
        
    //     for (Arc adjArc : adjMap.values()) {
    //         if (adjArc.getDeficit() > 0) {
    //             minAdjacentHeight = min(minAdjacentHeight, nodes.get(adjArc.getTo()).getHeight());
    //         }
    //     }

    //     if (minAdjacentHeight < Integer.MAX_VALUE) {
    //         node.setHeight(minAdjacentHeight + 1);
    //     }
    // }


    // private void getMaxFlow() { //push-relabel with Trees
    //     source.setHeight(nodes.size()); //set source node height to number of nodes
        
        

    // }

    public void addGreenTrain(int capacity) {this.addTransport(0, capacity);}
    public void addRedTrain(int capacity) {this.addTransport(1, capacity);}
    public void addGreenReindeer(int capacity) {this.addTransport(2, capacity);}
    public void addRedReindeer(int capacity) {this.addTransport(3, capacity);}

    private void addTransport(int type, int capacity) {
        if (type == 0) {
            Node transportNode = new Node(nextNodeID);
            transports.add(nextNodeID);
            greenTrain.add(nextNodeID);
            nodes.put(nextNodeID, transportNode);
            toList.add(new HashMap<Integer, Arc>());
            addMatching(transportNode, terminal, capacity);

        } else if (type == 1) {
            Node transportNode = new Node(nextNodeID);
            transports.add(nextNodeID);
            redTrain.add(nextNodeID);
            nodes.put(nextNodeID, transportNode);
            toList.add(new HashMap<Integer, Arc>());
            addMatching(transportNode, terminal, capacity);

        } else if (type == 2) {
            Node transportNode = new Node(nextNodeID);
            transports.add(nextNodeID);
            greenReindeer.add(nextNodeID);
            nodes.put(nextNodeID, transportNode);
            toList.add(new HashMap<Integer, Arc>());
            addMatching(transportNode, terminal, capacity);

        } else if (type == 3) {
            Node transportNode = new Node(nextNodeID);
            transports.add(nextNodeID);
            redReindeer.add(nextNodeID);
            nodes.put(nextNodeID, transportNode);
            toList.add(new HashMap<Integer, Arc>());
            addMatching(transportNode, terminal, capacity);

        }

        nextNodeID++;
    }

    public void bagStart() {
        bagStartID = nextNodeID;
        for (int i = 0; i < 10; i++) {
            nodes.put(nextNodeID, new Node(nextNodeID));
            toList.add(new HashMap<Integer, Arc>());
            nextNodeID++;
        }
    }

    public void addBag(String bagType, int numGifts) {
        //bag -or vertex id's start from 1
        if (bagType.equals("")) { //1
            transports.forEach(transport -> addMatching(nodes.get(bagStartID), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(bagStartID), numGifts);
            
        } else if (bagType.equals("b")) { //2
            greenTrain.forEach(transport -> addMatching(nodes.get(bagStartID+1), nodes.get(transport), numGifts));
            greenReindeer.forEach(transport -> addMatching(nodes.get(bagStartID+1), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(bagStartID+1), numGifts);

        } else if (bagType.equals("c")) { //3
            redReindeer.forEach(transport -> addMatching(nodes.get(bagStartID+2), nodes.get(transport), numGifts));
            redTrain.forEach(transport -> addMatching(nodes.get(bagStartID+2), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(bagStartID+2), numGifts);

        } else if (bagType.equals("d")) { //4
            redTrain.forEach(transport -> addMatching(nodes.get(bagStartID+3), nodes.get(transport), numGifts));
            greenTrain.forEach(transport -> addMatching(nodes.get(bagStartID+3), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(bagStartID+3), numGifts);

        } else if (bagType.equals("e")) { //5
            redReindeer.forEach(transport -> addMatching(nodes.get(bagStartID+4), nodes.get(transport), numGifts));
            greenReindeer.forEach(transport -> addMatching(nodes.get(bagStartID+4), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(bagStartID+4), numGifts);

        } else if (bagType.equals("bd")) { //6
            greenTrain.forEach(transport -> addMatching(nodes.get(bagStartID+5), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(bagStartID+5), numGifts);

        } else if (bagType.equals("be")) { //7
            greenReindeer.forEach(transport -> addMatching(nodes.get(bagStartID+6), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(bagStartID+6), numGifts);

        } else if (bagType.equals("cd")) { //8
            redTrain.forEach(transport -> addMatching(nodes.get(bagStartID+7), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(bagStartID+7), numGifts);

        } else if (bagType.equals("ce")) { //9
            redReindeer.forEach(transport -> addMatching(nodes.get(bagStartID+8), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(bagStartID+8), numGifts);

        } else if (bagType.equals("a")) { //10
            Node newBag = new Node(nextNodeID); 
            nodes.put(nextNodeID, newBag);
            toList.add(new HashMap<Integer, Arc>());
            redTrain.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));
            addMatching(source, newBag, numGifts);
            nextNodeID++;

        } else if (bagType.equals("ab")) { //11
            Node newBag = new Node(nextNodeID);
            nodes.put(nextNodeID, newBag);
            toList.add(new HashMap<Integer, Arc>());
            greenTrain.forEach(transport -> addMatching(nodes.get(bagStartID+10), nodes.get(transport), 1));
            greenReindeer.forEach(transport -> addMatching(nodes.get(bagStartID+10), nodes.get(transport), 1));
            addMatching(source, newBag, numGifts);
            nextNodeID++;

        } else if (bagType.equals("ac")) { //12
            Node newBag = new Node(nextNodeID);
            nodes.put(nextNodeID, newBag);
            toList.add(new HashMap<Integer, Arc>());
            redTrain.forEach(transport -> addMatching(nodes.get(12), nodes.get(transport), 1));
            redReindeer.forEach(transport -> addMatching(nodes.get(12), nodes.get(transport), 1));
            addMatching(source, newBag, numGifts);
            nextNodeID++;
            
        } else if (bagType.equals("ad")) { //13
            Node newBag = new Node(nextNodeID);
            nodes.put(nextNodeID, newBag);
            toList.add(new HashMap<Integer, Arc>());
            redTrain.forEach(transport -> addMatching(nodes.get(13), nodes.get(transport), 1));
            greenTrain.forEach(transport -> addMatching(nodes.get(13), nodes.get(transport), 1));
            addMatching(source, newBag, numGifts);
            nextNodeID++;

        } else if (bagType.equals("ae")) { //14
            Node newBag = new Node(nextNodeID);
            nodes.put(nextNodeID, newBag);
            toList.add(new HashMap<Integer, Arc>());
            redReindeer.forEach(transport -> addMatching(nodes.get(14), nodes.get(transport), 1));
            greenReindeer.forEach(transport -> addMatching(nodes.get(14), nodes.get(transport), 1));
            addMatching(source, newBag, numGifts);
            nextNodeID++;

        } else if (bagType.equals("abd")) { //15
            Node newBag = new Node(nextNodeID);
            nodes.put(nextNodeID, newBag);
            toList.add(new HashMap<Integer, Arc>());
            greenTrain.forEach(transport -> addMatching(nodes.get(15), nodes.get(transport), 1));
            addMatching(source, newBag, numGifts);
            nextNodeID++;

        } else if (bagType.equals("abe")) { //16
            Node newBag = new Node(nextNodeID);
            nodes.put(nextNodeID, newBag);
            toList.add(new HashMap<Integer, Arc>());
            greenReindeer.forEach(transport -> addMatching(nodes.get(16), nodes.get(transport), 1));
            addMatching(source, newBag, numGifts);
            nextNodeID++;
            
        } else if (bagType.equals("acd")) { //17
            Node newBag = new Node(nextNodeID);
            nodes.put(nextNodeID, newBag);
            toList.add(new HashMap<Integer, Arc>());
            redTrain.forEach(transport -> addMatching(nodes.get(17), nodes.get(transport), 1));
            addMatching(source, newBag, numGifts);
            nextNodeID++;

        } else if (bagType.equals("ace")) { //18
            Node newBag = new Node(nextNodeID);
            nodes.put(nextNodeID, newBag);
            toList.add(new HashMap<Integer, Arc>());
            redReindeer.forEach(transport -> addMatching(nodes.get(18), nodes.get(transport), 1));
            addMatching(source, newBag, numGifts);
            nextNodeID++;

        } else {
            //System.out.println("Input not matched: "+ bagType);
        }
    }

    private void addMatching(Node from, Node to, int flow) { 
        //an arrayList of index vertex, and that arrayList holds a hashmap of adjacent arcs. accesed by their integer id. 
        int fromID = from.getID();
        int toID = to.getID();

        if (toList.get(fromID).containsKey(toID)) {
            Arc arc = toList.get(fromID).get(toID);
            arc.addCapacity(flow);
        } else {
            HashMap<Integer, Arc> adjMap =  toList.get(fromID);
            Arc arc = new Arc(fromID, toID, flow);
            adjMap.put(toID, arc);
        }
    }

    private int min(int a, int b) {return (a <= b) ? a : b;}
}
