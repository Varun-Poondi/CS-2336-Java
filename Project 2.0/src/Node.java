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
        double outs = 0;
        double strikeouts = 0;
        double hitByPitch = 0;
        double sacrifices = 0;
        double atBats = 0;
        double plateApps = 0;
        //parse through batting record
        for(int i = 0; i < battingRecord.length(); i++){
            switch(battingRecord.charAt(i)){
                case 'H':
                    hits++;
                    atBats++;
                    plateApps++;
                    break;
                case 'K':
                    strikeouts ++;
                    atBats++;
                    plateApps++;
                    break;
                case 'O':
                    outs++;
                    atBats++;
                    plateApps++;
                    break;
                case 'W':
                    walks ++;
                    plateApps++;
                    break;
                case 'P':
                    hitByPitch++;
                    plateApps++;
                    break;
                case 'S':
                    sacrifices++;
                    plateApps++;
                    break;
                default:
                    break;
            }
        }
        double battingAverage = (hits)/(atBats);
        double onBasePercentage = (hits + walks + hitByPitch)/(plateApps);

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
        player.getStats()[0] = atBats;
        player.getStats()[1] = hits;
        player.getStats()[2] = walks;
        player.getStats()[3] = strikeouts;
        player.getStats()[4] = hitByPitch;
        player.getStats()[5] = sacrifices;
        player.getStats()[6] = battingAverage;
        player.getStats()[7] = onBasePercentage;
        player.getStats()[8] = outs;
    }
    static class Player {
        private final String name;
        private final double[] stats = new double[9];

        public Player(String name) {
            this.name = name;
        }
        public String getName() {
            return name;
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
            double BA = getStats()[6];
            double OB = getStats()[7];

            //convert to correct precision
            String strBA = String.format("%.3f", (float) BA);
            String strOB = String.format("%.3f", (float) OB);
            System.out.print(strBA + "\t" + strOB + "\n");
        }
    }
}

