/*
 * NAME: Varun Poondi
 * NET ID: VMP190003
 * PROF: Jason Smith
 * CLASS: CS 2336.001
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    private static class Player{ //Player class for individual player that is recorded from the txt file
        String name;
        String battingRecord;
        double [] stats = new double[6]; //All have 6 stats

        public Player(String name, String battingRecord) {
            this.name = name;
            this.battingRecord = battingRecord;
        }
        public void displayPlayerData(String name, double[] stats){

            double BA = stats[0];
            double OB = stats[1];

            //convert to correct precision
            String strBA = String.format("%.3f", (float) BA);
            String strOB = String.format("%.3f", (float) OB);

            System.out.println(name);
            System.out.println("BA: " + strBA);
            System.out.println("OB%: " + strOB);
            System.out.println("H: " + Math.round(stats[2]));
            System.out.println("BB: " + Math.round(stats[3]));
            System.out.println("K: " + Math.round(stats[4]));
            System.out.println("HBP: " + Math.round(stats[5]));
            System.out.println();
        }
    }

    public static final ArrayList<Player> players = new ArrayList<>();
    public static void main(String[] args) throws FileNotFoundException {
        System.out.print("Enter File Path: ");
        Scanner input = new Scanner(System.in);
        String userInput;

        userInput = input.nextLine();
        File file = new File(userInput);
        Scanner scanner = new Scanner(file);

        while (scanner.hasNext()) {
            String name = "";
            String battingRecord = "";
            String line = scanner.nextLine();
            String [] splitter = line.split(" ");     //split the name from the batting record
            name = splitter[0];
            battingRecord = splitter[1];
            Player player = new Player(name, battingRecord);                      //create new Player with name and batting record
            players.add(player);                                                  //add player to array list
            parseBattingRecord(player, battingRecord, player.stats);
            player.displayPlayerData(player.name, player.stats);
        }
        displayLeader(players);
    }
    public static void parseBattingRecord(Player player, String battingRecord, double stats[]){
        double hits = 0;
        double walks = 0;
        double strikeouts = 0;
        double hitByPitch = 0;
        double atBat = 0;
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
                    plateAppearances++;
                    break;
                default:
                    break;
            }
        }
        Double battingAverage = hits/atBat;
        Double onBasePercentage = (hits + walks + hitByPitch)/plateAppearances;

        //isNan is going to be used to detect if im dividing by 0, If that is the case for any of the Double values, set those values to 0.000
        boolean baIsNan = battingAverage.isNaN();
        boolean obIsNan = onBasePercentage.isNaN();

        if(baIsNan){
            battingAverage = 0.000;
        }
        if(obIsNan){
            onBasePercentage = 0.000;
        }
        //add the stat numbers to each individual player stat array[]
        player.stats[0] = battingAverage;
        player.stats[1] = onBasePercentage;
        player.stats[2] = hits;
        player.stats[3] = walks;
        player.stats[4] = strikeouts;
        player.stats[5] = hitByPitch;
    }
    public static double findLeader(ArrayList<Player> players, int index){
        double currentTopScore = players.get(0).stats[index]; //set current to the first player's stats at index 0
        for(int i = 0; i < players.size(); i++){ //cycle through arrayList
            if(index != 4) { //spacial case for outs
                if (currentTopScore <= players.get(i).stats[index]) { //if the current is less than or equal to the player at index i whose stat is at index
                    currentTopScore = players.get(i).stats[index]; // set it to that
                }
            }else{ //reverse of what I am doing on the other if, set the smallest number outs as the leader
                if(currentTopScore >= players.get(i).stats[index]){
                    currentTopScore = players.get(i).stats[index];
                }
            }
        }
        return currentTopScore; //return number
    }
    public static void displayLeader(ArrayList<Player> players) {
        double [] display = new double[6];                                 //Contains highScores
        String [] leaders = new String[6];                                 //Contains names of highScores
        Arrays.fill(leaders, "");                                      //initialization of array to empty strings
        Player currentPlayer = players.get(0);

        for(int i = 0; i < currentPlayer.stats.length; i++){               //travers through array
            double projectedLeader = findLeader(players, i);               // set variable to the Max Score in the category
            display[i] = projectedLeader;                                  //copy high scores into array
        }

        for(int i = 0; i < display.length; i++){                           //traverse through all the high scores
            boolean flag = true;                                           //set true so comma does appear first
            for(int j = 0 ; j < players.size(); j++){
                if(display[i] == players.get(j).stats[i]) {                //if the display equals the stat at player.stats(j)
                    if (flag) {
                        leaders[i] += players.get(j).name;                 //add name to array
                        flag = false;
                    }else{
                        leaders[i] += ", "+ players.get(j).name;           //concat names to leaders[i]
                    }
                }
            }
        }
        printLeaders(display, leaders);
    }
    public static void printLeaders(double [] display, String [] names){
        double BA = display[0];
        double OB = display[1];

        String strBA = String.format("%.3f", (float)BA);    //round to the thousandths spot
        String strOB = String.format("%.3f", (float)OB);    //round to the thousandths spot

        System.out.println("LEAGUE LEADERS");
        System.out.println("BA: " + names[0] + " " + strBA);
        System.out.println("OB%: " + names[1] + " " + strOB);
        System.out.println("H: " + names[2] + " " + Math.round(display[2]));
        System.out.println("BB: " + names[3] + " " + Math.round(display[3]));
        System.out.println("K: " + names[4] + " " + Math.round(display[4]));
        System.out.println("HBP: " + names[5] + " " + Math.round(display[5]));


        /*String names = func(walks[], 2, names[])
         * if(names.isEmpty()){
         * print out the name of the
         * }else{
         * System.out.println("W: " + names + " " + strBA);
         * }
         * */
    }
}