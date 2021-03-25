package BusinessLogic;

import DataModels.ProcessPolynomials2;
import DataModels.MonomialList.Node;

public class Operations {

    // -1X +2X^2 - 2X^7 +10X^2
    // 2X^7 -5X^2 -99
    // 3X^2 - X + 10
    // 2X^2
    // 4X^3 + 3X^2 + 1
    // X^3 - 2X^2 + 6X - 5
    // X^2 - 1
    public static class DivisionResult {
        public String quotient;
        public String reminder;

        public DivisionResult(String quotient, String reminder) {
            this.quotient = quotient;
            this.reminder = reminder;
        }
    }

    private static ProcessPolynomials2 getZeroPolynomial() {
        ProcessPolynomials2 p = new ProcessPolynomials2();
        p.process("1X^0 - 1X^0");
        return p;
    }

    public static String addition(ProcessPolynomials2 p1, ProcessPolynomials2 p2, boolean reverseSignForP2) {
        Node temp1 = p1.getHead();
        Node temp2 = p2.getHead();

        while(temp2 != null) {
            Node nextTemp2 = temp2.getNext(); //need to save next link because add() will set the next value to the inserted item to null
            if(reverseSignForP2)
                temp2.setCoef(-(temp2.getCoef(true).intValue()),true);
            temp1.add(temp2);
            temp2 = nextTemp2;
        }

        while (temp1 != null) {
            Node previous = temp1;
            Node traverse = temp1.getNext();
            while(traverse != null) {
                if(traverse.getDegree() == temp1.getDegree()) {
                    temp1.setCoef(temp1.getCoef(true).intValue() + traverse.getCoef(true).intValue(), true);
                    previous.setNext(traverse.getNext());
                    traverse = previous.getNext();
                }
                else {
                    previous = traverse;
                    traverse = traverse.getNext();
                }
            }

            temp1 = temp1.getNext();
        }

        p1.sortAfterDegrees();
        return p1.toString();
    }

    public static String deriv(ProcessPolynomials2 p) {
        Node temp = p.getHead();
        temp.removeDegree0();

        while (temp != null) {
            int degree = temp.getDegree();
            int coef = temp.getCoef(true).intValue();

            temp.setDegree(degree-1);
            temp.setCoef(coef*degree,true);

            temp = temp.getNext();
        }
        return addition(p,getZeroPolynomial(), false);
    }

    public static String multiply(ProcessPolynomials2 p1, ProcessPolynomials2 p2) {
        Node temp1 = p1.getHead();
        Node temp2 = p2.getHead();
        ProcessPolynomials2 result = new ProcessPolynomials2();

        while(temp1 != null) {

            temp2 = p2.getHead();
            while(temp2 != null) {
                int coef,degree;

                coef = temp1.getCoef(true).intValue() * temp2.getCoef(true).intValue();
                degree = temp1.getDegree() + temp2.getDegree();

                result.list.add(coef,degree,true);
                temp2 = temp2.getNext();
            }

            temp1 = temp1.getNext();
        }
        return addition(result, getZeroPolynomial(),false);
    }

    public static String integration(ProcessPolynomials2 p) {
        Node temp = p.getHead();
        while(temp != null) {
            int degree = temp.getDegree() + 1;
            float coef = temp.getCoef(false).floatValue() / ((float) degree);

            temp.setDegree(degree);
            temp.setCoef(coef,false);
            temp = temp.getNext();
        }
        return p.toStringFloat();
    }

    public static DivisionResult division(ProcessPolynomials2 p1, ProcessPolynomials2 p2) {

        if(p1.getHead().getDegree() < p2.getHead().getDegree()) {
            ProcessPolynomials2 aux = p2;
            p2 = p1;
            p1 = aux;
        }

        if(p2.toString().equals("0")) {
            return new DivisionResult("Can't divide by zero"," ");
        }
        ProcessPolynomials2 Q = new ProcessPolynomials2();
        while(true) {
            ProcessPolynomials2 R = new ProcessPolynomials2();

            int degreeMonomialQ = p1.getHead().getDegree() - p2.getHead().getDegree();
            float coefMonomialQ = p1.getHead().getCoef(false).floatValue() / p2.getHead().getCoef(false).floatValue();

            Q.list.add(coefMonomialQ, degreeMonomialQ, false);
            ProcessPolynomials2 toSubtract = new ProcessPolynomials2();

            ProcessPolynomials2 temp = new ProcessPolynomials2();
            temp.list.add(coefMonomialQ, degreeMonomialQ, false);

            toSubtract.process(multiply(temp, p2));
            R.process(addition(p1, toSubtract, true));
            p1 = R;

            if (R.getHead().getDegree() < p2.getHead().getDegree()) {
                return new DivisionResult(Q.toStringFloat(),R.toStringFloat());
            }
        }
    }
}
