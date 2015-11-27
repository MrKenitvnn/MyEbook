package ebook.ken.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import ebook.ken.activity.R;
import ebook.ken.activity.ReadingActivity;
import ebook.ken.adapter.FragmentFavoritesListViewAdapter;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyApp;

public class FavoritesListViewFragment extends Fragment {

    private View view;

    @Bind(R.id.lvFavorites)
    ListView lvFavorites;

    private List<BookOffline> listData; // list data book offline

    public static FragmentFavoritesListViewAdapter adapter;

    /**
     * fragment life cycle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorites_listview,
                container, false);
        ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            //get all book favorite
            listData = MyApp.listAllBookFavorites;

            // create adapter
            adapter = new FragmentFavoritesListViewAdapter(getActivity(), listData);

            // set data list view
            lvFavorites.setAdapter(adapter);

        } catch (Exception ex) {
            MZLog.d(Log.getStackTraceString(ex));
        }
    }

    /**
     * event
     */
    @OnItemClick(R.id.lvFavorites)
    void lvFavoriteItemClick(int position) {
        BookOffline book = (BookOffline) adapter.getItem(position);
        Intent intent = new Intent(getActivity(), ReadingActivity.class);
        intent.putExtra("BOOK", book);
        startActivity(intent);
    }
}
