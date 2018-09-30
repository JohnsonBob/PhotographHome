package cc.yelinvan.photographhome.flash.event;

public class UploadProgressEvent {
    int handle;
    boolean isHide;
    int photoId;
    int progress;
    int state;

    public UploadProgressEvent(int handle, int state, int progress, boolean isHide, int photoId) {
        this.handle = handle;
        this.state = state;
        this.progress = progress;
        this.isHide = isHide;
        this.photoId = photoId;
    }

    public int getHandle() {
        return this.handle;
    }

    public void setHandle(int handle) {
        this.handle = handle;
    }

    public int getState() {
        return this.state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getProgress() {
        return this.progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isHide() {
        return this.isHide;
    }

    public void setHide(boolean hide) {
        this.isHide = hide;
    }

    public int getPhotoId() {
        return this.photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public String toString() {
        return "UploadProgressEvent{handle=" + this.handle + ", state=" + this.state + ", progress=" + this.progress + ", isHide=" + this.isHide + ", photoId=" + this.photoId + '}';
    }
}
