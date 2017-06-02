package com.veinhorn.scrollgalleryview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.github.chrisbanes.photoview.PhotoViewAttacher;
import com.veinhorn.scrollgalleryview.loader.MediaLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by veinhorn on 29.8.15.
 */
public class ImageFragment extends Fragment {

    Uri mSaveInstanceStateBitmapUri;
    private MediaInfo mMediaInfo;
    private HackyViewPager viewPager;
    private ProgressBar progressBar;
    private ImageView backgroundImage;
    private PhotoViewAttacher photoViewAttacher;

    static Uri writeTempStateStoreBitmap(Context context, Bitmap bitmap, Uri uri) {
        try {
            boolean needSave = true;
            if (uri == null) {
                uri = Uri.fromFile(File.createTempFile("sgv_tmp_image", ".jpg", context.getCacheDir()));
            } else if (new File(uri.getPath()).exists()) {
                needSave = false;
            }
            if (needSave) {
                writeBitmapToUri(context, bitmap, uri, Bitmap.CompressFormat.JPEG, 100);
            }
            return uri;
        } catch (Exception e) {
            return null;
        }
    }

    static void writeBitmapToUri(Context context, Bitmap bitmap, Uri uri, Bitmap.CompressFormat compressFormat, int compressQuality) throws FileNotFoundException {
        OutputStream outputStream = null;
        try {
            outputStream = context.getContentResolver().openOutputStream(uri);
            bitmap.compress(compressFormat, compressQuality, outputStream);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException ignored) {
                }
            }
        }
    }

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
                backgroundImage.setImageURI((Uri) savedInstanceState.getParcelable(Constants.IMAGE));
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
            writeTempStateStoreBitmap(getContext(), ((BitmapDrawable) backgroundImage.getDrawable()).getBitmap(), mSaveInstanceStateBitmapUri);
            outState.putParcelable(Constants.IMAGE, mSaveInstanceStateBitmapUri);
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
