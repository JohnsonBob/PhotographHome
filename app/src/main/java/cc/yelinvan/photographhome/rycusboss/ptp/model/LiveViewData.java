package cc.yelinvan.photographhome.rycusboss.ptp.model;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;

public class LiveViewData {
    public Bitmap bitmap;
    public boolean hasAfFrame;
    public boolean hasHistogram;
    public ByteBuffer histogram;
    public int nikonAfFrameCenterX;
    public int nikonAfFrameCenterY;
    public int nikonAfFrameHeight;
    public int nikonAfFrameWidth;
    public int nikonWholeHeight;
    public int nikonWholeWidth;
    public int zoomFactor;
    public int zoomRectBottom;
    public int zoomRectLeft;
    public int zoomRectRight;
    public int zoomRectTop;
}
