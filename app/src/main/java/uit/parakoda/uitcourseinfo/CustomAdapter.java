package uit.parakoda.uitcourseinfo;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by parakoda on 12/10/16.
 */

abstract class CustomAdapter extends ArrayAdapter<BasicInfo> {
    protected Context mContext = null;
    protected int layoutId;

    public CustomAdapter(Context context, int layoutId, List<BasicInfo> listInfo) {
        super(context, layoutId, listInfo);
        this.mContext = context;
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)mContext).getLayoutInflater();
            view = inflater.inflate(layoutId, null);
        }
        return setView(view, position);
    }

    abstract protected View setView(View view, int position);
}


class NewsForumAdapter extends CustomAdapter {
    private ArrayList<Newest> listNews = null;

    public NewsForumAdapter(Context context, int layoutId, List<? extends BasicInfo> listNews) {
        super(context, layoutId, Collections.unmodifiableList(listNews));
        this.listNews = (ArrayList<Newest>)listNews;
    }

    @Override
    protected View setView(View view, int position) {
        TextView TVTitle = (TextView) view.findViewById(R.id.TextViewTitle);
        TextView TVAuthor = (TextView) view.findViewById(R.id.TextViewAuthor);
        TextView TVDate = (TextView) view.findViewById(R.id.TextViewDate);
        ImageView btnLink = (ImageView) view.findViewById(R.id.BtnBrowse);

        final Newest ANEWS = listNews.get(position);

        TVTitle.setText(ANEWS.getTitle());
        TVAuthor.setText(ANEWS.getAuthor());
        TVDate.setText(ANEWS.getDate());

        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenBrowse = new Intent(Intent.ACTION_VIEW);
                intenBrowse.setData(Uri.parse(ANEWS.getLink()));
                mContext.startActivity(intenBrowse);
            }
        });
        return view;
    }
}

class AssignmentAdapter extends CustomAdapter {

    private ArrayList<Assignment> listAss = null;

    public AssignmentAdapter(Context context, int layoutId, List<? extends BasicInfo> listAss) {
        super(context, layoutId, Collections.unmodifiableList(listAss));
        this.listAss = (ArrayList<Assignment>) listAss;
    }

    @Override
    protected View setView(View view, int position) {

        TextView TVTitle = (TextView) view.findViewById(R.id.TextViewTitle);
        TextView TVDeadLine = (TextView) view.findViewById(R.id.TextViewAuthor);
        TextView TVRemainTime = (TextView) view.findViewById(R.id.TextViewRemainTime);
        ImageView btnLink = (ImageView) view.findViewById(R.id.BtnBrowse);

        final Assignment AAss = listAss.get(position);

        TVTitle.setText(AAss.getTitle());
        TVDeadLine.setText(AAss.getTimeofDeadline());
        TVRemainTime.setText(AAss.getRemainingTime());

        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenBrowse = new Intent(Intent.ACTION_VIEW);
                intenBrowse.setData(Uri.parse(AAss.getLink()));
                mContext.startActivity(intenBrowse);
            }
        });
        return view;
    }
}



class ResourceAdapter extends CustomAdapter {

    private ArrayList<Resource> listRes = null;

    public ResourceAdapter(Context context, int layoutId, List<? extends BasicInfo> listRes) {
        super(context, layoutId, Collections.unmodifiableList(listRes));
        this.listRes = (ArrayList<Resource>) listRes;
    }

    @Override
    protected View setView(View view, int position) {
        TextView TVTitle = (TextView) view.findViewById(R.id.TextViewTitle);
        ImageView btnLink = (ImageView) view.findViewById(R.id.BtnBrowse);

        final Resource ARes = listRes.get(position);

        TVTitle.setText(ARes.getTitle());

        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenBrowse = new Intent(Intent.ACTION_VIEW);
                intenBrowse.setData(Uri.parse(ARes.getLink()));
                mContext.startActivity(intenBrowse);
            }
        });
        return view;
    }
}

class DaaNotifiAdapter extends CustomAdapter {

    private ArrayList<DaaNotifi> listDaaNotifi = null;

    public DaaNotifiAdapter(Context context, int layoutId, List<? extends BasicInfo> listDaaNotifi) {
        super(context, layoutId, Collections.unmodifiableList(listDaaNotifi));
        this.listDaaNotifi = (ArrayList<DaaNotifi>) listDaaNotifi;
    }

    @Override
    protected View setView(View view, int position) {
        TextView TVTitle = (TextView) view.findViewById(R.id.TextViewTitle);
        TextView TVPublishDate = (TextView) view.findViewById(R.id.TextViewPublicDate);
        ImageView btnLink = (ImageView) view.findViewById(R.id.BtnBrowse);

        final DaaNotifi ARes = listDaaNotifi.get(position);

        TVTitle.setText(ARes.getTitle());


        btnLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intenBrowse = new Intent(Intent.ACTION_VIEW);
                intenBrowse.setData(Uri.parse(ARes.getLink()));
                mContext.startActivity(intenBrowse);
            }
        });
        return view;
    }

}

class CustomSpinnerAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<String> data;
    private int layoutId;
    public Resources res;
    LayoutInflater inflater;

    public CustomSpinnerAdapter(Context context, int layoutId, ArrayList<String> objects) {
        super(context, layoutId, objects);
        mContext = context;
        data = objects;
        this.layoutId = layoutId;
        inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View row = inflater.inflate(layoutId, parent, false);

        TextView tvCategory = (TextView) row.findViewById(R.id.tvCategory);

        tvCategory.setText(data.get(position).toString());

        return row;
    }
}
