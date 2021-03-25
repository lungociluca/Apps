package test;

import BusinessLogic.Operations;
import DataModels.ProcessPolynomials2;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class additionTest {

    private ProcessPolynomials2 p0 = new ProcessPolynomials2();
    private ProcessPolynomials2 p1 = new ProcessPolynomials2();
    private ProcessPolynomials2 p2 = new ProcessPolynomials2();
    private ProcessPolynomials2 p3 = new ProcessPolynomials2();
    private ProcessPolynomials2 p4 = new ProcessPolynomials2();
    private ProcessPolynomials2 p5 = new ProcessPolynomials2();
    private ProcessPolynomials2 p6 = new ProcessPolynomials2();
    private ProcessPolynomials2 p7 = new ProcessPolynomials2();
    @Before
    public void processPolynomials() {
        p0.process("X + 1");
        p1.process("X + 1");
        p2.process("2X^3 + 7X^2 +2X +9");
        p3.process("2X^3 + 7X^2 + 2X - 2");
        p4.process("2x + 3");
        p5.process("-1X +2X^2 - 2X^7 +10X^2");
        p6.process("2X^7 -5X^2 -99");
        p7.process("");
    }
    @Test
    public void tryAdd() {
       String result =  Operations.addition(p1,p4,false);
       String resultExpected = "+3X^1 +4X^0";

        assertTrue(result.equals(resultExpected));
    }

    @Test
    public void trySubtract() {
        String result =  Operations.addition(p0,p1,true);
        String resultExpected = "0";

        assertTrue(result.equals(resultExpected));
    }

    @Test
    public void trySubtractWithOneOperand() {
        String result =  Operations.addition(p7,p1,true);
        String resultExpected = "-1X^1 -1X^0";

        assertTrue(result.equals(resultExpected));
    }

    @Test
    public void tryMultiply() {
        String result =  Operations.multiply(p2,p4);
        String resultExpected = "+4X^4 +20X^3 +25X^2 +24X^1 +27X^0";

        assertTrue(result.equals(resultExpected));
    }

    @Test
    public void tryDivide() {
        Operations.DivisionResult result =  Operations.division(p4, p2);
        String quotient = result.quotient;
        String reminder = result.reminder;

        String resultExpectedQuotient = "+1.00X^2 +2.00X^1 -2.00X^0";
        String resultExpectedReminder = "+15.00X^0";

        assertTrue(quotient.equals(resultExpectedQuotient));
        assertTrue(reminder.equals(resultExpectedReminder));
    }

    @Test
    public void tryDifferentiate() {
        String result =  Operations.deriv(p3);
        String resultExpected = "+6X^2 +14X^1 +2X^0";

        assertTrue(result.equals(resultExpected));
    }

    @Test
    public void tryIntegrate() {
        String result =  Operations.integration(p6);
        String resultExpected = "+0.25X^8 -1.66X^3 -99.00X^1 ";

        assertTrue(result.equals(resultExpected));
    }
}
