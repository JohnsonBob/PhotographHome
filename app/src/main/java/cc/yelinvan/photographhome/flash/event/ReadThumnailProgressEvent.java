package cc.yelinvan.photographhome.flash.event;

public class ReadThumnailProgressEvent {
    int current;
    int total;

    public ReadThumnailProgressEvent(int current, int total) {
        this.current = current;
        this.total = total;
    }

    public int getCurrent() {
        return this.current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String toString() {
        return "ReadThumnailProgressEvent{current=" + this.current + ", total=" + this.total + '}';
    }
}
