package operations.complex;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ComplexNumbersTest {
    @Test
    void createComplex() {
        ComplexNumber c = new ComplexNumber(4.3, 7.341);
        String s = c.toString();

        assertEquals("4.30 +7.34i", s);
    }

    @Test
    void addComplex() {
        ComplexNumber c1 = new ComplexNumber(4.3, 7.341);
        ComplexNumber c2 = new ComplexNumber(0.2, 3.2);

        ComplexNumber r = c1.add(c2);

        assertEquals("4.50 +10.54i", r.toString());
    }
}
