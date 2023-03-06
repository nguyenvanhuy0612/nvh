package org.huy.test;

public class Main1 {
    public static void main(String[] args) {
        Person p = new Person();
        p.request();
        Person p1 = new Student();
        p1.request();
        Student s = new Student() {
            public void print() {
                System.out.println("student2");
            }
        };
        s.request();
    }
}
