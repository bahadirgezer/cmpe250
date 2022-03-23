import java.util.PriorityQueue;
import java.util.Scanner;
import java.io.PrintStream;
import java.util.HashMap;


public class ExcelFedSystem {
    private PriorityQueue<Event> events;
    private HashMap<Integer, Player> players = new HashMap<Integer, Player>();

    private PriorityQueue<Event> trainingQueue,therapyQueue, massageQueue;
    
    private Statistics statistics;
    private HashMap<Integer, Double> therapyTimes;
    private boolean[] therapists;
    private int freeTrainers, freeMasseurs, freeTherapists;
    private Double time;

    public void addPlayer(int ID, int skill) {
        Player player = new Player(ID, skill);
        players.put(ID, player);
    }
    
    public void addEvent(int type, int playerID, double time, double duration) {
        Player player = players.get(playerID);
        Event event = new Event(type, player, time, duration); 
        events.add(event);
    }

    
    public boolean loop() {
        return events.size() != 0 ? true : false;
    }


    public void start(PrintStream logOut) {
        Event event = events.poll();
        this.time = event.getTime();
        int type = event.getType();
        logOut.println("["+time+"]");

        if (type == 1 && event.checkBusy(statistics, logOut)) {

            if (freeTrainers != 0 && !event.deQueue()) {
                logOut.print("Training Event Start - ");
                logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());


                statistics.startTraining(time);
                event.enterTrainingService();
                freeTrainers -= 1;
                events.add(event);
            } else if (freeTrainers != 0 && event.deQueue()) {
                logOut.print("Training Event Start After Queue - ");
                logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());


                statistics.startTraining(time);
                event.enterTrainingService();
                freeTrainers -= 1;
                events.add(event);
            } else {
                logOut.print("Training Event Queue - ");
                logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());

