package ebook.ken.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.adapter.FragmentHomeGridViewAdapter;
import ebook.ken.adapter.RecyclerGridViewAdapter;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.MZLog;

/**
 * Created by ken on 04/11/2015.
 */
public class HomeCardGridFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;

    //  data access object
    private BookOfflineDao bookOfflineDao;
    // list data book offline
    private List<BookOffline> listData;
    // adapter
    public static RecyclerGridViewAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home_gridview, container, false);

        // init controls
        recyclerView = (RecyclerView) view.findViewById(R.id.cardGrid);

        // create dao
        bookOfflineDao = new BookOfflineDao(getActivity());

        // setup recycler gridview
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        return view;
    }

    @Override
    public void onResume() {

        super.onResume();
        try{
            // create dao
            bookOfflineDao = new BookOfflineDao(getActivity());
            //get all book
            listData = bookOfflineDao.loadAllBookOffline();
            // create adapter
            adapter = new RecyclerGridViewAdapter(getActivity(), listData);

        recyclerView.setAdapter(adapter);

        } catch (Exception ex){
            MZLog.d(Log.getStackTraceString(ex));
        }

    }
}
