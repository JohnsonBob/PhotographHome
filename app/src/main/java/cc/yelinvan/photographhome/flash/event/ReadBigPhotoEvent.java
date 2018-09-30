package cc.yelinvan.photographhome.flash.event;

public class ReadBigPhotoEvent {
    String filePath;
    int handle;
    int orientation;

    public ReadBigPhotoEvent(int handle, String filePath, int orientation) {
        this.handle = handle;
        this.filePath = filePath;
        this.orientation = orientation;
    }

    public int getHandle() {
        return this.handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public int getOrientation() {
        return this.orientation;
    }

    public void setOrientation(int orientation) {
        this.orientation = orientation;
    }

    public String toString() {
        return "ReadBigPhotoEvent{handle=" + this.handle + ", filePath='" + this.filePath + '\'' + ", orientation=" + this.orientation + '}';
    }
}
