public class Player {
    private final int ID;
    private final int skill;
    private boolean busy;
    private int numMassages;
    private boolean threeMassages;
    private double wait;
    private double massageWait;

    public Player(int ID, int skill) {
        this.ID = ID;
        this.skill = skill;
        this.busy = false;
        this.numMassages = 0;
        this.threeMassages = false;
        wait = 0;
        massageWait =0;
    }

    public void makeBusy() {
        busy = true;
    }

    public void makeFree() {
        busy = false;
    }

    public boolean isBusy() {
        return busy;
    }

    public int getID() {
        return ID;
    }
    
    public int getSkill() {
        return skill;
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

    public void addMassage() {
        numMassages += 1;
        if (numMassages == 3) {
            threeMassages = true;
        }
    }

    public boolean hasThreeMassages() {
        return threeMassages;
    }

    public String toString() {
        String out = "";
        out = out + "Player ID: "+ ID + ", ";
        out = out + "isBusy? "+ busy + "\n";
        return out;
    }
}
