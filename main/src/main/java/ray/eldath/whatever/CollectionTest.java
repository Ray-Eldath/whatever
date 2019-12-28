package ray.eldath.whatever;

import java.util.function.Consumer;

import static ray.eldath.whatever.CollectionTestKt.INSTANCE;

public class CollectionTest {
    private static Consumer<Integer[]> a = (Integer... a) -> System.out.println(a);

    public static void main(String[] args) {
        Integer[] array1 = new Integer[]{1, 3, 4};
        a.accept(array1);
//        a.accept(2);

        System.out.println(INSTANCE.getList().getClass());
        System.out.println(INSTANCE.getArrayList().getClass());
        INSTANCE.getArrayList().add("a");
        System.out.println(INSTANCE.getArrayList().size());
        System.out.println(INSTANCE.getList().add("d"));
    }
}
