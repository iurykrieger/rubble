package fraiburgo.ifc.edu.br.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import fraiburgo.ifc.edu.br.rubble.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedFragment extends Fragment {

    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);
        /*TabHost tabHost = (TabHost) view.findViewById(R.id.tabHost_feed);
        tabHost.setup();

        TabHost.TabSpec tabSpec = tabHost.newTabSpec("amigos");
        tabSpec.setContent(R.id.tab_amigos);
        tabSpec.setIndicator("Amigos");
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec("regiao");
        tabSpec.setContent(R.id.tab_regiao);
        tabSpec.setIndicator("Região");
        tabHost.addTab(tabSpec);
        listView = (ListView) view.findViewById(R.id.listViewAmigos);
        */
        return view;
    }
}
