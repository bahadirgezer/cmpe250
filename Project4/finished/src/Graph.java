import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

public class Graph {
    //node ids' of transports, greenTrains, redTrains, greenReindeers, redReindeers, and giftBags
    private HashSet<Integer> transports, greenTrain, redTrain, greenReindeer, redReindeer, bags;
    //hashMap -> key: bag tag e.g. "acd", value: node id of this bag
    //used for adding together the same type of bags, other than bags with type 'a'
    private HashMap<String, Integer> cumulativeBagIDs;
    //arrayList -> index: fromNodeID, value: hashMap -> key: toNodeID, value: flow between these nodes
    //I don't hold capacities, only the residual graph and the reverse flows.
    private ArrayList<HashMap<Integer, Integer>> adjacencyFlow;
    //holds active nodes
    private ArrayDeque<Node> activeNodes;
    //hold every node
    private ArrayList<Node> nodes;
    private Node source, terminal;
    private int nextNodeID;

    public Graph() {
        transports = new HashSet<Integer>();
        greenTrain = new HashSet<Integer>();
        redTrain = new HashSet<Integer>();
        greenReindeer = new HashSet<Integer>();
        redReindeer = new HashSet<Integer>();
        bags = new HashSet<Integer>();
        cumulativeBagIDs = new HashMap<String, Integer>();

        adjacencyFlow = new ArrayList<HashMap<Integer, Integer>>();
        nodes = new ArrayList<Node>();        
        activeNodes = new ArrayDeque<Node>();

        nextNodeID = 0;
        source = this.addNode(0);        
        terminal = this.addNode(0);
    }

