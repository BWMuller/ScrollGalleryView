package com.veinhorn.example;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.builder.GalleryBuilder;
import com.veinhorn.scrollgalleryview.builder.MediaBuilder;

public class MainActivity extends FragmentActivity {
    private ScrollGalleryView scrollGalleryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scrollGalleryView = new GalleryBuilder(this)
                .fromView(R.id.scroll_gallery_view)
                .withThumbnailSize(200)
                .withZoom(true)
                .withFragmentManager(getSupportFragmentManager())
                // .addMedia(MediaInfo.mediaLoader(new DefaultVideoLoader(MOVIE, R.mipmap.default_video)))
                .addMedia(new MediaBuilder()
                        .fromResource(R.drawable.wallpaper1)
                        .fromResource(R.drawable.wallpaper2)
                        .fromResource(R.drawable.wallpaper3)
                        .fromResource(R.drawable.wallpaper4)
                        .fromResource(R.drawable.wallpaper5)
                        .fromResource(R.drawable.wallpaper6)
                        .fromResource(R.drawable.wallpaper7)
                        .fromResource(R.drawable.wallpaper1, R.drawable.wallpaper2, R.drawable.wallpaper3)
                        .build()
                )
                .build();
    }
}