package com.ckos.kcc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;

public class ResultActivity extends Activity {

    private WebView webResult;
    private Button btnCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        //获取查询目标
        String tagUrl = this.getIntent().getStringExtra("tagUrl");

        //初始化
        webResult = this.<WebView>findViewById(R.id.webResult);
        btnCallback = this.<Button>findViewById(R.id.btnCallback);
        //返回主界面按钮
        btnCallback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //打开查询结果
        WebSettings setting = webResult.getSettings();
        setting.setJavaScriptEnabled(true);
        setting.setSupportZoom(true);
        setting.setBuiltInZoomControls(true);

        webResult.loadUrl(tagUrl);

    }
}
