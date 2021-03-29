import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        System.out.println("Hello World");
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
            boolean isDefinite = true;
            
            for(int i = 0; i < expression.length(); i++){
                if(Character.isWhitespace(expression.charAt(i))){
                    expression.deleteCharAt(i);
                    i--;
                }
            }

            if(expression.charAt(0) == '|'){
                isDefinite = false;
                expression.replace(0,1,"");
            }else{
                int breakPoint = 0;
                if(expression.charAt(0) == '-'){
                    lowerBounds += expression.charAt(0);
                    
                    for(int i = 1; i < expression.length(); i++){
                        if(expression.charAt(i) == '|'){
                            breakPoint = i;
                            break;
                        }else{
                            lowerBounds += expression.charAt(i);
                        }
                    }
                    expression.replace(0,breakPoint+1,"");
                }else{
                    lowerBounds = expression.substring(0,1);
                    expression.replace(0,2,"");
                }
                if(expression.charAt(0) == '-'){
                    upperBounds = expression.substring(0,1);
                    expression.replace(0,2,"");
                }else{
                    upperBounds = expression.substring(0,1);
                    expression.replace(0,1,"");
                }
            }
            expression.replace(expression.length()-2,expression.length(),""); //removes dx
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
                    System.out.println(payload.getIntegral());
                    Node<Payload> node= new Node<>(payload);
                    
                    
                    
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
            //if integral is definite, get binary tree for integral and pass it into an integral evaluator function
            //else print integral with a + C at the end
        }
    }
    public static int GCD(int a, int b){
        if (b==0) return a;
        return GCD(b,a%b);
    }
    public static Payload integrate(String coefficient, String exponent){
        String stringCoefficient = "";
        int intCoefficient = 1;
        int intExponent;
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
        if(operand.equals("-")){
            intCoefficient = Integer.parseInt(stringCoefficient) * -1;
        }else{
            intCoefficient = Integer.parseInt(stringCoefficient);    
        }
        
        if(exponent.equals("") && !variable.equals("")){
            intExponent = 1;
        }else if(exponent.equals("")) {
            intExponent = 0;
        }else {
            intExponent = Integer.parseInt(exponent);
        }

        intExponent += 1;
        if(exponent.equals("") && variable.equals("")){
            //hard coded, figure out a way to find what variable to add.
            if(intCoefficient == 1){
                coefficient = "x";
            }else{
                coefficient = Integer.toString(intCoefficient) + "x";    
            }
            
        }else if(intCoefficient == intExponent){
            coefficient = "";
        }else if(GCD(intCoefficient, intExponent) != 1){
            int divisor = GCD(intCoefficient, intExponent);
            int tempExponent = intExponent;
            intCoefficient = intCoefficient/divisor;
            if(divisor < 0){
                divisor *= -1;
            }
            tempExponent = tempExponent/divisor;
            
            if(tempExponent == 1){  //6/1
                coefficient = Integer.toString(intCoefficient);
            }else{ //1/6
                coefficient = "(" + intCoefficient + "/" + (tempExponent) + ")";
            }
        }else{
            coefficient = "(" + intCoefficient + "/" + (intExponent) + ")";
        }
        exponent = Integer.toString(intExponent);

        String finalIntegral = "";
        if(intExponent == 1){
            finalIntegral = coefficient + variable;
        }else if(intCoefficient == -1){
            finalIntegral = "-" + variable + "^" + exponent;
        }else{
            finalIntegral = coefficient + variable + "^" + exponent;
        }
        

        return new Payload(intCoefficient, intExponent, finalIntegral, variable, operand);
    }
    
}
