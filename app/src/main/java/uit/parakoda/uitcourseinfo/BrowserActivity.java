package uit.parakoda.uitcourseinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.CookieManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class BrowserActivity extends AppCompatActivity {
    private WebView webview;
    private String url;
    private String username;
    private String password;
    private CookieManager mCookies = CookieManager.getInstance();
    String cookies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browser);
        webview = (WebView) findViewById(R.id.WebViewBrowser);
        Intent BrowseIntent = getIntent();
        url = BrowseIntent.getStringExtra(MainActivity.MESSAGE_BROWSE);
        getSharedPrefences();
        try {
            Map<String, String> Cookies = new AsyncTaskGetAuth().execute(username, password).get();
            mCookies.setAcceptCookie(true);
            mCookies.setCookie(url,
                    getString(R.string.cookie_name) + Cookies.entrySet().iterator().next().getValue());
            webview.loadUrl(url);
        } catch (InterruptedException | ExecutionException e) {
            Toast.makeText(BrowserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

    }

    private void getSharedPrefences() {
        SharedPreferences accInfo = this.getSharedPreferences(getString(R.string
                .shared_prefence_name), Context.MODE_PRIVATE);
        username = accInfo.getString(getString(R.string.shared_prefence_user), null);
        password = accInfo.getString(getString(R.string.shared_prefence_pass), null);
    }
}
