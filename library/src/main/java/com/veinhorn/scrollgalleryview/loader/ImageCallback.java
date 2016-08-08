package com.veinhorn.scrollgalleryview.loader;

import com.squareup.picasso.Callback;

/**
 * Created by veinhorn on 8.8.16.
 */
public class ImageCallback implements Callback {
    private final MediaLoader.SuccessCallback callback;

    public ImageCallback(MediaLoader.SuccessCallback callback) {
        this.callback = callback;
    }

    @Override public void onSuccess() {
        callback.onSuccess();
    }

    @Override public void onError() {}
}
