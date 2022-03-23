import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;


public class Graph {
    private HashSet<Integer> transports, greenTrain, redTrain, greenReindeer, redReindeer;
    private ArrayList<HashMap<Integer, Arc>> toList;
    private ArrayList<Node> nodes;
    private HashMap<String, Integer> cumulativeBagIDs;
    private PriorityQueue<Node> activeNodes;
    private int nextNodeID;
    private Node source, terminal;

    public Graph() {
        transports = new HashSet<Integer>();
        greenTrain = new HashSet<Integer>();
        redTrain = new HashSet<Integer>();
        greenReindeer = new HashSet<Integer>();
        redReindeer = new HashSet<Integer>();
        
        nodes = new ArrayList<Node>();
        toList = new ArrayList<HashMap<Integer, Arc>>();
        activeNodes = new PriorityQueue<Node>(new ActiveQueueComparator());
        cumulativeBagIDs = new HashMap<String, Integer>();

        source = new Node(0, 0);
        terminal = new Node(1, 0);

        nodes.add(source);
        toList.add(new HashMap<Integer, Arc>());
        nodes.add(terminal);
        toList.add(new HashMap<Integer, Arc>());
        nextNodeID = 2;
    }
    
    //start of max flow solving methods
    // Goldberg Tarjan Push Relabel Algorithm -with maximum height selection
    public int maximumBipartiteMatching() {
        for (Arc arc : toList.get(0).values()) {
            Node adjNode = nodes.get(arc.getTo());
            adjNode.setExcess(arc.getFlow());
            adjNode.makeActive();
            activeNodes.add(adjNode);
            Arc reverseArc = new Arc(0, arc.getFlow());
            toList.get(arc.getTo()).put(0, reverseArc);
            arc.setFlow(0);
        }
        source.setSource(nodes.size());
        
        while (!activeNodes.isEmpty()) {
            Node currentNode = activeNodes.poll();
            currentNode.makeInactive();

            while (currentNode.hasExcess()) {
                //push
                for (Arc toArc : toList.get(currentNode.getID()).values()) {
                    if (!currentNode.hasExcess()) {
                        break;
                    }
                    
                    if (nodes.get(toArc.getTo()).getHeight()+1 == currentNode.getHeight()) {
                        int flow = min(currentNode.getExcess(), toArc.getFlow());
                        if (flow == 0) {
                            continue;
                        }

                        toArc.removeFlow(flow);
                        if (toList.get(toArc.getTo()).containsKey(currentNode.getID())) {
                            Arc fromArc = toList.get(toArc.getTo()).get(currentNode.getID());
                            fromArc.addFlow(flow);
                        } else {
                            Arc fromArc = new Arc(currentNode.getID(), flow);
                            toList.get(toArc.getTo()).put(currentNode.getID(), fromArc);
                        }
                        currentNode.removeExcess(flow);
                        nodes.get(toArc.getTo()).addExcess(flow);

                        if (!nodes.get(toArc.getTo()).equals(source) && !nodes.get(toArc.getTo()).isActive()) {
                            if (nodes.get(toArc.getTo()).getID() == 1) {
                                continue;
                            }
                            nodes.get(toArc.getTo()).makeActive();
                            activeNodes.add(nodes.get(toArc.getTo()));
                        }
                    }
                }
                break;
            }

            if (currentNode.hasExcess()) {
                //relabel
                int minHeight = nodes.size()*2 + 1;
                for (Integer adjNodeID : toList.get(currentNode.getID()).keySet()) {
                    if (toList.get(currentNode.getID()).get(adjNodeID).getFlow() != 0) {
                        minHeight = min(minHeight, nodes.get(adjNodeID).getHeight());
                    }
                }
                currentNode.setHeight(minHeight+1);
                currentNode.makeActive();   
                activeNodes.add(currentNode);
            }
        }

        return terminal.getExcess();
    }

    //start of graph building methods
    public void addGreenTrain(int capacity) {this.addTransport(0, capacity);}
    public void addRedTrain(int capacity) {this.addTransport(1, capacity);}
    public void addGreenReindeer(int capacity) {this.addTransport(2, capacity);}
    public void addRedReindeer(int capacity) {this.addTransport(3, capacity);}

    private void addTransport(int type, int capacity) {
        Node transportNode = new Node(nextNodeID, 1); 
        if (type == 0) {
            transports.add(nextNodeID);
            greenTrain.add(nextNodeID);
        } else if (type == 1) {
            transports.add(nextNodeID);
            redTrain.add(nextNodeID);
        } else if (type == 2) {
            transports.add(nextNodeID);
            greenReindeer.add(nextNodeID);
        } else if (type == 3) {
            transports.add(nextNodeID);
            redReindeer.add(nextNodeID);
        }

        nodes.add(transportNode);
        toList.add(new HashMap<Integer, Arc>());
        addMatching(transportNode, terminal, capacity);
        nextNodeID++;
    }

