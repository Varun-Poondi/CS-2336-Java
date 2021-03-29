public class BinTree<T extends Comparable<T>>{
    private Node<T> root;

    public Node<T> getRoot() {
        return root;
    }

    public Node<T> Insert(Node<T> newNode, Node<T> currentNode){
        if(root == null){
            root = newNode;
        }else{
            if(newNode.compareTo(currentNode) <= 0){
                if(currentNode.getLeftNode() == null){
                    currentNode.setLeftNode(newNode);
                }else{
                    Insert(newNode, currentNode.getLeftNode());
                }
            }else{
               if(currentNode.getRightNode() == null){
                   currentNode.setRightNode(newNode);
               }else{
                   Insert(newNode, currentNode.getRightNode());
               }
            }
        }
        return root;
    }
    public void Delete(){
        
    }
    public Node<T> Search(T payLoad, Node<T> node){
        return null;
    }
    public void InOrderTraversal(){
        
    }
}
