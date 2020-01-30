package ray.eldath.whatever.performance.newinst;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.profile.GCProfiler;
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
@Fork(2)
@Measurement(iterations = 2, time = 2)
@Warmup(iterations = 4, time = 2)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class NewInstPerfTest {
    @Param({"1", "10", "50", "100", "1000", "10000"})
    private int SIZE;
    private SingleWrapper single;

    private List<String> strings = new ArrayList<>();
    private List<Integer> integers = new ArrayList<>();

    @Setup
    public void setup() {
        var random = new Random();
        single = new SingleWrapper("");

        for (var i = 0; i < SIZE; i++) {
            var array = new byte[8];
            random.nextBytes(array);

            strings.add(new String(array, StandardCharsets.UTF_8));
            integers.add(random.nextInt());
        }
    }

    @Benchmark
    public void newInstSingle() {
        for (var i = 0; i < SIZE; i++) {
            var string = strings.get(i);
            var integer = integers.get(i);

            var instance = new SingleWrapper(string);

            sink(instance);
            sink(string);
            sink(integer);
        }
    }

    @Benchmark
    public void newInstPair() {
        for (var i = 0; i < SIZE; i++) {
            var string = strings.get(i);
            var integer = integers.get(i);

            var instance = new PairWrapper(string, integer);

            sink(instance);
            sink(string);
            sink(integer);
        }
    }

    @Benchmark
    public void baseline() {
        for (var i = 0; i < SIZE; i++) {
            var string = strings.get(i);
            var integer = integers.get(i);

            sink(single);
            sink(string);
            sink(integer);
        }
    }

    @Benchmark
    public void baseline_() {
        for (var i = 0; i < SIZE; i++) {
            var string = strings.get(i);
            var integer = integers.get(i);

            sink(string);
            sink(integer);
        }
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public static void sink(String v) {
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public static void sink(int v) {
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public static void sink(PairWrapper v) {
    }

    @CompilerControl(CompilerControl.Mode.DONT_INLINE)
    public static void sink(SingleWrapper v) {
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
                .output(output.toString());

        if (args.length >= 1 && "prof".equals(args[0])) {
            options.addProfiler(GCProfiler.class);
        }

        new Runner(options.build()).run();

        System.out.println("Benchmark result: " + output);
        System.out.println("\t\tas well as " + result);
    }
}
