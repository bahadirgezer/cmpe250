import java.io.PrintStream;
import java.util.Comparator;

public class Event implements Comparable<Event> {
    private Player player;
    private double time;
    private double duration;
    private int type; 
    private int therapistID;
    private boolean inQueue;

    public Event(int type, Player player, double time, double duration) { 
        this.type = type;
        this.player = player;
        this.time = time;
        this.duration = duration;
        this.inQueue = false;
    }

    public boolean checkBusy(Statistics statistics, PrintStream logOut) {
        if (!player.isBusy() || inQueue) {
            return true;
        } else {
            statistics.cancelledAttempt();
            logOut.println("Training Attempt Cancelled - ");
            return false;
        }
    }

    public boolean checkMassageBusy(Statistics statistics, PrintStream logOut) {
        if (player.hasThreeMassages() && !inQueue){
            statistics.invalidAttempt();
            logOut.println("Massage Attempt Invalid - ");
            return false;
        } else {
            if (!player.isBusy() || inQueue) {
                return true;
            } else {
                statistics.cancelledAttempt();
                logOut.println("Massage Attempt Cancelled - ");
                return false;
            }
        }
    }

    public void enterTrainingService() {
        player.makeBusy();
        time += duration;
        type += 1;
        inQueue = false;
    }   
    
    public void enterTrainingQueue() {
        player.makeBusy();
        player.enterQueue(time);
    }

    public void exitTrainingQueue(Double time) {
        this.time = time;
        player.exitQueue(time);
        inQueue = true;
    }

    public void trainingToTherapyService(Double duration, int therapistID) {
        time += duration;
        type += 1;
        this.duration = duration;
        this.therapistID = therapistID;
        inQueue = false;
    }

    public void enterTherapyQueue() {
        player.enterTherapyQueue(time);
    }

    public void exitTherapyQueue(Double time) {
        this.time = time;
        player.exitTherapyQueue(time);
        inQueue = true;
    }

    public int exitTherapyService() {
        player.makeFree();
        return therapistID;
    }



    public void enterMassageService() {
        player.makeBusy();
        player.addMassage();
        time += duration;
        type += 1;
        inQueue = false;
    }   

    public void enterMassageServiceDeQueue() {
        player.makeBusy();
        time += duration;
        type += 1;
        inQueue = false;
    } 
    
    public void enterMassageQueue() {
        player.makeBusy();
        player.addMassage();
        player.enterMassageQueue(time); //if in  masssage then massage entry problem
    }

    public void exitMassageQueue(Double time) {
        this.time = time;
        player.exitMassageQueue(time);
        inQueue = true;
    }

    public void exitMassageService() {
        player.makeFree();
    }

    public boolean deQueue() {
        return inQueue;
    }





    
    public void setTime(double time) {
        this.time = time;
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

    @Override
    public int compareTo(Event o) {
        if (this.time > o.time) {
            return 1;
        } else if (this.time < o.time) {
            return -1;
        } else {
            return this.getPlayer().getID() > o.getPlayer().getID() ? 1 : -1;
        }
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
