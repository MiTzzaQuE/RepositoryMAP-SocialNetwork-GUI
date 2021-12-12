package domain;

import java.util.Objects;

import static java.lang.Math.abs;

/**
 * Define a Tuple o generic type entities
 * @param <E1> - tuple first entity type
 * @param <E2> - tuple second entity type
 */
public class Tuple<E1, E2> {
    private E1 e1;
    private E2 e2;

    /**
     * constructor for the tuple
     * @param e1 first element of type E
     * @param e2 second element of type E
     */
    public Tuple(E1 e1, E2 e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * return the first value from the tuple
     * @return e1 first element
     */
    public E1 getLeft() {
        return e1;
    }

    /**
     * set the first value of the tuple
     * @param e1 type E1
     */
    public void setLeft(E1 e1) {
        this.e1 = e1;
    }

    /**
     * return the second value from the tuple
     * @return e2 second element
     */
    public E2 getRight() {
        return e2;
    }

    /**
     * set the second value of the tuple
     * @param e2 second element
     */
    public void setRight(E2 e2) {
        this.e2 = e2;
    }

    @Override
    public String toString() {
        return "" + e1 + "," + e2;
    }

    @Override
    public boolean equals(Object obj) {
        // after the hashcode is equal the program will check if its are equal
        Tuple that=(Tuple) obj;
        if(that.getLeft()==this.getRight() && this.getLeft()==that.getRight())
            return true;
        return this.e1.equals(((Tuple) obj).e1) && this.e2.equals(((Tuple) obj).e2);
    }

    @Override
    public int hashCode() {
        int hash = 17;
        //this is a nice hashcode
        //I thought about a type of hash code that will eliminate the case (1 2) ==(2 1)
        hash = hash * 23 + (e2.hashCode()+e1.hashCode());
        hash = hash * 23 + abs(e2.hashCode()-e1.hashCode());
        //System.out.println(e1+" "+e2+" "+hash);
        return hash;

    }
}