import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
        Hashtable<String, Player> TeamsFile = readTeamsFile(sc1);
        
        
        
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
        
     return null;   
    }
    public static Hashtable<String, Player> readTeamsFile(Scanner scanner){
        return null;
    }
    
    
}
