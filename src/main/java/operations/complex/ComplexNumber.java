package operations.complex;

import java.text.DecimalFormat;

public class ComplexNumber {
    private double real;
    private double imaginary;

    public ComplexNumber(double r, double i) {
        real = r;
        imaginary = i;
    }

    @Override
    public String toString() {
        DecimalFormat df = new DecimalFormat("0.00");  // or "#.##" if 0 should become ""
        if (imaginary == 0.0)
            return df.format(real);
        return df.format(real) + " " + (imaginary > 0.0 ? "+" : "") + df.format(imaginary) + "i";
    }

    public ComplexNumber add(ComplexNumber c) {
        double r = real + c.getReal();
        double i = imaginary + c.getImaginary();

        return new ComplexNumber(r,i);
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }
}
