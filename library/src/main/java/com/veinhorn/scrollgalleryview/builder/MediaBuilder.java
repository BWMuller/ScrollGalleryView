package com.veinhorn.scrollgalleryview.builder;

import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.loader.FromResourceImageLoader;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by veinhorn on 8.8.16.
 */
public class MediaBuilder {
    private List<MediaInfo> infos;

    public MediaBuilder() {
        infos = new ArrayList<>();
    }

    // TODO: Check here if have image or video format and create proper loader using MediaLoaderFactory
    public MediaBuilder fromUrl(String url) {
        return this;
    }

    /** Loads image from resource */
    public MediaBuilder fromResource(int resourceId) {
        MediaLoader mediaLoader = new FromResourceImageLoader(resourceId);
        MediaInfo info = new MediaInfo(mediaLoader);
        infos.add(info);
        return this;
    }

    /** Loads images from array of resources */
    public MediaBuilder fromResource(Integer... resourceIds) {
        for (int resourceId : resourceIds) {
            infos.add(new MediaInfo(new FromResourceImageLoader(resourceId)));
        }
        return this;
    }

    public List<MediaInfo> build() {
        return infos;
    }
}
