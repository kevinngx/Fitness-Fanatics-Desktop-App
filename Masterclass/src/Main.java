public class Main {


    public static void main(String[] args) {

        Calculator myCalculator = new Calculator("Kevin's Casio");
        Calculator shitCalculator = new Calculator();

        int a = Calculator.addNumbers(1, 2);
        int b = Calculator.multiply(3, 6);

        int c = myCalculator.divide(4,8);
        int d = myCalculator.addNumbers(a , b, c);
    }

}




