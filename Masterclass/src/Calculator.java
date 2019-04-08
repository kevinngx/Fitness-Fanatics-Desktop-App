public class Calculator {

    String name;

    public Calculator(String name) {
        this.name = name;
    }

    public Calculator() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static int addNumbers(int a, int b) {
        return  a + b;
    }

    public int addNumbers(int a, int b, int c) {
        return a + b + c;
    }

    public int divide(int numerator, int denominator) {
        return numerator / denominator;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

}




