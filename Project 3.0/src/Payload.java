public class Payload implements Comparable<Payload> {
    private double coefficient;
    private double exponent;
    private String integral;
    private String variable;
    private String operator;
    
    public Payload(double coefficient, double exponent, String integral, String variable, String operator) {
        this.coefficient = coefficient;
        this.exponent = exponent;
        this.integral = integral;
        this.variable = variable;
        this.operator = operator;
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
}
