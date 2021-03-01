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
                head.setNext(head.getNext());
                node.setNext(null);
            }else{
                Node currentNode = head.getNext();
                while(currentNode.getNext() != null && !currentNode.getNext().getPlayer().getName().equals(playerName)){
                    currentNode = currentNode.getNext();
                }
                if(currentNode.getNext() != null){
                    node = currentNode.getNext();
                    currentNode.setNext(currentNode.getNext().getNext());
                    node.setNext(null);
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
        Node mergedNode = new Node(null);
        mergedNode.getPlayer().setName(currentNode.getPlayer().getName());
        mergedNode.getPlayer().getStats()[0] = currentNode.getPlayer().getStats()[0] + indexNode.getPlayer().getStats()[0];
        mergedNode.getPlayer().getStats()[1] = currentNode.getPlayer().getStats()[1] + indexNode.getPlayer().getStats()[1];
        mergedNode.getPlayer().getStats()[2] = currentNode.getPlayer().getStats()[2] + indexNode.getPlayer().getStats()[2];
        mergedNode.getPlayer().getStats()[3] = currentNode.getPlayer().getStats()[3] + indexNode.getPlayer().getStats()[3];
        mergedNode.getPlayer().getStats()[4] = currentNode.getPlayer().getStats()[4] + indexNode.getPlayer().getStats()[4];
        mergedNode.getPlayer().getStats()[5] = currentNode.getPlayer().getStats()[5] + indexNode.getPlayer().getStats()[5];
        mergedNode.getPlayer().getStats()[6] = currentNode.getPlayer().getStats()[6] + indexNode.getPlayer().getStats()[6];
        mergedNode.getPlayer().getStats()[7] = currentNode.getPlayer().getStats()[7] + indexNode.getPlayer().getStats()[7];
        
        return mergedNode;
    }
    public void sortAlphabetically(){
        Node currentNode = head;
        Node indexNode; //initialized to null
        if(head != null){
            while(currentNode != null){
                indexNode = currentNode.getNext();
                while(indexNode != null){
                    int compare = currentNode.getPlayer().getName().compareTo(indexNode.getPlayer().getName());
                    if(compare > 0){
                        swap(currentNode, indexNode);
                    }else if (compare == 0){
                        Node newNode = mergePlayer(currentNode, indexNode);
                        prepend(newNode);
                        deleteCopyNodes(newNode.getPlayer().getName());
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
