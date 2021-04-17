/*
 * Name: Varun Poondi
 * Net-ID: VMP190003
 * Prof: Jason Smith
 * Date: 3/30/2021
 * */

public class Payload implements Comparable<Payload> {
    private double coefficient;
    private double exponent;
    private double tIntNumber;
    private double tCofNumber;
    private String stringCoefficient = "";
    private String stringExponent = "";
    private String integral = "";
    private String variable = "";
    private String operator = "";
    private String trigNumberCoefficient = "";
    private String trigInteriorNumber = "";
    private String trigExpression = "";
    private boolean isTrig;
    public Payload(String coefficient, String exponent, boolean isTrig) {

        /*
         * After we get a payload, we need to parse the coefficient and exponent
         * We do the main parsing for the coefficient since it can have a plus or minus operator, a number, and/or a variable
         * This parse will help initialize variables stringCoefficient, stringExponent, and variable
         * */
        if (isTrig) { //trig has been detected
            for(int i = 0; i < coefficient.length(); i ++){
                char checker = coefficient.charAt(i);
                if (Character.isDigit(checker)) { //if a digit is detected add to to trigNumberCoefficient string
                    trigNumberCoefficient += coefficient.charAt(i);
                }else if(checker == 's' || checker == 'c'){ //if a trig name is detected, get full string
                    trigExpression += coefficient.substring(i);
                    break;
                }else if(checker == '+' || checker == '-'){ // get the operator 
                    operator += checker;
                }
            }
            for(int i = 0; i < trigExpression.length(); i++){
                char checker = trigExpression.charAt(i);
                if (Character.isDigit(checker)) {
                    trigInteriorNumber += checker;
                }
            }
            if (trigNumberCoefficient.isEmpty()) { //if no coefficient was detected
                this.tCofNumber = 1; //coefficient is 1
            } else {
                this.tCofNumber = Integer.parseInt(trigNumberCoefficient); //parse the number
            }
            if(trigInteriorNumber.isEmpty()){ 
                this.tIntNumber = 1; //default value
            }else{
                this.tIntNumber = Integer.parseInt(trigInteriorNumber); //else record the parsed value
            }
            
            
            this.isTrig = true;
            this.coefficient = tCofNumber;
            this.exponent = -1000000;  //using this arbitrary so that this will not get confused as a regular expression in search. Band-aid fix 
        } else {
            for (int i = 0; i < coefficient.length(); i++) {  //traverse through coefficient
                char checker = coefficient.charAt(i);
                if (Character.isDigit(checker)) {  //if the checker is a digit
                    stringCoefficient += coefficient.charAt(i); //append to stringCoefficient
                } else if (checker == '+' || checker == '-') { //if an operand was detected
                    this.operator += checker; //append to operand string
                } else {
                    variable += coefficient.charAt(i);  //the only other case is that a variable was detected
                }
            }
            if (stringCoefficient.isEmpty()) { //if no coefficient was detected
                this.coefficient = 1; //coefficient is 1
            } else {
                this.coefficient = Integer.parseInt(stringCoefficient); //parse the number
            }
            stringCoefficient = coefficient; //save the coefficient


            if (exponent.isEmpty() && !variable.isEmpty()) { //if there was no exponent, and there is a variable (ex: 4x)
                this.exponent = 1;
            } else if (exponent.isEmpty()) {  //if there was no exponent and variable  (ex: 4)
                this.exponent = 0;
            } else {
                this.exponent = Integer.parseInt(exponent); //save the exponent number
            }
            stringExponent = exponent;  //save the string exponent into class field
        }
    }