    public void addBag(String bagType, int numGifts) {
        //bag -or vertex id's start from 1
        if (bagType.equals("")) { //1
            String key = "";
            if (!cumulativeBagIDs.containsKey(key)) {
                Node newBag = new Node(nextNodeID, 2); 
                nodes.add(newBag);
                toList.add(new HashMap<Integer, Arc>());
                cumulativeBagIDs.put(key, nextNodeID);
                nextNodeID++;    
            }
            transports.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("b")) { //2
            String key = "b";
            if (!cumulativeBagIDs.containsKey(key)) {
                Node newBag = new Node(nextNodeID, 2);
                nodes.add(newBag);
                toList.add(new HashMap<Integer, Arc>());
                cumulativeBagIDs.put(key, nextNodeID);
                nextNodeID++;
            }
            greenTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            greenReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("c")) { //3
            String key = "c";
            if (!cumulativeBagIDs.containsKey(key)) {
                Node newBag = new Node(nextNodeID, 2);
                nodes.add(newBag);
                toList.add(new HashMap<Integer, Arc>());
                cumulativeBagIDs.put(key, nextNodeID);
                nextNodeID++;
            }
            redReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            redTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("d")) { //4
            String key = "d";
            if (!cumulativeBagIDs.containsKey(key)) {
                Node newBag = new Node(nextNodeID, 2);
                nodes.add(newBag);
                toList.add(new HashMap<Integer, Arc>());
                cumulativeBagIDs.put(key, nextNodeID);
                nextNodeID++;
            }
            redTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            greenTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("e")) { //5
            String key = "e";
            if (!cumulativeBagIDs.containsKey(key)) {
                Node newBag = new Node(nextNodeID, 2);
                nodes.add(newBag);
                toList.add(new HashMap<Integer, Arc>());
                cumulativeBagIDs.put(key, nextNodeID);
                nextNodeID++;
            }
            redReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            greenReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("bd")) { //6
            String key = "bd";
            if (!cumulativeBagIDs.containsKey(key)) {
                Node newBag = new Node(nextNodeID, 2);
                nodes.add(newBag);
                toList.add(new HashMap<Integer, Arc>());
                cumulativeBagIDs.put(key, nextNodeID);
                nextNodeID++;
            }
            greenTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("be")) { //7
            String key = "be";
            if (!cumulativeBagIDs.containsKey(key)) {
                Node newBag = new Node(nextNodeID, 2);
                nodes.add(newBag);
                toList.add(new HashMap<Integer, Arc>());
                cumulativeBagIDs.put(key, nextNodeID);
                nextNodeID++;
            }
            greenReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("cd")) { //8
            String key = "cd";
            if (!cumulativeBagIDs.containsKey(key)) {
                Node newBag = new Node(nextNodeID, 2);
                nodes.add(newBag);
                toList.add(new HashMap<Integer, Arc>());
                cumulativeBagIDs.put(key, nextNodeID);
                nextNodeID++;
            }
            redTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("ce")) { //9
            String key = "ce";
            if (!cumulativeBagIDs.containsKey(key)) {
                Node newBag = new Node(nextNodeID, 2);
                nodes.add(newBag);
                toList.add(new HashMap<Integer, Arc>());
                cumulativeBagIDs.put(key, nextNodeID);
                nextNodeID++;
            }
            redReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else {
            Node newBag = new Node(nextNodeID, 2); 
            nodes.add(newBag);
            toList.add(new HashMap<Integer, Arc>());
            addMatching(source, newBag, numGifts);
            nextNodeID++;

            if (bagType.equals("a")) { //10
                transports.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));

            } else if (bagType.equals("ab")) { //11
                greenTrain.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));
                greenReindeer.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));

            } else if (bagType.equals("ac")) { //12
                redTrain.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));
                redReindeer.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));

            } else if (bagType.equals("ad")) { //13
                redTrain.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));
                greenTrain.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));

            } else if (bagType.equals("ae")) { //14
                redReindeer.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));
                greenReindeer.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));

            } else if (bagType.equals("abd")) { //15
                greenTrain.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));

            } else if (bagType.equals("abe")) { //16
                greenReindeer.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));

            } else if (bagType.equals("acd")) { //17
                redTrain.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));

            } else if (bagType.equals("ace")) { //18
                redReindeer.forEach(transport -> addMatching(newBag, nodes.get(transport), 1));
            }
        }
    }

    //Creates a new arc with the given capacity, increases the capacity if the arc already exists. 
    private void addMatching(Node from, Node to, int capacity) {
        int fromID = from.getID();
        int toID = to.getID();

        if (toList.get(fromID).containsKey(toID)) {
            toList.get(fromID).get(toID).addCapacity(capacity);
        } else {
            HashMap<Integer, Arc> adjMap = toList.get(fromID);
            adjMap.put(toID, new Arc(toID, capacity));
        }
    }

    private int min(int a, int b) {return (a <= b) ? a : b;}
}

class ActiveQueueComparator implements Comparator<Node> {

    @Override
    public int compare(Node o1, Node o2) {
        if (o1.getHeight() > o2.getHeight()) {
            return -1;
        } else if (o1.getHeight() < o2.getHeight()) {
            return 1;
        } else {
            return (o2.getID() > o1.getID()) ? 1 : (o2.getID() < o1.getID()) ? -1 : 0;
        }
    }
}