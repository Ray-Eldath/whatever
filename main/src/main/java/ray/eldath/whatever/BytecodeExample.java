package ray.eldath.whatever;

class BytecodeExample {
    private String acc = "Hello World";
    private String abc = "Hello";

    public static void main(String... args) {
        var instance = new BytecodeExample();
        System.out.println(instance.acc);
    }
}