public class Payload implements Comparable<Payload> {
    private double coefficient;
    private double exponent;
    private String integral;
    private String variable;
    private String operator;
    
    public Payload(String coefficient, String exponent) {
        integrate(coefficient, exponent);
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getVariable() {
        return variable;
    }

    public void setVariable(String variable) {
        this.variable = variable;
    }

    public double getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getExponent() {
        return exponent;
    }

    public void setExponent(double exponent) {
        this.exponent = exponent;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }
    

    @Override
    public String toString() {
        
        return integral;
    }

    @Override
    public int compareTo(Payload obj) {
        //-1 current payLoad  is less than the obj
        // 0 
        return Double.compare(this.exponent, obj.exponent);


    }
    
    public double evaluateNodeIntegral(int bound){
        if(variable.equals("")){
            return coefficient;
        }
        return coefficient*Math.pow(bound, exponent);
    }
    
    public double GCD(double a, double b){
        if (b==0) return a;
        return GCD(b,a%b);
    }
    
    public void integrate(String coefficient, String exponent){
        String stringCoefficient = "";
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
            this.coefficient = 1;
        }else{
            this.coefficient = Integer.parseInt(stringCoefficient);
        }

        if(exponent.equals("") && !variable.equals("")){
            this.exponent = 1;
        }else if(exponent.equals("")) {
            this.exponent = 0;
        }else {
            this.exponent = Integer.parseInt(exponent);
        }

        this.exponent += 1;
        if(exponent.equals("") && variable.equals("")){
            //hard coded, figure out a way to find what variable to add.
            if(this.coefficient == 1){
                coefficient = "x";
            }else{
                coefficient = (int) this.coefficient + "x";
            }
            variable = "x";
            
        }else if(this.coefficient == this.exponent){
            coefficient = "";
            this.coefficient = 1;
        }else if(GCD(this.coefficient, this.exponent) != 1){
            double divisor = GCD(this.coefficient, this.exponent);
            double tempExponent = this.exponent;
            this.coefficient = this.coefficient/divisor;
            tempExponent = tempExponent/divisor;

            if(tempExponent == 1){  //6/1
                coefficient = Integer.toString((int) this.coefficient);
            }else if(tempExponent == -1) {
                coefficient = Integer.toString((int)this.coefficient* -1);
            }else{ //1/6

                coefficient = "(" + (int) this.coefficient + "/" + (int) (tempExponent) + ")";
                this.coefficient =  this.coefficient / tempExponent;

            }
        }else{
            coefficient = "(" + (int) this.coefficient + "/" + (int) (this.exponent) + ")";
            this.coefficient = this.coefficient / this.exponent;
        }
        exponent = Integer.toString((int) this.exponent);

        String finalIntegral = "";
        if(this.exponent == 1 && !operand.equals("")){
            finalIntegral = operand + " " + coefficient;
        }else if(this.exponent == 1){
            finalIntegral = coefficient + variable;
        }else if(this.coefficient == -1){
            finalIntegral = "- " + variable + "^" + exponent;
        }else if(this.coefficient < -1) {
            coefficient = Integer.toString((int) this.coefficient * -1);
            finalIntegral = "- " + coefficient + variable + "^" + exponent;

        }else if(operand.equals("+") ||operand.equals("-")){
            finalIntegral = operand + " " + coefficient + variable + "^" + exponent;
        }else{
            finalIntegral = coefficient + variable + "^" + exponent;
        }
        this.integral = finalIntegral;
        this.operator = operand;
        this.variable = variable;

    }
}
