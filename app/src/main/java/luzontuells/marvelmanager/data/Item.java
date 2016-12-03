package luzontuells.marvelmanager.data;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

public class Item {
    private String mImage;
    private String mName;
    private String mBody;
    private String mId;

    public Item(String aImage, String aTitle, String aBody, String aId) {
        this.mImage = aImage;
        this.mName = aTitle;
        this.mBody = aBody;
        this.mId = aId;
    }

    public String getmImage() {
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