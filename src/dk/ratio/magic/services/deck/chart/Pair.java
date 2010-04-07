package dk.ratio.magic.services.deck.chart;

public class Pair<V, T> {
    private V first;
    private T second;

    public Pair(V first, T second) {
        this.first = first;
        this.second = second;
    }

    public V getFirst() {
        return first;
    }

    public T getSecond() {
        return second;
    }

    public void setSecond(T second) {
        this.second = second;
    }

    public void setFirst(V first) {
        this.first = first;
    }
}