                trainingQueue.add(event);
                event.enterTrainingQueue();
                statistics.startTrainingQueueWait(time, trainingQueue.size());
            }

        } else if (type == 2) {

            if (freeTherapists != 0 && !event.deQueue()) {
                logOut.println("Training Event End - ");
                logOut.print("TherapyEvent Start - ");


                statistics.endTraining(time);
                statistics.startTherapy(time);
                Pair<Double, Integer> p = useTherapist();
                event.trainingToTherapyService(p.getFirst(), p.getSecond());
                freeTherapists -= 1;
                freeTrainers += 1;
                events.add(event);
                logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());

                if (!trainingQueue.isEmpty()) {
                    logOut.print("Training Event DeQueue - ");
    
                    Event deQueue = trainingQueue.poll();
                    logOut.println("player id: " + deQueue.getPlayer().getID() + ", duration: "+ deQueue.getDuration());
    
                    deQueue.exitTrainingQueue(time);
                    statistics.endTrainingQueueWait(time);
                    events.add(deQueue);
                }
    
            } else if (freeTherapists != 0 && event.deQueue()) {
                logOut.print("Therapy Event Start After Queue - ");


                statistics.startTherapy(time);
                Pair<Double, Integer> p = useTherapist();
                event.trainingToTherapyService(p.getFirst(), p.getSecond());
                freeTherapists -= 1;
                events.add(event);
                logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());

            } else {
                logOut.println("Training Event End - ");
                logOut.print("Therapy Event Queue - ");


                therapyQueue.add(event);
                event.enterTherapyQueue();
                statistics.endTraining(time);
                statistics.startTherapyQueueWait(time, therapyQueue.size());
                freeTrainers += 1;
                logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());

                if (!trainingQueue.isEmpty()) {
                    logOut.print("Training Event DeQueue - ");
    
                    Event deQueue = trainingQueue.poll();
                    logOut.println("player id: " + deQueue.getPlayer().getID() + ", duration: "+ deQueue.getDuration());
    
                    deQueue.exitTrainingQueue(time);
                    statistics.endTrainingQueueWait(time);
                    events.add(deQueue);
                }
    
            }


        } else if (type == 3) {

            logOut.print("Therapy Event End - ");
            logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());

            statistics.endTherapy(time);
            freeTherapist(event.exitTherapyService());
            freeTherapists += 1;
            event = null;
            
            if (!therapyQueue.isEmpty()) {
                logOut.print("Therapy Event DeQueue - ");

                Event deQueue = therapyQueue.poll();
                logOut.println("player id: " + deQueue.getPlayer().getID() + ", duration: "+ deQueue.getDuration());

                deQueue.exitTherapyQueue(time);
                statistics.endTherapyQueueWait(time);
                events.add(deQueue);
            }


        } else if (type == 4 && event.checkMassageBusy(statistics, logOut)) {

            if (freeMasseurs != 0 && !event.deQueue()) {
                logOut.print("Massage Event Start - ");
                logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());


                statistics.startMassage(time);
                event.enterMassageService();
                freeMasseurs -= 1;
                events.add(event);
            } else if (freeMasseurs != 0 && event.deQueue()) {
                logOut.print("Massage Event Start After DeQueue - ");
                logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());

                statistics.startMassage(time);
                event.enterMassageServiceDeQueue();
                freeMasseurs -= 1;
                events.add(event);
            } else {
                logOut.print("Massage Event Queue - ");
                logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());

                massageQueue.add(event);
                event.enterMassageQueue();
                statistics.startMassageQueueWait(time, massageQueue.size());
            }

        } else if (type == 5) {

            logOut.print("Massage Event End - ");
            logOut.println("player id: " + event.getPlayer().getID() + ", duration: "+ event.getDuration());

            statistics.endMassage(time);
            event.exitMassageService();
            event = null;
            freeMasseurs += 1;

            if (!massageQueue.isEmpty()) {
                logOut.print("Massage Event DeQueue - ");

                Event deQueue = massageQueue.poll();
                logOut.println("player id: " + deQueue.getPlayer().getID() + ", duration: "+ deQueue.getDuration());

                deQueue.exitMassageQueue(time);
                statistics.endMassageQueueWait(time);
                events.add(deQueue);
            }
        }
        statistics.recordTime(time);
    }

   public void start2() {

   }


    public Pair<Double, Integer> useTherapist() {
        for (int i = 0; i < therapists.length; i++) {
            if (therapists[i] == false) {
                therapists[i] = true;
                return new Pair<Double, Integer>(therapyTimes.get(i), i);
            }
        }   
        System.out.println("problem");
        return new Pair<Double, Integer>(0.0 ,0); 
    }
    public void freeTherapist(int therapistID) {
        therapists[therapistID] = false;
    }

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

    public HashMap<Integer, Player> getPlayers() {
        return players;
    }




    public void consoleDebuggerBefore() {
        System.out.println("============================================");

        System.out.println("Events PriorityQueue: ");
        for (Event event : events) {
            System.out.println("\t"+event);
        }

    }
    public void consoleDebuggerAfter() {
        System.out.println("\nCurrent Time: "+time);

        System.out.println("Free Trainers: "+freeTrainers);
        System.out.println("Free Therapists: "+freeTherapists);
        System.out.println("Free Masseurs: "+freeMasseurs);

        System.out.println("Phystiotherapists: ");
        for (boolean a : therapists) {
            System.out.println("\t"+a);
        }

        System.out.println("Training PriorityQueue: ");
        for (Event event : trainingQueue) {
            System.out.println("\t"+event);
        }
        System.out.println("Therapy PriorityQueue: ");
        for (Event event : therapyQueue) {
            System.out.println("\t"+event);
        }

        System.out.println("Massage PriorityQueue: ");
        for (Event event : massageQueue) {
            System.out.println("\t"+event);
        }
        System.out.println("============================================");

    }
    public void consoleDebuggerCheckPlayers() {
        for (Player player : players.values()){
            System.out.println(player);
        }
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