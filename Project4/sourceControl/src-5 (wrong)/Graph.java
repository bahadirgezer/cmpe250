import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.PriorityQueue;

// check sink and source first, combine transports, do checking accordingly
public class Graph {
    private HashMap<String, Integer> cumulativeBagIDs;
    private int nextNodeID;

    private Node source, terminal, greenTrain, redTrain, greenReindeer, redReindeer;
    private ArrayList<Node> nodes;
    //arrayList -> index: fromNodeID, value: hashMap -> key: toNodeID, value: flow;
    private ArrayList<HashMap<Integer, Integer>> adjacencyFlow;
    //arrayList -> index: toNodeID, value: HashSet -> value: fromNodeID; 
    //private ArrayList<HashSet<Integer>> reverseAdjacency;
    //arrayList -> index: nodeID, value: minimumHeightOfAdjacentNodes;
    //private ArrayList<Integer> minHeight; 
    
    //private ArrayDeque<Node> activeNodes;
    private PriorityQueue<Node> activeNodes;


    public Graph() {
        cumulativeBagIDs = new HashMap<String, Integer>();

        adjacencyFlow = new ArrayList<HashMap<Integer, Integer>>();
        //reverseAdjacency = new ArrayList<HashSet<Integer>>();
        //minHeight = new ArrayList<Integer>();
        nodes = new ArrayList<Node>();
        
        //activeNodes = new ArrayDeque<Node>();
        activeNodes = new PriorityQueue<Node>(new ActiveQueueComparator());


        nextNodeID = 0;
        source = this.addNode(0);        
        terminal = this.addNode(0);
    }

    // firstCheckFor the sink and source when pushing or discharging;;;;;
    //start of max flow solving methods
    public int maximumBipartiteFlow() {
        Iterator<Integer> preflowIterator = adjacencyFlow.get(0).keySet().iterator();
        while (preflowIterator.hasNext()) {
            Integer sourceAdjID = preflowIterator.next();
            Node node = nodes.get(sourceAdjID);
            int flow = adjacencyFlow.get(0).get(sourceAdjID);
            node.addExcess(flow);
            preflowIterator.remove();
            adjacencyFlow.get(sourceAdjID).put(0, flow);
            //reverseAdjacency.get(sourceAdjID).remove(0);
            //reverseAdjacency.get(0).add(sourceAdjID);
            this.addActiveNode(node);
        
        }
        source.setSource(nodes.size());

        while(!activeNodes.isEmpty()) {
            Node fromNode = this.selectActiveNode();
            Integer fromNodeID = fromNode.getID();

            //discharge
            if (fromNode.hasExcess()) {
                HashMap<Integer, Integer> adjacent = adjacencyFlow.get(fromNodeID);
                Iterator<Integer> adjacentIterator = adjacent.keySet().iterator();

                while(adjacentIterator.hasNext()) {
                    int toNodeID = adjacentIterator.next();
                    Node toNode = nodes.get(toNodeID);
                    int excess = fromNode.getExcess();

                    if (excess == 0) {
                        break;
                    }

                    if (toNode.getHeight() + 1 != fromNode.getHeight()) {
                        continue;
                    }
                    int capacity = adjacent.get(toNodeID);
                    int flow = min(capacity, excess);
                    //HashSet<Integer> adjacentNodesOfToNode = reverseAdjacency.get(toNodeID);

                    fromNode.removeExcess(flow);
                    toNode.addExcess(flow);

                    if (capacity == flow) {
                        adjacentIterator.remove();                        
                        //adjacentNodesOfToNode.remove(fromNodeID);
                    } else {
                        adjacent.replace(toNodeID, capacity - flow);
                    }

                    Integer reverseFlow = adjacencyFlow.get(toNodeID).get(fromNodeID);
                    if (reverseFlow == null) {
                        adjacencyFlow.get(toNodeID).put(fromNodeID, flow);
                    } else {
                        adjacencyFlow.get(toNodeID).put(fromNodeID, reverseFlow+flow);
                    }

                    if (!toNode.equals(source) && !toNode.equals(terminal) && !toNode.isActive()) {
                        this.addActiveNode(toNode);
                    }   
                }
            }

            if (fromNode.hasExcess()) {
                int minHeight = 2*nodes.size()+1;
                for (Integer adjID : adjacencyFlow.get(fromNodeID).keySet()) {
                    minHeight = min(minHeight, nodes.get(adjID).getHeight());
                }
                fromNode.setHeight(minHeight+1);
                this.addActiveNode(fromNode);
            }
        }

        return terminal.getExcess();
    }

    private void addActiveNode(Node node) {
        node.makeActive();
        activeNodes.add(node);
    }

    private Node selectActiveNode() {
        Node node = activeNodes.poll();
        node.makeInactive();
        return node;
    }

    //start of graph building methods
    public void addGreenTrain(int capacity, int numTransport) {
        cumulativeBagIDs.put("greenTrain", numTransport);
        greenTrain = this.addNode(1);
        addMatching(greenTrain, terminal, capacity);
    }

    public void addRedTrain(int capacity, int numTransport) {
        cumulativeBagIDs.put("redTrain", numTransport);
        redTrain = this.addNode(1);
        addMatching(redTrain, terminal, capacity);
    }

    public void addGreenReindeer(int capacity, int numTransport) {
        cumulativeBagIDs.put("greenReindeer", numTransport);
        greenReindeer = this.addNode(1);
        addMatching(greenReindeer , terminal, capacity);
    }

