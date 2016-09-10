package com.alexeychurchill.plotbuilder.math;

import android.util.Log;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 * Mathematical parser.
 */

public class MathParser {
    public static final String LOG_TAG = "MathParser";
    private final String equationSource;

    public MathParser(String equationSource) {
        this.equationSource = equationSource;
    }

    public boolean parse() {
        List<String> tokenList = splitTokens(equationSource);
        //...
//        for (String token : tokenList) {
//            Log.d(LOG_TAG, "Token: ".concat(token));
//        }
        //...
//        Log.d(LOG_TAG, "----------------------------------");
        List<String> postfixList = postfixNotation(tokenList);
        for (String token : postfixList) {
            Log.d(LOG_TAG, "Postfix token: ".concat(token));
        }
        //...
        return true;
    }

    private List<String> postfixNotation(List<String> tokenList) {
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
