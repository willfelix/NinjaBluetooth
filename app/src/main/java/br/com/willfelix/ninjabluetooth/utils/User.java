package br.com.willfelix.ninjabluetooth.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.Serializable;

/**
 * Created by willfelix on 8/8/15.
 */
public class User implements Serializable {

    private static User user;

    private String name;

    private String message;

    private boolean another;

    private byte[] image;

    private Bitmap bitmap;

    private User() {
        name = "";
        image = null;
    }

    public User(User user, boolean another) {
        name = user.getName();
        message = user.getMessage();
        image = user.getImage();
        this.another = another;
    }

    public static User getInstance() {
        if (user == null) {
            user = new User();
        }

        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public Bitmap getBitmap() {
        if (bitmap != null)
            return bitmap;

        return BitmapFactory.decodeByteArray(getImage(), 0, getImage().length);
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isAnother() {
        return another;
    }

    public void setAnother(boolean another) {
        this.another = another;
    }
}
