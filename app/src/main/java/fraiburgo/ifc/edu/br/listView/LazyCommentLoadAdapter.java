package fraiburgo.ifc.edu.br.listView;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import fraiburgo.ifc.edu.br.fragments.ImageViewer;
import fraiburgo.ifc.edu.br.rubble.R;
import fraiburgo.ifc.edu.br.utils.ImageLoader;

public class LazyCommentLoadAdapter extends BaseAdapter {

    private Activity activity;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    private List<CommentListViewItem> comments;

    public LazyCommentLoadAdapter(Activity a, List<CommentListViewItem> comments) {
        activity = a;
        this.comments = comments;
        inflater = LayoutInflater.from(activity);
        imageLoader = new ImageLoader(activity.getApplicationContext());
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public Object getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public void addItem(CommentListViewItem listViewItem){
        this.comments.add(listViewItem);
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        public TextView title;
        public TextView subtitle;
        public ImageView user;
        public ImageView image;
    }

    @Override
    public View getView(int pos, View view, ViewGroup parent) {
        CommentListViewItem item = comments.get(pos);
        view = inflater.inflate(R.layout.comment_item_listview, null);
        ((TextView) view.findViewById(R.id.tv_listview_item_title)).setText(item.getTitle());
        ((TextView) view.findViewById(R.id.tv_listview_item_subtitle)).setText(item.getSubtitle());
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_listview_item_foto);
        ImageView iv_user = (ImageView) view.findViewById(R.id.iv_listview);
        if (comments.get(pos).getFoto() != null) {
            final String url_foto = activity.getString(R.string.server_ip) + "/" + activity.getString(R.string.application_name) + comments.get(pos).getFoto();
            imageLoader.displayImage(url_foto, iv_image);
            iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, ImageViewer.class);
                    intent.putExtra("url", url_foto);
                    activity.startActivity(intent);
                }
            });
        } else {
            iv_image.setImageBitmap(null);
            iv_image.setAdjustViewBounds(false);
        }
        if (comments.get(pos).getImage() != null) {

        } else {
            iv_user.setImageResource(R.drawable.no_user);
        }
        return view;
    }
}
