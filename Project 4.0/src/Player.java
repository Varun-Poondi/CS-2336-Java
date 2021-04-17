import java.util.Arrays;
import java.util.Hashtable;

public class Player implements Comparable<Player>{
    private final String name;
    private final String team;
    private final double[] stats = new double[9];
    private double plateApps;
    public Player(String name, String team) {
        this.name = name;
        this.team = team;
        plateApps = 0;
    }

    public String getTeam() {
        return team;
    }

    public String getName() {
        return name;
    }

    public double[] getStats() {
        return stats;
    }
    public void incrementStats(int index){
        getStats()[index] += 1;
    }

    public void incrementPlateApps() {
        this.plateApps += 1;
    }

    public void decipherCode(String code, Hashtable<String, String> keyTable){
        String codeResult = keyTable.get(code);
        switch (codeResult) {
            case "HITS":
                incrementStats(1);
                incrementStats(0);
                incrementPlateApps();
                break;
            case "STRIKEOUT":
                incrementStats(3);
                incrementStats(0);
                incrementPlateApps();
                break;
            case "OUTS":
                incrementStats(8);
                incrementStats(0);
                incrementPlateApps();
                break;
            case "WALK":
                incrementStats(2);
                incrementPlateApps();
                break;
            case "HITBYPITCH":
                incrementStats(4);
                incrementPlateApps();
                break;
            case "SACRIFICE":
                incrementStats(5);
                incrementPlateApps();
                break;
            case "ERRORS":
                incrementPlateApps();
                break;
        }
        double battingAverage = (stats[1])/(stats[0]);
        double onBasePercentage = (stats[1] + stats[2] + stats[4])/(plateApps);

        boolean baIsNan = Double.isNaN(battingAverage);
        boolean obIsNan = Double.isNaN(onBasePercentage);
        
        if(baIsNan){
            battingAverage = 0.000;
        }
        if(obIsNan){
            onBasePercentage = 0.000;
        }
        stats[6] = battingAverage;
        stats[7] = onBasePercentage;
    }

    @Override
    public String toString() {
        String stat6 = String.format("%.3f", (float) stats[6]);
        String stat7 = String.format("%.3f", (float) stats[7]);
        return name + "\t" + Math.round(stats[0]) + "\t" + Math.round(stats[1]) + "\t" + Math.round(stats[2]) + "\t" + Math.round(stats[3]) + "\t" +
                Math.round(stats[4]) + "\t" + Math.round(stats[5]) + "\t" + stat6 + "\t" +stat7; 
    }

    @Override
    public int compareTo(Player o) {
        return this.getName().compareTo(o.getName());
    }
}
