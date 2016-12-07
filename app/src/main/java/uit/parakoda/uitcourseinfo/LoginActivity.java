package uit.parakoda.uitcourseinfo;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static android.Manifest.permission.READ_CONTACTS;

public class LoginActivity extends AppCompatActivity{

    private static Context context;
    private EditText EditTextUser;
    private EditText EditTextPass;
    private EditText EditTextCourseCode;
    private Button BtnSignIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FindViewByID();
        BtnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckValidInfo(v);

            }
        });

    }

    private void FindViewByID() {
        EditTextUser = (EditText)findViewById(R.id.EditTextUserName);
        EditTextPass = (EditText)findViewById(R.id.EditTextPassword);
        EditTextCourseCode = (EditText)findViewById(R.id.EditTextCourseCode);
        BtnSignIn = (Button)findViewById(R.id.buttonSignIn);
    }

    private String[] parseCourseCode() {
        String CourseCode = EditTextCourseCode.getText().toString();
        String[] tokens = CourseCode.split(",");
        for (int i = 0; i < tokens.length; i++) {
            tokens[i] = tokens[i].replaceAll("\\s+","");
        }
        return tokens;
    }

    public static Context getContext() {
        return context;
    }

    private void CheckValidInfo(View v) {
        if (EditTextUser.getText().length() == 0 ||
                EditTextPass.getText().length() == 0 ||
                EditTextCourseCode.getText().length() == 0)
            Toast.makeText(v.getContext(), R.string.error_not_fill_msg,
                    Toast.LENGTH_SHORT).show();
        else {
            String userName = EditTextUser.getText().toString();
            String passWord = EditTextPass.getText().toString();
            String[] CourseCode = parseCourseCode();
            if (CourseCode.length == 0) {
                Toast.makeText(v.getContext(),
                        R.string.error_no_course_code, Toast.LENGTH_SHORT).show();
            }
            else {
                CheckvalidAccountOnline(v, userName, passWord, CourseCode);
            }
        }
    }

    private void CheckvalidAccountOnline (View v, String userName, String passWord, String[] CourseCode) {
        try {
            InfoWraper wrapInfo = new InfoWraper(userName, passWord, CourseCode);
            context = LoginActivity.this;
            ArrayList<Course> courses = new DownloadCourseName().execute(wrapInfo).get();
            if (courses.size() == 0) {
                Toast.makeText(v.getContext(),
                        R.string.error_invalid_acc_or_course_code, Toast.LENGTH_SHORT).show();
            }
            else {
                SaveSharePrenfes(v, userName, passWord, courses);
                Intent callMain = new Intent(this, MainActivity.class);
                startActivity(callMain);

            }

        } catch (Exception e) {
            Toast.makeText(v.getContext(), e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveSharePrenfes(View v, String userName, String passWord, ArrayList<Course> courses) {
        SharedPreferences accInfo = v.getContext().getSharedPreferences("AccInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = accInfo.edit();
        editor.putString(getString(R.string.shared_prefence_user), userName);
        editor.putString(getString(R.string.shared_prefence_pass), passWord);
        Gson gson = new Gson();
        String json = gson.toJson(courses);
        editor.putString(getString(R.string.shared_prefence_course), json);
        editor.commit();
    }

}

