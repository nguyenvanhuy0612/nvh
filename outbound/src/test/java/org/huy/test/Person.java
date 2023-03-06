package org.huy.test;

public class Person {
    public void print() {
        System.out.println("person");
    }

    public void request() {
        print();
        this.print();
    }
}
