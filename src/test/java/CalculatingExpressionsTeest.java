import org.example.CalculatingExpressions;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

class CalculatingExpressionsTest {
    private CalculatingExpressions calculator;
    private final InputStream originalSystemIn = System.in;

    @BeforeEach
    void setUp() {
        calculator = new CalculatingExpressions();
    }

    @AfterEach
    void tearDown() {
        calculator.close();
        System.setIn(originalSystemIn);
    }

    @Test
    void testBasicArithmeticOperations() {
        assertEquals(5.0, calculator.Calculate("2 + 3"));
        assertEquals(-1.0, calculator.Calculate("2 - 3"));
        assertEquals(6.0, calculator.Calculate("2 * 3"));
        assertEquals(0.666, calculator.Calculate("2 / 3"), 0.001);
        assertEquals(8.0, calculator.Calculate("2 ^ 3"));
        assertEquals(2.0, calculator.Calculate("5 % 3"));
    }

    @Test
    void testOperatorPrecedence() {
        assertEquals(7.0, calculator.Calculate("2 + 3 * 5 / 3"));
        assertEquals(17.0, calculator.Calculate("2 * 3 + 5 * 2 + 1"));
        assertEquals(20.0, calculator.Calculate("2 * (3 + 5) + 4"));
        assertEquals(512.0, calculator.Calculate("2 ^ 3 ^ 2"));
    }

    @Test
    void testVariables() {
        Scanner testScanner = new Scanner("42\n");
        CalculatingExpressions testCalculator = new CalculatingExpressions(testScanner);
        assertEquals(47.0, testCalculator.Calculate("x + 5"));
        assertEquals(42.0, testCalculator.Calculate("x"));
        testScanner.close();
    }

    @Test
    void testComplexExpressions() {
        assertEquals(4.666, calculator.Calculate("(2 + 3 * 4) / (5 + 1 - 3)"), 0.001);
        assertEquals(17.0, calculator.Calculate("2 + 3 * 5"));
        assertEquals(25.0, calculator.Calculate("(2 + 3) * 5"));
        assertEquals(1.0, calculator.Calculate("5 % 2"));
        assertEquals(0.5, calculator.Calculate("1 / 2"));
    }
}