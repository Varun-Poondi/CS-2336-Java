public class Node {
   
    private Node next;
    private Player player;

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

    public void displayStats() {
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
