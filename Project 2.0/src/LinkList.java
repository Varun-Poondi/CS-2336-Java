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
    public void print(){
        printList(head);
    }
    public void printList(Node node){
        if(node != null){
            node.getPlayer().displayStats();
            
            printList(node.getNext());
        }
        
    }

    
}
