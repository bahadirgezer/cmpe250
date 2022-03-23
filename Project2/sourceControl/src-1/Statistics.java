import java.text.DecimalFormat;

public class Statistics {
    private double totalTrainQWait, totalMassageQWait, totalPhysioQWait;
    private double totalTrainTime, totalMassageTime, totalPhysioTime;
    private int maxTrainQ, maxMassageQ, maxPhysioQ;
    private int numTraining, numMassages, numTherapies;
    private int numTrainingQ, numMassageQ, numTherapyQ;

    private int invalidAttempts;

    public Statistics() {
        totalTrainQWait=0;
        totalMassageQWait=0; //physiotherapy
        totalPhysioQWait=0;
    
        totalTrainTime=0;
        totalMassageTime=0;
        totalPhysioTime=0;

        maxTrainQ=0;
        maxMassageQ=0;
        maxPhysioQ=0;
        
        numTraining=0; 
        numMassages=0; 
        numTherapies=0;

        numTrainingQ=0;
        numMassageQ=0; 
        numTherapyQ=0;
    
        invalidAttempts=0;
    }
    
    public String output() {
        String out = "";
        DecimalFormat df = new DecimalFormat("#.###");

        out = out + Integer.toString(maxTrainQ) + "\n";
        out = out + Integer.toString(maxPhysioQ) + "\n";
        out = out + Integer.toString(maxMassageQ) + "\n";

        out = out + df.format(totalTrainQWait/numTrainingQ) + "\n";
        out = out + df.format(totalPhysioQWait/numTherapyQ) + "\n";
        out = out + df.format(totalMassageQWait/numMassageQ) + "\n";

        out = out + df.format(totalTrainTime/numTraining) + "\n";
        out = out + df.format(totalPhysioTime/numTherapies) + "\n";
        out = out + df.format(totalMassageTime/numMassages) + "\n";

        out = out + df.format((totalPhysioTime+totalTrainTime)/numTraining) + "\n";

        return out;
    }

    public void trainingQueueChecker(int qLen) {
        numTrainingQ+=1;
        if (qLen > maxTrainQ) {
            maxTrainQ = qLen;
        }
    }
    public void physioQueueChecker(int qLen) {
        numTherapyQ+=1;
        if (qLen > maxPhysioQ) {
            maxPhysioQ = qLen;
        }
    }

    public void massageQueueChecker(int qLen) {
        numMassageQ+=1;
        if (qLen > maxMassageQ) {
            maxMassageQ = qLen;
        }
    }

    public void startTraining(double time) {
        numTraining+=1;
        totalTrainTime -= time;
    }
    
    public void endTraining(double time) {
        totalTrainTime += time;
    }

    public void startTherapy(double time) {
        numTherapies+=1;
        totalPhysioTime -= time;
    }

    public void endTherapy(double time) {
        totalPhysioTime += time;
    }

    public void startMassage(double time) {
        numMassages+=1;
        totalMassageTime -= time;
    }
    
    public void endMassage(double time) {
        totalMassageTime += time;
    }

    public void invalidAttempt() {
        invalidAttempts += 1;
    }

}

