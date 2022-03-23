import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.HashMap;

public class ExcelFedSystem {
    private PriorityQueue<Event> events; // Main event queue. Every executed event passes through this queue.
    //Each player is accessed through this HashMap by their unique IDs.
    private HashMap<Integer, Player> players = new HashMap<Integer, Player>(); 
    private PriorityQueue<Event> trainingQueue,therapyQueue, massageQueue; //waiting queues for each event.
    private int freeTrainers, freeMasseurs, freeTherapists; //
    private HashMap<Integer, Double> therapyTimes; //Contains therapy times.
    private boolean[] therapists; //Is used in parallel with therapyTimes, keeps track of therapist availability.
    private Double time; //Current time of the simulation.
    private Statistics statistics; //Class to keep track of the statistics.

    // Adds the specified Player to the system. Only used when taking inputs.
    public void addPlayer(int ID, int skill) { 
        Player player = new Player(ID, skill);
        players.put(ID, player);
    }
    
    //Adds the specified Event to the system. Only used when taking inputs.
    public void addEvent(int type, int playerID, double time, double duration) {
        Player player = players.get(playerID);
        Event event = new Event(type, player, time, duration); 
        events.add(event); // Adds it to the main event queue.
    }

    //Loops the simulation until the event queue is empty. Is used in the main method.
    public boolean loop() {
        return events.size() != 0 ? true : false;
    }

    //Main method to conduct the whole simulation.
    public void start() {
        Event event = events.poll(); //current event to process.
        this.time = event.getTime();
        int type = event.getType();

        if (type == 1 && event.checkBusy(statistics)) { //training event

            //If the event hasn't entered trainingQueue and there are trainers available.
            if (freeTrainers != 0 && !event.deQueue()) {
                statistics.startTraining(time);
                event.enterTrainingService();
                freeTrainers -= 1;
                events.add(event);

            //If the event exited the trainingQueue the previous loop and there is a trainer available.
            } else if (freeTrainers != 0 && event.deQueue()) {
                statistics.startTraining(time);
                event.enterTrainingService();
                freeTrainers -= 1;
                events.add(event);
            
            //If there isn't an available trainer.
            } else {
                trainingQueue.add(event);
                event.enterTrainingQueue();
                statistics.startTrainingQueueWait(time, trainingQueue.size());
            }

        } else if (type == 2) { //training to therapy event

            //If the event hasn't entered therapistQueue and there are therapists available.
            if (freeTherapists != 0 && !event.deQueue()) {
                statistics.endTraining(time);
                statistics.startTherapy(time);
                Pair<Double, Integer> p = useTherapist();
                event.trainingToTherapyService(p.getFirst(), p.getSecond());
                freeTherapists -= 1;
                freeTrainers += 1;
                events.add(event);

                //A trainer is free now, takes someone out of the waiting queue if there is one.
                if (!trainingQueue.isEmpty()) {    
                    Event deQueue = trainingQueue.poll();
                    deQueue.exitTrainingQueue(time);
                    statistics.endTrainingQueueWait(time);
                    events.add(deQueue);
                }
    
            //If the event exited the therapyQueue the previous loop and there is a therapist available.
            } else if (freeTherapists != 0 && event.deQueue()) {
                statistics.startTherapy(time);
                Pair<Double, Integer> p = useTherapist();
                event.trainingToTherapyService(p.getFirst(), p.getSecond());
                freeTherapists -= 1;
                events.add(event);

            //If there isn't an available therapist.
            } else {
                therapyQueue.add(event);
                event.enterTherapyQueue();
                statistics.endTraining(time);
                statistics.startTherapyQueueWait(time, therapyQueue.size());
                freeTrainers += 1;

                //A trainer is free now, takes someone out of the waiting queue if there is one.
                if (!trainingQueue.isEmpty()) {
                    Event deQueue = trainingQueue.poll();
                    deQueue.exitTrainingQueue(time);
                    statistics.endTrainingQueueWait(time);
                    events.add(deQueue);
                }    
            }

        } else if (type == 3) { //therapy ending event
            statistics.endTherapy(time);
            freeTherapist(event.exitTherapyService());
            freeTherapists += 1;
            event = null;
            
                //A therapist is free now, takes someone out of the waiting queue if there is one.
                if (!therapyQueue.isEmpty()) {
                Event deQueue = therapyQueue.poll();
                deQueue.exitTherapyQueue(time);
                statistics.endTherapyQueueWait(time);
                events.add(deQueue);
            }


        } else if (type == 4 && event.checkMassageBusy(statistics)) { //massage event.

            //If the event hasn't entered massageQueue and there are masseur available.
            if (freeMasseurs != 0 && !event.deQueue()) {
                statistics.startMassage(time);
                event.enterMassageService();
                freeMasseurs -= 1;
                events.add(event);

            //If the event exited the masssageQueue the previous loop and there is a masseur available.
            } else if (freeMasseurs != 0 && event.deQueue()) {
                statistics.startMassage(time);
                event.enterMassageServiceDeQueue();
                freeMasseurs -= 1;
                events.add(event);

            //If there isn't an available masseur.
            } else {
                massageQueue.add(event);
                event.enterMassageQueue();
                statistics.startMassageQueueWait(time, massageQueue.size());
            }

        } else if (type == 5) { //massage ending event.

            statistics.endMassage(time);
            event.exitMassageService();
            event = null;
            freeMasseurs += 1;

            //A masseur is free now, takes someone out of the waiting queue if there is one.
            if (!massageQueue.isEmpty()) {
                Event deQueue = massageQueue.poll();
                deQueue.exitMassageQueue(time);
                statistics.endMassageQueueWait(time);
                events.add(deQueue);
            }
        }
        statistics.recordTime(time);
    }

