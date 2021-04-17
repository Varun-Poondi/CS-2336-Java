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
        Scanner scanner1 = new Scanner(System.in);
        Scanner scanner2 = new Scanner(System.in);
        String keyFileName = fileChecker(scanner1);
        String teamsFileName = fileChecker(scanner2);
        Scanner sc = new Scanner(new BufferedReader(new FileReader(keyFileName)));
        Scanner sc1 = new Scanner(new BufferedReader(new FileReader(teamsFileName)));

        Hashtable<String, String> KeyFile = readKeyFile(sc);
        Hashtable<String, Player> TeamsTable = readTeamsFile(sc1, KeyFile);
        
        ArrayList<Player> Home = new ArrayList<>();
        ArrayList<Player> Away = new ArrayList<>();
        
        separateMasterHashTable(TeamsTable, Home, Away);

        printStats(Away, "AWAY");
        printStats(Home, "HOME");
        
        displayToScores(Home, Away);
        //KeyFile.forEach((k, v) -> System.out.println("Key : " + k + ", Value : " + v));


    }
    public static String fileChecker(Scanner scanner){
        String fileName = scanner.next();
        File fileObj = new File(fileName);
        boolean fileIsReadable = false;
        while(!fileIsReadable) {
            try {
                if (!fileObj.canRead()) {
                    throw new FileNotFoundException();
                } else {
                    fileIsReadable = true;
                }
            } catch (Exception e) {
                System.out.println("File was not found, please try again.");
                fileName = scanner.next();
                fileObj = new File(fileName); //ask for file input again to be tested
            }
        }
        return fileName;
    }
    public static Hashtable<String, String> readKeyFile(Scanner scanner){
        Hashtable<String, String> table = new Hashtable<>();
        String headerName = ""; //remove the hashTags later
        String keyValue;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            boolean isHeader = line.contains("#");
            if(isHeader){
                headerName =  line;
                headerName = headerName.replace("#", "");
                headerName = headerName.replace(" ", "");
            }else if(!line.isEmpty()){
                keyValue = line;
                table.put(keyValue, headerName);
            }
        }
     return table;   
    }
    public static Hashtable<String, Player> readTeamsFile(Scanner scanner, Hashtable<String, String> keyTable){
        Hashtable<String, Player> table = new Hashtable<>();
        String name;
        String code;
        String team;
        while(scanner.hasNextLine()){
            String line = scanner.nextLine();
            team = line.substring(0,1);
            name = line.substring(2, line.lastIndexOf(" "));
            code = line.substring(line.lastIndexOf(" ") + 1);
            if(table.get(name) == null){
                Player player = new Player(name, team);
                player.decipherCode(code, keyTable);
                table.put(name, player);
            }else{
                table.get(name).decipherCode(code, keyTable);
            }
        }
        return table;
    }
    public static void separateMasterHashTable(Hashtable<String, Player> TeamsTable, ArrayList<Player> Home, ArrayList<Player> Away){
        ArrayList<Player> master = new ArrayList<>(TeamsTable.values());
        for (Player player : master) {
            if (player.getTeam().equals("A")) {
                Away.add(player);
            } else {
                Home.add(player);
            }
        }
    }
    public static void printStats(ArrayList<Player> team, String title){
        System.out.println(title);
        Collections.sort(team);
        for (Player player : team) {
            System.out.println(player);
        }
        System.out.println();
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
        ArrayList<Player> Overall = new ArrayList<>();
        FindLeaders(Away, Overall, categoryIndex);
        FindLeaders(Home, Overall, categoryIndex);
        //Collections.sort(Overall); //sort alphabetically using the compareTo method in the player class
        FindOverallLeaders(Overall, categoryIndex, categoryName);
    }
    public static void FindLeaders(ArrayList<Player> Team, ArrayList<Player> Overall, int categoryIndex){  //finds the leaders in each individual category. The function will get both Away and Home passed in this order
        //initialize to a low value to be able to find max highScore, chose -100 cuz why not?? 
        double firstPlace = -100.0;
        double secondPlace = -100.0;
        double thirdPlace = -100.0;
        double compareValue = 0;

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
        for (Player player : Overall) {
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
