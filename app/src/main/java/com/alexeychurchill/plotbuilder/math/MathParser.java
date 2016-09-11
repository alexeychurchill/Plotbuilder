package com.alexeychurchill.plotbuilder.math;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Mathematical parser.
 */

public class MathParser {
    public static final String LOG_TAG = "MathParser";
    private List<String> postfix;
    private Map<String, Double> variables = new HashMap<>();

    public boolean parse(String source) {
        List<String> tokenList = splitTokens(source);
        if (tokenList == null) {
            return false;
        }
        postfix = postfixNotation(tokenList);
        return postfix != null;
    }

    public void setVariable(String name, double value) {
        variables.put(name, value);
    }

    public void unsetVariable(String name) {
        variables.remove(name);
    }

    public double getVariable(String name) {
        return variables.get(name);
    }

    //TODO: Calculation
    public double calculate() {
        Deque<Double> stack = new ArrayDeque<>();
        for (String token : postfix) {
            if (Utils.isNumber(token)) { //Number
                double tokenNumber = Double.parseDouble(token);
                stack.push(tokenNumber);
                continue;
            }
            if (token.length() == 1 && Utils.isOperation(token.charAt(0))) { //Operation
                double a, b, result = 0.0;
                if (stack.size() < 2) {
                    return 0.0;
                }
                b = stack.pop();
                a = stack.pop();
                switch (token.charAt(0)) {
                    case '+':
                        result = a + b;
                        break;
                    case '-':
                        result = a - b;
                        break;
                    case '*':
                        result = a * b;
                        break;
                    case '/':
                        result = a / b;
                        break;
                    case '^':
                        result = Math.pow(a, b);
                        break;
                }
                stack.push(result);
                continue;
            }
            if (Utils.isFunction(token)) { //Function
                if (stack.size() < 1) {
                    return 0.0;
                }
                double a = stack.pop();
                double result = calculateFunction(token, a);
                stack.push(result);
                continue;
            }
            if (variables.containsKey(token)) { //Variable
                double variableValue = variables.get(token);
                stack.push(variableValue);
                continue;
            }
            return 0.0; //TODO: Error handling!
        }
        if (stack.size() == 1) {
            return stack.pop();
        } else {
            return 0.0;
        }
    }

    private double calculateFunction(String function, double arg) {
        if (function.contentEquals("abs")) {
            return Math.abs(arg);
        }
        if (function.contentEquals("sin")) {
            return Math.sin(arg);
        }
        if (function.contentEquals("cos")) {
            return Math.cos(arg);
        }
        if (function.contentEquals("tg")) {
            return Math.tan(arg);
        }
        if (function.contentEquals("ctg")) {
            return 1.0 / Math.tan(arg);
        }
        if (function.contentEquals("lg")) {
            return Math.log10(arg);
        }
        if (function.contentEquals("ln")) {
            return Math.log(arg);
        }
        return 0.0;
    }

    private List<String> postfixNotation(List<String> tokenList) {
        //TODO: Error checking
        //TODO: String tokens -> good tokens
        List<String> postfix = new LinkedList<>();
        Deque<String> stack = new ArrayDeque<>();
        String buffer = "";
        for (String token : tokenList) {
            if (token.length() == 1) { //Parenthesis, operation or 1-char variable
                char chr = token.charAt(0);
                if (chr == '(') { //Opening parenthesis met
                    stack.push(String.valueOf(chr));
                    if (buffer.length() > 0) { //We have function name in the buffer
                        stack.push(buffer);
                        buffer = "";
                    }
                }
                if (buffer.length() > 0) { //If this wasn't the function
                    postfix.add(buffer);
                    buffer = "";
                }
                if (chr == ')') { //Closing parenthesis met
                    while (!stack.isEmpty()) {
                        if (stack.peek().contentEquals("(")) {
                            break;
                        }
                        postfix.add(stack.pop());
                    }
                    if (!stack.isEmpty()) { //Popping "("
                        stack.pop();
                    }
                }
                if (Utils.isOperation(chr)) { //Operation met
                    if (stack.isEmpty()) { //Stack is empty
                        stack.push(String.valueOf(chr));
                    } else {
                        if (stack.peek().length() > 1) {
                            stack.push(String.valueOf(chr));
                        } else {
                            char stackOperation = stack.peek().charAt(0);
                            if (Utils.operationPriority(chr) > Utils.operationPriority(stackOperation)) {
                                stack.push(String.valueOf(chr));
                            }
                            if (Utils.operationPriority(chr) <= Utils.operationPriority(stackOperation)) {
                                while (!stack.isEmpty()) {
                                    postfix.add(stack.pop());
                                    if (stack.isEmpty()) {
                                        break;
                                    }
                                    if (stack.peek().length() > 1) {
                                        break;
                                    }
                                    stackOperation = stack.peek().charAt(0);
                                    if (stackOperation == '(') {
                                        break;
                                    }
                                    if (Utils.operationPriority(chr) > Utils.operationPriority(stackOperation)) {
                                        break;
                                    }
                                }
                                stack.push(String.valueOf(chr));
                            }
                        }
                    }
                }
                if (Character.isLetter(chr)) { //1-Char variable
                    postfix.add(String.valueOf(chr));
                }
                if (Character.isDigit(chr)) { //Digit
                    postfix.add(String.valueOf(chr));
                }
            } else {
//                postfix.add(token);
                buffer = token;
            }
        }
        if (buffer.length() > 0) {
            postfix.add(buffer);
        }
        while (!stack.isEmpty()) {
            postfix.add(stack.pop());
        }
        return postfix;
    }

