package com.ums.wifiprobe;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.ums.wifiprobe.service.ProbeService;

import java.util.List;

public class MainActivity extends Activity {
ImageView imageView;
    ProgressBar bar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);
//        imageView = (ImageView) findViewById(R.id.imageView);
//        GradientDrawable drawable1 = (GradientDrawable) imageView.getBackground();
//        drawable1.setColor(Color.RED);
//        bar = (ProgressBar) findViewById(R.id.progressbar);
//        int length = bar.getProgress();
//        Log.d("TAG","--"+length);
////      bar.setProgressDrawable(new ColorDrawable(Color.BLUE));
////        bar.setProgress(length);
//
//
//        GradientDrawable p=new GradientDrawable();
//        p.setCornerRadius(16);
//        p.setColor(Color.BLUE);
//        ClipDrawable progress = new ClipDrawable(p, Gravity.LEFT, ClipDrawable.HORIZONTAL);
//        GradientDrawable background = new GradientDrawable();
//        background.setColor(Color.RED);
//        background.setCornerRadius(16);
//        LayerDrawable pd=new LayerDrawable(new Drawable[]{background,progress});
//        bar.setProgressDrawable(pd);
//        bar.setProgress(length-1);
//        bar.setProgress(length);
    }


}
