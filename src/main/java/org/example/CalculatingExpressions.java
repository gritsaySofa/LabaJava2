package org.example;

import java.util.Stack;

public class CalculatingExpressions {

    public double Calculate(String expression){
        expression=expression.replaceAll("\\s+","");
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i=0;i<expression.length(); i++) {
            char symbol =expression.charAt(i);

            if (Character.isDigit(symbol) || symbol == '.' || (symbol == '-' && isUnaryMinus(expression, i))) {
                StringBuilder numStr = new StringBuilder();
                if (symbol == '-') {
                    numStr.append(symbol);
                    i++;
                    if (i >= expression.length()) {
                        throw new IllegalArgumentException("Некорректное выражение");
                    }
                    symbol = expression.charAt(i);
                }
                while (i < expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == '.')) {
                    numStr.append(expression.charAt(i));
                    i++;
                }
                i--;
                numbers.push(Double.parseDouble(numStr.toString()));
            }
            else if (symbol == '(') {
                operators.push(symbol);
            }else if(symbol== ')'){
                while (!operators.isEmpty() && operators.peek() != '(') {
                    calculatingOperation(numbers, operators);
                }
                if (operators.isEmpty()) {
                    throw new IllegalArgumentException("Несбалансированные скобки");
                }
                operators.pop();
            } else if (isOperator(symbol)) {
                while (!operators.isEmpty() && operatorHigherPrecedence(operators.peek(), symbol)) {
                    calculatingOperation(numbers, operators);
                }
                operators.push(symbol);
            } else {
                throw new IllegalArgumentException("Недопустимый символ: " + symbol);
            }
        }

        while (!operators.isEmpty()) {
            calculatingOperation(numbers, operators);
        }
        if (numbers.size() != 1) {
            throw new IllegalArgumentException("Некорректное выражение");
        }
        return numbers.pop();
    }

    private boolean isUnaryMinus(String expression, int index) {
        return index == 0 || expression.charAt(index - 1) == '(';
    }

    private boolean isOperator(char symbol) {
        return symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/' || symbol == '^' || symbol == '%';
    }

    private boolean operatorHigherPrecedence(char op1, char op2) {
            if (op1 == '^') return true; // Степень имеет наивысший приоритет
            if ((op1 == '*' || op1 == '/' || op1 == '%') && (op2 == '+' || op2 == '-')) return true;
            return false;
        }

        private void calculatingOperation(Stack<Double> numbers, Stack<Character> operators) {
        if (numbers.size() < 2 || operators.isEmpty()) {
            throw new IllegalArgumentException("Некорректное выражение");
        }

        double b = numbers.pop();
        double a = numbers.pop();
        char operator = operators.pop();

        double result;
        switch (operator) {
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
                if (b == 0) {
                    throw new ArithmeticException("Деление на ноль!");
                }
                result = a / b;
                break;
            case '^':
                result = Math.pow(a, b);
                break;
            case '%':
                result = a % b;
                break;
            default:
                throw new IllegalArgumentException("Неизвестный оператор: " + operator);
        }
        numbers.push(result);
    }
}

