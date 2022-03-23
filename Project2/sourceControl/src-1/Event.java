import java.util.Comparator;

public abstract class Event implements Comparable<Event> {
    private Player player;
    private double time;
    private double duration;
    private int type; 
    //1=training or trainging queue entry
    //2=training
    //3=physiotherapy queue
    //4=physiotherapy
    //5=massage queue
    //6=massage


    public Event(int type, Player player, double time, double duration) { 
        this.type = type;
        this.player = player;
        this.time = time;
        this.duration = duration;
        

    }

    public void incrementType() {
        type+=1;
    }
    
    public void incrementTime(double increment) {
        time += increment;
    }

    public int getType() {
        return type;
    }

    public Player getPlayer() {
        return player;
    }
    
    public double getTime() {
        return time;
    }

    public double getDuration() {
        return duration;
    }


    public static void massageEntry() {

    }
    
    public static void massageExit() {

    }
    
    ///////////////////////////////////////////////////////

    public static void trainingStart() {

    }   
    
    public static void trainingExit() {

    }

    ///////////////////////////////////////////////////////
    
    public void physioStart() {

    }

    public void physioEnd() {

    }

    ///////////////////////////////////////////////////////
    
    public void enterMassageQueue() {

    }

    public void exitMassageQueue() {

    }

    ///////////////////////////////////////////////////////

    public void enterTrainingQueue() {

    }
    
    public void exitTrainingQueue() {

    }

    ///////////////////////////////////////////////////////

    public void enterPhysioQueue() {

    }

    public void exitPhysioQueue() {

    }

    @Override
    public int compareTo(Event o) {
        return this.time > o.time ? 1: -1;
    }

    public String toString() {
        String out = "";
        out += "Time: "+ time +", ";
        out += "ID: "+ this.getPlayer().getID()+", ";
        out += "Type: "+ type +", ";
        out += "Duration: "+ duration +", ";

        return out;
    }

}

class TrainingQueueComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        if (o1.getTime() > o2.getTime()) {
            return 1;
        } else if (o1.getTime() < o2.getTime()) {
            return -1;
        } else {
            return o1.getPlayer().getID() > o2.getPlayer().getID() ? 1 : -1;
        }

    }
}

class TheraphyQueueComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
        if (o1.getDuration() > o2.getDuration()) {
            return -1;
        } else if (o1.getDuration() < o2.getDuration()) {
            return 1;
        } else {
            if (o1.getTime() > o2.getTime()) {
                return 1;
            } else if (o1.getTime() < o2.getTime()) {
                return -1;
            } else {
                return o1.getPlayer().getID() > o2.getPlayer().getID() ? 1 : -1;
            }
        }
    }
}

class MassageQueueComparator implements Comparator<Event> {

    @Override
    public int compare(Event o1, Event o2) {
       if (o1.getPlayer().getSkill() > o2.getPlayer().getSkill()) {
            return -1;
       } else if (o1.getPlayer().getSkill() < o2.getPlayer().getSkill()) {
            return 1;
       } else {
           if (o1.getTime() > o2.getTime()) {
                return 1;
            } else if (o1.getTime() < o2.getTime()) {
                return -1;
            } else {
                return o1.getPlayer().getID() > o2.getPlayer().getID() ? 1 : -1;
            }
       }
    }
}
