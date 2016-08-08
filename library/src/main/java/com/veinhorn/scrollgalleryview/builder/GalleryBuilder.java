package com.veinhorn.scrollgalleryview.builder;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.ScrollGalleryViewException;

import java.util.List;

public class GalleryBuilder implements Gallery<GalleryBuilder> {
    private Activity activity;
    private FragmentManager fragmentManager;
    private ScrollGalleryView gallery;

    public GalleryBuilder(Activity activity) {
        this.activity = activity;
    }

    @Override public GalleryBuilder fromView(int id) {
        gallery = (ScrollGalleryView) activity.findViewById(id);
        return this;
    }

    @Override public GalleryBuilder withFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        gallery.setFragmentManager(fragmentManager);
        return this;
    }

    @Override public GalleryBuilder withZoom(boolean zoomEnabled) {
        gallery.setZoom(zoomEnabled);
        return this;
    }

    @Override public GalleryBuilder withThumbnailSize(int thumbnailSize) {
      gallery.setThumbnailSize(thumbnailSize);
      return this;
    }

    @Override public GalleryBuilder addMedia(MediaInfo info) {
        gallery.addMedia(info);
        return this;
    }

    @Override public GalleryBuilder addMedia(List<MediaInfo> infos) {
        gallery.addMedia(infos);
        return this;
    }

    @Override public GalleryBuilder withHiddenThumbnails(boolean enabled) {
        gallery.hideThumbnails(enabled);
        return this;
    }

    @Override public GalleryBuilder withOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        gallery.addOnPageChangeListener(listener);
        return this;
    }

    public ScrollGalleryView build() throws ScrollGalleryViewException {
        if (fragmentManager == null)
            throw new ScrollGalleryViewException("FragmentManager is not initialized.");
        return gallery;
    }
}
