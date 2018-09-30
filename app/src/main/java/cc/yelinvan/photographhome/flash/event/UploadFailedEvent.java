package cc.yelinvan.photographhome.flash.event;

public class UploadFailedEvent {
    int sum;

    public UploadFailedEvent(int sum) {
        this.sum = sum;
    }

    public int getSum() {
        return this.sum;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }

    public String toString() {
        return "UploadFailedEvent{sum=" + this.sum + '}';
    }
}
