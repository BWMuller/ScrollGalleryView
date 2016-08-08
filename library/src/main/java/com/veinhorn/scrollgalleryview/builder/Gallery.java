package com.veinhorn.scrollgalleryview.builder;

import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;

import com.veinhorn.scrollgalleryview.MediaInfo;

import java.util.List;

/** Contains all available methods of the ScrollGalleryView API */
public interface Gallery<T> {
    /** Finds a ScrollGalleryView that was identified by id attribute from the XML */
    T fromView(int id);

    /** Set up FragmentManager that is used by internal ViewPager (mandatory) */
    T withFragmentManager(FragmentManager fragmentManager);

    /** Enables zoom functionality on images */
    T withZoom(boolean zoomEnabled);

    /** Specifies thumbnail size (width and height) in pixels */
    T withThumbnailSize(int thumbnailSize);

    /** Adds a MediaInfo to ScrollGalleryView */
    T addMedia(MediaInfo info);

    /** Adds a List of MediaInfo's to ScrollGalleryView */
    T addMedia(List<MediaInfo> infos);

    /** Hides a horizontally scrolling View in the bottom of the screen */
    T withHiddenThumbnails(boolean enabled);

    /** Specifies OnPageChangeListener for internal ViewPager */
    T withOnPageChangeListener(ViewPager.OnPageChangeListener listener);
}
