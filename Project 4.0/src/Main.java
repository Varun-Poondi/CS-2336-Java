/* Name: Varun Poondi
* Net ID: VMP190003
* Prof: Jason Smith
* Date: 4/27/2021
* */





import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {

        Scanner scanner2 = new Scanner(System.in); //reads the teamsFile
        String teamsFileName = fileChecker(scanner2);
        
        //by the time it gets to this statement, both files have been checked if they exist
        Scanner sc = new Scanner(new BufferedReader(new FileReader("keyfile.txt"))); 
        Scanner sc1 = new Scanner(new BufferedReader(new FileReader(teamsFileName)));

        Hashtable<String, String> KeyFile = readKeyFile(sc);  //read the keyFile and store the info into the keyFile hashTable
        Hashtable<String, Player> TeamsTable = readTeamsFile(sc1, KeyFile); //read the teamsFile and store the info into the teamsTable hashTable
        
        //create two arraylists, these arrayList will contain the hashTables in a list format and will be used in the sorting and leader finding process
        ArrayList<Player> Home = new ArrayList<>();
        ArrayList<Player> Away = new ArrayList<>();
        
        separateMasterHashTable(TeamsTable, Home, Away);

        printStats(Away, "AWAY");
        printStats(Home, "HOME");
        
        displayToScores(Home, Away);
    }
    public static String fileChecker(Scanner scanner){
        String fileName = scanner.next();
        File fileObj = new File(fileName);
        boolean fileIsReadable = false;
        while(!fileIsReadable) { //if the file is not readable
            try {
                if (!fileObj.canRead()) { //check if the given file is not readable
                    throw new FileNotFoundException(); //throw exception
                } else {
                    fileIsReadable = true; //update boolean value
                }
            } catch (Exception e) { //catch exception
                System.out.println("File was not found, please try again."); //prompt
                fileName = scanner.next(); //read the new fileName. Ask for file input again to be tested
                fileObj = new File(fileName);
            }
        }
        return fileName; //this file is a valid readable file
    }
    public static Hashtable<String, String> readKeyFile(Scanner scanner){ 
        Hashtable<String, String> table = new Hashtable<>();
        String headerName = ""; //remove the hashTags later
        String keyValue;
        while(scanner.hasNextLine()){ 
            String line = scanner.nextLine(); //get the line of text to be parsed
            boolean isHeader = line.contains("#"); //create boolean value depending if line contains a #, meaning header
            if(isHeader){ //if true
                headerName =  line; // set the line as the header name
                headerName = headerName.replace("#", ""); //remove the hashtag
                headerName = headerName.replace(" ", ""); //remove the spaces
            }else if(!line.isEmpty()){ //if the line is not empty
                keyValue = line; //the keyValue along with the header is stored in the table
                table.put(keyValue, headerName); //store the key and value
            }
        }
     return table;   //return table
    }
    public static Hashtable<String, Player> readTeamsFile(Scanner scanner, Hashtable<String, String> keyTable){
        Hashtable<String, Player> table = new Hashtable<>();
        String name;
        String code;
        String team;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine(); //get the line of text to be parsed
            team = line.substring(0,1); //get the team name
            name = line.substring(2, line.lastIndexOf(" ")); // get the player name
            code = line.substring(line.lastIndexOf(" ") + 1); //get the player's batting code
            if(table.get(name) == null){ //if the table does not have this player recorded,
                Player player = new Player(name, team); //create a new player obj
                player.decipherCode(code, keyTable); //decipher the code and update the player's stats array
                table.put(name, player); //put this value into the table
            }else{
                table.get(name).decipherCode(code, keyTable); //get the player found and update their stats array
            }
        }
        return table;
    }
    public static void separateMasterHashTable(Hashtable<String, Player> TeamsTable, ArrayList<Player> Home, ArrayList<Player> Away){
        ArrayList<Player> master = new ArrayList<>(TeamsTable.values()); //master array List contains the TeamsTable which has both home and away players
        for (Player player : master) {
            if (player.getTeam().equals("A")) { //if the player is playing for the away team 
                Away.add(player); //store the player into the away team arraylist
            } else { 
                Home.add(player); //store the player into the home team arraylist
            }
        }
    }
    public static void printStats(ArrayList<Player> team, String title){
        System.out.println(title); 
        Collections.sort(team); //sort the team based on alphabetical precedence
        for (Player player : team) {
            System.out.println(player); //print the players
        }
        System.out.println(); //create a new line
    }
    public static void displayToScores(ArrayList<Player> Home, ArrayList<Player> Away){  //simple function that will print the leaders based on the index of the required print
        System.out.println("LEAGUE LEADERS");
        printLeaders(6,Home, Away, "BATTING AVERAGE");
        printLeaders(7,Home, Away, "ON-BASE PERCENTAGE");
        printLeaders(1,Home, Away,"HITS");
        printLeaders(2,Home, Away, "WALKS");
        printLeaders(3,Home, Away, "STRIKEOUTS");
        printLeaders(4,Home, Away,"HIT BY PITCH");
    }
    public static void printLeaders(int categoryIndex, ArrayList<Player> Home, ArrayList<Player> Away, String categoryName){ //helper function that will pass in the arrayList and print out the overallLeaders
        ArrayList<Player> Overall = new ArrayList<>(); //overall will be used to get the best players from each team and find who were the best players from both teams
        FindLeaders(Away, Overall, categoryIndex); //get the leaders from the away team 
        FindLeaders(Home, Overall, categoryIndex); //get the leaders from the home team 
        //Collections.sort(Overall); //sort alphabetically using the compareTo method in the player class
        FindOverallLeaders(Overall, categoryIndex, categoryName); // find the leaders from leaders of the home and away team
    }
    public static void FindLeaders(ArrayList<Player> Team, ArrayList<Player> Overall, int categoryIndex){  //finds the leaders in each individual category. The function will get both Away and Home passed in this order
        //initialize to a low value to be able to find max highScore, chose -100 cuz why not?? 
        double firstPlace = -100.0;
        double secondPlace = -100.0;
        double thirdPlace = -100.0;
        double compareValue;

        if(categoryIndex == 3) { //these variables will be changed only if we are trying to find the StrikeOuts leaders 
            firstPlace = Double.MAX_VALUE;
            secondPlace = Double.MAX_VALUE;
            thirdPlace = Double.MAX_VALUE;
        }
        for (Player player : Team) {
            compareValue = player.getStats()[categoryIndex];

            if (categoryIndex == 3) {
                if (compareValue < firstPlace) { //store in first place
                    thirdPlace = secondPlace;
                    secondPlace = firstPlace;
                    firstPlace = compareValue;
                } else if (compareValue < secondPlace) { //store in second place
                    thirdPlace = secondPlace;
                    secondPlace = compareValue;
                } else if (compareValue < thirdPlace) { //store in third place
                    thirdPlace = compareValue;
                }
            } else {
                if (compareValue > firstPlace) { //store in first place
                    thirdPlace = secondPlace;
                    secondPlace = firstPlace;
                    firstPlace = compareValue;
                } else if (compareValue > secondPlace) { //store in second place
                    thirdPlace = secondPlace;
                    secondPlace = compareValue;
                } else if (compareValue > thirdPlace) { //store in third place
                    thirdPlace = compareValue;
                }
            }
        }

       // After this point, I have gotten the top scores for the category, I need to now find the player objects that possess these values and store them into the overall array.
        for (Player currentPlayer : Team) {
            double statsCheck = currentPlayer.getStats()[categoryIndex];
            if (statsCheck == firstPlace || statsCheck == secondPlace || statsCheck == thirdPlace) {
                Overall.add(currentPlayer);
            }
        }
       // I have now added the Player objects that were in the top 3 places into the overall arrayList
    }


    public static void FindOverallLeaders(ArrayList<Player> Overall, int categoryIndex, String categoryName){
        //initialize to a low value to be able to find max highScore, chose -100 cuz why not??
        double firstPlace = -100.0;
        double secondPlace = -100.0;
        double thirdPlace = -100.0;

        double compareValue;
        String playerName;

        boolean isFComma = true;
        boolean isSComma = true;
        boolean isTComma = true;

        StringBuilder firstLeaders = new StringBuilder();
        StringBuilder secondLeaders = new StringBuilder();
        StringBuilder thirdLeaders = new StringBuilder();

        if(categoryIndex == 3) { //these variables will be changed only if we are trying to find the StrikeOuts leaders 
            firstPlace = Double.MAX_VALUE;
            secondPlace = Double.MAX_VALUE;
            thirdPlace = Double.MAX_VALUE;
        }
        for (Player player : Overall) { //traverse through the overall arrayList
            compareValue = player.getStats()[categoryIndex]; //init the compare value

            if (categoryIndex == 3) {
                if (compareValue < firstPlace) { //store in first place
                    thirdPlace = secondPlace;
                    secondPlace = firstPlace;
                    firstPlace = compareValue;
                } else if (compareValue < secondPlace) { //store in second place
                    thirdPlace = secondPlace;
                    secondPlace = compareValue;
                } else if (compareValue < thirdPlace) { //store in third place
                    thirdPlace = compareValue;
                }
            } else {
                if (compareValue > firstPlace) { //store in first place
                    thirdPlace = secondPlace;
                    secondPlace = firstPlace;
                    firstPlace = compareValue;
                } else if (compareValue > secondPlace) { //store in second place
                    thirdPlace = secondPlace;
                    secondPlace = compareValue;
                } else if (compareValue > thirdPlace) { //store in third place
                    thirdPlace = compareValue;
                }
            }
        }

        //find all the names of the players who were able to achieve this score, no need to sort again since everything is in alphabetic order
        for (Player player : Overall) {
            compareValue = player.getStats()[categoryIndex];
            playerName = player.getName();
            if (compareValue == firstPlace) {
                if (isFComma) {
                    firstLeaders.append(playerName);
                    isFComma = false;
                } else {
                    firstLeaders.append(", ").append(playerName);
                }
            } else if (compareValue == secondPlace) {
                if (isSComma) {
                    secondLeaders.append(playerName);
                    isSComma = false;
                } else {
                    secondLeaders.append(", ").append(playerName);
                }
            } else if (compareValue == thirdPlace) {
                if (isTComma) {
                    thirdLeaders.append(playerName);
                    isTComma = false;
                } else {
                    thirdLeaders.append(", ").append(playerName);
                }
            }
        }
        System.out.println(categoryName);
        if (categoryIndex == 6 || categoryIndex == 7) { //special case, 6, 7 have decimal precision 
            boolean isNanFirst = Double.isNaN(firstPlace);
            boolean isNanSecond = Double.isNaN(secondPlace);
            boolean isNanThird = Double.isNaN(thirdPlace);
            //check if any of the places are getting divided by 0, if so, set the places to 0.000
            if (isNanFirst) firstPlace = 0.000;
            if (isNanSecond) secondPlace = 0.000;
            if (isNanThird) thirdPlace = 0.000;

            String fP = String.format("%.3f", (float) firstPlace);
            String sP = String.format("%.3f", (float) secondPlace);
            String tP = String.format("%.3f", (float) thirdPlace);

            //as long as the strings is not empty, display the names 
            if (!firstLeaders.toString().equals("")) System.out.println(fP + "\t" + firstLeaders);
            if (!secondLeaders.toString().equals("")) System.out.println(sP + "\t" + secondLeaders);
            if (!thirdLeaders.toString().equals("")) System.out.println(tP + "\t" + thirdLeaders);

        } else {
            //as long as the strings are not empty, display the names
            if (!firstLeaders.toString().equals("")) System.out.println(Math.round(firstPlace) + "\t" + firstLeaders);
            if (!secondLeaders.toString().equals("")) System.out.println(Math.round(secondPlace) + "\t" + secondLeaders);
            if (!thirdLeaders.toString().equals("")) System.out.println(Math.round(thirdPlace) + "\t" + thirdLeaders);
        }

    }
}
