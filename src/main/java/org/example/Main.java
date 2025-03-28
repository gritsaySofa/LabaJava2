package org.example;

import java.util.Stack;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введите арифметическое выражение: ");
        String expression = scanner.nextLine();
        scanner.close();

        CalculatingExpressions evaluator = new CalculatingExpressions();
        try {
            double result = evaluator.Calculate(expression);
            System.out.println("Результат: " + result);
        } catch (IllegalArgumentException | ArithmeticException e) {
            System.err.println("Ошибка: " + e.getMessage());
        }
    }
}