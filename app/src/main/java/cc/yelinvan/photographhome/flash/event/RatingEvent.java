package cc.yelinvan.photographhome.flash.event;

public class RatingEvent {
    int handle;

    public RatingEvent(int handle) {
        this.handle = handle;
    }

    public int getHandle() {
        return this.handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public String toString() {
        return "RatingEvent{handle=" + this.handle + '}';
    }
}
