package org.example;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.Stack;

public class CalculatingExpressions {
    private final Map<String, Double> variables;
    private final Scanner inputScanner;

    public CalculatingExpressions() {
        this(new Scanner(System.in));
    }
    public CalculatingExpressions(Scanner scanner) {
        this.variables = new HashMap<>();
        this.inputScanner = scanner;
        variables.put("PI", Math.PI);
        variables.put("E", Math.E);
    }

    public void setVariable(String name, double value) {
        if (!name.matches("[a-zA-Z][a-zA-Z0-9]*")) {
            throw new IllegalArgumentException("Некорректное имя переменной: " + name);
        }
        variables.put(name, value);
    }

    public double Calculate(String expression) {
        expression = expression.replaceAll("\\s+", "");
        expression = expression.replaceAll("(?<=^|[(+\\-*/%^])-", "0-");
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operators = new Stack<>();

        for (int i = 0; i < expression.length(); i++) {
            char symbol = expression.charAt(i);

            if (Character.isDigit(symbol) || symbol == '.' || (symbol == '-' && isUnaryMinus(expression, i))) {
                i = processNumber(expression, i, numbers);
            }
            else if (Character.isLetter(symbol)) {
                i = processVariable(expression, i, numbers);
            }
            else if (symbol == '(') {
                operators.push(symbol);
            }
            else if (symbol == ')') {
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

    private int processNumber(String expr, int index, Stack<Double> nums) {
        StringBuilder numStr = new StringBuilder();
        boolean isNegative = false;

        // Обработка унарного минуса
        if (expr.charAt(index) == '-' && isUnaryMinus(expr, index)) {
            isNegative = true;
            index++;
        }

        // Проверяем, что после минуса идет цифра или точка
        if (index >= expr.length() || (!Character.isDigit(expr.charAt(index)) && expr.charAt(index) != '.')) {
            throw new IllegalArgumentException("Некорректное число после унарного минуса");
        }

        // Собираем цифры и точки
        while (index < expr.length() && (Character.isDigit(expr.charAt(index)) || expr.charAt(index) == '.')) {
            numStr.append(expr.charAt(index++));
        }

        double number = Double.parseDouble(numStr.toString());
        if (isNegative) {
            number = -number;
        }

        nums.push(number);
        return index - 1;
    }

    private int processVariable(String expr, int index, Stack<Double> nums) {
        StringBuilder varName = new StringBuilder();
        while (index < expr.length() && Character.isLetterOrDigit(expr.charAt(index))) {
            varName.append(expr.charAt(index++));
        }

        String name = varName.toString();
        if (!variables.containsKey(name)) {
            System.out.print("Введите значение для '" + name + "': ");
            double value = inputScanner.nextDouble();
            inputScanner.nextLine();
            setVariable(name, value);
        }

        nums.push(variables.get(name));
        return index - 1;
    }

    private boolean isUnaryMinus(String expression, int index) {
        return index == 0 || expression.charAt(index - 1) == '(' || isOperator(expression.charAt(index - 1));
    }

    private boolean isOperator(char symbol) {
        return symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/' || symbol == '^' || symbol == '%';
    }

    private boolean operatorHigherPrecedence(char op1, char op2) {
        if (op1 == '^' && op2 == '^') {
            return false;
        }
        if (op1 == '^') return true;
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
            case '/' :
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

    public void close() {
        inputScanner.close();
    }
}