    public void addRedReindeer(int capacity, int numTransport) {
        cumulativeBagIDs.put("redReindeer", numTransport);
        redReindeer = this.addNode(1);
        addMatching(redReindeer , terminal, capacity);
    }

    public void addBag(String bagType, int numGifts) {
        //bag -or vertex id's start from 1
        if (bagType.equals("")) { //1
            String key = "";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                this.addNode(2);
            }
            Node node = nodes.get(cumulativeBagIDs.get(key));
            addMatching(node, greenTrain, numGifts);
            addMatching(node, redTrain, numGifts);
            addMatching(node, greenReindeer, numGifts);
            addMatching(node, redReindeer, numGifts);
            addMatching(source, node, numGifts);

        } else if (bagType.equals("b")) { //2
            String key = "b";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                this.addNode(2);
            }
            Node node = nodes.get(cumulativeBagIDs.get(key));
            addMatching(node, greenTrain, numGifts);
            addMatching(node, greenReindeer, numGifts);
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("c")) { //3
            String key = "c";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                this.addNode(2);
            }
            Node node = nodes.get(cumulativeBagIDs.get(key));
            addMatching(node, redReindeer, numGifts);
            addMatching(node, redTrain, numGifts);
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("d")) { //4
            String key = "d";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                this.addNode(2);
            }
            Node node = nodes.get(cumulativeBagIDs.get(key));
            addMatching(node, greenTrain, numGifts);
            addMatching(node, redTrain, numGifts);
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("e")) { //5
            String key = "e";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                this.addNode(2);
            }
            Node node = nodes.get(cumulativeBagIDs.get(key));
            addMatching(node, greenReindeer, numGifts);
            addMatching(node, redReindeer, numGifts);
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("bd")) { //6
            String key = "bd";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                this.addNode(2);
            }
            Node node = nodes.get(cumulativeBagIDs.get(key));
            addMatching(node, greenTrain, numGifts);
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("be")) { //7
            String key = "be";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                this.addNode(2);
            }
            Node node = nodes.get(cumulativeBagIDs.get(key));
            addMatching(node, greenReindeer, numGifts);
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("cd")) { //8
            String key = "cd";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                this.addNode(2);
            }
            Node node = nodes.get(cumulativeBagIDs.get(key));
            addMatching(node, redTrain, numGifts);
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("ce")) { //9
            String key = "ce";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                this.addNode(2);
            }
            Node node = nodes.get(cumulativeBagIDs.get(key));
            addMatching(node, redReindeer, numGifts);
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else {
            Node node = this.addNode(2);
            addMatching(source, node, numGifts);

            if (bagType.equals("a")) { //10
                addMatching(node, greenTrain, cumulativeBagIDs.get("greenTrain"));
                addMatching(node, redTrain, cumulativeBagIDs.get("redTrain"));
                addMatching(node, greenReindeer, cumulativeBagIDs.get("greenReindeer"));
                addMatching(node, redReindeer, cumulativeBagIDs.get("redReindeer"));
            } else if (bagType.equals("ab")) { //11
                addMatching(node, greenTrain, cumulativeBagIDs.get("greenTrain"));
                addMatching(node, greenReindeer, cumulativeBagIDs.get("greenReindeer"));
            } else if (bagType.equals("ac")) { //12
                addMatching(node, redTrain, cumulativeBagIDs.get("redTrain"));
                addMatching(node, redReindeer, cumulativeBagIDs.get("redReindeer"));
            } else if (bagType.equals("ad")) { //13
                addMatching(node, greenTrain, cumulativeBagIDs.get("greenTrain"));
                addMatching(node, redTrain, cumulativeBagIDs.get("redTrain"));
            } else if (bagType.equals("ae")) { //14
                addMatching(node, greenReindeer, cumulativeBagIDs.get("greenReindeer"));
                addMatching(node, redReindeer, cumulativeBagIDs.get("redReindeer"));
            } else if (bagType.equals("abd")) { //15
                addMatching(node, greenTrain, cumulativeBagIDs.get("greenTrain"));
            } else if (bagType.equals("abe")) { //16
                addMatching(node, greenReindeer, cumulativeBagIDs.get("greenReindeer"));
            } else if (bagType.equals("acd")) { //17
                addMatching(node, redTrain, cumulativeBagIDs.get("redTrain"));
            } else if (bagType.equals("ace")) { //18
                addMatching(node, redReindeer, cumulativeBagIDs.get("redReindeer"));
            }
        }
    }

    //Creates a new arc with the given capacity, increases the capacity if the arc already exists. 
    private void addMatching(Node from, Node to, int capacity) {
        int fromID = from.getID();
        int toID = to.getID();

        HashMap<Integer, Integer> map = adjacencyFlow.get(fromID);
        Integer oldCapacity = map.get(toID);
        if (oldCapacity == null) {
            //reverseAdjacency.get(toID).add(fromID);
            map.put(toID, capacity);
        } else {
            map.put(toID, capacity+oldCapacity);
        }
    }
        
    //bags 2, transports 1, source and terminal 0
    private Node addNode(int nodeType) {
        Node node = new Node (nextNodeID, nodeType);
        nodes.add(node);
        adjacencyFlow.add(new HashMap<Integer, Integer>());
        //reverseAdjacency.add(new HashSet<Integer>());
        //minHeight.add(null);
        nextNodeID++;
        return node;
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