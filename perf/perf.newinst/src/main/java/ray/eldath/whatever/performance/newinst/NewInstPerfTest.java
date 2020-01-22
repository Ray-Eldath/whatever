package ray.eldath.whatever.performance.newinst;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@BenchmarkMode(Mode.AverageTime)
@Fork(1)
@Threads(1)
@Measurement(iterations = 3, time = 3)
@Warmup(iterations = 4, time = 2)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class NewInstPerfTest {
    @Param({"1", "5", "10", "50", "100", "500", "1000"})
    private static int SIZE;

    private static final List<String> strings = new ArrayList<>();
    private static final List<Integer> integers = new ArrayList<>();

    @Setup
    public void setup() {
        var random = new Random();

        for (var i = 0; i < SIZE; i++) {
            var array = new byte[8];
            random.nextBytes(array);

            strings.add(new String(array, StandardCharsets.UTF_8));
            integers.add(random.nextInt());
        }
    }

    @Benchmark
    public void newInstSingle(Blackhole blackhole) {
        for (var i = 0; i < SIZE; i++) {
            var string = strings.get(i);

            var instance = new SingleWrapper(string);

            blackhole.consume(instance);
            blackhole.consume(string);
        }
    }

    @Benchmark
    public void baselineSingle(Blackhole blackhole) {
        for (var i = 0; i < SIZE; i++) {
            var string = strings.get(i);

            blackhole.consume(string);
        }
    }

    @Benchmark
    public void newObj(Blackhole blackhole) {
        for (var i = 0; i < SIZE; i++) {
            var string = strings.get(i);
            var integer = integers.get(i);

            var instance = new Object(); // creation

            blackhole.consume(instance);
            blackhole.consume(string);
            blackhole.consume(integer);
        }
    }

    @Benchmark
    public void newInstPair(Blackhole blackhole) {
        for (var i = 0; i < SIZE; i++) {
            var string = strings.get(i);
            var integer = integers.get(i);

            var instance = new PairWrapper(string, integer); // creation

            blackhole.consume(instance);
            blackhole.consume(string);
            blackhole.consume(integer);
        }
    }

    @Benchmark
    public void baselinePair(Blackhole blackhole) {
        for (var i = 0; i < SIZE; i++) {
            var string = strings.get(i);
            var integer = integers.get(i);

            blackhole.consume(string);
            blackhole.consume(integer);
        }
    }

    public static void main(String[] args) throws RunnerException, IOException {
        System.setProperty("file.encoding", "UTF-8");
        System.out.println("Now testing " + NewInstPerfTest.class.getName() + "...");
        System.out.println("This may takes 10 or more minutes to complete.");

        var now = String.valueOf(System.currentTimeMillis());
        var nowLength = now.length();
        var suffix = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE) + "_" + now.substring(nowLength - 4, nowLength);
        var output = Files.createFile(Paths.get("benchmark_" + suffix + ".txt"));
        var result = Files.createFile(Paths.get("benchmark_result_" + suffix + ".csv"));

        var options = new OptionsBuilder()
                .include(NewInstPerfTest.class.getSimpleName())
                .resultFormat(ResultFormatType.CSV)
                .result(result.toString())
                .output(output.toString()).build();
        new Runner(options).run();

        System.out.println("Benchmark result: " + output);
        System.out.println("\t\tas well as " + result);
    }
}
