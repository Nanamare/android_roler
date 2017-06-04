package com.buttering.roler.util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;

import com.alexvasilkov.gestures.views.GestureImageView;
import com.bumptech.glide.Glide;
import com.buttering.roler.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SeungTaek.Lim on 2017. 5. 18..
 */

public class ImageScaleDialog extends Dialog {
    @BindView(R.id.scalable_image) protected GestureImageView mImageView;
    @BindView(R.id.scalable_layer) protected RelativeLayout mScalableLayer;

    private final String mImagePath;

    public ImageScaleDialog(@NonNull Context context, String imagePath) {
        super(context, R.style.NoActionBarDialog);
        mImagePath = imagePath;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_image_scale);
        ButterKnife.bind(this);

        mImageView.getController().getSettings()
                .setFillViewport(true)
                .setRotationEnabled(true)
                .setOverzoomFactor(1.0f)
                .setMaxZoom(3.0f);
    }

    @Override
    protected void onStart() {
        super.onStart();

        Glide.with(getContext())
                .load(mImagePath)
                .into(mImageView);
    }
}
