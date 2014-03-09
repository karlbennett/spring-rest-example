package scratch.spring.webapp.steps;

import java.util.Deque;
import java.util.Iterator;

public class DequeHolder<T> extends Holder<Deque<T>> implements Iterable<T> {

    public DequeHolder(Deque<T> deque) {
        super(deque);
    }

    public void push(T value) {
        get().push(value);
    }

    public T peek() {
        return get().peek();
    }

    public void clear() {
        get().clear();
    }

    @Override
    public Iterator<T> iterator() {
        return get().iterator();
    }
}
