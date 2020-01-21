package ray.eldath.whatever.performance.newinst;

public class Wrapper {
    private String string;
    private int integer;

    public Wrapper(String string, int integer) {
        this.string = string;
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public int getInteger() {
        return integer;
    }
}
