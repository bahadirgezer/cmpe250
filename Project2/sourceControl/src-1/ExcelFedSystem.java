import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExcelFedSystem {
    private PriorityQueue<Event> events;
    private HashMap<Integer, Player> players = new HashMap<Integer, Player>();

    private PriorityQueue<Event> training;
    private PriorityQueue<Event> massage;
    private PriorityQueue<Event> physio;
    
    private Statistics statistics;
    private PriorityQueue<Physiotherapist> emptyTherapists;
    private HashMap<Integer, Physiotherapist> busyTherapists;
    private List<Double> therapistDurations;


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


    public void start() {
        Event event = events.poll();
        Player player = event.getPlayer();
        int type = event.getType();
        this.time = event.getTime();

        if (type == 1) {
            player.makeBusy();
            if (freeTrainers != 0) {
                event.incrementType();
                event.incrementTime(event.getDuration());
                statistics.startTraining(time);
                events.add(event);
                freeTrainers -= 1;

            } else {
                training.add(event);
                statistics.trainingQueueChecker(training.size());
            }

        } else if (type == 2) {
            statistics.endTraining(time);
            freeTrainers +=1;

            if (training.peek() != null) {
                Event trainingEvent = training.poll();
                trainingEvent.incrementType();
                trainingEvent.incrementTime(trainingEvent.getDuration());
                statistics.startTraining(time);
                events.add(event);

                freeTrainers -= 1;
            }
            
            if (freeTherapists != 0) {
                double duration = allocateTherapy(player);
                event.incrementType();
                event.incrementTime(duration);
                statistics.startTherapy(time);
                events.add(event);

                freeTherapists -= 1;
            } else {
                physio.add(event);
                statistics.physioQueueChecker(physio.size());
            }

        } else if (type == 3) {
            statistics.endTherapy(time);

            Physiotherapist therapist = busyTherapists.get(player.getID());
            emptyTherapists.add(therapist);
            busyTherapists.remove(player.getID());
            event = null;
            freeTherapists += 1;
            player.makeFree();

            if (physio.peek() != null) {
                Event therapyEvent = physio.poll();
                Player therapyPlayer = therapyEvent.getPlayer();
                double duration = allocateTherapy(therapyPlayer);
                therapyEvent.incrementType();
                therapyEvent.incrementTime(duration);
                statistics.startTherapy(time);
                events.add(event);
                
                freeTherapists -= 1;
            }

        } else if (type == 4) {
            if (player.getMassage(statistics)){
                player.makeBusy();
                if (freeMasseurs != 0) {
                    event.incrementType();
                    event.incrementTime(event.getDuration());
                    statistics.startMassage(time);
                    events.add(event);
    
                    freeMasseurs -= 1;
    
                } else {
                    massage.add(event);
                    statistics.massageQueueChecker(massage.size());
                }
            }

        } else if (type == 5) {
            statistics.endMassage(time);
            freeMasseurs += 1;
            event = null;
            player.makeFree();

            if (massage.peek() != null) {
                Event massageEvent = training.poll();
                massageEvent.incrementType();
                massageEvent.incrementTime(massageEvent.getDuration());
                statistics.startMassage(time);
                events.add(event);

                freeMasseurs -= 1;
            }
        }
    }

    // private double allocateTherapy(Player player) {
    //     Physiotherapist therapist = emptyTherapists.poll();
    //     double duration = therapist.getDuration();
    //     busyTherapists.put(player.getID(), therapist);

    //     return duration;
    // }

    public boolean loop() {
        return events.size() != 0 ? true : false;
    }

    public void initialize(int numInputs) {
        events = new PriorityQueue<>(numInputs);
    }

    public void initialize(Statistics statistics) {
        this.statistics = statistics;
    }

    public void initialize(int trainers, int masseurs, int numInputs) {
        training = new PriorityQueue<Event>(numInputs/trainers , new TrainingQueueComparator());
        massage = new PriorityQueue<Event>(numInputs/masseurs, new MassageQueueComparator());
        freeMasseurs = masseurs;
        freeTrainers = trainers;
    }

    public void initialize(int numTherapists, String therapists, int numInputs) {
        physio = new PriorityQueue<Event>(numInputs/numTherapists, new TheraphyQueueComparator());
        freeTherapists = numTherapists;
        therapistDurations = new ArrayList<Double>();
        for (String s : therapists.split("\\s")) {  
        therapistDurations.add(Double.parseDouble(s));  
        }
        therapistDurations.toArray();









        // emptyTherapists = new PriorityQueue<Physiotherapist>(numTherapists);
        // busyTherapists = new HashMap<Integer, Physiotherapist>(numTherapists);
        // String[] tokens = therapists.split(" ");
        
        // for (int id=0; id<tokens.length; id++) {
        //     Physiotherapist therapist = new Physiotherapist(id, Integer.parseInt(tokens[id]));
        //     emptyTherapists.add(therapist);
        // }
    }

    public void consoleDebuggerAfter() {
        System.out.println("\nCurrent Time: "+time);

        System.out.println("Free Trainers: "+freeTrainers);
        System.out.println("Free Therapists: "+freeTherapists);
        System.out.println("Free Masseurs: "+freeMasseurs);
    
        System.out.println("Training PriorityQueue: ");
        for (Event event : training) {
            System.out.println(event);
        }
        System.out.println("Therapy PriorityQueue: ");
        for (Event event : physio) {
            System.out.println(event);
        }

        for (Physiotherapist therapist : emptyTherapists) {
            System.out.println(therapist);
        }

        System.out.println("Massage PriorityQueue: ");
        for (Event event : massage) {
            System.out.println(event);
        }
        System.out.println("============================================");

    }
    public void consoleDebuggerBefore() {
        System.out.println("============================================");

        System.out.println("Events PriorityQueue: ");
        for (Event event : events) {
            System.out.println(event);
        }

    }
}
