package utitlity;

public class Pair<T, S> {

    private T firstElement;
    private S secondElement;

    public Pair(T firstElement, S secondElement) {
        this.firstElement = firstElement;
        this.secondElement = secondElement;
    }

    public T getFirstElement() {
        return firstElement;
    }

    public void setFirstElement(T firstElement) {
        this.firstElement = firstElement;
    }

    public S getSecondElement() {
        return secondElement;
    }

    public void setSecondElement(S secondElement) {
        this.secondElement = secondElement;
    }
}
