import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.PriorityQueue;


public class Graph {
    private HashSet<Integer> transports, greenTrain, redTrain, greenReindeer, redReindeer, bags;
    private HashMap<String, Integer> cumulativeBagIDs;
    private int nextNodeID;

    private Node source, terminal;
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
        transports = new HashSet<Integer>();
        greenTrain = new HashSet<Integer>();
        redTrain = new HashSet<Integer>();
        greenReindeer = new HashSet<Integer>();
        redReindeer = new HashSet<Integer>();
        bags = new HashSet<Integer>();
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
    public int maximumBipartiteFlow2() {
        long cumulativeDischarge = 0;
        long cumulativeRelabel = 0;

        long first = System.currentTimeMillis();
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
        
        long second = System.currentTimeMillis();
        System.out.println("preflow: "+(second-first)+" ms");

        // int loopCount = 0;
        // boolean recalculated = false;
        while(!activeNodes.isEmpty()) {
            Node fromNode = this.selectActiveNode();
            Integer fromNodeID = fromNode.getID();

            if (transports.isEmpty()) {
                break;
            }

            long third = System.currentTimeMillis();
            //discharge
            if (fromNode.hasExcess()) {
                if (adjacencyFlow.get(fromNodeID).containsKey(0) && source.getHeight() + 1 == fromNode.getHeight()) {
                    int capacity = adjacencyFlow.get(fromNodeID).get(0);
                    int excess = fromNode.getExcess();
                    int flow = min(capacity, excess);
                    //HashSet<Integer> adjacentNodesOfToNode = reverseAdjacency.get(toNodeID);
        
                    fromNode.removeExcess(flow);
                    source.addExcess(flow);
        
                    if (capacity == flow) {
                        adjacencyFlow.get(fromNodeID).remove(0);                        
                        //adjacentNodesOfToNode.remove(fromNodeID);
                    } else {
                        adjacencyFlow.get(fromNodeID).replace(0, capacity - flow);
                    }
        
                    Integer reverseFlow = adjacencyFlow.get(0).get(fromNodeID);
                    if (reverseFlow == null) {
                        adjacencyFlow.get(0).put(fromNodeID, flow);
                    } else {
                        adjacencyFlow.get(0).put(fromNodeID, reverseFlow+flow);
                    }
                }

                if (adjacencyFlow.get(fromNodeID).containsKey(1) && terminal.getHeight() + 1 == fromNode.getHeight()) {
                    int capacity = adjacencyFlow.get(fromNodeID).get(1);
                    int excess = fromNode.getExcess();
                    int flow = min(capacity, excess);
                    //HashSet<Integer> adjacentNodesOfToNode = reverseAdjacency.get(toNodeID);
        
                    fromNode.removeExcess(flow);
                    terminal.addExcess(flow);
        
                    if (capacity == flow) {
                        adjacencyFlow.get(fromNodeID).remove(1);                        
                        //adjacentNodesOfToNode.remove(fromNodeID);
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
            
            long fourth = System.currentTimeMillis();
            cumulativeDischarge += fourth - third;

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

            long fifth = System.currentTimeMillis();
            cumulativeRelabel += fifth - fourth;

            // loopCount++;
        }
        System.out.println("discharge: "+ cumulativeDischarge+" ms");
        System.out.println("relabel: "+ cumulativeRelabel+" ms");

        return terminal.getExcess();
    }








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

            if (transports.isEmpty()) {
                break;
            }

            //discharge
            if (fromNode.hasExcess()) {
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
            
            if (fromNode.hasExcess()) {
                if (transports.contains(fromNodeID)) {
                    transports.remove(fromNodeID);
                }
                // if (bags.contains(fromNodeID)) {
                //     continue;
                // }
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
        node.makeActive();
        activeNodes.add(node);
    }

    private Node selectActiveNode() {
        Node node = activeNodes.poll();
        node.makeInactive();
        return node;
    }

    //start of graph building methods
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

    public void addBag(String bagType, int numGifts) {
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
            bags.add(nextNodeID);
            Node node = this.addNode(2);
            addMatching(source, node, numGifts);

            if (bagType.equals("a")) { //10
                transports.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ab")) { //11
                greenTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));
                greenReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ac")) { //12
                redTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));
                redReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ad")) { //13
                redTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));
                greenTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ae")) { //14
                redReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));
                greenReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("abd")) { //15
                greenTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("abe")) { //16
                greenReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("acd")) { //17
                redTrain.forEach(transport -> addMatching(node, nodes.get(transport), 1));

            } else if (bagType.equals("ace")) { //18
                redReindeer.forEach(transport -> addMatching(node, nodes.get(transport), 1));
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

        // public int bipartiteSpecializedPushRelabel() {
    //     // Iterator<Integer> preflowIterator = adjacencyFlow.get(0).keySet().iterator();
    //     // while (preflowIterator.hasNext()) {
    //     //     Integer sourceAdjID = preflowIterator.next();
    //     //     Node node = nodes.get(sourceAdjID);
    //     //     int flow = adjacencyFlow.get(0).get(sourceAdjID);
    //     //     node.addExcess(flow);
    //     //     preflowIterator.remove();
    //     //     adjacencyFlow.get(sourceAdjID).put(0, flow);
    //     //     //reverseAdjacency.get(sourceAdjID).remove(0);
    //     //     //reverseAdjacency.get(0).add(sourceAdjID);
    //     //     //this.addActiveNode(node);
        
    //     // }
    //     // source.setSource(nodes.size());
        
    //     // Iterator<Integer> firstBagIterator = bags.iterator();
    //     // while (firstBagIterator.hasNext()) {
    //     //     Integer fromNodeID = firstBagIterator.next();
    //     //     Node fromNode = nodes.get(fromNodeID);

    //     //     HashMap<Integer, Integer> adjacent = adjacencyFlow.get(fromNodeID);
    //     //     Iterator<Integer> firstBagToNodeIterator = adjacent.keySet().iterator();
    //     //     while (firstBagToNodeIterator.hasNext()) {
    //     //         Integer toNodeID = firstBagToNodeIterator.next();
    //     //         Node toNode = nodes.get(toNodeID);              
    //     //         int excess = fromNode.getExcess();

    //     //         if (toNode.equals(source)) {
    //     //             continue;
    //     //         }
                
    //     //         int capacity = adjacent.get(toNodeID);
    //     //         int flow = min(capacity, excess);
    //     //         //HashSet<Integer> adjacentNodesOfToNode = reverseAdjacency.get(toNodeID);
    
    //     //         fromNode.removeExcess(flow);
    //     //         toNode.addExcess(flow);
    
    //     //         if (capacity == flow) {
    //     //             firstBagToNodeIterator.remove();                        
    //     //             //adjacentNodesOfToNode.remove(fromNodeID);
    //     //         } else {
    //     //             adjacent.replace(toNodeID, capacity - flow);
    //     //         }
    
    //     //         Integer reverseFlow = adjacencyFlow.get(toNodeID).get(fromNodeID);
    //     //         if (reverseFlow == null) {
    //     //             adjacencyFlow.get(toNodeID).put(fromNodeID, flow);
    //     //         } else {
    //     //             adjacencyFlow.get(toNodeID).put(fromNodeID, reverseFlow+flow);
    //     //         }
    //     //     }
    //     // }

    //     // Iterator<Integer> firstTransportIterator = transports.iterator();
    //     // while (firstTransportIterator.hasNext()) {
    //     //     Integer fromNodeID = firstTransportIterator.next();
    //     //     Node fromNode = nodes.get(fromNodeID);

    //     //     int capacity = adjacencyFlow.get(fromNodeID).get(1);
    //     //     int excess = fromNode.getExcess();
    //     //     int flow = min(capacity, excess);
    //     //     //HashSet<Integer> adjacentNodesOfToNode = reverseAdjacency.get(toNodeID);

    //     //     fromNode.removeExcess(flow);
    //     //     terminal.addExcess(flow);

    //     //     if (capacity == flow) {
    //     //         adjacencyFlow.get(fromNodeID).remove(1);                        
    //     //         //adjacentNodesOfToNode.remove(fromNodeID);
    //     //     } else {
    //     //         adjacencyFlow.get(fromNodeID).replace(1, capacity - flow);
    //     //     }

    //     //     Integer reverseFlow = adjacencyFlow.get(1).get(fromNodeID);
    //     //     if (reverseFlow == null) {
    //     //         adjacencyFlow.get(1).put(fromNodeID, flow);
    //     //     } else {
    //     //         adjacencyFlow.get(1).put(fromNodeID, reverseFlow+flow);
    //     //     }
    //     // }
            
    //     bags.forEach(fromNodeID -> nodes.get(fromNodeID).setExcess(adjacencyFlow.get(0).get(fromNodeID)));

    //     for (Integer bagID : bags) {
    //         discharge2(bagID);
    //     }

    //     while(!transports.isEmpty()) {

    //     }
    //     for (Integer transportID : transports) {
    //         Node transport = nodes.get(transportID);

            

    //     }


    //     // for (Integer fromNodeID : bags) {
    //     //     Node fromNode = nodes.get(fromNodeID);
            
    //     //     while (!adjacencyFlow.get(fromNodeID).isEmpty()) {
    //     //         for (Integer toNodeID : adjacencyFlow.get(fromNodeID).keySet()) {
    //     //             Node toNode = nodes.get(toNodeID);
    //     //             int flow = min(adjacencyFlow.get(fromNodeID).get(toNodeID),  fromNode.getExcess());
    //     //             toNode.addExcess(flow);
    //     //             int terminalFlow = min(adjacencyFlow.get(toNodeID).get(1), toNode.getExcess());
    //     //             terminal.addExcess(terminalFlow);
    //     //             adjacencyFlow.get(fromNodeID).remove(toNodeID);

    //     //             if (terminalFlow < flow) {
    //     //                 fromNode.removeExcess(terminalFlow);
    //     //                 break;
    //     //             }
    //     //         }
    
    //     //     }
    //     // }

    //     return terminal.getExcess();
    // }

    // private void discharge2(int bagID) {
    //     Node bag = nodes.get(bagID);
    //     HashMap<Integer, Integer> bagAdjacent = adjacencyFlow.get(bagID);
    //     Iterator<Integer> adjacentIterator = bagAdjacent.keySet().iterator();
    //     while (adjacentIterator.hasNext()) {
    //         int transportID = adjacentIterator.next();
    //         Node transport = nodes.get(transportID);
    //         int matchingCapacity = bagAdjacent.get(transportID);
    //         int matchingFlow = min(bag.getExcess(), matchingCapacity);
    //         int terminalCapacity = adjacencyFlow.get(transportID).get(1);
    //         int terminalFlow = min(matchingFlow, terminalCapacity);
            
    //         transport.addExcess(matchingFlow-terminalFlow);
    //         terminal.addExcess(terminalFlow);
    //         bag.removeExcess(matchingFlow);
            
    //         if (matchingFlow != 0) {
    //             adjacencyFlow.get(transportID).put(bagID, matchingFlow);
    //         }

    //         if (matchingCapacity == matchingFlow) {
    //             bagAdjacent.put(bagID, matchingCapacity - matchingFlow);
    //         } else {
    //             bagAdjacent.remove(bagID);
    //         }

    //         if (terminalFlow != 0) {
    //             adjacencyFlow.get(transportID).put(1, terminalCapacity-terminalFlow);
    //         }
    //     }
    // }
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