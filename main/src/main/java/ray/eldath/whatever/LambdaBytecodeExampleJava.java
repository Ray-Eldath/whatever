package ray.eldath.whatever;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class LambdaBytecodeExampleJava {
    BiFunction<Integer, Integer, Double> average = (a, b) -> (a + b) * 0.5;

    private static BiConsumer<Integer, int[]> test = (Integer a, int... list) -> System.out.println(list[0]);
    private static BiConsumer<Integer, int[]> test1 = (Integer a, int[] list) -> System.out.println(list[0]);

    public static void main(String[] args) {
        /*Float a = -4.0F;
        Function<Integer, Integer> function = (Integer i) -> i * 2;
        var b = +a;
        System.out.println((int) ((float) a));
        System.out.println(b);*/
    }

    Consumer<Integer[]> f1 = (final @Annotation1 Integer @Annotation1 @Annotation2 ... a) -> new ArrayList<>().add(a);

    Consumer<Integer[][]> f2 = (final @Annotation1 Integer @Annotation1 [] @Annotation2 [] a) -> new ArrayList<>().add(a);

    private static String typeOf(Object obj) {
        return obj.getClass().toString();
    }

    @Target(ElementType.TYPE_USE)
    @interface Annotation1 {
    }

    @Target(ElementType.TYPE_USE)
    @interface Annotation2 {
    }
}