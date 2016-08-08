package com.veinhorn.scrollgalleryview;

import com.veinhorn.scrollgalleryview.loader.MediaLoader;

/**
 * Media Info contains the information required to load and display the media in the gallery.
 */
public class MediaInfo {
    private MediaLoader mediaLoader;

    public MediaInfo(MediaLoader mediaLoader) {
        this.mediaLoader = mediaLoader;
    }

    public MediaLoader getLoader() {
        return mediaLoader;
    }
}
