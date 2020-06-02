package com.company;

import java.math.BigDecimal;
import java.util.Scanner;
import java.util.*;

public class Calc {
    public static Map<String, Integer> MAIN_MATH_OPERATIONS;

    static{
        MAIN_MATH_OPERATIONS = new HashMap<String, Integer>();
        MAIN_MATH_OPERATIONS.put("*", 1);
        MAIN_MATH_OPERATIONS.put("/", 1);
        MAIN_MATH_OPERATIONS.put("+", 2);
        MAIN_MATH_OPERATIONS.put("-", 2);
    }
    public static String sortingStation (String expression, Map<String, Integer> operation, String leftBracket, String rightBracket) {
        if (expression == null || expression.length() == 0)
            throw new IllegalStateException("Выражение не указано.");
        if (expression == null || operation.isEmpty())
            throw new IllegalStateException("Операции не указаны");
        List<String> out = new ArrayList<String>();
        Stack<String> stack = new Stack<String>();
        expression = expression.replace(" ","");
        Set<String> operationSymbols = new HashSet<String>(operation.keySet());
        operationSymbols.add(leftBracket);
        operationSymbols.add(rightBracket);
        int index = 0;
        boolean findNext = true;
        while(findNext) {
            int nextOperationIndex = expression.length();
            String nextOperation = "";
            for (String operat : operationSymbols) {
                int i = expression.indexOf(operat, index);
                if (i >= 0 && i < nextOperationIndex) {
                    nextOperation = operat;
                    nextOperationIndex = i;
                }
            }
            if (nextOperationIndex == expression.length()) {
                findNext = false;
            } else {
                if (index != nextOperationIndex) {
                    out.add(expression.substring(index, nextOperationIndex));
                }
                if (nextOperation.equals(leftBracket)) {
                    stack.push(nextOperation);
                }
                else if (nextOperation.equals(rightBracket)) {
                    while (!stack.peek().equals(leftBracket)) {
                        out.add(stack.pop());
                        if (stack.empty()) {
                            throw new IllegalArgumentException("Unmatched brackets");
                        }
                    }
                    stack.pop();
                }
                else {
                    while (!stack.empty() && !stack.peek().equals(leftBracket) &&
                            (operation.get(nextOperation) >= operation.get(stack.peek()))) {
                        out.add(stack.pop());
                    }
                    stack.push(nextOperation);
                }
                index = nextOperationIndex + nextOperation.length();
            }
        }
        // Добавление в выходную строку операндов после последнего операнда.
        if (index != expression.length()) {
            out.add(expression.substring(index));
        }
        // Пробразование выходного списка к выходной строке.
        while (!stack.empty()) {
            out.add(stack.pop());
        }
        StringBuffer result = new StringBuffer();
        if (!out.isEmpty())
            result.append(out.remove(0));
        while (!out.isEmpty())
            result.append(" ").append(out.remove(0));

        return result.toString();
    }


    public static String sortingStation(String expression, Map<String, Integer> operations) {
        return sortingStation(expression, operations, "(", ")");
    }

    public static BigDecimal calculateExpression(String expression) {
        String rpn = sortingStation(expression, MAIN_MATH_OPERATIONS);
        StringTokenizer tokenizer = new StringTokenizer(rpn, " ");
        Stack<BigDecimal> stack = new Stack<BigDecimal>();
        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            // Операнд.
            if (!MAIN_MATH_OPERATIONS.keySet().contains(token)) {
                stack.push(new BigDecimal(token));
            } else {
                BigDecimal operand2 = stack.pop();
                BigDecimal operand1 = stack.empty() ? BigDecimal.ZERO : stack.pop();
                if (token.equals("*")) {
                    stack.push(operand1.multiply(operand2));
                } else if (token.equals("/")) {
                    stack.push(operand1.divide(operand2));
                } else if (token.equals("+")) {
                    stack.push(operand1.add(operand2));
                } else if (token.equals("-")) {
                    stack.push(operand1.subtract(operand2));
                }
            }
        }
        if (stack.size() != 1)
            throw new IllegalArgumentException("Expression syntax error.");
        return stack.pop();
    }
    private void ExpressionUtils() {
    }
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        String expression = scan.nextLine();
        System.out.println("Инфиксная нотация:         " + expression);
        System.out.println("\tРезультат 3");
        String rpn = sortingStation(expression, MAIN_MATH_OPERATIONS);
        System.out.println("Обратная польская нотация: " + rpn);
        System.out.println("\tРезультат " + calculateExpression(expression));
    }
}