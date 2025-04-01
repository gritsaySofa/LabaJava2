package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CalculatingExpressions calculator = new CalculatingExpressions();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите арифметическое выражение: ");
        System.out.println("Доступные команды");
        System.out.println("1. Вычислить выражение");
        System.out.println("2. Присвоить значение переменной");
        System.out.println("3. Выход");

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("выход")) {
                break;
            }

            try {
                if (input.contains("=")) {
                    processAssignment(input, calculator);
                } else {
                    double result = calculator.Calculate(input);
                    System.out.println("Результат: " + result);
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }

        calculator.close();
        scanner.close();
        System.out.println("Работа завершена.");
    }

    private static void processAssignment(String input, CalculatingExpressions calculator) {
        String[] parts = input.split("=", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Некорректный формат присваивания");
        }

        String varName = parts[0].trim();
        String expr = parts[1].trim();

        double value = calculator.Calculate(expr);
        calculator.setVariable(varName, value);
        System.out.printf("Присвоено: %s = %.2f%n", varName, value);
    }
}