    //Allocates therapist. Returns a pair of values. Therapy time and therapist ID.
    private Pair<Double, Integer> useTherapist() { 
        for (int i = 0; i < therapists.length; i++) {
            if (therapists[i] == false) {
                therapists[i] = true;
                return new Pair<Double, Integer>(therapyTimes.get(i), i);
            }
        }   
        System.out.println("problem");
        return new Pair<Double, Integer>(0.0 ,0); 
    }

    //Releases the specified therapist. 
    private void freeTherapist(int therapistID) {
        therapists[therapistID] = false;
    }

    //Start of initializer functions for the ExcelFedSystem class.
    public void initialize(int numInputs) {
        events = new PriorityQueue<>(numInputs);
    }

    public void initialize(Statistics statistics) {
        this.statistics = statistics;
    }

    public void initialize(int trainers, int masseurs, int numInputs) {
        trainingQueue = new PriorityQueue<Event>(numInputs/trainers , new TrainingQueueComparator());
        massageQueue = new PriorityQueue<Event>(numInputs/masseurs, new MassageQueueComparator());
        freeMasseurs = masseurs;
        freeTrainers = trainers;
    }

    public void initialize(int numTherapists, String therapists, int numInputs) {
        therapyQueue = new PriorityQueue<Event>(numInputs/numTherapists, new TheraphyQueueComparator());
        freeTherapists = numTherapists;
        therapyTimes = new HashMap<Integer, Double>(numTherapists);
        this.therapists = new boolean[numTherapists];
        Scanner sc = new Scanner(therapists);
        for (int i=0; i<numTherapists; i++) {
            therapyTimes.put(i, sc.nextDouble());
        }
        sc.close();
    }
    //End of initializer functions for the ExcelFedSystem class.

    public HashMap<Integer, Player> getPlayers() { //Getter for players.
        return players;
    }
}

class Pair<T, U> {         
    public final T t;
    public final U u;
    public Pair(T t, U u) {        
        this.t= t;
        this.u= u;
    }
    public T getFirst() {
        return t;
    }
    public U getSecond() {
        return u;
    }
}