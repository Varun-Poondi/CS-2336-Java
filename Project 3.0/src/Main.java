/*
* Name: Varun Poondi
* Net-ID: VMP190003
* Prof: Jason Smith
* Date: 3/30/2021
* */
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static final BinTree<Payload> tree = new BinTree<>();
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        String fileName;
        fileName = input.next();
        File fileObj = new File(fileName);
        boolean fileIsReadable = false;
        
        //file readable checker
        while(!fileIsReadable) {
            try {
                if (!fileObj.canRead()) {
                    throw new FileNotFoundException();
                } else {
                    fileIsReadable = true;
                }
            } catch (Exception e) {
                System.out.println("File was not found, please try again.");
                fileName = input.next();
                fileObj = new File(fileName); //ask for file input again to be tested
            }
        }
        
        Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName))); //sc used to read the fileName
        while(sc.hasNextLine()){
            StringBuilder expression = new StringBuilder(sc.nextLine());
            String coefficient = "";
            String exponent = "";
            String upperBounds = "";
            String lowerBounds = "";
            boolean isTrig = false;
            removesWhiteSpaceUntilChar(expression);//removes all the whitespace until a char has been detected
            if(expression.charAt(0) == '|'){  //if this it first char, not a definite integral
                expression.replace(0,expression.indexOf("|")+1,"");  //delete chars until the bar
            }else{
                removesWhiteSpaceUntilChar(expression);
                lowerBounds = expression.substring(0,expression.indexOf("|")); //get the lowerBounds until we hit the bar
                lowerBounds = lowerBounds.replaceAll(" ",""); //delete the spaces in the string
                expression.replace(0,expression.indexOf("|")+1,""); //delete all the chars until the bar
                removesWhiteSpaceUntilChar(expression);
                upperBounds = expression.substring(0,expression.indexOf(" ")); //get the upperBound until a space is detected
                expression.replace(0,expression.indexOf(" ")+1,""); //delete until that space
            }
            expression = new StringBuilder(removeALlWhiteSpace(expression)); //remove all the the whitespace in the expression
            expression.replace(expression.length()-2,expression.length(),""); //removes dx

            
            boolean finishedExpression = false;
            int j = 0;
            int k = 0;
            boolean beginningOpNotFoundC = true;
            boolean foundCarrot = false;
            for(int i = 0; i < expression.length(); i ++){ //traverse through expression
                //operator (+/-) and index 0
                char debugChar = expression.charAt(i);
                if(debugChar == '^'){ //if carrot was detected we get the exponent
                    foundCarrot = true;
                    boolean beginningOpNotFoundE = true;
                    for(j = i+1; j < expression.length(); j++) { //traverse from the carrot + 1 to the end

                        if((expression.charAt(j) != '+' && expression.charAt(j) != '-')){ //if the char is not the first val after the carrot 
                            exponent += expression.charAt(j); //append to the exponent string
                            beginningOpNotFoundE = false; //was not found in the beginning
                        }else if(((expression.charAt(j) == '+' || expression.charAt(j) == '-'))){ //if found, that means we have found the full the exponent
                            if(beginningOpNotFoundE) { //not passed through previous if, break 
                                exponent += expression.charAt(j); //append to the expression
                                beginningOpNotFoundE = false; //set back to false
                            }else{
                                break;
                            }
                        }
                    } 
                    finishedExpression = true; //after you break, you are done getting the expression
                }else if((debugChar != '+' && debugChar != '-') && (debugChar != 's' && debugChar != 'c')){ // if the char at i is not an operator
                    
                    
                    coefficient +=  expression.charAt(i); //add to the operator
                    beginningOpNotFoundC = false; //the operator was not found in the beginning of the read
                    
                    
                } else if(debugChar == '+' || debugChar == '-'){ //if operator was detected
                   
                   
                    if(beginningOpNotFoundC) { //and if found in the beginning, append to the coefficient
                        coefficient += expression.charAt(i); 
                        beginningOpNotFoundC = false;
                    }else{
                        finishedExpression = true; //you have completed the expression
                    }
                    
                    
                }else {  //final case were trig must have been found
                    isTrig = true;  //set to true
                    for (k = i; k < expression.length(); k++) { //get whole expression and append to coefficient
                        if (expression.charAt(k) != '+' && expression.charAt(k) != '-') {
                            coefficient += expression.charAt(k);
                        } else {
                            break;
                        }
                    }
                    finishedExpression = true; //expression is complete
                }
                if(finishedExpression || (i+1) == expression.length()){//the expression for a node is done or we are at the end of the expression
                    if(isTrig){
                        i = k - 1;
                    }else if(foundCarrot) {
                        i = j - 1;  //update bounds 
                        foundCarrot = false;
                    }else if((i+1) != expression.length()){ //reset the index value to finish traversal
                        i-=1;
                    }
                    
                    //reset more values
                    finishedExpression = false;  
                    beginningOpNotFoundC = true;
                    Payload payload = new Payload(coefficient,exponent, isTrig); //added parsed values into new Payload
                    Node<Payload> node= new Node<>(payload); //and payLoad into a new Node

//                    if(isTrig){
//                        i = k - 1;
//                    }
                    //2x + 2x   -> x^2 + x^3   -> 2x^2
                    //3x + 2x -> 3/2x^2 + x^2

                    // search through the tree to see if their is a node that has the same coefficient
                    Node<Payload> checkNode = tree.Search(payload,tree.getRoot()); 
                    
                    if(checkNode != null && !checkNode.getPayLoad().isTrig()) { //return nulls if there was no similar node found
                        checkNode.getPayLoad().combinePayLoad(node.getPayLoad());
                    }else{
                        tree.Insert(node, tree.getRoot()); //insert into the tree
                    }
                    //reset coefficient and exponent
                    coefficient = "";
                    exponent = "";
                    isTrig = false;
                }
            }
            integrateEachNode(tree.getRoot()); //integrate each node in the tree
            deleteZeroCoefficients(tree.getRoot()); //delete any nodes that have a 0 coefficient value
            printFinalIntegral(lowerBounds, upperBounds); //print out the integrals depending on if it's definite or indefinite
            tree.clearTree(); // clear the tree
        }
        sc.close(); //close the scanner
    }
    
    public static String removeALlWhiteSpace(StringBuilder expression){
        String hold = expression.toString(); //convert expression into a string
        hold = hold.replaceAll("\\s", ""); //delete all whitespaces
        return hold; //return the string
    }
    
    public static void removesWhiteSpaceUntilChar(StringBuilder expression){ 
        for(int i = 0; i < expression.length(); i++){
            if(expression.charAt(i) != ' '){ //if a char was found
                expression.replace(0,i,""); //delete all of the char after that detection
                break; //break
            }
        }
    }
    
    
    public static void integrateEachNode(Node<Payload> currentNode){
        if(currentNode != null){
            Payload currentLoad = currentNode.getPayLoad(); //gets the currentNode to integrate
            currentLoad.integrate(); //integrate the currentNode
            integrateEachNode(currentNode.getLeftNode()); //move left
            integrateEachNode(currentNode.getRightNode()); //move right
        }
    }

    /*
     * ∫ 𝑥 = a|b x dx
     * o The endpoints of the interval for the definite integral will be integer values
     * o Do not assume a < b
     * */
    public static void printFinalIntegral(String lowerBound, String upperBound){
        String finalIntegral = tree.descendingOrderPrint(tree.getRoot()); //get the final integral expression
        StringBuilder format = new StringBuilder(finalIntegral);  
        if(!finalIntegral.equals("")) {  //do not print if there was no integral detected
            if (!lowerBound.equals("")) { //if the integral is definite
                float leftSide;
                float rightSide;
                float answer;
                
                int intLowerBound = Integer.parseInt(lowerBound);
                int intUpperBound = Integer.parseInt(upperBound);
                
//                rightSide = evaluateFullIntegral(tree.getRoot(), intLowerBound); //get evaluated expression from right side passing lowerBound
//                leftSide = evaluateFullIntegral(tree.getRoot(), intUpperBound); //get evaluated expression from the left side passing upperBound
//                
//                if(intUpperBound < intLowerBound){
//                    answer = rightSide - leftSide;
//                }else{
//                    answer = leftSide - rightSide;
//                }
                
                
                if(intUpperBound < intLowerBound){ //if the lowerBound is greater than the upperBound
                    //swap both values
                    int temp = intUpperBound;
                    intUpperBound = intLowerBound;
                    intLowerBound = temp;
                    leftSide = evaluateFullIntegral(tree.getRoot(), intLowerBound);  //get evaluated expression from left side passing lowerBound
                    rightSide = evaluateFullIntegral(tree.getRoot(), intUpperBound); //get evaluated expression from the right side passing upperBound
                }else {

                    rightSide = evaluateFullIntegral(tree.getRoot(), intLowerBound); //get evaluated expression from right side passing lowerBound
                    leftSide = evaluateFullIntegral(tree.getRoot(), intUpperBound); //get evaluated expression from the left side passing upperBound
                }
                
                
                
                
                
                answer = leftSide - rightSide; //save to the answer
                format.replace(format.length() - 1, format.length(), ""); 
                if (format.charAt(0) == '+') { //if the first char is a +
                    format.replace(0, 2, ""); //delete the +
                }
                if (format.charAt(0) == '-'&& format.indexOf("(") == 2) { //if the first char is a - and there is a fraction as the first term
                    format.insert(3, "-"); //insert inside the fraction
                    format.replace(0, 2, ""); //delete the -
                }

                System.out.print(format + ", " + lowerBound + "|" + upperBound + " = "); 
                System.out.format("%.3f\n", answer); //format the answer to the 100th float decimal place

            } else {
                if (format.charAt(0) == '+') {  //if the first char is a +
                    format.replace(0, 2, ""); //delete the +
                }
                if (format.charAt(0) == '-' && format.indexOf("(") == 2) { //if the first char is a - and there is a fraction as the first term
                    format.insert(3, "-"); //insert inside the fraction
                    format.replace(0, 2, ""); //delete the -
                }
                System.out.println(format + "+ C"); //print the integral with a + C
            }
        }else{
            System.out.println("0 + C");
        }
    }
    public static void deleteZeroCoefficients(Node<Payload> currentNode){
        if(currentNode != null){
            if(currentNode.getPayLoad().getCoefficient() == 0){ //if the current node's coefficient is 0
                tree.Delete(currentNode); //delete from the tree
            }
            deleteZeroCoefficients(currentNode.getLeftNode()); //move left
            deleteZeroCoefficients(currentNode.getRightNode()); // move right
        }
    }
    
    
    public static float evaluateFullIntegral(Node<Payload> currentNode, int bound){
        float total = 0; 
        if(currentNode == null){
            return 0;
        }else{
            total +=  evaluateFullIntegral(currentNode.getLeftNode(), bound); //get the total from the left subtree
            total +=  evaluateFullIntegral(currentNode.getRightNode(), bound); //get the total from the right subtree
            total += currentNode.getPayLoad().evaluateNodeIntegral(bound); //get the currentNode's evaluated value and add to the total
        }
        return total; //return the total
    }
}
