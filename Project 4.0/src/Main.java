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
        String keyValue = "";
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
        String name = "";
        String code = "";
        String team = "";
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
    
    
}
