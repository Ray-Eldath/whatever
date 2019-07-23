package ray.eldath.whatever;

import static ray.eldath.whatever.CollectionTestKt.INSTANCE;

public class CollectionTest {
    public static void main(String[] args) {
        System.out.println(INSTANCE.getList().getClass());
        System.out.println(INSTANCE.getArrayList().getClass());
        INSTANCE.getArrayList().add("a");
        System.out.println(INSTANCE.getArrayList().size());
        System.out.println(INSTANCE.getList().add("d"));
    }
}
