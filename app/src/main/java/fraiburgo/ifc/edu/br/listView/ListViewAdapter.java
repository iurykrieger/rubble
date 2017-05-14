package fraiburgo.ifc.edu.br.listView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import fraiburgo.ifc.edu.br.controllers.ImageController;
import fraiburgo.ifc.edu.br.rubble.R;

/**
 * Created by iury on 23/03/2015.
 */
public class ListViewAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<ListViewItem> itens;

    public ListViewAdapter(Context context, ArrayList<ListViewItem> itens) {
        //Itens que preencheram o listview
        this.itens = itens;
        //responsavel por pegar o Layout do item.
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return itens.size();
    }

    public ArrayList<ListViewItem> getItems(){
        return this.itens;
    }

    public ListViewItem getItem(int position) {
        return itens.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public void addItem(ListViewItem listViewItem){
        this.itens.add(listViewItem);
        notifyDataSetChanged();
    }

    public View getView(int position, View view, ViewGroup parent) {
        //Pega o item de acordo com a posção.
        ListViewItem item = itens.get(position);
        //infla o layout para podermos preencher os dados
        view = mInflater.inflate(R.layout.item_listview, null);

        //atravez do layout pego pelo LayoutInflater, pegamos cada id relacionado
        //ao item e definimos as informações.
        ((TextView) view.findViewById(R.id.tv_listview_item_title)).setText(item.getTitle());
        ((TextView) view.findViewById(R.id.tv_listview_item_subtitle)).setText(item.getSubtitle());
        if (item.getImage() != null) {
            ImageView iv = (ImageView) view.findViewById(R.id.iv_listview);
            String url_foto = view.getContext().getString(R.string.server_ip) + "/" + view.getContext().getString(R.string.application_name) + item.getImage();
            ImageController ic = new ImageController(iv);
            ic.execute(url_foto);
        }
        return view;
    }
}
