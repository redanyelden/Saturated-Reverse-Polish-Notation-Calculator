import java.io.*;
import java.util.Stack;
import java.lang.Math;
import java.math.BigInteger;

public class SRPN {

    //fields
    private String storeUntilNotDigit = "";
    private int maxStackSize = 23;

    private int maxValue = 2147483647;
    private int minValue = -2147483648;

    private	BigInteger currentBI;
    private	BigInteger firstBI;
    private	BigInteger secondBI;
    private	BigInteger maxBI = BigInteger.valueOf(2147483647);
    private	BigInteger minBI = BigInteger.valueOf(-2147483648);

    private	long currentLong;
    private	long firstLong;
    private	long secondLong;

    private BigInteger octal = BigInteger.valueOf(0);
    private BigInteger charBI;
    private BigInteger octalTotal = BigInteger.valueOf(0);

    private Stack myStack = new Stack();

    //checks for max/min values
    public boolean checkMaxMin(BigInteger currentBI){
        //compares two BigIntegers
        if(currentBI.compareTo(maxBI) == 1) {
            myStack.push(maxValue);
            return false;
        }
        else if(currentBI.compareTo(minBI) == -1) {
            myStack.push(minValue);
            return false;
        }
        return true;
    }


    //addition
    public int add() {

        int a = (int) myStack.pop();
        int b = (int) myStack.pop();

        firstBI = BigInteger.valueOf(a);
        secondBI = BigInteger.valueOf(b);
        currentBI = firstBI.add(secondBI);

        //checks for max/min values
        if(checkMaxMin(currentBI)){
            myStack.push(a + b);
        }
        return 0;
    }

    //subtraction
    public int subtract() {

        int c = (int) myStack.pop();
        int b = (int) myStack.pop();

        firstBI = BigInteger.valueOf(b);
        secondBI = BigInteger.valueOf(c);
        currentBI = firstBI.subtract(secondBI);

        if(checkMaxMin(currentBI)){
            myStack.push(b - c);
        }
        return 0;
    }

    //multiplication
    public int multiply() {

        int c = (int) myStack.pop();
        int b = (int) myStack.pop();

        firstBI = BigInteger.valueOf(b);
        secondBI = BigInteger.valueOf(c);
        currentBI = firstBI.multiply(secondBI);

        if(checkMaxMin(currentBI)){
            myStack.push(b * c);
        }
        return 0;
    }

    //division
    public int divide() {
        int c = (int) myStack.pop();
        int b = (int) myStack.pop();

        myStack.push((int) (b / c));
        return 0;
    }

    //powers
    public int power() {

        int c = (int) myStack.pop();
        int b = (int) myStack.pop();

        firstLong = (long) b;
        secondLong = (long) c;
        firstBI = BigInteger.valueOf(b);

        currentLong = firstLong+secondLong;
        if(b == 0){
            myStack.push(0);
        }
        else if(b == 1){
            myStack.push(1);
        }

        else{

            //checks for max/min values
            if(currentLong > (long) maxValue) {
                myStack.push(maxValue);
            }
            else if(currentLong < (long) minValue) {
                myStack.push(minValue);
            }
            else{
                myStack.push((int) (Math.pow(b,c)));
            }
        }
        return 0;
    }

    //modulo
    public int modulo() {

        int c = (int) myStack.pop();
        int b = (int) myStack.pop();
        myStack.push(b % c);
        return 0;
    }


    //if there is a stack underflow false is returned
    public boolean checkStackUnderflow(){
        if(myStack.size()<=1) {
            System.out.println("Stack underflow.");
            return false;
        }
        return true;
    }

    //if there is a stack overflow false is returned
    public boolean checkStackOverflow(){
        if(myStack.size()>=maxStackSize) {
            System.out.println("Stack overflow.");
            return false;
        }
        return true;

    }

