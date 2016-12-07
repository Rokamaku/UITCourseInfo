package uit.parakoda.uitcourseinfo;

import android.content.res.Resources;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by parakoda on 12/3/16.
 */

class CourseParser {

    private Document doc;
    private static String[] CourseCode;
    public static void setCourseCode(String[] CourseCode) { CourseParser.CourseCode = CourseCode; }
    public CourseParser(Document doc) {
        this.doc = doc;
    }
    public static ArrayList<Course> getCourseInfo(Document doc, String[] CoursesCode) {
        ArrayList<Course> Courses = new ArrayList<Course>();
        Elements courseName = doc.select(LoginActivity.getContext().getString(R.string.Course_pos));
        for (int i = 0; i < CoursesCode.length; i++) {
            boolean isExist = false;
            for( Element ele : courseName) {
                if (ele.text().contains(CoursesCode[i])) {
                    isExist = true;
                    Course aCourse = new Course();
                    aCourse.setTitle(ele.text());
                    aCourse.setLink(ele.child(0).attr("href"));
                    aCourse.setCourseCode(CoursesCode[i]);
                    Courses.add(aCourse);
                    break;
                }
            }
            if (!isExist)
                Toast.makeText(LoginActivity.getContext(), CoursesCode[i] + "not exists" , Toast.LENGTH_SHORT);
        }
        return Courses;
    }

    public static ArrayList<DaaNotifi> getDaaNotifi(Document doc) {
        ArrayList<DaaNotifi> Notifications = new ArrayList<DaaNotifi>();
        Elements BlockNotifies = doc.select(MainActivity.getContext().getString(R.string.Notifi_pos));
        Elements BlockNotifiesRoom = doc.select(MainActivity.getContext().getString(R.string.NotifiRoom_pos));
        Notifications.addAll(getBlockNotifi(BlockNotifies));
        Notifications.addAll(getBlockNotifi(BlockNotifiesRoom));
        return Notifications;
    }

    private static ArrayList<DaaNotifi> getBlockNotifi(Elements Block) {
        ArrayList<DaaNotifi> BlockNotifi = new ArrayList<DaaNotifi>();
        Elements listContent = Block.select("div[class=item-list]");
        Elements listNotifi = listContent.get(0).child(0).children();
        for (int i = 0; i < CourseCode.length; i++) {
            for (Element aNotifi : listNotifi) {
                if (aNotifi.child(1).text().contains(CourseCode[i])) {
                    DaaNotifi aNotification = new DaaNotifi();
                    aNotification.setTitle(aNotifi.text());
                    aNotification.setLink(MainActivity.getContext()
                            .getString(R.string.daa_link) + aNotifi.child(1).attr("href"));
                    BlockNotifi.add(aNotification);
                }
            }

        }
        return BlockNotifi;
    }



    public ArrayList<Newest> parseNewest() {
        ArrayList<Newest> News = new ArrayList<Newest>();
        Elements newsPost = doc.select(MainActivity.getContext().getString(R.string.News_pos));
        for (Element ele : newsPost) {
            Newest Post = new Newest();
            Post.setDate(ele.child(0).child(0).text());
            Post.setAuthor(ele.child(0).child(1).text());
            Post.setTitle(ele.child(1).child(0).text());
            Post.setLink(ele.child(1).child(0).attr("href"));
            News.add(Post);
        }
        return News;
    }

    public ArrayList<Assignment> parseAssignment() {
        ArrayList<Assignment> Assignments = new ArrayList<Assignment>();
        Elements AssignmentPost = doc.select(MainActivity.getContext().getString(R.string.Ass_pos));
        for (Element ele : AssignmentPost) {
            Assignment ass = new Assignment();
            ass.setTitle(ele.child(1).text());
            ass.setTimeofDeadline(ele.child(2).text());
            Assignments.add(ass);
        }
        return Assignments;
    }

    public ArrayList<Resource> parseResource() {
        ArrayList<Resource> Resources = new ArrayList<Resource>();
        Elements ResourcePost = doc.select(MainActivity.getContext().getString(R.string.Res_pos));
        for (Element ele : ResourcePost) {
            Resource res = new Resource();
            res.setTitle(ele.child(0).child(0).child(1).child(0).child(0).child(1).text());
            res.setLink(ele.child(0).child(0).child(1).child(0).child(0).attr("href"));
            Resources.add(res);
        }
        return Resources;
    }


}
