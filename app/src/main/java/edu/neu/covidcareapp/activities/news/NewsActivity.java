package edu.neu.covidcareapp.activities.news;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import edu.neu.covidcareapp.R;
import edu.neu.covidcareapp.activities.community.NetworkUtil;

public class NewsActivity extends AppCompatActivity {

    private WebView mWebView;

    private EditText mWebDestEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mWebView = (WebView)findViewById(R.id.webview);
        //mWebDestEditText = (EditText)findViewById(R.id.webview_edit_text);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.loadUrl("https://www.cdc.gov/coronavirus/2019-ncov/index.html?CDC_AA_refVal=https%3A%2F%2Fwww.cdc.gov%2Fcoronavirus%2Findex.html");

        // Setting the WebViewClient to allow the WebView to handle
        // redirects within the WebView, as opposed to being launched in a browser.
        mWebView.setWebViewClient(new WebViewClient(){
            // Api < 24
            // Deprecated in API 24, but the alternative is not compatible with Android <19
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl("https://www.cdc.gov/coronavirus/2019-ncov/index.html?CDC_AA_refVal=https%3A%2F%2Fwww.cdc.gov%2Fcoronavirus%2Findex.html");
                return true;
            }
            // Api > 24
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                String url = request.getUrl().toString();
                view.loadUrl(url);
                return true;
            }
        });

    }

    public void loadWebsite(View view){

        String url = mWebDestEditText.getText().toString().trim();
        try {
            url = NetworkUtil.validInput(url);
            mWebView.loadUrl(url);
        } catch (NetworkUtil.MyException e) {
            Toast.makeText(getApplication(),e.toString(),Toast.LENGTH_SHORT).show();
        }
    }

}
