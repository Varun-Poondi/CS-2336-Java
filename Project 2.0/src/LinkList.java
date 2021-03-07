public class LinkList {
    private Node head;
    
    public void append(Node newNode){
        if(head == null){
           head = newNode;
        }else{
            Node currentNode = head;
            while(currentNode.getNext() != null){
                currentNode = currentNode.getNext();
            }
            currentNode.setNext(newNode);
        }
    }
    private void prepend(Node newNode){
       newNode.setNext(head);
       head = newNode;
    }
    private void deleteCopyNodes(String playerName){
        
        if(head != null){
            Node node = head;
            if(head.getPlayer().getName().equals(playerName)){
                head = head.getNext();
                node.setNext(null);
            }else{
                Node currentNode = head;
                
                while(currentNode.getNext() != null){
                    if(currentNode.getNext().getPlayer().getName().equals(playerName)){
                        node = currentNode.getNext();
                        currentNode.setNext(currentNode.getNext().getNext());
                        node.setNext(null);
                    }else{
                        currentNode = currentNode.getNext();
                    }
                }
            }
        }
    }
    private void swap(Node currentNode, Node indexNode){
        Player temp = currentNode.getPlayer();
        currentNode.setPlayer(indexNode.getPlayer());
        indexNode.setPlayer(temp);
    }
    private Node mergePlayer(Node currentNode, Node indexNode){
        int counter = 1;
        
        Node traverseNode = currentNode;
        while(traverseNode.getNext() != null) {
            if (traverseNode.getNext().getPlayer().getName().equals(indexNode.getPlayer().getName())){
                counter++;
            }
            traverseNode = traverseNode.getNext();
        } 
        currentNode.getPlayer().setStats(0, indexNode.getPlayer().getStats()[0] * counter);
        currentNode.getPlayer().setStats(1, indexNode.getPlayer().getStats()[1] * counter);
        currentNode.getPlayer().setStats(2, indexNode.getPlayer().getStats()[2] * counter);
        currentNode.getPlayer().setStats(3, indexNode.getPlayer().getStats()[3] * counter);
        currentNode.getPlayer().setStats(4, indexNode.getPlayer().getStats()[4] * counter);
        currentNode.getPlayer().setStats(5, indexNode.getPlayer().getStats()[5] * counter);
        currentNode.getPlayer().setStats(6, indexNode.getPlayer().getStats()[6] * counter);
        currentNode.getPlayer().setStats(7, indexNode.getPlayer().getStats()[7] * counter);
        
        return currentNode;
    }
    public void sortAlphabetically(){ //section sort algorithm
        Node currentNode = head;
        Node indexNode;
        if(head != null){
            while(currentNode != null){
                indexNode = currentNode.getNext();
                while(indexNode != null){
                    int compare = currentNode.getPlayer().getName().compareTo(indexNode.getPlayer().getName());
                    if(compare > 0){
                        swap(currentNode, indexNode);
                    }else if (compare == 0){
                        Node newNode = mergePlayer(currentNode, indexNode);
                        deleteCopyNodes(currentNode.getPlayer().getName());
                        prepend(newNode);
                        sortAlphabetically();
                    }
                    indexNode = indexNode.getNext();
                }
                currentNode = currentNode.getNext();
            }
        }
    }
    public void displayTopScores(){
        System.out.println("Batting Average");
        findLeaders(6);
        System.out.println("ON-BASE PERCENTAGE");
        findLeaders(7);
        System.out.println("HITS");
        findLeaders(1);
        System.out.println("WALKS");
        findLeaders(2);
        System.out.println("STRIKEOUTS");
        findLeaders(3);
        System.out.println("HIT BY PITCH");
        findLeaders(4);
    }
    private void findLeaders(int index) {
        double firstPlace = -1;
        double secondPlace = -1;
        double thirdPlace = -1;

        String firstLeaders = "";
        String secondLeaders = "";
        String thirdLeaders = "";


        if (head != null) {
            Node currentNode = head;
            while (currentNode != null) {
                if (currentNode.getPlayer().getStats()[index] >= firstPlace) {
                    thirdPlace = secondPlace;
                    secondPlace = firstPlace;
                    firstPlace = currentNode.getPlayer().getStats()[index];
                } else if (currentNode.getPlayer().getStats()[index] >= secondPlace) {
                    thirdPlace = secondPlace;
                    secondPlace = currentNode.getPlayer().getStats()[index];
                } else if (currentNode.getPlayer().getStats()[index] >= thirdPlace) {
                    thirdPlace = currentNode.getPlayer().getStats()[index];
                }
                currentNode = currentNode.getNext();
            }
            currentNode = head;
            while (currentNode != null) {
                double statCheck = currentNode.getPlayer().getStats()[index];
                String playerName = currentNode.getPlayer().getName();
                if (statCheck == firstPlace) {
                    firstLeaders += playerName + ",";
                } else if (statCheck == secondPlace) {
                    secondLeaders += playerName + ",";
                } else if (statCheck == thirdPlace) {
                    thirdLeaders += playerName + ",";
                }
                currentNode = currentNode.getNext();
            }
        }
        if (index == 6 || index == 7) {
            boolean isNanFirst = Double.isNaN(firstPlace);
            boolean isNanSecond = Double.isNaN(secondPlace);
            boolean isNanThird = Double.isNaN(thirdPlace);
            if (isNanFirst) {
                firstPlace = 0.000;
            }
            if (isNanSecond) {
                secondPlace = 0.000;
            }
            if (isNanThird) {
                thirdPlace = 0.000;
            }
            String fP = String.format("%.3f", (float) firstPlace);
            String sP = String.format("%.3f", (float) secondPlace);
            String tP = String.format("%.3f", (float) thirdPlace);


            System.out.println(
                    fP + "\t" + firstLeaders + "\n" + 
                    sP + "\t" + secondLeaders + "\n" + 
                    tP + "\t" + thirdLeaders + "\n");


        }else{
            System.out.println(
                    firstPlace + "\t" + firstLeaders + "\n" +
                            secondPlace + "\t" + secondLeaders + "\n" +
                            thirdPlace + "\t" + thirdLeaders + "\n");
        }
    }
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
