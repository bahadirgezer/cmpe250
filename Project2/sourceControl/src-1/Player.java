public class Player {
    private final int ID;
    private final int skill;
    private boolean busy;
    private int numMassages;

    public Player(int ID, int skill) {
        this.ID = ID;
        this.skill = skill;
        this.busy = false;
        this.numMassages = 0;

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

    public boolean getMassage(Statistics statistics) {
        if (numMassages < 3) {
            numMassages += 1;
            return true;
        } else {
            statistics.invalidAttempt();
            return false;
        }
    }
}
