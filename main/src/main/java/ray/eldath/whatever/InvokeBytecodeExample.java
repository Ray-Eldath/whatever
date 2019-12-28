package ray.eldath.whatever;

import java.util.ArrayList;
import java.util.List;

public class InvokeBytecodeExample {
    // private static Runnable unit = () -> System.out.println("Hello World!");

    public static void main(String[] args) {
        // unit.run();
        var sc = new InvokeBytecodeExample();
        sc.run();
    }

    private void run() {
        List<String> ls = new ArrayList<>();
        ls.add("Good Day");

        var als = new ArrayList<String>();
        als.add("Dydh Da");
    }
}