import java.util.Comparator;

public class Event implements Comparable<Event> {
    private Player player;
    private double time;
    private double duration;
    private int type; 
    private int therapistID;
    private boolean inQueue, deQueue;

    public Event(int type, Player player, double time, double duration) { 
        this.type = type;
        this.player = player;
        this.time = time;
        this.duration = duration;
        this.inQueue = false;
        this.deQueue = false;
    }

    public boolean checkBusy(Statistics statistics) {
        if (!player.isBusy() || inQueue) {
            return true;
        } else {
            statistics.cancelledAttempt();
            return false;
        }
    }

    public boolean checkMassageBusy(Statistics statistics) {
        if (player.hasThreeMassages()){
            statistics.invalidAttempt();
            return false;
        } else {
            if (!player.isBusy() || inQueue) {
                return true;
            } else {
                statistics.cancelledAttempt();
                return false;
            }
        }
    }

    public void enterTrainingService() {
        player.makeBusy();
        time += duration;
        type += 1;
        deQueue = false;
    }   
    
    public void enterTrainingQueue() {
        player.makeBusy();
        player.enterQueue(time);
    }

    public void exitTrainingQueue(Double time) {
        this.time = time;
        player.exitQueue(time);
        inQueue = true;
        deQueue = true;
    }

    public void trainingToTherapyService(Double duration, int therapistID) {
        time += duration;
        type += 1;
        this.duration = duration;
        this.therapistID = therapistID;
        deQueue = false;
    }

    public void enterTherapyQueue() {
        player.enterQueue(time);
    }

    public void exitTherapyQueue(Double time) {
        this.time = time;
        player.exitQueue(time);
        deQueue = true;
    }

    public int exitTherapyService() {
        player.makeFree();
        return therapistID;
    }



    public void enterMassageService() {
        player.makeBusy();
        time += duration;
        type += 1;
        deQueue = false;
    }   
    
    public void enterMassageQueue() {
        player.makeBusy();
        player.enterMassageQueue(time);
    }

    public void exitMassageQueue(Double time) {
        this.time = time;
        player.exitMassageQueue(time);
        inQueue = true;
        deQueue = true;
    }

    public void exitMassageService() {
        player.addMassage();
        player.makeFree();
    }

    public boolean deQueue() {
        return deQueue;
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