    /*****GETTERS AND SETTERS*****/
    public boolean isTrig() {
        return isTrig;
    }
    
    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }
    
    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }
    
    @Override
    public String toString() { //this will be used when printing out the payLoads final content
        return this.integral;
    }

    @Override
    public int compareTo(Payload obj) {
        //-1 current payLoad's exponent  is less than the obj payLoad's exponent
        // 0 current payLoad's exponent is equal to the obj payLoad's exponent
        //1 current payLoad's exponent  is greater than the obj payLoad's exponent 
        return Double.compare(this.exponent, obj.exponent);
    }
    /*
    * The purpose of this function is to merge the payLoad of another node that has the name coefficient
    * This is done with the help of the compareTo function
    * Only updates the coefficient since the exponent and variable aren't in need to be updated
    * */
    public void combinePayLoad(Payload checkLoad){
        double tempCoA;   
        double tempCoB;
        double totalCof;
        if(checkLoad.getOperator().equals("-") && this.operator.equals("-")){   //case where operators are negative
            tempCoB = checkLoad.getCoefficient(); 
            tempCoA = this.coefficient;
            totalCof = tempCoA + tempCoB; //since there are abs() we can just add them regularly 
            this.setOperator("-"); //set as negative
            this.setCoefficient(totalCof); 
        }else if(checkLoad.getOperator().equals("-") || this.operator.equals("-")){
            if(checkLoad.getOperator().equals("-")){
                tempCoB = checkLoad.getCoefficient() * -1;  //change to negative since payLoad is negative
                tempCoA = this.coefficient;
            } else {
                tempCoB = checkLoad.getCoefficient();
                tempCoA = this.coefficient * -1; //change to negative since payLoad is negative
            }
            totalCof = tempCoA + tempCoB; //find total
            if(totalCof < 0){
                this.setCoefficient(totalCof * -1); //reconvert it back to abs() value
                this.setOperator("-"); //set negative
            }else{ 
                this.setCoefficient(totalCof); //the number is already positive
                this.setOperator("+"); //set positive
            }
        } else{ //base case both numbers are positive
            tempCoB = checkLoad.getCoefficient();  
            tempCoA = this.coefficient;
            totalCof = tempCoA + tempCoB;  //get positive number
            this.setCoefficient(totalCof); //store coefficient
            this.setOperator("+"); //set positive
        }
    }
    
    /*this functions evaluates the this payload with the given bound from main */
    public double evaluateNodeIntegral(int bound){
        
        if(variable.isEmpty()){
            return coefficient;
        }
        if(operator.equals("- ")) {
            return (coefficient * -1) * Math.pow(bound, exponent);  //standard equation
        }
        return coefficient * Math.pow(bound, exponent);
    }
    
    public int GCD(int a, int b){ //find the greatest common denominator, standard algorithm
        if (b==0) return a; 
        return GCD(b,a%b); //modulus to find when the value equal zero, when it does, go through the stack and return a
    }
    
    public void integrate() {

        /*I made the coefficient and exponent to be doubles since
         * if we were to evaluate the expression in main, I want a precise decimal answer
         */
        if (isTrig) {
            if(tCofNumber == 1){
                trigNumberCoefficient = "";
            }else if(tIntNumber == 1){
                trigInteriorNumber = "";
            }
            
            
            if(!trigNumberCoefficient.isEmpty() && !trigInteriorNumber.isEmpty()){ //if both a coefficient and interior number exist for the trig expression
                int divisor = GCD((int) tCofNumber, (int) tIntNumber);
                
                double tempNumber = tIntNumber; //get the GCD value
                if(divisor != 1){ //as long as GCD is not 1, get values to simplify
                    tCofNumber = tCofNumber / divisor;
                    tempNumber = tempNumber / divisor;
                }
                if(trigExpression.charAt(0) == 's'){  //if sine
                    trigExpression = "(" + (int) tCofNumber + "/" + (int) tempNumber + ")cos " + (int) tIntNumber + "x"; //base set-up
                    if(tempNumber == 1){ //simplify to just coefficient
                        if(tCofNumber == 1){  //if the coefficient is 1, don't print the 1
                            trigExpression = "cos " + (int) tIntNumber + "x";
                        }else {
                            trigExpression = (int) tCofNumber + "cos " + (int) tIntNumber + "x"; //else regular case
                        }
                    }else if(tempNumber == -1){ 
                        if(tCofNumber == 1){ //if the coefficient is 1, don't print the 1
                            trigExpression = "cos " + (int) tIntNumber + "x";
                        }else {
                            trigExpression = (int) tCofNumber * -1 + "cos " + (int) tIntNumber + "x"; //else regular case
                        }
                    }
                    if(operator.equals("-")) {  //check for operation conflicts
                        operator = "+ ";
                    }else{
                        operator = "- ";
                    }
                }else if(trigExpression.charAt(0) == 'c'){ //if cosine
                    trigExpression = "(" + (int) tCofNumber + "/" + (int) tempNumber + ")sin " + (int) tIntNumber + "x";
                    if(tempNumber == 1){
                        if(tCofNumber == 1) { //if the coefficient is 1, don't print the 1
                            trigExpression = "sin " + (int) tIntNumber + "x";
                        }else{
                            trigExpression = (int) tCofNumber + "sin " + (int) tIntNumber + "x"; //else regular case
                        }
                    }else if(tempNumber == -1){ //if the coefficient is 1, don't print the 1
                        if(tCofNumber == 1) {
                            trigExpression = "sin " + (int) tIntNumber + "x";
                        }else{
                            trigExpression = (int) tCofNumber * -1 + "sin " + (int) tIntNumber + "x"; //else regular case
                        }
                    }
                    if(operator.equals("-")){ //check for operation conflicts
                        operator = "- ";
                    }else {
                        operator = "+ ";
                    }
                }
            }else if(trigNumberCoefficient.isEmpty() && trigInteriorNumber.isEmpty()){  //if both strings are recorded to be empty
                if(trigExpression.charAt(0) == 's'){ 
                    trigExpression = "cos x";  //set base case
                    if(operator.equals("-")) { //check for operation conflicts
                        operator = "+ ";
                    }else{
                        operator = "- ";
                    }
                }else if(trigExpression.charAt(0) == 'c'){
                    trigExpression = "sin x"; //set base case
                    if(operator.equals("-")){ //check for operation conflicts
                        operator = "- ";
                    }else{
                        operator = "+ ";    
                    }
                    
                }
            }else if(!trigNumberCoefficient.isEmpty()){ //if the coefficient is not empty by the interior number is
                if(trigExpression.charAt(0) == 's'){ 
                    trigExpression = trigNumberCoefficient + "cos x"; //append the string to the trig expression
                    if(operator.equals("-")) { //check for operation conflicts
                        operator = "+ ";
                    }else{
                        operator = "- ";
                    }
                }else if(trigExpression.charAt(0) == 'c'){
                    trigExpression = trigNumberCoefficient + "sin x"; //append the string to the trig expression
                    if(operator.equals("-")){ //check for operation conflicts
                        operator = "- ";
                    }else{
                        operator = "+ ";
                    }
                }
            } else {
                if(trigExpression.charAt(0) == 's'){ //if the coefficient is empty but the interior number exists
                    trigExpression = "(1/" + trigInteriorNumber + ")cos " + trigInteriorNumber + "x"; //format the coefficient to 1 and append the interior number to the trig expression
                    if(operator.equals("-")) { //check for operation conflicts
                        operator = "+ ";
                    }else{
                        operator = "- ";
                    }
                }else if(trigExpression.charAt(0) == 'c') {
                    trigExpression = "(1/" + trigInteriorNumber + ")sin " + trigInteriorNumber + "x"; //format the coefficient to 1 and append the interior number to the trig expression
                    if(operator.equals("-")){ //check for operation conflicts
                        operator = "- ";
                    }else{
                        operator = "+ ";
                    }
                }
            }
            integral = operator + trigExpression; //construct final integral and save 
            coefficient = tCofNumber; //save value for future reference
        } else {

            exponent += 1;
            if (stringExponent.isEmpty() && variable.isEmpty()) { //this will take care of cases such as 1 and 4
                if (coefficient == 1) {  // 1 integrates to x
                    stringCoefficient = "";
                } else { //4 integrates to 4 * x = 4x
                    stringCoefficient = "" + (int) coefficient;
                }
                variable = "x";  //initialize variable

            } else if (coefficient == exponent) {   //case where 5x^5 -> x^5 since 5/5 is 1
                stringCoefficient = "";
                coefficient = 1;
            } else {
                int divisor = GCD((int) coefficient, (int) exponent); //6x^2 -> (6/3)x^3 -> (2/1)x^3 
                double tempExponent = exponent;
                if (divisor != 1) { //precondition check
                    coefficient = coefficient / divisor; //update the coefficient
                    tempExponent = tempExponent / divisor; //update the tempExponent for future use
                }

                /*change everything to the positive number,
                 * this makes it easier to format since we can just add the specified operator outside the stringCoefficient
                 */
                stringCoefficient = "(" + Math.abs((int) coefficient) + "/" + Math.abs((int) (tempExponent)) + ")";

                if (tempExponent == 1) {  // case such as (6/1), we want 6 
                    stringCoefficient = Integer.toString((int) coefficient);
                } else if (tempExponent == -1) {// same case but if its negative
                    stringCoefficient = Integer.toString((int) coefficient * -1);
                }

                coefficient = coefficient / tempExponent; //save this for future use
            }

            stringExponent = Integer.toString((int) exponent); //get the double value and save it to the stringExponent 
            String finalIntegral;

            //operator collision cases
            if (operator.equals("+")) {
                //if something like + -5x^-3, this will resolve this case
                if (coefficient < 0) {
                    operator = "- ";
                    stringCoefficient = stringCoefficient.replace("-", "");
                } else {
                    operator += " "; //if not the case do regular case
                }
            } else if (operator.equals("-")) {
                //something like - +5x^3, resolve the case to (-)
                if (coefficient < 0) {
                    operator = "+ ";
                    stringCoefficient = stringCoefficient.replace("-", "");
                } else {
                    operator += " "; //if not the case do regular case
                }
            }
            if (operator.isEmpty()) { //default operator
                operator = "+ ";
            }
            if (stringExponent.equals("1")) { //if the exponent, is 1, don't print it
                stringExponent = "";
            } else {
                stringExponent = "^" + stringExponent;
            }

            if (!stringCoefficient.isEmpty() && stringCoefficient.charAt(0) == '-') {  //issue with these blocks of code
                operator = "- ";  //save the operator
                stringCoefficient = stringCoefficient.replace("-", "");
            }
            if (!stringCoefficient.isEmpty() && stringCoefficient.equals("1")) {
                stringCoefficient = stringCoefficient.replace("1", "");
            }

            //print the final integral
            finalIntegral = operator + stringCoefficient + variable + stringExponent;

            //save the parameters to obj
            integral = finalIntegral;
            stringExponent = "" + exponent;
            stringCoefficient = "" + coefficient;

            if (coefficient < 0) {
                coefficient *= -1;
            }

        }
    }
}
