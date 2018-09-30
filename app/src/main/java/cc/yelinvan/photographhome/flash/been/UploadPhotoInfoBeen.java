package cc.yelinvan.photographhome.flash.been;

import java.util.Map;

public class UploadPhotoInfoBeen {
    String actId;
    String bigFilePath;
    String captureDate;
    Map<String, Object> exifInfos;
    int failCount;
    String fileName;
    String filePath;
    long fileSize;
    String handle;
    String imageHeight;
    String imageWidth;
    int isAllowRaw;
    boolean isFirst;
    int photoId;
    String rotate;
    String sepId;
    int stateCode;
    String timestamp;

    public String getCaptureDate() {
        return this.captureDate;
    }

    public void setCaptureDate(String captureDate) {
        this.captureDate = captureDate;
    }

    public String getBigFilePath() {
        return this.bigFilePath;
    }

    public void setBigFilePath(String bigFilePath) {
        this.bigFilePath = bigFilePath;
    }

    public long getFileSize() {
        return this.fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }

    public boolean isFirst() {
        return this.isFirst;
    }

    public void setFirst(boolean first) {
        this.isFirst = first;
    }

    public int getFailCount() {
        return this.failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public int getIsAllowRaw() {
        return this.isAllowRaw;
    }

    public void setIsAllowRaw(int isAllowRaw) {
        this.isAllowRaw = isAllowRaw;
    }

    public String getActId() {
        return this.actId;
    }

    public void setActId(String actId) {
        this.actId = actId;
    }

    public String getSepId() {
        return this.sepId;
    }

    public void setSepId(String sepId) {
        this.sepId = sepId;
    }

    public String getHandle() {
        return this.handle;
    }

    public void setHandle(String handle) {
        this.handle = handle;
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getRotate() {
        return this.rotate;
    }

    public void setRotate(String rotate) {
        this.rotate = rotate;
    }

    public String getImageWidth() {
        return this.imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return this.imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileName() {
        return this.fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getStateCode() {
        return this.stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public int getPhotoId() {
        return this.photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    public Map<String, Object> getExifInfos() {
        return this.exifInfos;
    }

    public void setExifInfos(Map<String, Object> exifInfos) {
        this.exifInfos = exifInfos;
    }

    public String toString() {
        return "UploadPhotoInfoBeen{exifInfos=" + this.exifInfos + ", actId='" + this.actId + '\'' + ", sepId='" + this.sepId + '\'' + ", photoId=" + this.photoId + ", stateCode=" + this.stateCode + ", filePath='" + this.filePath + '\'' + ", fileName='" + this.fileName + '\'' + ", imageWidth='" + this.imageWidth + '\'' + ", imageHeight='" + this.imageHeight + '\'' + ", handle='" + this.handle + '\'' + ", timestamp='" + this.timestamp + '\'' + ", rotate='" + this.rotate + '\'' + ", isAllowRaw=" + this.isAllowRaw + ", failCount=" + this.failCount + '}';
    }

    public boolean equals(Object obj) {
        if (((UploadPhotoInfoBeen) obj).getFilePath().equals(this.filePath)) {
            return true;
        }
        return false;
    }
}
