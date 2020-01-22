package ray.eldath.whatever.performance.newinst;

public class PairWrapper {
    private String string;
    private int integer;

    public PairWrapper(String string, int integer) {
        this.string = string;
        this.integer = integer;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public int getInteger() {
        return integer;
    }
}
