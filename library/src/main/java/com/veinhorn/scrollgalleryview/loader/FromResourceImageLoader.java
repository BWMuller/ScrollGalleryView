package com.veinhorn.scrollgalleryview.loader;

import android.content.Context;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.veinhorn.scrollgalleryview.R;

/**
 * Created by veinhorn on 8.8.16.
 */
public class FromResourceImageLoader implements MediaLoader {
    private int resourceId;

    public FromResourceImageLoader(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override public boolean isImage() {
        return true;
    }

    @Override public void loadMedia(Context context, ImageView imageView, SuccessCallback callback) {
        Picasso.with(context)
                .load(resourceId)
                .placeholder(R.drawable.placeholder_image)
                .into(imageView, new ImageCallback(callback));
    }

    @Override public void loadThumbnail(Context context, ImageView imageView, SuccessCallback callback) {
        Picasso.with(context)
                .load(resourceId)
                .resize(100, 100)
                .placeholder(R.drawable.placeholder_image)
                .centerInside()
                .into(imageView, new ImageCallback(callback));
    }
}
