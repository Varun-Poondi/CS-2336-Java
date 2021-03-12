/*Name: Varun Poondi
* Net-ID: VMP190003
* Prof: Jason Smith
* Date: 3/4/2021 */
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    
    public static final LinkList linkedList = new LinkList();
    public static void main(String[] args) throws FileNotFoundException {
        System.out.print("Enter File Path: ");
        Scanner input = new Scanner(System.in);
        String fileName;
        fileName = input.next();
        Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName))); //sc used to read the fileName
        while(sc.hasNext()){
            String name;
            String battingRecord;
            String line = sc.nextLine();
            String [] splitter = line.split(" ");     //split the name from the batting record, regex magic
            name = splitter[0];
            battingRecord = splitter[1];
            Player player = new Player(name, battingRecord);                      //create new Player with name and batting record
            Node node = new Node(player);                                         //add player to a Node
            node.parseBattingRecord(player, battingRecord);                       //parse the player batting record and store it into the player's stats array
            linkedList.append(node);                                              //append the node the beginning of the list
        }
        //linkedList list function calls
        linkedList.sortAlphabetically();                // sort the linkedList in an alphabetical manner
        linkedList.print();                             // print the nodes of the linkedList
        linkedList.displayTopScores();                  //  displays the highScores of players in the top 3 places
    }
}
