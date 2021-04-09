/*
 * Name: Varun Poondi
 * Net-ID: VMP190003
 * Prof: Jason Smith
 * Date: 3/30/2021
 * */

public class BinTree<T extends Comparable<T>>{
    private Node<T> root;

    public Node<T> getRoot() { //used a lot in main
        return root;
    }

    public void Insert(Node<T> newNode, Node<T> currentNode){
        if(root == null){ 
            root = newNode; //insert at root
        }else{
            if(newNode.compareTo(currentNode) <= 0){ //if the newNode is less the currentNode
                if(currentNode.getLeftNode() == null){ // and the leftSide is null
                    currentNode.setLeftNode(newNode); //insert on currentNode's left side
                }else{
                    Insert(newNode, currentNode.getLeftNode()); //start traversing on the left side 
                }
            }else{
               if(currentNode.getRightNode() == null){ //the newNode is greater than the new Node, and if the right side is null
                   currentNode.setRightNode(newNode); //insert on currentNode's right side
               }else{
                   Insert(newNode, currentNode.getRightNode()); //insert on currentNode's right side
               }
            }
        }
    }
    
    public void Delete(Node<T> searchNode){ //this function will be called in main
       DeleteHelper(searchNode,root, root);  //root as the currentParent, since we want to start from the beginning 
    }
    
    private Node<T> DeleteHelper(Node<T> searchNode, Node<T> currentNode, Node<T> parent) {
        /*
         * Three cases of Delete
         * 1) delete a leaf Node
         * 2) delete a parent with one child
         * 3) delete the parent with two children
         */
        Node<T> replacement = currentNode; //will be used to return the desired node to delete
        if (currentNode == null) {  //exit case 
            return null;
        }
        if (searchNode.compareTo(currentNode) < 0) { // move left
            parent = currentNode; //set the parent as the currentNode before we update the node
           currentNode.setLeftNode(DeleteHelper(searchNode, currentNode.getLeftNode(), parent)); //recurse on the left subtree
           
        } else if (searchNode.compareTo(currentNode) > 0) { //move right
            parent = currentNode; //set the parent as the currentNode before we update the node
            currentNode.setRightNode(DeleteHelper(searchNode, currentNode.getRightNode(), parent)); //recurse on the right subtree
            
        } else { //this is when both the searchNode and currentNode are the same node
            if(currentNode.getLeftNode() == null && currentNode.getRightNode() == null){ //leaf node case
                if(currentNode == root){ //if the currentNode is a lead node and the currentNode is also the root
                    clearTree(); //clear the tree
                }
                if(parent.getLeftNode() == currentNode){ //if the parent's leftNode equals the currentNode
                    parent.setLeftNode(null); //set the currentNode to null
                    replacement = null; 
                }else if(parent.getRightNode() == currentNode){
                    parent.setRightNode(null);
                    replacement = null;
                }
                //case were the currentNode has one child in the left or right subtree
            }else if(currentNode.getLeftNode() == null){ //if currentNode doesn't have a leftNode
                if(parent.getLeftNode() == currentNode){  //if the parent, left side is currentNode
                    parent.setLeftNode(currentNode.getRightNode()); //assign parent's left side to equal currentNode's right side
                }else{
                    parent.setRightNode(currentNode.getRightNode()); //assign parent's right side ot equal currentNode's right side
                }
                replacement = currentNode.getRightNode(); //update the replacement to the node that got deleted
            }else if(currentNode.getRightNode() == null){ //if currentNode doesn't have a rightNode
                if(parent.getLeftNode() == currentNode){ //if the parent, right side is currentNode
                    parent.setLeftNode(currentNode.getLeftNode()); //assign parent's left side to equal the currentNode's left side
                }else{
                    parent.setRightNode(currentNode.getLeftNode()); //assign parent's right side to equal the currentNode's left side
                }
                replacement = currentNode.getLeftNode(); //assign parent's right side ot equal currentNode's right side
            } else {
                //tricky case where the currentNode has both a left and right child
                replacement = findMinRight(currentNode.getRightNode()); //the replacement is the smallest Node found on the right subtree of the currentNode
                //acts like a swap method
                T temp = replacement.getPayLoad(); //save generic temp to the replacement's payLoad
                Delete(replacement); //delete the replacement using recursion
                currentNode.setPayLoad(temp); //place in temp to update the tree
            }
        }
        
        return replacement; //function will exit when null is reached
    }
    
    /*Finds the smallest node in rightSubTree of the currentNode passed*/
    private Node<T> findMinRight(Node<T> currentNode){ 
        if(currentNode.getLeftNode() != null){
            return findMinRight(currentNode.getLeftNode()); // return recursive call
        }
        return currentNode; //return if the leftSide of the currentNode has reached null
    }
    
    public Node<T> Search(T payLoad, Node<T> currentNode){
        if(currentNode == null){ //the payLoad was not found
            return null;    
        }
        Node<T>checkNode = new Node<>(payLoad);
        if(checkNode.compareTo(currentNode) == 0){ //if the payLoad was found, return the currentNode
            return currentNode;
        }else if(checkNode.compareTo(currentNode) < 0){ //move left
            return Search(payLoad, currentNode.getLeftNode());
        }else if(checkNode.compareTo(currentNode) > 0){ //move right
            return Search(payLoad, currentNode.getRightNode());
        }
        return null; //never will reach this case.
    }
    
    public String descendingOrderPrint(Node<T> currentNode){  //custom traversal
        StringBuilder string = new StringBuilder();
        if(currentNode != null) {
            string.append(descendingOrderPrint(currentNode.getRightNode())); //go the right subtree to print the larger nodes
            string.append(currentNode.getPayLoad()).append(" "); //print the node
            string.append(descendingOrderPrint(currentNode.getLeftNode())); //go the left subtree to print the smaller nodes  
        }
        
        return string.toString(); //return the string
    }
    public void clearTree(){ //garbage collection will handle the rest
        root = null;
    }
    
}
