package fraiburgo.ifc.edu.br.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.ArrayList;

import fraiburgo.ifc.edu.br.controllers.TrendingController;
import fraiburgo.ifc.edu.br.listView.LazyPostagemLoadAdapter;
import fraiburgo.ifc.edu.br.listView.ListViewAdapter;
import fraiburgo.ifc.edu.br.listView.ListViewItem;
import fraiburgo.ifc.edu.br.rubble.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TrendingFragment extends Fragment {

    private ListView listView;
    private ListViewAdapter adapterListView;
    private ArrayList<ListViewItem> itens;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_trending, container, false);

        final String url_trend = getString(R.string.server_ip) + "/" + getString(R.string.application_name) + "/PostagemServlet";
        ListView lv_views = (ListView) view.findViewById(R.id.listView_views);

        TrendingController tc = new TrendingController(view, container, 0, 6, "views");
        tc.execute(url_trend);

        lv_views.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                TrendingController tc = new TrendingController(view, container, totalItemsCount, 6, "views");
                tc.execute(url_trend);
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        ListView lv_views = (ListView) view.findViewById(R.id.listView_views);
        LazyPostagemLoadAdapter adapter  = (LazyPostagemLoadAdapter) lv_views.getAdapter();
        if(adapter != null){
            adapter.getImageLoader().clearCache();
        }
        super.onPause();
    }

    public abstract class EndlessScrollListener implements AbsListView.OnScrollListener {
        // The minimum amount of items to have below your current scroll position
        // before loading more.
        private int visibleThreshold = 5;
        // The current offset index of data you have loaded
        private int currentPage = 0;
        // The total number of items in the dataset after the last load
        private int previousTotalItemCount = 0;
        // True if we are still waiting for the last set of data to load.
        private boolean loading = true;
        // Sets the starting page index
        private int startingPageIndex = 0;

        public EndlessScrollListener() {
        }

        public EndlessScrollListener(int visibleThreshold) {
            this.visibleThreshold = visibleThreshold;
        }

        public EndlessScrollListener(int visibleThreshold, int startPage) {
            this.visibleThreshold = visibleThreshold;
            this.startingPageIndex = startPage;
            this.currentPage = startPage;
        }

        // This happens many times a second during a scroll, so be wary of the code you place here.
        // We are given a few useful parameters to help us work out if we need to load some more data,
        // but first we check if we are waiting for the previous load to finish.
        @Override
        public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount)
        {
            // If the total item count is zero and the previous isn't, assume the
            // list is invalidated and should be reset back to initial state
            if (totalItemCount < previousTotalItemCount) {
                this.currentPage = this.startingPageIndex;
                this.previousTotalItemCount = totalItemCount;
                if (totalItemCount == 0) { this.loading = true; }
            }
            // If it’s still loading, we check to see if the dataset count has
            // changed, if so we conclude it has finished loading and update the current page
            // number and total item count.
            if (loading && (totalItemCount > previousTotalItemCount)) {
                loading = false;
                previousTotalItemCount = totalItemCount;
                currentPage++;
            }

            // If it isn’t currently loading, we check to see if we have breached
            // the visibleThreshold and need to reload more data.
            // If we do need to reload some more data, we execute onLoadMore to fetch the data.
            if (!loading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
                onLoadMore(currentPage + 1, totalItemCount);
                loading = true;
            }
        }

        // Defines the process for actually loading more data based on page
        public abstract void onLoadMore(int page, int totalItemsCount);

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // Don't take any action on changed
        }
    }
}
