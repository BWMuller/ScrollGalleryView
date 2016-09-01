package com.veinhorn.scrollgalleryview;

import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.widget.ImageView;

import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.util.UUID;

/**
 * Media Info contains the information required to load and display the media in the gallery.
 */
public class MediaInfo {
    private String id;
    private MediaLoader mLoader;
    private int backgroundColor = -1;
    private int placeholder = -1;
    private ImageView currentImageView;

    public static MediaInfo mediaLoader(MediaLoader mediaLoader) {
        return new MediaInfo().setLoader(mediaLoader).setId(UUID.randomUUID().toString());
    }

    public MediaLoader getLoader() {
        return mLoader;
    }

    public MediaInfo setLoader(MediaLoader loader) {
        mLoader = loader;
        return this;
    }

    public MediaInfo setId(String id) {
        this.id = id;
        return this;
    }

    public String getId() {
        return id;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public MediaInfo setBackgroundColor(@ColorRes int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public void setPlaceholder(@DrawableRes int placeholder) {
        this.placeholder = placeholder;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public ImageView getCurrentImageView() {
        return currentImageView;
    }

    public void setCurrentImageView(ImageView currentImageView) {
        this.currentImageView = currentImageView;
    }
}
