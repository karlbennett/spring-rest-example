package scratch.spring.webapp.steps;

public class Holder<T> {

    private final T value;

    public Holder(T value) {
        this.value = value;
    }

    public T get() {
        return value;
    }
}
