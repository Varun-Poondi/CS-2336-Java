import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static final LinkList linkedList = new LinkList();
    public static void main(String[] args) throws FileNotFoundException {
        System.out.print("Enter File Path: ");
        Scanner input = new Scanner(System.in);
        String userInput;

        userInput = input.nextLine();
        File file = new File(userInput);
        Scanner scanner = new Scanner(file);
        while(scanner.hasNext()){
            String name;
            String battingRecord;
            String line = scanner.nextLine();
            String [] splitter = line.split(" ");     //split the name from the batting record
            name = splitter[0];
            battingRecord = splitter[1];
            Player player = new Player(name, battingRecord);                      //create new Player with name and batting record
            parseBattingRecord(player, battingRecord);
            Node node = new Node(player);
            linkedList.append(node);
        }
        linkedList.sortAlphabetically();
        linkedList.print();
    }
    public static void parseBattingRecord(Player player, String battingRecord){
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
