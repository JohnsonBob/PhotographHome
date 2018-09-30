package cc.yelinvan.photographhome.flash.event;

public class BatteryEvent {
    String currentBattery;

    public BatteryEvent(String currentBattery) {
        this.currentBattery = currentBattery;
    }

    public String getCurrentBattery() {
        return this.currentBattery;
    }

    public void setCurrentBattery(String currentBattery) {
        this.currentBattery = currentBattery;
    }
}
