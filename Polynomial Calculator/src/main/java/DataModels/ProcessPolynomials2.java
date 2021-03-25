package DataModels;

public class ProcessPolynomials2 {
    public MonomialList list = new MonomialList();

    public MonomialList.Node getHead() {
        return list.getHeadRef();
    }

    public boolean process(String p) {
        p = checkIfZeroPolynomial(p);
        if(p.equals(""))
            return false;
        p =  formatString(p);
        if(!p.equals("error")){
            getCoef(p);
            getDegree(p);
            sortAfterDegrees();
            System.out.println("Sorted: "+toString());
            return true;
        }
        else
            return false;
    }

    private static String formatString(String p) {
        p = p.replaceAll("\\s","");
        p = p.replaceAll("x","X");
        p = p + "E";

        char[] pAsChar = p.toCharArray();

        if(pAsChar[0] != '+' && pAsChar[0] != '-') {
            p = "+" + p;
            pAsChar = p.toCharArray();
        }

        return addIfMissingDegrees(pAsChar,p.length());
    }

    private static String addIfMissingDegrees(char[] pAsChar, int length) {
        boolean beginCoef = false, beginDegree = false;
        for(int i = 0; i < length; i++) {
            if(beginCoef) {
                if(pAsChar[i] == 'X') {
                    pAsChar = insertAt(pAsChar,i-1,1,length);
                    length++;
                }
                while(pAsChar[i] >= 48 && pAsChar[i] <= 57)
                    i++;
                if(pAsChar[i] != 'X') {
                    pAsChar = insertAt(pAsChar,i-1,3, length);
                    length += 3;
                }
                i--;
                beginCoef = false;
            }
            else if(beginDegree) {
                if(pAsChar[i] < 48 || pAsChar[i] > 57)
                    return "error";
                while(pAsChar[i] >= 48 && pAsChar[i] <= 57)
                    i++;
                i--;
                beginDegree = false;
            }
            else if(pAsChar[i] == '+' || pAsChar[i] == '-') {
                beginCoef = true;
            }
            else if(pAsChar[i] == 'X'){
                if(pAsChar[i+1] != '^' && pAsChar[i+1] != '+' && pAsChar[i+1] != '-' && pAsChar[i+1] != 'E')
                    return "error";
                if(pAsChar[i+1] != '^') { //if we find X at power one
                    pAsChar = insertAt(pAsChar,i, 2,length);
                    length += 2;
                }
                i++;
                beginDegree = true;
            }
            else if(pAsChar[i] != 'E'){
                return "error";
            }
        }
        return toString(pAsChar);
    }

    private static char[] insertAt(char[] p, int index, int additionalSize, int initSize) {
        char[] pToReturn = new char[initSize + additionalSize];

        int j= 0;
        for(char c : p) {
            pToReturn[j++] = c;
        }

        for(int i = initSize + additionalSize - 1; i > index + additionalSize; i--) {
            pToReturn[i] = pToReturn[i-additionalSize];
        }
        if(additionalSize == 1) {
            pToReturn[index+1] = '1';
        }
        else if(additionalSize == 2) {
            pToReturn[index+1] = '^';
            pToReturn[index+2] = '1';
        }else {
            pToReturn[index+1] = 'X';
            pToReturn[index+2] = '^';
            pToReturn[index+3] = '0';
        }
        return pToReturn;
    }

    public static String toString(char[] pAsChar) {
        String p = " ";
        for(char c : pAsChar) {
            p = p + c;
        }
        return p;
    }

    private void getCoef(String p) {
        while(!p.equals(p.replaceAll("X\\^[0-9]", "X^"))) {
            p = p.replaceAll("X\\^[0-9]", "X^");
        }
        p = p.replaceAll("[^0-9+-]"," ");
        p = p.replaceAll(" +"," ");
        addDataToList(p,true);
    }

    private void getDegree(String p) {
        //get a string where all the numbers are degrees
        while(!p.equals(p.replaceAll("[0-9]X", "X"))) {
            p = p.replaceAll("[0-9]X", "X");
        }
        p = p.replaceAll("[^0-9]"," ");
        p = p.replaceAll(" +"," ");
        addDataToList(p,false);
    }

    private void addDataToList(String p, boolean isCoef) {
        MonomialList.Node head = list.getHeadRef();
        char[] pAsChar = p.toCharArray();
        int powerOfTen = 1, currentNumber = 0;
        for(int i = p.length() - 2; i >= 0; i--) {
            if(pAsChar[i] >= 48 && pAsChar[i] <= 57) { //form the number with every digit
                currentNumber += (pAsChar[i] - '0') * powerOfTen;
                powerOfTen *= 10;
            }
            else { //when reach space the number was formed, store it
                if(isCoef) {
                    if(currentNumber != 0)
                        if (pAsChar[i] == '-')
                            list.add(-currentNumber, 0,true);
                        else
                            list.add(currentNumber, 0,true);
                } else {
                    if(head == null)
                        return;
                    head.setDegree(currentNumber);
                    head = head.getNext();
                }
                powerOfTen = 1;
                currentNumber = 0;
            }
        }
    }

    public void sortAfterDegrees() {
        try{
            list.sort();
        } catch (NullPointerException e) {
            list.add(0,0,false);
        }

    }

    private String checkIfZeroPolynomial(String s) {
        if(s.equals(""))
            return "0";
        else
            return  s;
    }

    public String toString() {
        String toReturn="";
        MonomialList.Node temp = getHead();
        while(temp != null) {
            int coef = temp.getCoef(true).intValue();
            int degree = temp.getDegree();
            String degreeS="", coefS="";
            char sign = '+';
            if(coef < 0) {
                sign = '-';
                coef = Math.abs(coef);
            }

            while (coef != 0) {
                int digit = coef % 10;
                coef /= 10;
                char c = (char) (digit + '0');
                coefS = c + coefS ;
            }
            coefS = sign + coefS;

            while (degree != 0) {
                int digit = degree % 10;
                degree /= 10;
                char c = (char) (digit + '0');
                degreeS = c + degreeS ;
            }
            if(!coefS.equals("+"))
                toReturn = toReturn + coefS + "X^" + degreeS + " ";
            temp = temp.getNext();
        }
        toReturn = toReturn.replaceAll("X\\^[^0-9]","X^0");
        return checkIfZeroPolynomial(toReturn);
    }

    public String toStringFloat() {
        String toReturn="";
        MonomialList.Node temp = getHead();
        while(temp != null) {
            int coef = (int) (temp.getCoef(false).floatValue() * 100);
            int degree = temp.getDegree();
            String degreeS="", coefS="";
            char sign = '+';
            if(coef < 0) {
                sign = '-';
                coef = Math.abs(coef);
            }
            int count = 0;
            while (coef != 0) {
                count++;
                int digit = coef % 10;
                coef /= 10;
                char c = (char) (digit + '0');
                coefS = c + coefS ;

                if(count == 2) {
                    coefS = '.'+coefS;
                    if(coef == 0)
                        coefS = '0' + coefS;
                }
            }
            coefS = sign + coefS;

            while (degree != 0) {
                int digit = degree % 10;
                degree /= 10;
                char c = (char) (digit + '0');
                degreeS = c + degreeS ;
            }
            if(!coefS.equals("+"))
                toReturn = toReturn + coefS + "X^" + degreeS + " ";
            temp = temp.getNext();
        }
        toReturn = toReturn.replaceAll("X\\^[^0-9]","X^0");
        return checkIfZeroPolynomial(toReturn);
    }
}
