package luzontuells.marvelmanager.data;

import android.graphics.Bitmap;

public class Item {
    private Bitmap mImage;
    private String mName;
    private String mBody;
    private String mId;

    public Item(Bitmap aImage, String aTitle, String aBody, String aId) {
        this.mImage = aImage;
        this.mName = aTitle;
        this.mBody = aBody;
        this.mId = aId;
    }

    public Bitmap getmImage() {
        return mImage;
    }

    public String getmName() {
        return mName;
    }

    public String getmBody() {
        return mBody;
    }

    public String getmId() { return mId; }
}