package com.miller.tyler.recipebuddy;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;


public class WebviewActivity extends Activity {

    protected String mUrl;
    protected  WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        Intent intent = getIntent();
        String displayName = intent.getStringExtra("name");
        Uri recipeUri = intent.getData();
        mUrl = recipeUri.toString();

        mWebView = (WebView) findViewById(R.id.webView);
        mWebView.loadUrl(mUrl);

        //Set actionbar title
        ActionBar ab = getActionBar();
        ab.setTitle(displayName);

        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);






    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
