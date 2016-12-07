package uit.parakoda.uitcourseinfo;

import org.jsoup.Connection;

import java.util.ArrayList;

/**
 * Created by parakoda on 12/3/16.
 */


class CourseInfo {
    private String CourseName;
    private ArrayList<Newest> News;
    private ArrayList<Assignment> Assignments;
    private ArrayList<Resource> Resources;
    private ArrayList<DaaNotifi> Notifications;

    public String getCourseName() { return CourseName; }
    public ArrayList<Newest> getNews() { return News; }
    public ArrayList<Assignment> getAss() { return Assignments; }
    public ArrayList<Resource> getResources() { return Resources; }
    public ArrayList<DaaNotifi> getNotifications() { return Notifications; }

    public void setCourseName(String CourseName) { this.CourseName = CourseName; }
    public void setNews(ArrayList<Newest> News) { this.News = News; }
    public void setAssignments(ArrayList<Assignment> Assignments) { this.Assignments = Assignments; }
    public void setResources(ArrayList<Resource> Resources) { this.Resources = Resources; }
    public void setNotifications(ArrayList<DaaNotifi> Notifications) { this.Notifications = Notifications; }
}

class BasicInfo {
    protected String Title;
    protected String Link;
    protected BasicInfo() {}
    protected String getTitle() { return Title; }
    protected String getLink() { return Link; }
    protected void setTitle(String Title) { this.Title = Title; }
    protected void setLink(String Link) { this.Link = Link; }
}

class Course extends BasicInfo {
    private String CourseCode;
    public String getCourseCode() { return CourseCode; }
    public void setCourseCode(String CourseCode) { this.CourseCode = CourseCode; }
}

class Newest extends BasicInfo {
    private String Author;
    private String Date;
    public String getDate() { return Date; }
    public String getAuthor() {
        return Author;
    }
    public void setDate(String Date) {
        this.Date = Date;
    }
    public void setAuthor(String Author) {
        this.Author = Author;
    }
}

class Assignment extends BasicInfo {
    private String TimeofDeadline;
    public String getTimeofDeadline() {
        return TimeofDeadline;
    }
    public void setTimeofDeadline(String TimeofDeadline) {
        this.TimeofDeadline = TimeofDeadline;
    }

}

class Resource extends BasicInfo {

}

class DaaNotifi extends BasicInfo {

}


