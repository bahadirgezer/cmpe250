public class Player {
    private final int ID;
    private final int skill;
    private boolean busy;
    private int numMassages;
    private boolean threeMassages;
    private double wait;
    private double massageWait;
    private double therapyWait;

    public Player(int ID, int skill) {
        this.ID = ID;
        this.skill = skill;
        this.busy = false;
        this.numMassages = 0;
        this.threeMassages = false;
        wait = 0;
        massageWait =0;
        therapyWait=0;
    }

    //All methods below are pretty self explanatory.
    public void makeBusy() {
        busy = true;
    }

    public void makeFree() {
        busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public void enterQueue(Double time) {
        wait -= time;
    }

    public void exitQueue(Double time) {
        wait += time;
    }

    public void enterMassageQueue(Double time) {
        wait -= time;
        massageWait -= time;
    }

    public void exitMassageQueue(Double time) {
        wait += time;
        massageWait += time;
    }

    public void enterTherapyQueue(Double time) {
        wait -= time;
        therapyWait -= time;
    }

    public void exitTherapyQueue(Double time) {
        wait += time;
        therapyWait += time;
    }

    public void addMassage() {
        numMassages += 1;
        if (numMassages == 3) {
            threeMassages = true;
        }
    }

    public boolean hasThreeMassages() {
        return threeMassages;
    }

    public double getMassageWait() {
        return massageWait;
    }

    public double getTotalWait() {
        return wait;
    }

    public double getTherapyWait() {
        return therapyWait;
    }

    public int getID() {
        return ID;
    }
    
    public int getSkill() {
        return skill;
    }

    public String toString() {
        String out = "";
        out = out + "Player ID: "+ ID + ", ";
        out = out + "isBusy? "+ busy + "\n";
        return out;
    }
}
