package ray.eldath.whatever;

import java.util.function.BiFunction;

public class LambdaBytecodeExampleJava {
    BiFunction<Integer, Integer, Double> average = (a, b) -> (a + b) * 0.5;
}
