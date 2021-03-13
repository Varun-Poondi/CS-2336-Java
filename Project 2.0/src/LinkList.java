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
    private void prepend(Node newNode){ //adds node to beginning of the LinkedList
       newNode.setNext(head); //sets the next value to the head
       head = newNode; //make the newNode as the head
    }
    private void deleteCopyNodes(String playerName){ //linear search through linkedList and deletes Nodes that have the same playerName
        
        if(head != null){ //null check
            Node node = head;
            if(head.getPlayer().getName().equals(playerName)){ //if the head has the same player
                head = head.getNext(); //set the head as the next value
                node.setNext(null); //delete node
            }else{
                Node currentNode = head; //traversing node
                while(currentNode.getNext() != null){
                    if(currentNode.getNext().getPlayer().getName().equals(playerName)){ //if the currentNode finds the target playerName
                        node = currentNode.getNext(); //set node to point to the nextNode of the currentNode
                        currentNode.setNext(currentNode.getNext().getNext()); // make the currentNode to point to the next next node so that the middle node will be isolated
                        node.setNext(null); //delete the isolated node
                    }else{
                        currentNode = currentNode.getNext(); //move forward
                    }
                }
            }
        }
    }
    private void swap(Node currentNode, Node indexNode){ //simple swap function
        Player temp = currentNode.getPlayer(); //temp value holds Player of the currentNode
        currentNode.setPlayer(indexNode.getPlayer()); //Swaps the player classes
        indexNode.setPlayer(temp); //index node takes in previous player
    }
    private Node mergePlayer(Node currentNode){ //Merges the Data of player that have the same name from different Nodes
        double stat0 = 0;
        double stat1 = 0;
        double stat2 = 0;
        double stat3 = 0;
        double stat4 = 0;
        double stat5 = 0;
        double stat6 = 0;
        double stat7 = 0;
        Node traverseNode = currentNode;
        while(traverseNode.getNext() != null) { //while loop is used to find the number of times player name is shared by the currentNode and the Traverse node. Finding number of duplicate players
            if (traverseNode.getNext().getPlayer().getName().equals(currentNode.getPlayer().getName())){ //playerName has been detected
                //update the stat values by adding from the  traverseNode's stats
                stat0 += traverseNode.getNext().getPlayer().getStats()[0];
                stat1 += traverseNode.getNext().getPlayer().getStats()[1];
                stat2 += traverseNode.getNext().getPlayer().getStats()[2];
                stat3 += traverseNode.getNext().getPlayer().getStats()[3];
                stat4 += traverseNode.getNext().getPlayer().getStats()[4];
                stat5 += traverseNode.getNext().getPlayer().getStats()[5];
                stat6 += traverseNode.getNext().getPlayer().getStats()[6];
                stat7 += traverseNode.getNext().getPlayer().getStats()[7];
            }
            traverseNode = traverseNode.getNext();
        }
        //add the currentNode stats to the stat variables since we never covered the currentNode itself
        currentNode.getPlayer().setStats(0, currentNode.getPlayer().getStats()[0] + stat0); 
        currentNode.getPlayer().setStats(1, currentNode.getPlayer().getStats()[1] + stat1);
        currentNode.getPlayer().setStats(2, currentNode.getPlayer().getStats()[2] + stat2);
        currentNode.getPlayer().setStats(3, currentNode.getPlayer().getStats()[3] + stat3);
        currentNode.getPlayer().setStats(4, currentNode.getPlayer().getStats()[4] + stat4);
        currentNode.getPlayer().setStats(5, currentNode.getPlayer().getStats()[5] + stat5);
        currentNode.getPlayer().setStats(6, currentNode.getPlayer().getStats()[6] + stat6);
        currentNode.getPlayer().setStats(7, currentNode.getPlayer().getStats()[7] + stat7);
        
        return currentNode;
    }
    // Sorts the linkedList alphabetically based on the player's names 
    public void sortAlphabetically(){ //section sort algorithm
        Node currentNode = head;
        Node indexNode; 
        if(head != null){
            while(currentNode != null){
                indexNode = currentNode.getNext(); //this node will move currentNode + 1 position
                while(indexNode != null){
                    //compareTo() > 0 -> swap needs to occur since the indexNode's name is alphabetically superior 
                    //compareTo() < 0 -> now swap need, already in correct place
                    //compareTo == 0 -> Names are the same
                    int compare = currentNode.getPlayer().getName().compareTo(indexNode.getPlayer().getName()); //compare to Value.
                    if(compare > 0){
                        swap(currentNode, indexNode); 
                    }else if (compare == 0){  //must merge Nodes
                        Node newNode = mergePlayer(currentNode); //create Node with the values of the mergedNode
                        deleteCopyNodes(currentNode.getPlayer().getName()); //delete all copies of Nodes that have the same name as the currentNode
                        prepend(newNode); //add the newNode to the beginning of the linkedList
                        sortAlphabetically(); //recursive call to sort the node into it's correct place
                    }
                    indexNode = indexNode.getNext();
                }
                currentNode = currentNode.getNext();
            }
        }
    }
    //public function that will call the findLeaders() function to print the highScores and top 3 leaders based on the specified category(index value) 
    public void displayTopScores(){
        System.out.println("\nBatting Average");
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
    }
    //prints the top 3 leaders and highScores
    private void findLeaders(int index) {
        //initialize to -1 to be able to find max highScore
        double firstPlace = -1;
        double secondPlace = -1;
        double thirdPlace = -1;

        StringBuilder firstLeaders = new StringBuilder();
        StringBuilder secondLeaders = new StringBuilder();
        StringBuilder thirdLeaders = new StringBuilder();

        boolean isFComma = true;
        boolean isSComma = true;
        boolean isTComma = true;
        
        if (head != null) {
            Node currentNode = head;
            while (currentNode != null) {
                //we find the places by using an algorithm that passes on the smallest value recorded to the places underneath it, insuring that we correctly record the placing in a 1st,2nd, 3rd order
                if (currentNode.getPlayer().getStats()[index] >= firstPlace) { //store in first place
                    thirdPlace = secondPlace; 
                    secondPlace = firstPlace;
                    firstPlace = currentNode.getPlayer().getStats()[index];
                } else if (currentNode.getPlayer().getStats()[index] >= secondPlace) { //store in second place
                    thirdPlace = secondPlace;
                    secondPlace = currentNode.getPlayer().getStats()[index];
                } else if (currentNode.getPlayer().getStats()[index] >= thirdPlace) { //store in third place
                    thirdPlace = currentNode.getPlayer().getStats()[index];
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
            if (!firstLeaders.toString().equals("")) System.out.println(firstPlace + "\t" + firstLeaders);
            if (!secondLeaders.toString().equals("")) System.out.println(secondPlace + "\t" + secondLeaders);
            if (!thirdLeaders.toString().equals("")) System.out.println(thirdPlace + "\t" + thirdLeaders);
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
