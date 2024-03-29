/*
 * Name: Varun Poondi
 * Net-ID: VMP190003
 * Prof: Jason Smith
 * Date: 3/30/2021
 * */

public class Node<N extends Comparable <N>> implements Comparable<Node<N>>{ 
    private Node<N> leftNode;
    private N payLoad;
    private Node<N> rightNode;

    public Node(N payLoad) {
        this.payLoad = payLoad;
        leftNode = null;
        rightNode = null;
    }

    /*****GETTERS AND SETTERS*****/
    public Node<N> getLeftNode() {
        return leftNode;
    }

    public void setLeftNode(Node<N> leftNode) {
        this.leftNode = leftNode;
    }

    public N getPayLoad() {
        return payLoad;
    }

    public void setPayLoad(N payLoad) {
        this.payLoad = payLoad;
    }

    public Node<N> getRightNode() {
        return rightNode;
    }

    public void setRightNode(Node<N> rightNode) {
        this.rightNode = rightNode;
    }

    @Override
    public int compareTo(Node<N> obj) { //compare to method will call the compareTo method in payLoad
       return this.payLoad.compareTo(obj.payLoad);
    }
}
