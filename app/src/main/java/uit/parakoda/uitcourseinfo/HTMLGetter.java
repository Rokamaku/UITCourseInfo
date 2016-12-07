package uit.parakoda.uitcourseinfo;

import android.widget.Toast;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.MappedByteBuffer;
import java.util.List;
import java.util.Map;


/**
 * Created by parakoda on 12/5/16.
 */

class HTMLGetter {
    protected String urlPage = null;
    protected final String USER_AGENT = "Mozilla/5.0";

    public HTMLGetter() {
        urlPage = "https://courses.uit.edu.vn";
    }
    public HTMLGetter(String Page) {
        urlPage = Page;
    }

    public void seturlPage(String urlPage) {
        this.urlPage = urlPage;
    }

    public Document getDoc() throws IOException {
        Connection.Response course = Jsoup.connect(urlPage)
                .followRedirects(true)
                .userAgent(USER_AGENT)
                .method(Connection.Method.GET)
                .execute();
        Document doc = course.parse();
        return doc;
    }
}


class HTMLAuthGetter extends HTMLGetter {
    private Map<String, String> Cookies = null;
    private String urlLogin = null;
    private String userName;
    private String passWord;
    public HTMLAuthGetter(String userName, String passWord) {
        super();
        urlLogin = "https://courses.uit.edu.vn/login/index.php";
        this.userName = userName;
        this.passWord = passWord;
    }

    public HTMLAuthGetter(String urlPage, String urlLogin, String userName, String passWord) {
        super(urlPage);
        this.urlLogin = urlLogin;
        this.userName = userName;
        this.passWord = passWord;
    }

    public void getAuthentication() throws IOException {

        Connection.Response Firstreq = Jsoup.connect(urlLogin)
                .followRedirects(true)
                .header("Connection", "Keep-alive")
                .userAgent(USER_AGENT)
                .method(Connection.Method.GET)
                .execute();
        Connection.Response getAuth = Jsoup.connect(urlLogin)
                .followRedirects(true)
                .cookies(Firstreq.cookies())
                .method(Connection.Method.POST)
                .data("username", userName)
                .data("password", passWord)
                .referrer(urlPage)
                .execute();
        Cookies = getAuth.cookies();
    }

    @Override
    public Document getDoc() throws IOException {
        Connection.Response course = Jsoup.connect(urlPage)
                .followRedirects(true)
                .cookies(Cookies)
                .userAgent(USER_AGENT)
                .method(Connection.Method.GET)
                .execute();
        Document doc = course.parse();
        return doc;
    }
}