    /** 
        Heavily modified version of the Goldberg Tarjan push relabel algorithm. Returns maximum matching.
        I modified the algorithm to end prematurely. This happens when every transport has been relabeled. 
        The output is correct because maximum flow is the excess in the terminal node. So when the terminal 
        node is not reachable (every other node has height of at least 2 which means every transport node
        has been relabeled.) there is no need to further run the program. Additionally, adding nodes to the 
        active node queue is modified for bipartite matching. Furthermore, in each iteration the terminal 
        node is checked first to improve runtime.
        <p>
        I found a paper titled "Improved Algorithms for Bipartite Network Flow" by Ahuja, Orlin, Stein and Tarjan. 
        It shows modified versions of max network flow algorithms that work on bipartite graphs. I would have liked 
        to implement these, however, since the finals are coming up I do not have time to implement 
        improved versions of the push relabel algorithm for bipartite graphs as proposed by the paper above. 
        Before finding this paper did try to create a 'bipush-relabel' method myself however I encountered 
        some errors. So I made do with just modifying my existing method.
        
        @returns maximum flow
    */
    public int maximumBipartiteMatching() {
        //preflow
        Iterator<Integer> preflowIterator = adjacencyFlow.get(0).keySet().iterator();
        while (preflowIterator.hasNext()) {
            Integer sourceAdjID = preflowIterator.next();
            Node node = nodes.get(sourceAdjID);
            int flow = adjacencyFlow.get(0).get(sourceAdjID);
            node.addExcess(flow);
            preflowIterator.remove();
            adjacencyFlow.get(sourceAdjID).put(0, flow);
            this.addActiveNode(node);        
        }
        source.setSource(nodes.size());
        
        //main loop
        while(!activeNodes.isEmpty()) {
            Node fromNode = this.selectActiveNode();
            Integer fromNodeID = fromNode.getID();

            //if there isn't a way to reach the terminal node anymore, then finish.
            if (transports.isEmpty()) {
                break;
            }

            //discharge
            if (fromNode.hasExcess()) {
                //check terminal node first
                if (adjacencyFlow.get(fromNodeID).containsKey(1) && terminal.getHeight() + 1 == fromNode.getHeight()) {
                    int capacity = adjacencyFlow.get(fromNodeID).get(1);
                    int excess = fromNode.getExcess();
                    int flow = min(capacity, excess);
                    fromNode.removeExcess(flow);
                    terminal.addExcess(flow);
        
                    if (capacity == flow) {
                        adjacencyFlow.get(fromNodeID).remove(1);                        
                    } else {
                        adjacencyFlow.get(fromNodeID).replace(1, capacity - flow);
                    }
        
                    Integer reverseFlow = adjacencyFlow.get(1).get(fromNodeID);
                    if (reverseFlow == null) {
                        adjacencyFlow.get(1).put(fromNodeID, flow);
                    } else {
                        adjacencyFlow.get(1).put(fromNodeID, reverseFlow+flow);
                    }
                }

                if (fromNode.hasExcess()) {
                    this.discharge(fromNode);
                }
            }
            
            //relabel
            if (fromNode.hasExcess()) {
                if (transports.contains(fromNodeID)) {
                    transports.remove(fromNodeID);
                }

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

    //discharges the fromNode. Includes the push operation.
    private void discharge(Node fromNode) {
        int fromNodeID = fromNode.getID();
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

            //push
            int capacity = adjacent.get(toNodeID);
            int flow = min(capacity, excess);
            fromNode.removeExcess(flow);
            toNode.addExcess(flow);

            if (capacity == flow) {
                adjacentIterator.remove();                        
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

    private void addActiveNode(Node node) {
        int height = node.getHeight();
        if (height == nodes.size() + 1) {
            return;
        }
        
        //improved node priority
        node.makeActive();
        if (height == 1) {
            activeNodes.addFirst(node);
        } else {
            activeNodes.add(node);            
        }
    }

    private Node selectActiveNode() {
        Node node = activeNodes.poll();
        node.makeInactive();
        return node;
    }

    /*      start of graph building methods     */

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
        nextNodeID++;
        return node;
    }

    public void addGreenTrain(int capacity) {this.addTransport(0, capacity);}
    public void addRedTrain(int capacity) {this.addTransport(1, capacity);}
    public void addGreenReindeer(int capacity) {this.addTransport(2, capacity);}
    public void addRedReindeer(int capacity) {this.addTransport(3, capacity);}

    private void addTransport(int type, int capacity) {
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
        
        addMatching(this.addNode(1), terminal, capacity);
    }

    public boolean addBag(String bagType, int numGifts) {
        //bag -or vertex id's start from 1
        if (bagType.equals("")) { //1
            String key = "";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                bags.add(nextNodeID);
                this.addNode(2);
            }
            transports.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("b")) { //2
            String key = "b";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                bags.add(nextNodeID);
                this.addNode(2);
            }
            greenTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            greenReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("c")) { //3
            String key = "c";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                bags.add(nextNodeID);
                this.addNode(2);
            }
            redReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            redTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("d")) { //4
            String key = "d";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                bags.add(nextNodeID);
                this.addNode(2);
            }
            redTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            greenTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("e")) { //5
            String key = "e";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                bags.add(nextNodeID);
                this.addNode(2);
            }
            redReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            greenReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("bd")) { //6
            String key = "bd";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                bags.add(nextNodeID);
                this.addNode(2);
            }
            greenTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("be")) { //7
            String key = "be";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                bags.add(nextNodeID);
                this.addNode(2);
            }
            greenReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("cd")) { //8
            String key = "cd";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                bags.add(nextNodeID);
                this.addNode(2);
            }
            redTrain.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else if (bagType.equals("ce")) { //9
            String key = "ce";
            if (!cumulativeBagIDs.containsKey(key)) {
                cumulativeBagIDs.put(key, nextNodeID);
                bags.add(nextNodeID);
                this.addNode(2);
            }
            redReindeer.forEach(transport -> addMatching(nodes.get(cumulativeBagIDs.get(key)), nodes.get(transport), numGifts));
            addMatching(source, nodes.get(cumulativeBagIDs.get(key)), numGifts);

        } else {

            if (bagType.equals("a")) { //10
                bags.add(nextNodeID);
                Node node = this.addNode(2);
                addMatching(source, node, numGifts);    
                transports.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ab")) { //11
                bags.add(nextNodeID);
                Node node = this.addNode(2);
                addMatching(source, node, numGifts);    
                greenTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));
                greenReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ac")) { //12
                bags.add(nextNodeID);
                Node node = this.addNode(2);
                addMatching(source, node, numGifts);    
                redTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));
                redReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ad")) { //13
                bags.add(nextNodeID);
                Node node = this.addNode(2);
                addMatching(source, node, numGifts);    
                redTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));
                greenTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ae")) { //14
                bags.add(nextNodeID);
                Node node = this.addNode(2);
                addMatching(source, node, numGifts);    
                redReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));
                greenReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("abd")) { //15
                bags.add(nextNodeID);
                Node node = this.addNode(2);
                addMatching(source, node, numGifts);    
                greenTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("abe")) { //16
                bags.add(nextNodeID);
                Node node = this.addNode(2);
                addMatching(source, node, numGifts);    
                greenReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("acd")) { //17
                bags.add(nextNodeID);
                Node node = this.addNode(2);
                addMatching(source, node, numGifts);    
                redTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ace")) { //18
                bags.add(nextNodeID);
                Node node = this.addNode(2);
                addMatching(source, node, numGifts);    
                redReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));
            } else {
                return false;
            }
        }
        return true;
    }

    private int min(int a, int b) {return (a <= b) ? a : b;}
}

class Node {
    private final int id;
    private int height;
    private int excess;
    private boolean active;

    Node(int id, int height) {
        this.id = id;
        this.height = height;
        excess = 0;
        active = false;
    }

    public boolean hasExcess() {
        return (excess > 0) ? true : false;
    }

    public int getExcess() {
        return excess;
    }

    public void removeExcess(int excess) {
        this.excess -= excess;
    }

    public void addExcess(int excess) {
        this.excess += excess;
    }

    public void setSource(int numNodes) {
        height = numNodes;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getID() {
        return id;
    }

    public int getHeight() {
        return height;
    }

    public void makeActive() {
        active = true;
    }

    public void makeInactive() {
        active = false;
    }

    public boolean isActive() {
        return active;
    }
}
