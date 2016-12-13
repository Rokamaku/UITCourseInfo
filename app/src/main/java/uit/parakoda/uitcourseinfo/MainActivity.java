package uit.parakoda.uitcourseinfo;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.support.v4.view.MenuItemCompat.getActionView;

public class MainActivity extends AppCompatActivity {
    private static Context context;
    private String username;
    private String password;
    private ArrayList<Course> courses;
    private ArrayList<CourseInfo> coursesDetail = null;
    private int courseChoosed;
    private int infoChoosed;
    private ListView listViewShow = null;
    private DrawerLayout mDrawerLayout;
    private Toolbar mToolbar;
    private NavigationView mNavView;
    private Spinner mSpinner_nav;
    private SharedPreferences accInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (HTMLGetter.haveNetworkConnection(MainActivity.this)) {
            accInfo = this.getSharedPreferences(getString(R.string
                    .shared_prefence_name), Context.MODE_PRIVATE);
            if (hasAccInfo(accInfo)) {
                processData(accInfo);
                setView();
            }  else {
                callSignIn();
            }
        } else {
            Toast.makeText(MainActivity.this, getString(R.string.error_no_network),
                    Toast.LENGTH_LONG).show();
            finishAffinity();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (coursesDetail == null)
            return false;
        getMenuInflater().inflate(R.menu.activity_main_actionsbar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }

        return (super.onOptionsItemSelected(item));
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
        SignInForm.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        SignInForm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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


    private void processData(SharedPreferences accInfo) {
        getSharedPrefences(accInfo);
        InfoCourseWraper wraper = new InfoCourseWraper(username, password, courses);
        String[] courseCode = getListCourseString();
        CourseParser.setCourseCode(courseCode);
        context = MainActivity.this;
        try {
            coursesDetail = new AsyncTaskDownloadCourseInfo(MainActivity.this).execute(wraper).get();
            if (coursesDetail == null) {
                Toast.makeText(MainActivity.this, getString(R.string
                        .error_connection), Toast.LENGTH_LONG).show();
                return;
            }
        } catch (ExecutionException | InterruptedException e) {
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private void setView() {
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.activity_main) ;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        listViewShow = (ListView)findViewById(R.id.listViewShow);
        mSpinner_nav = (Spinner) findViewById(R.id.spinner_nav);
        setViewNavigtionView();
        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        addItemsToSpinner();

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_dehaze_white_36dp);
        ab.setDisplayHomeAsUpEnabled(true);

    }

    private void setViewNavigtionView() {
        mNavView = (NavigationView) findViewById(R.id.nav_menu);
        mNavView.setItemIconTintList(null);
        mNavView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        mDrawerLayout.closeDrawers();
                        int ItemId = menuItem.getItemId();
                        if (ItemId ==  R.id.Daa_item) {
                            infoChoosed = 0;
                            UncheckOthers(ItemId);
                            setItemDisplay();
                            return true;
                        }
                        else if (ItemId == R.id.Forum_item) {
                            infoChoosed = 1;
                            UncheckOthers(ItemId);
                            setItemDisplay();
                            return true;
                        }
                        else if (ItemId == R.id.Ass_item) {
                            infoChoosed = 2;
                            UncheckOthers(ItemId);
                            setItemDisplay();
                            return true;
                        }
                        else if (ItemId == R.id.Resource_item) {
                            infoChoosed = 3;
                            UncheckOthers(ItemId);
                            setItemDisplay();
                            return true;
                        }
                        else if (ItemId == R.id.signout_item) {
                            clearSharedPrefences();
                            callSignIn();
                            return true;
                        }
                        else {
                            finishAffinity();
                            return true;
                        }
                    }
                });
        TextView TVusername = (TextView)mNavView.getHeaderView(0).findViewById(R.id.TVusername);
        TVusername.setText(username);
        mNavView.getMenu().getItem(0).setChecked(true);

    }

    private void UncheckOthers(int ItemId) {
        Menu menu = mNavView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            if (menu.getItem(i).getItemId() != ItemId) {
                menu.getItem(i).setChecked(false);
            }
        }

    }


    private String[] getListCourseString() {
        String[] courseCode = new String[courses.size()];
        for (int i = 0; i < courses.size(); i++) {
            courseCode[i] = courses.get(i).getCourseCode();
        }
        return courseCode;
    }

    private ArrayList<String> getListCourseString(ArrayList<CourseInfo> listCourse) {
        ArrayList<String> courseCode = new ArrayList<String>();
        for (int i = 0; i < listCourse.size(); i++) {
            courseCode.add(listCourse.get(i).getCourseName());
        }
        return courseCode;
    }

    private void addItemsToSpinner() {

        ArrayList<String> listCourseString = getListCourseString(coursesDetail);
        CustomSpinnerAdapter spinAdapter = new CustomSpinnerAdapter(this,
                R.layout.spinner_row, listCourseString);

        mSpinner_nav.setAdapter(spinAdapter);
        mSpinner_nav.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v,
                                       int position, long id) {
                courseChoosed = position;
                setItemDisplay();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {

            }
        });
    }

    private void setItemDisplay() {
        CourseInfo courseDetailChoosed = coursesDetail.get(courseChoosed);
        listViewShow.setEmptyView(findViewById(R.id.TextView_Empty));
        switch (infoChoosed) {
            case 0:
                ArrayList<DaaNotifi> listDaaNotifi = courseDetailChoosed.getNotifications();
                DaaNotifiAdapter AdapterDaaNotifi = new DaaNotifiAdapter(this,
                        R.layout.daanotifi_item, listDaaNotifi);
                listViewShow.setAdapter(AdapterDaaNotifi);
                break;
            case 1:
                ArrayList<Newest> listNews = courseDetailChoosed.getNews();
                NewsForumAdapter AdapterNews = new NewsForumAdapter(this,
                        R.layout.newsforum_item, listNews);
                listViewShow.setAdapter(AdapterNews);
                break;
            case 2:
                ArrayList<Assignment> listAss = courseDetailChoosed.getAss();
                AssignmentAdapter AdapterAss = new AssignmentAdapter(this,
                    R.layout.assignment_item, listAss);
                listViewShow.setAdapter(AdapterAss);
                break;
            case 3:
                ArrayList<Resource> listRes = courseDetailChoosed.getResources();
                ResourceAdapter AdapterRes = new ResourceAdapter(this,
                R.layout.resource_item, listRes);
                listViewShow.setAdapter(AdapterRes);
                break;
        }
    }


    private void clearSharedPrefences() {
        SharedPreferences.Editor edit = accInfo.edit();
        edit.clear();
        edit.commit();
    }

}
