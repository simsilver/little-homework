package com.simsilver.android;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.simsilver.view.PaintView;

import java.io.File;

import utils.Utils;

public class PaintViewActivity extends Activity implements View.OnClickListener {

    PaintView mPaintView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint_view);
        mPaintView = new PaintView(this, Color.WHITE, Color.BLACK);
        LinearLayout BaseView = (LinearLayout) findViewById(R.id.ll_screen);
        BaseView.addView(mPaintView);
        Button clear = (Button) findViewById(R.id.btn_clearPic);
        clear.setOnClickListener(this);
        Button show = (Button) findViewById(R.id.btn_showPic);
        show.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clearPic:
                mPaintView.clear();
                break;
            case R.id.btn_showPic:
                File jpgFile = new File(Environment.getExternalStorageDirectory(), "paint.jpg");
                Utils.saveJPG(mPaintView.getCachedBitmap(), jpgFile.getPath());
                Intent intent = Utils.getImageFileIntent(jpgFile.getPath());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

}
