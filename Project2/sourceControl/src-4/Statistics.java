import java.text.DecimalFormat;
import java.util.HashMap;

public class Statistics {
    private double totalTrainQWait, totalMassageQWait, totalTherapyQWait;
    private double totalTrainTime, totalMassageTime, totalTherapyTime;
    private int maxTrainQ, maxMassageQ, maxTherapyQ;
    private int numTraining, numMassages, numTherapies;
    private String _1, _2, _3, _4, _5, _6, _7, _8, _9, _10, _11, _12, _13, _14, _15;
    private double lastTime;
    private int invalidAttempts, cancelledAttempts;

    public Statistics() {
        totalTrainQWait=0;
        totalMassageQWait=0;
        totalTherapyQWait=0;
    
        totalTrainTime=0;
        totalMassageTime=0;
        totalTherapyTime=0;

        maxTrainQ=0;
        maxMassageQ=0;
        maxTherapyQ=0;
        
        numTraining=0; 
        numMassages=0; 
        numTherapies=0;

        invalidAttempts=0;
        cancelledAttempts=0;
    }

    private void generate(ExcelFedSystem system) { //Readies the statistics for outputting.
        DecimalFormat df = new DecimalFormat("0.000");

        _1 = Integer.toString(maxTrainQ);
        _2 = Integer.toString(maxTherapyQ);
        _3 = Integer.toString(maxMassageQ);

        if (totalTrainQWait == 0) {
            _4 = "0.000";
        } else {
            _4 = df.format(totalTrainQWait/numTraining);
        }

        if (totalTherapyQWait == 0) {
            _5 = "0.000";
        } else {
            _5 = df.format(totalTherapyQWait/numTherapies);
        }

        if (totalMassageQWait == 0) {
            _6 = "0.000";
        } else { 
            _6 = df.format(totalMassageQWait/numMassages);
        }        

        if (totalTrainTime == 0) {
            _7 = "0.000";
        } else {
            _7 = df.format(totalTrainTime/numTraining);
        }

        if (totalTherapyTime == 0) {
            _8 = "0.000";
        } else {
            _8 = df.format(totalTherapyTime/numTherapies);
        }

        if (totalMassageTime == 0) {
            _9 = "0.000";
        } else { 
            _9 = df.format(totalMassageTime/numMassages);
        }

        if (numTraining == 0) {
            _10 = "0.000";
        } else {
            Double totalTime = totalTrainQWait + totalTherapyQWait + totalTrainTime + totalTherapyTime;
            _10 = df.format(totalTime/numTraining);
        }

        HashMap<Integer, Player> players = system.getPlayers();
        Double _11Wait=0.0;
        Integer _11ID=0;
        Double currTherapy = 0.0; 
        Boolean isAllocated = false;
        for (Player player : players.values()){
            
            if (currTherapy < player.getTherapyWait()) {
                isAllocated = true;
                currTherapy = player.getTherapyWait();
                _11ID = player.getID();       
            } else if (currTherapy == player.getTherapyWait()) {
                isAllocated = true;
                if (player.getID() < _11ID) {
                    _11ID = player.getID();
                }
            }
        }
        if (!isAllocated) {
            _11ID = 0;
            _11Wait = 0.0;
        } else {
            _11Wait = players.get(_11ID).getTherapyWait();
        }
        _11=Integer.toString(_11ID) + " " + df.format(_11Wait);

        Double _12Wait;
        Integer _12ID = 0;
        Double currMassage = Double.MAX_VALUE;
        isAllocated = false;
        for (Player player : players.values()) {
            if (player.hasThreeMassages()) {
                if (currMassage > player.getMassageWait()) {
                    isAllocated = true;
                    currMassage = player.getMassageWait();
                    _12ID = player.getID();
                } else if (currMassage == player.getMassageWait()) {
                    isAllocated = true;
                    if (player.getID() < _12ID) {
                        _12ID = player.getID();
                    }
                }
            }
        }
        if (!isAllocated) {
            _12ID = -1;
            _12Wait = -1.0;
        } else  {
            _12Wait = players.get(_12ID).getMassageWait();
        }
        _12 = Integer.toString(_12ID) + " " + df.format(_12Wait);

        _13 = Integer.toString(invalidAttempts);
        _14 = Integer.toString(cancelledAttempts);
        _15 = df.format(lastTime);
    }

    public String output(ExcelFedSystem system) {
        this.generate(system);

        String out = "";
        out = out + _1  + "\n";
        out = out + _2  + "\n";
        out = out + _3  + "\n";
        out = out + _4  + "\n";
        out = out + _5  + "\n";
        out = out + _6  + "\n";
        out = out + _7  + "\n";
        out = out + _8  + "\n";
        out = out + _9  + "\n";
        out = out + _10 + "\n";
        out = out + _11 + "\n";
        out = out + _12 + "\n";
        out = out + _13 + "\n";
        out = out + _14 + "\n";
        out = out + _15 + "\n";
        return out;
    }    

    public void recordTime(double time) {
        lastTime = time;
    }
    
    //event times are added by subtracting the entry time and adding the exit time 
    //from the cumulative total time.
    public void startTrainingQueueWait(double time, int qLen) {
        totalTrainQWait -= time; 
        trainingQueueChecker(qLen);
    }

    private void trainingQueueChecker(int qLen) {
        if (qLen > maxTrainQ) {
            maxTrainQ = qLen;
        }
    }

    public void endTrainingQueueWait(double time) {
        totalTrainQWait += time;
    }

    public void startTraining(double time) {
        numTraining+=1;
        totalTrainTime -= time;
    }

    public void endTraining(double time) {
        totalTrainTime += time;
    }    
    
    public void startTherapyQueueWait(double time, int qLen) {
        totalTherapyQWait -= time;
        therapyQueueChecker(qLen);

    }

    private void therapyQueueChecker(int qLen) {
        if (qLen > maxTherapyQ) {
            maxTherapyQ = qLen;
        }
    }

    public void endTherapyQueueWait(double time) {
        totalTherapyQWait += time;
    }

    public void startTherapy(double time) {
        numTherapies+=1;
        totalTherapyTime -= time;
    }

    public void endTherapy(double time) {
        totalTherapyTime += time;
    }

    public void startMassageQueueWait(double time, int qLen) {
        totalMassageQWait -= time;
        massageQueueChecker(qLen);

    }

    private void massageQueueChecker(int qLen) {
        if (qLen > maxMassageQ) {
            maxMassageQ = qLen;
        }
    }

    public void endMassageQueueWait(double time) {
        totalMassageQWait += time;
    }

    public void startMassage(double time) {
        numMassages+=1;
        totalMassageTime -= time;
    }

    public void endMassage(double time) {
        totalMassageTime += time;
    }

    public void cancelledAttempt() {
        cancelledAttempts += 1;
    }

    public void invalidAttempt() {
        invalidAttempts += 1;
    }
}
