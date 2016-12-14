package uit.parakoda.uitcourseinfo;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.provider.ContactsContract;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.MissingFormatArgumentException;
import java.util.Objects;

/**
 * Created by parakoda on 12/4/16.
 */

class InfoWraper {
    private String userName;
    private String passWord;
    private String[] courseCode;
    InfoWraper(String userName, String passWord, String[] courseCode) {
        this.userName = userName;
        this.passWord = passWord;
        this.courseCode = courseCode;
    }

    public String getUserName() { return userName; }
    public String getPassWord() { return passWord; }
    public String[] getCourseCode() { return courseCode; }
}


class InfoCourseWraper {
    private String userName;
    private String passWord;
    private ArrayList<Course> courses;
    InfoCourseWraper(String userName, String passWord, ArrayList<Course> courses) {
        this.userName = userName;
        this.passWord = passWord;
        this.courses = courses;
    }

    public String getUserName() { return userName; }
    public String getPassWord() { return passWord; }

    public ArrayList<Course> getCourses() {
        return courses;
    }
}



class AsyncTaskDownloadCourseName extends AsyncTask<InfoWraper, Void, ArrayList<Course> > {
    ProgressDialog progDailog;
    public AsyncTaskDownloadCourseName(Context context) {
        progDailog = new ProgressDialog(context);
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progDailog = new ProgressDialog(LoginActivity.getContext());
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(true);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
}
    @Override
    protected ArrayList<Course> doInBackground(InfoWraper... params) {
        HTMLAuthGetter HTMLFile = new HTMLAuthGetter(params[0].getUserName(), params[0].getPassWord());
        Document HTMLCourse = null;
        try {
            HTMLFile.getAuthentication();
            HTMLCourse = HTMLFile.getDoc();
        } catch (IOException e) {
           return null;
        }
        ArrayList<Course> courses = CourseParser.getCourseInfo(HTMLCourse,
                params[0].getCourseCode());
        return courses;
    }
    @Override
    protected void onPostExecute(ArrayList<Course> params) {
        super.onPostExecute(params);
        progDailog.dismiss();
    }

}


class AsyncTaskDownloadCourseInfo extends AsyncTask<InfoCourseWraper, Void, ArrayList<CourseInfo>> {
    public AsyncTaskDownloadCourseInfo() {
    }
    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }


    @Override
    protected ArrayList<CourseInfo> doInBackground(InfoCourseWraper... params) {
        ArrayList<CourseInfo> listCourseDetail = new ArrayList<CourseInfo>();
        HTMLAuthGetter HTMLFile = new HTMLAuthGetter(params[0].getUserName(), params[0].getPassWord());
        HTMLGetter HTMLDaaFile = new HTMLGetter(MainActivity.getContext().getString(R.string.daa_link));
        try {
            HTMLFile.getAuthentication();
            Document docCourse = null;
            Document docDaa = null;
            for (Course aCourse : params[0].getCourses()) {
                CourseInfo aCourseDetail = new CourseInfo();
                HTMLFile.seturlPage(aCourse.getLink());
                docCourse = HTMLFile.getDoc();
                docDaa = HTMLDaaFile.getDoc();
                CourseParser parseCourse = new CourseParser(docCourse);
                aCourseDetail.setCourseName(aCourse.getTitle());
                aCourseDetail.setNews(parseCourse.parseNewest());
                aCourseDetail.setAssignments(parseCourse.parseAssignment());
                aCourseDetail.setResources(parseCourse.parseResource());
                aCourseDetail.setNotifications(CourseParser.getDaaNotifi(docDaa));
                listCourseDetail.add(aCourseDetail);
            }
        } catch (IOException e) {
            return null;
        }
        return listCourseDetail;
    }

    @Override
    protected void onPostExecute(ArrayList<CourseInfo> params) {
        super.onPostExecute(params);

    }

}


class AsyncTaskGetAuth extends AsyncTask<String, Void, Map<String, String>> {
    @Override
    protected Map<String, String> doInBackground(String... params) {
        HTMLAuthGetter getAuth = new HTMLAuthGetter(params[0], params[1]);
        try {
            getAuth.getAuthentication();
        } catch (IOException e) {
            return null;
        }
        return getAuth.getCookies();
    }
}

