package fraiburgo.ifc.edu.br.listView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import fraiburgo.ifc.edu.br.rubble.R;
import fraiburgo.ifc.edu.br.utils.ImageLoader;

public class LazyPostagemLoadAdapter extends BaseAdapter {

    private Context context;
    private static LayoutInflater inflater = null;
    public ImageLoader imageLoader;
    private List<PostListViewItem> posts;

    public LazyPostagemLoadAdapter(Context context, List<PostListViewItem> posts) {
        this.context = context;
        this.posts = posts;
        inflater = LayoutInflater.from(context);
        imageLoader = new ImageLoader(context);
    }

    public ImageLoader getImageLoader(){
        return this.imageLoader;
    }

    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int pos) {
        return pos;
    }

    @Override
    public long getItemId(int pos) {
        return pos;
    }

    public void addItem(PostListViewItem listViewItem){
        this.posts.add(listViewItem);
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
        PostListViewItem item = posts.get(pos);
        view = inflater.inflate(R.layout.post_item_listview, null);
        ((TextView) view.findViewById(R.id.tv_listview_item_title)).setText(item.getTipo());
        ((TextView) view.findViewById(R.id.tv_listview_item_subtitle)).setText(item.getDesc());
        TextView tv_numviews = (TextView) view.findViewById(R.id.tv_postitem_numviews);
        TextView tv_numcomments = (TextView) view.findViewById(R.id.tv_postitem_numcomments);
        TextView tv_data = (TextView) view.findViewById(R.id.tv_postitem_data);
        tv_numviews.setText(String.valueOf(item.getNumViews()));
        tv_numcomments.setText(String.valueOf(item.getNumComments()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        tv_data.setText(sdf.format(item.getData()));
        ImageView iv_image = (ImageView) view.findViewById(R.id.iv_listview);
        if (posts.get(pos).getFoto() != null) {
            final String url_foto = context.getString(R.string.server_ip) + "/" + context.getString(R.string.application_name) + posts.get(pos).getFoto();
            imageLoader.displayImage(url_foto, iv_image);
        } else {
            iv_image.setImageResource(R.drawable.no_user);
        }
        return view;
    }
}
