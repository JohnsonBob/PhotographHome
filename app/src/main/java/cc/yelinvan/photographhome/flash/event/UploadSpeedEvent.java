package cc.yelinvan.photographhome.flash.event;

public class UploadSpeedEvent {
    String speed;

    public UploadSpeedEvent(String speed) {
        this.speed = speed;
    }

    public String getSpeed() {
        return this.speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String toString() {
        return "UploadSpeedEvent{speed=" + this.speed + '}';
    }
}
