package com.veinhorn.scrollgalleryview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by veinhorn on 6.8.15.
 */
public class ScrollGalleryView extends LinearLayout {
    private FragmentManager fragmentManager;
    private Context context;
    private Point displayProps;
    private PagerAdapter pagerAdapter;
    private List<MediaInfo> mListOfMedia;

    // Options
    private int thumbnailSize; // width and height in pixels
    int thumbnailMarginTop = 10;
    int thumbnailMarginLeft = 10;
    int thumbnailMarginRight = 10;
    int thumbnailMarginBottom = 10;
    private boolean zoomEnabled;
    private boolean thumbnailsHiddenEnabled;

    // Views
    private LinearLayout thumbnailsContainer;
    private HorizontalScrollView horizontalScrollView;
    private ViewPager viewPager;

    // Listeners
    private final ViewPager.SimpleOnPageChangeListener viewPagerChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override public void onPageSelected(int position) {
            scroll(thumbnailsContainer.getChildAt(position));
        }
    };

    private final OnClickListener thumbnailOnClickListener = new OnClickListener() {
        @Override public void onClick(View v) {
            scroll(v);
            viewPager.setCurrentItem((int) v.getTag(), true);
        }
    };
    private ScreenSlidePagerAdapter pagerAdapter;
    private List<MediaInfo> mListOfMedia;

    public ScrollGalleryView(Context context) {
        this(context, null);
    }

    public ScrollGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        mListOfMedia = new ArrayList<>();

        setOrientation(VERTICAL);
        displayProps = getDisplaySize();

        @LayoutRes int layout = R.layout.scroll_gallery_view;
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScrollGalleryView, 0, 0);

            layout = a.getResourceId(R.styleable.ScrollGalleryView_sgvBaseLayout, R.layout.scroll_gallery_view);
            a.recycle();
        }

        LayoutInflater inflater = LayoutInflater.from(context);
        inflater.inflate(layout, this, true);

        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.thumbnails_scroll_view);

        thumbnailsContainer = (LinearLayout) findViewById(R.id.thumbnails_container);
        thumbnailsContainer.setPadding(displayProps.x / 2, 0, displayProps.x / 2, 0);
    }

    public ScrollGalleryView setFragmentManager(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
        initializeViewPager();
        return this;
    }

    public ViewPager getViewPager() {
        return viewPager;
    }

    /**
     * Set up OnPageChangeListener for internal ViewPager
     * @param listener
     */
    public void addOnPageChangeListener(final ViewPager.OnPageChangeListener listener) {
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override public void onPageSelected(int position) {
                scroll(thumbnailsContainer.getChildAt(position));
                listener.onPageSelected(position);
            }

            @Override public void onPageScrollStateChanged(int state) {
                listener.onPageScrollStateChanged(state);
            }
        });
    }

    public ScrollGalleryView addMedia(MediaInfo mediaInfo) {
        if (mediaInfo == null) {
            throw new NullPointerException("Infos may not be null!");
        }

        return addMedia(Collections.singletonList(mediaInfo));
    }

    private int placeholder = -1;

    public ScrollGalleryView setPlaceholder(@DrawableRes int placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public ScrollGalleryView addMedia(List<MediaInfo> infos) {
        if (infos == null) {
            throw new NullPointerException("Infos may not be null!");
        }

        for (MediaInfo info : infos) {
            info.setPlaceholder(placeholder);
            mListOfMedia.add(info);

            final ImageView thumbnail = addThumbnail(getDefaultThumbnail());
            info.getLoader().loadThumbnail(getContext(), thumbnail, new MediaLoader.SuccessCallback() {
                @Override
                public void onSuccess() {
                    thumbnail.setScaleType(ImageView.ScaleType.FIT_CENTER);
                }
            });

            pagerAdapter.notifyDataSetChanged();
        }
        return this;
    }


    private Bitmap getDefaultThumbnail() {
        if (placeholder == -1)
            return ((BitmapDrawable) ContextCompat.getDrawable(context, R.drawable.placeholder_image)).getBitmap();
        return BitmapFactory.decodeResource(context.getResources(), placeholder);
    }

    /**
     * Set the current item displayed in the view pager.
     *
     * @param i a zero-based index
     * @return
     */
    public ScrollGalleryView setCurrentItem(int i) {
        viewPager.setCurrentItem(i, false);
        return this;
    }

    public ScrollGalleryView setThumbnailSize(int thumbnailSize) {
        this.thumbnailSize = thumbnailSize;
        return this;
    }

    public ScrollGalleryView setThumbnailMargin(int margin) {
        return setThumbnailMargin(margin, margin, margin, margin);
    }

    public ScrollGalleryView setThumbnailMargin(int left, int top, int right, int bottom) {
        thumbnailMarginLeft = left;
        thumbnailMarginTop = top;
        thumbnailMarginRight = right;
        thumbnailMarginBottom = bottom;
        return this;
    }

    public ScrollGalleryView setZoom(boolean zoomEnabled) {
        this.zoomEnabled = zoomEnabled;
        return this;
    }

    public ScrollGalleryView hideThumbnails(boolean thumbnailsHiddenEnabled) {
        this.thumbnailsHiddenEnabled = thumbnailsHiddenEnabled;
        horizontalScrollView.setVisibility(GONE);
        return this;
    }

    private Point getDisplaySize() {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(point);
        } else {
            point.set(display.getWidth(), display.getHeight());
        }
        return point;
    }

    private ImageView addThumbnail(Bitmap image) {
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(thumbnailSize, thumbnailSize);
        lp.setMargins(thumbnailMarginLeft, thumbnailMarginTop, thumbnailMarginRight, thumbnailMarginBottom);
        Bitmap thumbnail = createThumbnail(image);

        ImageView thumbnailView = createThumbnailView(lp, thumbnail);
        thumbnailsContainer.addView(thumbnailView);
        return thumbnailView;
    }

    private ImageView createThumbnailView(LinearLayout.LayoutParams lp, Bitmap thumbnail) {
        ImageView thumbnailView = new ImageView(context);
        thumbnailView.setLayoutParams(lp);
        thumbnailView.setImageBitmap(thumbnail);
        thumbnailView.setTag(mListOfMedia.size() - 1);
        thumbnailView.setOnClickListener(thumbnailOnClickListener);
        thumbnailView.setScaleType(ImageView.ScaleType.CENTER);
        return thumbnailView;
    }

    private Bitmap createThumbnail(Bitmap image) {
        return ThumbnailUtils.extractThumbnail(image, thumbnailSize, thumbnailSize);
    }

    private
    @IdRes
    int viewPagerResourceId = R.id.viewPager;

    private void initializeViewPager() {
        viewPager = (HackyViewPager) findViewById(viewPagerResourceId);
        pagerAdapter = new ScreenSlidePagerAdapter(fragmentManager, mListOfMedia, zoomEnabled);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerChangeListener);
    }

    public void removeItem(MediaInfo mediaInfo) {
        if (mListOfMedia.contains(mediaInfo)) {
            int index = mListOfMedia.indexOf(mediaInfo);
            thumbnailsContainer.removeViewAt(index);
            mListOfMedia.remove(mediaInfo);
            viewPager.removeAllViews();
            //make this to update the pager
            viewPager.setAdapter(pagerAdapter);
        }
    }

    public MediaInfo getItem(int position) {
        return pagerAdapter.getMediaItem(position);
    }

    public MediaInfo getCurrentItem() {
        return pagerAdapter.getMediaItem(viewPager.getCurrentItem());
    }

    private void scroll(View thumbnail) {
        int thumbnailCoords[] = new int[2];
        thumbnail.getLocationOnScreen(thumbnailCoords);

        int thumbnailCenterX = thumbnailCoords[0] + thumbnailSize / 2;
        int thumbnailDelta = displayProps.x / 2 - thumbnailCenterX;

        horizontalScrollView.smoothScrollBy(-thumbnailDelta, 0);
    }

    private int calculateInSampleSize(int imgWidth, int imgHeight, int maxWidth, int maxHeight) {
        int inSampleSize = 1;
        while (imgWidth / inSampleSize > maxWidth || imgHeight / inSampleSize > maxHeight) {
            inSampleSize *= 2;
        }
        return inSampleSize;
    }

    public int getItemCount() {
        return mListOfMedia.size();
    }
}