    //to push number on stack
    public boolean pushNumberOnStack(){
        //if a number has been entered
        if(storeUntilNotDigit != "") {
            if(myStack.size()>=maxStackSize) {
                System.out.println("Stack overflow.");
                storeUntilNotDigit = "";
                return false;
            }

            currentBI = new BigInteger(storeUntilNotDigit);

            if(storeUntilNotDigit.length()>1 && storeUntilNotDigit.charAt(0) == '0'){
                int tmp = 0;
                for(int k = storeUntilNotDigit.length()-1; k >0; k--){
                    charBI = BigInteger.valueOf(Character.getNumericValue(storeUntilNotDigit.charAt(k)));
                    octal = octal.add(charBI);
                    octal = octal.multiply((BigInteger.valueOf((int)Math.pow(8,tmp))));
                    octalTotal = octalTotal.add(octal);
                    octal = BigInteger.valueOf(0);
                    tmp++;
                }

                if(octal.compareTo(maxBI) == 1) {
                    myStack.push(maxValue);
                }
                else if(octal.compareTo(minBI) == -1) {
                    myStack.push(minValue);
                }
                else{
                    myStack.push(octalTotal.intValue());
                }
                octalTotal = BigInteger.valueOf(0);
            }

            //checks for max/min values
            else if(currentBI.compareTo(maxBI) == 1) {
                myStack.push(maxValue);
            }
            else if(currentBI.compareTo(minBI) == -1) {
                myStack.push(minValue);
            }
            else{
                myStack.push(Integer.parseInt(storeUntilNotDigit));
            }

            storeUntilNotDigit = "";
        }
        return true;
    }



    public void processCommand(String command){

        for(int i = 0; i < command.length(); i++) {

            //first checks if what was entered is a digit or not. if it is a digit:
            if(Character.isDigit(command.charAt(i))) {
                storeUntilNotDigit = storeUntilNotDigit+command.charAt(i);
            }
            //otherwise:
            else{
                //checking for negative numbers
                if(command.charAt(i) == '-') {
                    if(storeUntilNotDigit != ""){
                        myStack.push(Integer.parseInt(storeUntilNotDigit));
                    }
                    //if the first digit is a '-'
                    if(i < command.length()-1 && Character.isDigit(command.charAt(i+1))){
                        storeUntilNotDigit = "-";
                        //so that it doesn't stop after the first digit after the '-'
                        while(i < command.length()-1 && Character.isDigit(command.charAt(i+1))){
                            storeUntilNotDigit += command.charAt(i+1);
                            i++;
                        }
                    }
                }

                if(pushNumberOnStack()){
                    if(!Character.isDigit(command.charAt(i))){
                        //'#' is a comment
                        if(command.charAt(i) == '#') {
                            break;
                        }

                        //'d' prints stack
                        else if(command.charAt(i) == 'p') {
                            for(int j = 0; j < myStack.size(); j++) {
                                System.out.println(myStack.elementAt(j));
                            }
                        }

                        // '=' prints the top of the stack
                        else if (command.charAt(i) == '=') {
                            if(myStack.size() == 0) {
                                System.out.println("Stack empty.");
                            }
                            else {
                                System.out.println(myStack.peek());
                            }
                        }

                        else if(command.charAt(i) == '+'){
                            if(checkStackUnderflow()){
                                add();
                            }
                        }
                        else if(command.charAt(i) == '-'){
                            if(checkStackUnderflow()){
                                subtract();
                            }
                        }
                        else if(command.charAt(i) == '*'){
                            if(checkStackUnderflow()){
                                multiply();
                            }
                        }
                        else if(command.charAt(i) == '/'){
                            if(checkStackUnderflow()){
                                if((int) myStack.peek() == 0) {
                                    System.out.println("Divide by 0.");
                                }
                                else{
                                    divide();
                                }
                            }
                        }
                        else if(command.charAt(i) == '^'){
                            if(checkStackUnderflow()){
                                if((int) myStack.peek() < 0) {
                                    System.out.println("Negative power.");
                                }
                                else{
                                    power();
                                }
                            }

                        }
                        else if(command.charAt(i) == '%'){
                            if(checkStackUnderflow()){
                                if((int) myStack.peek() == 0) {
                                    System.out.println("Divide by 0.");
                                }
                                else{
                                    modulo();
                                }
                            }
                        }

                        // for unrecognised input
                        else if(command.charAt(i) != ' ') {
                            System.out.println("Unrecognised operator or operand \""+command.charAt(i)+"\"");
                        }
                    }
                }
            }
        }
        // for when it doesn't go through the else statement above (i.e. when two numbers are typed after each other on new lines)
        pushNumberOnStack();
    }



    public static void main(String[] args) {

        SRPN srpn = new SRPN();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        //stack
        Stack myStack = new Stack();

        try {
            //Keep on accepting input from the command-line
            while(true) {
                String command = reader.readLine();

                //Close on an End-of-file (EOF) (Ctrl-D on the terminal)
                if(command == null)
                {
                    //Exit code 0 for a graceful exit
                    System.exit(0);
                }

                srpn.processCommand(command);
            }
        }catch(IOException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }

}