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
    private void findLeaders(int index) {
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
        if (index == 6 || index == 7) {
            boolean isNanFirst = Double.isNaN(firstPlace);
            boolean isNanSecond = Double.isNaN(secondPlace);
            boolean isNanThird = Double.isNaN(thirdPlace);
            
            if (isNanFirst) firstPlace = 0.000;
            if (isNanSecond) secondPlace = 0.000;
            if (isNanThird) thirdPlace = 0.000;
            
            String fP = String.format("%.3f", (float) firstPlace);
            String sP = String.format("%.3f", (float) secondPlace);
            String tP = String.format("%.3f", (float) thirdPlace);


            if (!firstLeaders.toString().equals("")) System.out.println(fP + "\t" + firstLeaders);
            if (!secondLeaders.toString().equals("")) System.out.println(sP + "\t" + secondLeaders);
            if (!thirdLeaders.toString().equals("")) System.out.println(tP + "\t" + thirdLeaders);


        } else {
            if (!firstLeaders.toString().equals("")) System.out.println(firstPlace + "\t" + firstLeaders);
            if (!secondLeaders.toString().equals("")) System.out.println(secondPlace + "\t" + secondLeaders);
            if (!thirdLeaders.toString().equals("")) System.out.println(thirdPlace + "\t" + thirdLeaders);
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