    private List<String> splitTokens(String source) {
        List<String> tokenList = new LinkedList<>();
        int parsePointer = 0;
        boolean incrementNeeded;
        while (parsePointer < source.length()) {
            char chr = source.charAt(parsePointer);
            incrementNeeded = true;
            if (chr == '(') { //Opening parenthesis
                tokenList.add(String.valueOf(chr));
            }
            if (chr == ')') { //Closing parenthesis
                tokenList.add(String.valueOf(chr));
            }
            if (Utils.isOperation(chr)) { //Operation
                tokenList.add(String.valueOf(chr));
            }
            if (Character.isDigit(chr)) { //Digit is met, it can be NUMBER
                String number = Utils.parseNumber(source, parsePointer);
                parsePointer += number.length();
                tokenList.add(number);
                incrementNeeded = false;
            }
            if (Character.isLetter(chr)) { //Variable or function
                String charSequence = Utils.parseCharSequence(source, parsePointer);
                parsePointer += charSequence.length();
                tokenList.add(charSequence);
                incrementNeeded = false;
            }
            if (incrementNeeded) {
                parsePointer++;
            }
        }
        return tokenList;
    }

    public static class Utils {
        /*
        * Parses char sequence - variable or function name
        * */
        public static String parseCharSequence(String in, int start) {
            StringBuilder charSequenceBuilder = new StringBuilder();
            int pointer = start;
            while (pointer < in.length()) {
                char toBeParsed = in.charAt(pointer++);
                if (Character.isLetter(toBeParsed)) {
                    charSequenceBuilder.append(toBeParsed);
                } else {
                    break;
                }
            }
            return charSequenceBuilder.toString();
        }
        /*
        * Parses number
        * */
        public static String parseNumber(String in, int start) {
            StringBuilder numberBuilder = new StringBuilder();
            int pointer = start;
            while (pointer < in.length()) {
                char toBeParsed = in.charAt(pointer++);
                if (Character.isDigit(toBeParsed) || toBeParsed == '.') {
                    numberBuilder.append(toBeParsed);
                } else {
                    break;
                }
            }
            return numberBuilder.toString();
        }
        /*
        * Checks if input string is function
        * */
        public static boolean isFunction(String s) {
            String[] funcs = {"abs", "sin", "cos", "tg", "ctg", "lg", "ln"};
            for (String func : funcs) {
                if (func.contentEquals(s.toLowerCase())) {
                    return true;
                }
            }
            return false;
        }
        /*
        * Checks if input string is a number
        * */
        public static boolean isNumber(String s) {
            for (char chr : s.toCharArray()) {
                if (!(Character.isDigit(chr) || chr == '.')) {
                    return false;
                }
            }
            return true;
        }
        /*
        * Checks if char c is mathematical operation
        * */
        public static boolean isOperation(char c) {
            switch (c) {
                case '+':
                case '-':
                case '*':
                case '/':
                case '^':
                    return true;
            }
            return false;
        }
        /*
        * Returns priority of operation
        * */
        public static int operationPriority(char operation) {
            switch (operation) {
                case '+':
                case '-':
                    return 0;
                case '*':
                case '/':
                    return 1;
                case '^':
                    return 2;
            }
            return -1;
        }
    }
}
