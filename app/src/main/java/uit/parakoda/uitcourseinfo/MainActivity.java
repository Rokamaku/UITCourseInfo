package uit.parakoda.uitcourseinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.LinkedBlockingDeque;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private String username;
    private String password;
    private ArrayList<Course> courses;
    private ArrayList<CourseInfo> coursesDetail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences accInfo = this.getSharedPreferences("AccInfo",Context.MODE_PRIVATE);
        if (hasAccInfo(accInfo)) {
            getSharedPrefences(accInfo);
            InfoCourseWraper wraper = new InfoCourseWraper(username, password, courses);
            setContentView(R.layout.activity_main);
            String[] courseCode = new String[courses.size()];
            for (int i = 0; i < courses.size(); i++) {
                courseCode[i] = courses.get(i).getCourseCode();
            }
            CourseParser.setCourseCode(courseCode);
            context = MainActivity.this;
            try {
                coursesDetail = new DownloadCourseInfo().execute(wraper).get();
            } catch (ExecutionException | InterruptedException e) {
                Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT);
            }
        }
        else {
            callSignIn();
        }
    }

    public static Context getContext() {
        return context;
    }

    private boolean hasAccInfo(SharedPreferences accInfo) {
        if (accInfo.contains(getString(R.string.shared_prefence_user)))
                return true;
        return false;
    }

    private void callSignIn() {
        Intent SignInForm = new Intent(this, LoginActivity.class);
        startActivity(SignInForm);
    }

    private void getSharedPrefences(SharedPreferences accInfo) {
        username = accInfo.getString(getString(R.string.shared_prefence_user), null);
        password = accInfo.getString(getString(R.string.shared_prefence_pass), null);
        Gson gson = new Gson();
        String json = accInfo.getString(getString(R.string.shared_prefence_course), null);
        Type dataListType = new TypeToken<ArrayList<Course>>() {}.getType();
        courses = gson.fromJson(json, dataListType);

    }
}
