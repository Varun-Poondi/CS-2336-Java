/*
* Name: Varun Poondi
* Net-ID: VMP190003
* Prof: Jason Smith
* Date: 3/30/2021
* 
* */


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static final BinTree<Payload> tree = new BinTree<>();
    public static void main(String[] args) throws FileNotFoundException {
        Scanner input = new Scanner(System.in);
        String fileName;
        fileName = input.next();
        Scanner sc = new Scanner(new BufferedReader(new FileReader(fileName))); //sc used to read the fileName

        while(sc.hasNextLine()){
            StringBuilder expression = new StringBuilder(sc.nextLine());
            String coefficient = "";
            String exponent = "";
            String upperBounds = "";
            String lowerBounds = "";
            
            removesWhiteSpace(expression);
            if(expression.charAt(0) == '|'){
                expression.replace(0,expression.indexOf("|")+1,"");
            }else{
                removesWhiteSpace(expression);
                lowerBounds = expression.substring(0,expression.indexOf("|"));
                lowerBounds = lowerBounds.replaceAll(" ","");
                expression.replace(0,expression.indexOf("|")+1,"");
                removesWhiteSpace(expression);
                upperBounds = expression.substring(0,expression.indexOf(" "));
                expression.replace(0,expression.indexOf(" ")+1,"");
               
            }
            removeALlWhiteSpace(expression);
            expression.replace(expression.length()-2,expression.length(),""); //removes dx
//            removesWhiteSpace(expression);
            
            
            
            boolean finishedExpression = false;
            int j = 0;
            boolean beginningOpNotFoundC = true;
            boolean foundCarrot = false;
            for(int i = 0; i < expression.length(); i ++){ //traverse through expression
                //operator (+/-) and index 0
                if(expression.charAt(i) == '^'){
                    foundCarrot = true;
                    boolean beginningOpNotFoundE = true;
                    for(j = i+1; j < expression.length(); j++) {

                        if((expression.charAt(j) != '+' && expression.charAt(j) != '-')){ //^3x + 
                            exponent += expression.charAt(j);
                            beginningOpNotFoundE = false;
                        }else if(((expression.charAt(j) == '+' || expression.charAt(j) == '-'))){
                            if(beginningOpNotFoundE) {
                                exponent += expression.charAt(j);
                                beginningOpNotFoundE = false;
                            }else{
                                break;
                            }
                        }
                    }
                    finishedExpression = true;
                }else if((expression.charAt(i) != '+' && expression.charAt(i) != '-')){
                    coefficient +=  expression.charAt(i);
                    beginningOpNotFoundC = false;
                }else if(((expression.charAt(i) == '+' || expression.charAt(i) == '-'))){
                    if(beginningOpNotFoundC) {
                        coefficient += expression.charAt(i);
                        beginningOpNotFoundC = false;
                    }else{
                        finishedExpression = true;
                    }
                }
                if(finishedExpression || (i+1) == expression.length()){
                    finishedExpression = false;
                    beginningOpNotFoundC = true;

                    // integrate expression
                    // add payload to node
                    // add node to the tree
                    Payload payload = integrate(coefficient, exponent);
             
                    //System.out.println(payload.getIntegral());
                    Node<Payload> node= new Node<>(payload);
                    tree.Insert(node, tree.getRoot());
                    
                    
                    //reset variables
                    coefficient = "";
                    exponent = "";
                    if(foundCarrot) {
                        i = j - 1;  //update bounds
                        foundCarrot = false;
                    }else if((i+1) != expression.length()){
                        i-=1;
                    }
                }
            }
            printFinalIntegral(lowerBounds, upperBounds);
            tree.clearTree();
            //if integral is definite, get binary tree for integral and pass it into an integral evaluator function
            //else print integral with a + C at the end
        }
    }
    public static void removeALlWhiteSpace(StringBuilder expression){
        for(int i = 0; i < expression.length(); i++){
            if(Character.isWhitespace(expression.charAt(i))){
                expression.deleteCharAt(i);
                i--;
            }
        }
    }
    public static void removesWhiteSpace(StringBuilder expression){
        for(int i = 0; i < expression.length(); i++){
            if(expression.charAt(i) != ' '){
                expression.replace(0,i,"");
                break;
            }
        }
    }
    public static double GCD(double a, double b){
        if (b==0) return a;
        return GCD(b,a%b);
    }
    public static Payload integrate(String coefficient, String exponent){
        String stringCoefficient = "";
        double doubleCoefficient;
        double doubleExponent;

        String variable = "";
        String operand = "";
        for(int i = 0; i < coefficient.length(); i++){
            char checker = coefficient.charAt(i);
            if(Character.isDigit(checker)){
                stringCoefficient += coefficient.charAt(i);
            }else if(checker == '+' || checker == '-'){
                operand += checker;
            }else{
                variable += coefficient.charAt(i);
            }
        }
        if(stringCoefficient.equals("")) {
            doubleCoefficient = 1;
        }else{
            doubleCoefficient = Integer.parseInt(stringCoefficient);
        }

        if(exponent.equals("") && !variable.equals("")){
            doubleExponent = 1;
        }else if(exponent.equals("")) {
            doubleExponent = 0;
        }else {
            doubleExponent = Integer.parseInt(exponent);
        }

        doubleExponent += 1;
        if(exponent.equals("") && variable.equals("")){
            //hard coded, figure out a way to find what variable to add.
            if(doubleCoefficient == 1){
                coefficient = "x";
            }else{
                coefficient = (int) doubleCoefficient + "x";
            }
            variable = "x";

        }else if(doubleCoefficient == doubleExponent){
            coefficient = "";
            doubleCoefficient = 1;
        }else if(GCD(doubleCoefficient, doubleExponent) != 1){
            double divisor = GCD(doubleCoefficient, doubleExponent);
            double tempExponent = doubleExponent;
            doubleCoefficient = doubleCoefficient/divisor;
            tempExponent = tempExponent/divisor;

            if(tempExponent == 1){  //6/1
                coefficient = Integer.toString((int) doubleCoefficient);
            }else if(tempExponent == -1) {
                coefficient = Integer.toString((int)doubleCoefficient* -1);
            }else{ //1/6

                coefficient = "(" + (int) doubleCoefficient + "/" + (int) (tempExponent) + ")";
                doubleCoefficient =  doubleCoefficient / tempExponent;

            }
        }else{
            coefficient = "(" + (int) doubleCoefficient + "/" + (int) (doubleExponent) + ")";
            doubleCoefficient = doubleCoefficient / doubleExponent;
        }
        exponent = Integer.toString((int) doubleExponent);

        String finalIntegral = "";
        if(doubleExponent == 1 && !operand.equals("")){
            finalIntegral = operand + " " + coefficient;
        }else if(doubleExponent == 1){
            finalIntegral = coefficient + variable;
        }else if(doubleCoefficient == -1){
            finalIntegral = "- " + variable + "^" + exponent;
        }else if(doubleCoefficient < -1) {
            coefficient = Integer.toString((int) doubleCoefficient * -1);
            finalIntegral = "- " + coefficient + variable + "^" + exponent;
        
        }else if(operand.equals("+") ||operand.equals("-")){
            finalIntegral = operand + " " + coefficient + variable + "^" + exponent;
        }else{
            finalIntegral = coefficient + variable + "^" + exponent;
        }
        
        return new Payload(doubleCoefficient, doubleExponent, finalIntegral, variable, operand);
    }
    public static void printFinalIntegral(String lowerBound, String upperBound){
        String finalIntegral = tree.postOrderTraversal("",tree.getRoot());
        StringBuilder format = new StringBuilder(finalIntegral);       
        if(!lowerBound.equals("")) {
            float leftSide;
            float rightSide;
            int intLowerBound = Integer.parseInt(lowerBound);
            int intUpperBound = Integer.parseInt(upperBound);
            
            rightSide = evaluateFullIntegral(tree.getRoot(), intLowerBound);
            leftSide = evaluateFullIntegral(tree.getRoot(), intUpperBound);
            
            float answer = leftSide - rightSide;
            format.replace(format.length()-1, format.length(),"");
            if(format.charAt(0) == '+'){
                format.replace(0,2,"");
            }
            if(format.charAt(0) == '-'){
                format.insert(3,"-");
            }
            
            System.out.print(format + ", " + lowerBound + "|" + upperBound + " = ");
            System.out.format("%.3f\n",answer);
            
        }else{
            if(format.charAt(0) == '+'){
                format.replace(0,2,"");
            }
            if(format.charAt(0) == '-'){
                format.insert(3,"-");
                format.replace(0,format.indexOf("("),"");
            }
            System.out.println(format + "+ C");
        }
    }
    
    
    public static float evaluateFullIntegral(Node<Payload> currentNode, int bound){
        float total = 0;
        if(currentNode == null){
            return 0;
        }else{
            total +=  evaluateFullIntegral(currentNode.getLeftNode(), bound);
            total +=  evaluateFullIntegral(currentNode.getRightNode(), bound);
            total += currentNode.getPayLoad().evaluateNodeIntegral(bound);

            //System.out.println(total);
        }
        return total;
    }
    
    
}
