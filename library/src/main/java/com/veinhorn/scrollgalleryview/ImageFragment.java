package com.veinhorn.scrollgalleryview;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by veinhorn on 29.8.15.
 */
public class ImageFragment extends Fragment {

    private MediaInfo mMediaInfo;

    private HackyViewPager viewPager;
    private ProgressBar progressBar;
    private ImageView backgroundImage;
    private PhotoViewAttacher photoViewAttacher;

    public void setMediaInfo(MediaInfo mediaInfo) {
        mMediaInfo = mediaInfo;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View rootView = inflater.inflate(R.layout.image_fragment, container, false);
        backgroundImage = (ImageView) rootView.findViewById(R.id.backgroundImage);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        viewPager = (HackyViewPager) getActivity().findViewById(R.id.viewPager);

        if (savedInstanceState != null) {
            boolean isLocked = savedInstanceState.getBoolean(Constants.IS_LOCKED, false);
            viewPager.setLocked(isLocked);
            if (savedInstanceState.containsKey(Constants.IMAGE)) {
                backgroundImage.setImageBitmap((Bitmap) savedInstanceState.getParcelable(Constants.IMAGE));
            }
            createViewAttacher(savedInstanceState);
        }

        loadImageToView();

        return rootView;
    }

    private void loadImageToView() {
        if (mMediaInfo != null) {
            if (mMediaInfo.getBackgroundColor() != -1) {
                backgroundImage.setBackgroundResource(mMediaInfo.getBackgroundColor());
            }
            if (mMediaInfo.getPlaceholder() != -1) {
                backgroundImage.setImageResource(mMediaInfo.getPlaceholder());
            }
            mMediaInfo.setCurrentImageView(backgroundImage);
            mMediaInfo.getLoader().loadMedia(getActivity(), backgroundImage, new MediaLoader.SuccessCallback() {
                @Override
                public void onSuccess() {
                    progressBar.setVisibility(View.GONE);
                    createViewAttacher(getArguments());
                }
            });
        }
    }

    private void createViewAttacher(Bundle savedInstanceState) {
        if (savedInstanceState.getBoolean(Constants.ZOOM)) {
            photoViewAttacher = new PhotoViewAttacher(backgroundImage);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (isViewPagerActive()) {
            outState.putBoolean(Constants.IS_LOCKED, viewPager.isLocked());
        }
        if (isBackgroundImageActive()) {
            outState.putParcelable(Constants.IMAGE, ((BitmapDrawable) backgroundImage.getDrawable()).getBitmap());
        }
        outState.putBoolean(Constants.ZOOM, photoViewAttacher != null);
        super.onSaveInstanceState(outState);
    }

    private boolean isViewPagerActive() {
        return viewPager != null;
    }

    private boolean isBackgroundImageActive() {
        return backgroundImage != null && backgroundImage.getDrawable() != null && backgroundImage.getDrawable() instanceof BitmapDrawable;
    }
}
