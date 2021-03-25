package DataModels;

public class MonomialList {

    public class Node {
        private Number coef;
        private int degree;
        private Node next;

        public Node(Number coef, int degree, Node next) {
            this.coef = coef;
            this.degree = degree;
            this.next = next;
        }

        public Number getCoef(boolean integer) {
            if (integer)
                return this.coef.intValue();
            else
                return this.coef.floatValue();
        }

        public int getDegree() {
            return this.degree;
        }

        public void setDegree(int value) {
            this.degree = value;
        }

        public void setCoef(Number value, boolean isInteger) {
            if(isInteger)
                coef = value.intValue();
            else
                coef = value.floatValue();
        }

        public Node getNext() {
            return this.next;
        }

        public void removeDegree0() {
            Node first = head;
            if(first.degree == 0) {
                first.coef = 0;
                return;
            }

            if(first.next == null)
                return;

            Node second = head;
            first = first.next;

            while(first != null) {
                if(first.degree == 0) {
                    second.next = first.next;
                    return;
                }
                second = first;
                first = first.next;
            }
        }

        public void setNext(Node newValue) {
            this.next = newValue;
        }

        public void add(Node temp) {
            temp.next = null;
            Node headRef = head;
            while(headRef.next != null)
                headRef = headRef.next;
            headRef.next = temp;
        }

        public void printList() {
            Node temp = head;
            while (temp != null) {
                System.out.println("Coef: " + temp.coef + "   Degrees: " + temp.degree);
                temp = temp.next;
            }
        }
    }

    private Node head;

    public void add(Number coef, int degree, boolean integer) {
        if (head == null) {
            if (integer)
                head = new Node(coef.intValue(), degree, null);
            else
                head = new Node(coef.floatValue(), degree, null);
            return;
        }

        Node tail = head;
        while (tail.next != null)
            tail = tail.next;

        tail.next = new Node(coef, degree, null);
    }

    public Node getHeadRef() {
        return head;
    }

    public void sort() {
        boolean ok = false;
        if (head.next == null)
            return;
        while (!ok) {
            ok = true;
            Node first = head.next;

            if (head.degree < first.degree) {
                ok = false;
                Node aux = first.next;
                first.next = head;
                head.next = aux;
                head = first;
            } else
                first = head;

            Node second = head.next;
            while (second.next != null) {
                if (second.degree < second.next.degree) {
                    ok = false;
                    Node aux = second.next.next;
                    second.next.next = second;
                    first.next = second.next;
                    second.next = aux;
                    first = first.next;
                } else {
                    first = second;
                    second = second.next;
                }
            }
        }
    }
}
