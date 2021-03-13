public class Node {
    private Node next; //next pointer
    private Player player; //contains a Player as it's data

    public Node(Player player) {
        this.next = null;
        this.player = player;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void parseBattingRecord(Player player, String battingRecord){ //parses a player's batting average and stores it into their stat's array
        double hits = 0;
        double walks = 0;
        double strikeouts = 0;
        double hitByPitch = 0;
        double atBat = 0;
        double sacrifices = 0;
        double plateAppearances = 0;
        //parse through batting record
        for(int i = 0; i < battingRecord.length(); i++){
            switch(battingRecord.charAt(i)){
                case 'H':
                    hits++;
                    atBat ++;
                    plateAppearances++;
                    break;
                case 'K':
                    strikeouts ++;
                    atBat ++;
                    plateAppearances++;
                    break;
                case 'O':
                    atBat++;
                    plateAppearances++;
                    break;
                case 'W':
                    walks ++;
                    plateAppearances++;
                    break;
                case 'P':
                    hitByPitch++;
                    plateAppearances++;
                    break;
                case 'S':
                    sacrifices++;
                    plateAppearances++;
                    break;
                default:
                    break;
            }
        }
        double battingAverage = hits/atBat;
        double onBasePercentage = (hits + walks + hitByPitch)/plateAppearances;

        //isNan is going to be used to detect if im dividing by 0, If that is the case for any of the Double values, set those values to 0.000
        boolean baIsNan = Double.isNaN(battingAverage);
        boolean obIsNan = Double.isNaN(onBasePercentage);

        if(baIsNan){
            battingAverage = 0.000;
        }
        if(obIsNan){
            onBasePercentage = 0.000;
        }
        //add the stat numbers to each individual player stat array[]
        player.getStats()[0] = atBat;
        player.getStats()[1] = hits;
        player.getStats()[2] = walks;
        player.getStats()[3] = strikeouts;
        player.getStats()[4] = hitByPitch;
        player.getStats()[5] = sacrifices;
        player.getStats()[6] = battingAverage;
        player.getStats()[7] = onBasePercentage;
    }
}
class Player {
    private String name;
    private String battingRecord;
    private final double[] stats = new double[8];

    public Player(String name, String battingRecord) {
        this.name = name;
        this.battingRecord = battingRecord;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBattingRecord() {
        return battingRecord;
    }

    public void setBattingRecord(String battingRecord) {
        this.battingRecord = battingRecord;
    }

    public double[] getStats() {
        return stats;
    }
    public void setStats(int index, double stats){
        getStats()[index] = stats;
    }

    public void displayStats() {  //displays the stats in the player's stats array 
        System.out.print(getName() + "\t"
                + Math.round(getStats()[0]) + "\t"
                + Math.round(getStats()[1]) + "\t"
                + Math.round(getStats()[2]) + "\t"
                + Math.round(getStats()[3]) + "\t"
                + Math.round(getStats()[4]) + "\t"
                + Math.round(getStats()[5]) + "\t");
        double BA = stats[6];
        double OB = stats[7];

        //convert to correct precision
        String strBA = String.format("%.3f", (float) BA);
        String strOB = String.format("%.3f", (float) OB);
        System.out.print(strBA + "\t" + strOB + "\n");
    }
}
