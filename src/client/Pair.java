package client;

public class Pair<T, V> {

    public T getFirst() {
        return first;
    }

    public void setFirst(T first) {
        this.first = first;
    }

    private T first;

    public V getSecond() {
        return second;
    }

    public void setSecond(V second) {
        this.second = second;
    }

    private V second;

    public Pair(T first, V second) {
        this.first = first;
        this.second = second;
    }
}
