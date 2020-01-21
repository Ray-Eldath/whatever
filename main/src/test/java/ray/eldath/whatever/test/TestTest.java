package ray.eldath.whatever.test;

import java.lang.ref.Reference;

public class TestTest {
    public static void main(String[] args) {
        var a = new Object();

        Reference.reachabilityFence(a);
    }
}
