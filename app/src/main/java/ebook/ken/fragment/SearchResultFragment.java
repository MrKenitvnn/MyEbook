package ebook.ken.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ebook.ken.activity.R;
import ebook.ken.adapter.SearchResultAdapter;
import ebook.ken.listener.CustomItemClickListener;
import ebook.ken.objects.BookOnline;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyApp;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by ken on 11/27/2015.
 */
public class SearchResultFragment extends Fragment {

    @Bind(R.id.rcSearchResult)
    RecyclerView rcSearchResult;

    private List<BookOnline> listData;
    public static SearchResultAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_result, container, false);

        ButterKnife.bind(this, view);

        rcSearchResult.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rcSearchResult.setLayoutManager(linearLayoutManager);

        // enable option menu
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (MyApp.listBookBySearch != null) {
                listData = MyApp.listBookBySearch;

                // set up data
                adapter = new SearchResultAdapter(getActivity(), listData, new CustomItemClickListener() {
                    @Override
                    public void onItemClick(View v, int position) {
                        // get item by position click
                        BookOnline item = adapter.getItem(position);
                        // set book current
                        MyApp.currentBookDetail = item;
                        // start child fragment
                        ((MaterialNavigationDrawer) getActivity()).setFragmentChild(new BookStoreDetailFragment(), item.getBookName());
                    }

                    @Override
                    public void onItemLongClick(View view, final int position) {
                    }
                });
                rcSearchResult.setAdapter(adapter);
            }
        } catch (Exception e) {
            MZLog.e("ERROR : SearchResultFragment : onResume");
        }
    }

    /**
     * option menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }
}

