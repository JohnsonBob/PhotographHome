package cc.yelinvan.photographhome.flash.event;

public class CameraStateEvent {
    int state;

    public CameraStateEvent(int state) {
        this.state = state;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String toString() {
        return "CameraStateEvent{state=" + this.state + '}';
    }
}
