package cc.yelinvan.photographhome.flash.event;

import java.io.File;

public class ReadThumbnailEvent {
    File file;
    int state;

    public ReadThumbnailEvent(int state, File file) {
        this.state = state;
        this.file = file;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public File getFile() {
        return this.file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String toString() {
        return "ReadThumbnailEvent{state=" + this.state + '}';
    }
}
