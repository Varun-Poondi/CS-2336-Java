public class LinkList {
    private Node head;
    
    public void append(Node newNode){ //adds node to the end of the LinkedList  
        if(head == null){
           head = newNode; //set the newNode has the head
        }else{
            Node currentNode = head;
            while(currentNode.getNext() != null){ //while loop will traverse to the end of linkedList
                currentNode = currentNode.getNext();
            }
            currentNode.setNext(newNode); //addNode at the end
        }
    }
    
    private void deleteCopyNodes(String playerName, Node specialNode){ //linear search through linkedList and deletes Nodes that have the same playerName
        if(head != null){ //null check
            Node currentNode = head; //traversing node
            Node previousNode = null;
            while(currentNode != null){
                //move forward
                if(currentNode.getPlayer().getName().equals(playerName) && currentNode != specialNode){ //if the currentNode finds the target playerName
                    if(currentNode == head){ //check if currentNode is head
                        head = head.getNext(); //set the head as the next value
                    }else if(previousNode != null){
                        previousNode.setNext(currentNode.getNext()); //set node to point to the nextNode of the currentNode
                    }
                }else{
                    previousNode = currentNode;
                }
                currentNode = currentNode.getNext();
            }
        }
    }
    
    private void swap(Node currentNode, Node indexNode){ //simple swap function
        Node.Player temp = currentNode.getPlayer(); //temp value holds Player of the currentNode
        currentNode.setPlayer(indexNode.getPlayer()); //Swaps the player classes
        indexNode.setPlayer(temp); //index node takes in previous player
    }
    
    private void mergePlayer(Node currentNode){ //Merges the Data of player that have the same name from different Nodes
        double atBats = 0; 
        double hits = 0;  
        double walks = 0; 
        double outs = 0;
        double strikeouts = 0; 
        double hitByPitch = 0;  
        double sacrifices = 0;  
        double battingAverage = 0;
        double onBasePercentage = 0;
        Node traverseNode = currentNode;
        
        while(traverseNode != null) { //while loop is used to find the number of times player name is shared by the currentNode and the Traverse node. Finding number of duplicate players
            if (traverseNode.getPlayer().getName().equals(currentNode.getPlayer().getName())){ //playerName has been detected
                //update the stat values by adding from the  traverseNode's stats
                atBats += traverseNode.getPlayer().getStats()[0];
                hits += traverseNode.getPlayer().getStats()[1];
                walks += traverseNode.getPlayer().getStats()[2];
                strikeouts += traverseNode.getPlayer().getStats()[3];
                hitByPitch += traverseNode.getPlayer().getStats()[4];
                sacrifices += traverseNode.getPlayer().getStats()[5];
                outs += traverseNode.getPlayer().getStats()[8];

                battingAverage = (hits)/(hits + outs + strikeouts);
                onBasePercentage = (hits + walks + hitByPitch)/(hits + strikeouts + outs + walks + hitByPitch + sacrifices);
            }
            traverseNode = traverseNode.getNext();
        }
        
        boolean baIsNan = Double.isNaN(battingAverage);
        boolean obIsNan = Double.isNaN(onBasePercentage);

        if(baIsNan){
            battingAverage = 0.000;
        }
        if(obIsNan){
            onBasePercentage = 0.000;
        }
        
        //update the current node 
        if(currentNode != null) {
            currentNode.getPlayer().setStats(0, atBats);
            currentNode.getPlayer().setStats(1, hits);
            currentNode.getPlayer().setStats(2, walks);
            currentNode.getPlayer().setStats(3, strikeouts);
            currentNode.getPlayer().setStats(4, hitByPitch);
            currentNode.getPlayer().setStats(5, sacrifices);
            currentNode.getPlayer().setStats(6, battingAverage);
            currentNode.getPlayer().setStats(7, onBasePercentage);
            currentNode.getPlayer().setStats(8, outs);
        }
    }
    // Sorts the linkedList alphabetically based on the player's names 
    // compareTo() > 0 -> swap needs to occur since the indexNode's name is alphabetically superior 
    // compareTo() < 0 -> now swap need, already in correct place
    // compareTo == 0 -> Names are the same
    public void sortAlphabetically(){ 
        Node currentNode = head;
        Node indexNode;
     
        if(head != null){
            while(currentNode != null){
                indexNode = currentNode.getNext(); //this node will move currentNode + 1 position
                while(indexNode != null){
                    int compare = currentNode.getPlayer().getName().compareTo(indexNode.getPlayer().getName()); //compare to Value.
                    if(compare > 0){
                        swap(currentNode, indexNode); 
                    }else if (compare == 0){  //must merge Nodes
                        mergePlayer(currentNode); //update the currentNode 
                        deleteCopyNodes(currentNode.getPlayer().getName(), currentNode); //delete all copies of Nodes that have the same name as the currentNode and ignore the updated Node
                        indexNode = currentNode.getNext(); //traverse
                    }else {
                        indexNode = indexNode.getNext();
                    }
                }
                currentNode = currentNode.getNext();
            }
        }
    }
    //public function that will call the findLeaders() function to print the highScores and top 3 leaders based on the specified category(index value) 
    public void displayTopScores(){
        System.out.println();
        System.out.println("LEAGUE LEADERS");
        System.out.println("BATTING AVERAGE");
        findLeaders(6);
        System.out.println("\nON-BASE PERCENTAGE");
        findLeaders(7 );
        System.out.println("\nHITS");
        findLeaders(1);
        System.out.println("\nWALKS");
        findLeaders(2);
        System.out.println("\nSTRIKEOUTS");
        findLeaders(3);
        System.out.println("\nHIT BY PITCH");
        findLeaders(4);
        System.out.println();
    }
    //prints the top 3 leaders and highScores
    private void findLeaders(int index) {
        //initialize to a low value to be able to find max highScore, chose -100 cuz why not??
        double firstPlace = -100.0;
        double secondPlace = -100.0;
        double thirdPlace = -100.0;

        StringBuilder firstLeaders = new StringBuilder();
        StringBuilder secondLeaders = new StringBuilder();
        StringBuilder thirdLeaders = new StringBuilder();

        boolean isFComma = true;
        boolean isSComma = true;
        boolean isTComma = true;
        
        if (head != null) {
            Node currentNode = head;
            if(index == 3) { //these variables will be changed only if we are tyring to find the StrikeOuts leaders 
                firstPlace = Double.MAX_VALUE;
                secondPlace = Double.MAX_VALUE;
                thirdPlace = Double.MAX_VALUE;
            }
            while (currentNode != null) {
                //we find the places by using an algorithm that passes on the smallest value recorded to the places underneath it, insuring that we correctly record the placing in a 1st,2nd, 3rd order
                double compareValue = currentNode.getPlayer().getStats()[index];
                if(index != 3) {
                    if (compareValue > firstPlace) { //store in first place
                        thirdPlace = secondPlace;
                        secondPlace = firstPlace;
                        firstPlace = currentNode.getPlayer().getStats()[index];
                    } else if (compareValue > secondPlace) { //store in second place
                        thirdPlace = secondPlace;
                        secondPlace = currentNode.getPlayer().getStats()[index];
                    } else if (compareValue > thirdPlace) { //store in third place
                        thirdPlace = currentNode.getPlayer().getStats()[index];
                    }
                }else{
                    if (compareValue < firstPlace) { //store in first place
                        thirdPlace = secondPlace;
                        secondPlace = firstPlace;
                        firstPlace = currentNode.getPlayer().getStats()[index];
                    } else if (compareValue < secondPlace) { //store in second place
                        thirdPlace = secondPlace;
                        secondPlace = currentNode.getPlayer().getStats()[index];
                    } else if (compareValue < thirdPlace) { //store in third place
                        thirdPlace = currentNode.getPlayer().getStats()[index];
                    }
                }
                currentNode = currentNode.getNext();
            }
            currentNode = head;
            
            //traverses the linkedList again to get player names that have the same highScores in their stats array 
            while (currentNode != null) {
                double statCheck = currentNode.getPlayer().getStats()[index];
                String playerName = currentNode.getPlayer().getName();
                if (statCheck == firstPlace) {
                    if(isFComma) {
                        firstLeaders.append(playerName);
                        isFComma = false;
                    }else{
                        firstLeaders.append(", ").append(playerName);
                    }
                } else if (statCheck == secondPlace) {
                    if(isSComma) {
                        secondLeaders.append(playerName);
                        isSComma = false;
                    }else{
                        secondLeaders.append(", ").append(playerName);
                    }
                } else if (statCheck == thirdPlace) {
                    if(isTComma) {
                        thirdLeaders.append(playerName);
                        isTComma = false;
                    }else{
                        thirdLeaders.append(", ").append(playerName);
                    }
                }
                currentNode = currentNode.getNext();
            }
        }
        if (index == 6 || index == 7) { //special case, 6, 7 have decimal precision 
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
    //recursive function to print the linkedList
    public void print(){
        printList(head);
    }
    private void printList(Node node){
        if(node != null){
            node.getPlayer().displayStats();
            printList(node.getNext());
        }
        
    }
}
