package com.ums.wifiprobe.ui.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.widget.TextView;

import com.ums.wifiprobe.R;
import com.ums.wifiprobe.app.GlobalValueManager;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by chenzhy on 2018/3/6.
 */

public class ProtocolActivity extends Activity {
    TextView tvTitle, tvContent;
    String protocolFileName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_protocol);
        protocolFileName = getIntent().getStringExtra("protocol");
        tvTitle = (TextView) findViewById(R.id.title);
        tvContent = (TextView) findViewById(R.id.text);
        SpannableStringBuilder titleSsb = new SpannableStringBuilder();
        SpannableStringBuilder textSsb = new SpannableStringBuilder();
        try {
            InputStream open = getAssets().open(protocolFileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(open, "utf-8"));
            String title = reader.readLine();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                titleSsb.append(title + "\n", new TextAppearanceSpan(this, R.style.p_title), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                SpannableString ss = new SpannableString(title + "\n");
                ss.setSpan(new TextAppearanceSpan(this, R.style.p_title), 0, title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                titleSsb.append(ss + "\n");
            }
            String line;
            while (true) {
                line = reader.readLine();
                if (line == null) {
                    break;
                } else {
//                } else if (line.isEmpty()) {
////                    textSsb.append("\n", new TextAppearanceSpan(this,R.style.p_text), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                } else if (line.matches("^\\s*(一|二|三|四|五|六|七|八|九|十)+.*")) {
//                    textSsb.append("\n" + line, new TextAppearanceSpan(this, R.style.p_t1), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                } else {
//                    if (line.matches("^\\s*\\(?\\d+.*")) {
//                        textSsb.append("\n" + line, new TextAppearanceSpan(this, R.style.p_text), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    } else {
//                        textSsb.append(line, new TextAppearanceSpan(this, R.style.p_text), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        textSsb.append(line, new TextAppearanceSpan(this, R.style.p_text), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    } else {
                        SpannableString ss = new SpannableString(line + "\n");
                        ss.setSpan(new TextAppearanceSpan(this, R.style.p_text), 0, line.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        textSsb.append(ss);
                    }
                }
            }
            tvTitle.setText(titleSsb);
            tvContent.setText(textSsb);
        } catch (Exception e) {
            tvContent.setText(Log.getStackTraceString(e));
        }
    }
}
