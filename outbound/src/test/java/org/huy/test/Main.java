package org.huy.test;

public class Main {
    public static void main(String[] args) {
        A a = new A();
        a.request();
        a.request();
        a.request();
        a.request();
    }
}


class A {

    public void request() {
        if (Env.allow) {
            System.out.println("get requests");
        } else {
            System.out.println("not allow to requests");
        }
    }
}

class Env {
    public static boolean allow = readSettingsVariable(".env", "allow");

    private static boolean readSettingsVariable(String s, String allow) {
        return false;
    }
}