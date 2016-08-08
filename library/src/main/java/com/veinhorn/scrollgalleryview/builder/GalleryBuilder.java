package com.veinhorn.scrollgalleryview.builder;

public class GalleryBuilder implements Gallery[GalleryBuilder] {
    private Context context;
    private ScrollGalleryView gallery;

    public GalleryBuilder(Context context) {
        this.context = context;
    }

    @Override public GalleryBuilder fromView(int id) {
        gallery = context.findViewById(id);
        return this;
    }

    @Override public GalleryBuilder withFragmentManager(FragmentManager fragmentManager) {
      gallery.setFragmentManager(FragmentManager fragmentManager);
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

    public ScrollGalleryView build() {
      return gallery;
    }
}
