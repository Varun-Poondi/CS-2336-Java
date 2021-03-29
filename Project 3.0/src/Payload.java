public class Payload implements Comparable<Payload> {
    private int coefficient;
    private int exponent;
    private String integral;
    private String variable;
    private String operator;
    public Payload(int coefficient, int exponent, String integral, String variable, String operator) {
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

    public int getCoefficient() {
        return coefficient;
    }

    public void setCoefficient(int coefficient) {
        this.coefficient = coefficient;
    }

    public int getExponent() {
        return exponent;
    }

    public void setExponent(int exponent) {
        this.exponent = exponent;
    }

    public String getIntegral() {
        return integral;
    }

    public void setIntegral(String integral) {
        this.integral = integral;
    }

    @Override
    public int compareTo(Payload obj) {
        //-1 current payLoad  is less than the obj
        // 0 
        return Integer.compare(this.exponent, obj.exponent);


    }
}
