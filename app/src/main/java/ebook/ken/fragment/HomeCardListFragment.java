package ebook.ken.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.activity.ReadingActivity;
import ebook.ken.listener.CustomItemClickListener;
import ebook.ken.adapter.RecyclerListViewAdapter;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.MZLog;


public class HomeCardListFragment extends Fragment {
    // UI
    private View view;
    private RecyclerView recyclerView;

    //  data access object
    private BookOfflineDao bookOfflineDao;

    static final int ANIMATION_DURATION = 400;

    // list data book offline
    private List<BookOffline> listData;

    // adapter
    public static RecyclerListViewAdapter adapter;

    ////////////////////////////////////////////////////////////////////////////////
    // TODO fragment life cycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home_listview, container, false);

        // init controls
        recyclerView = (RecyclerView) view.findViewById(R.id.cardList);
        recyclerView.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        return view;
    }// end-func onCreateView


    @Override
    public void onResume() {
        super.onResume();
        try {
            // create dao
            bookOfflineDao = new BookOfflineDao(getActivity());

            //get all book
            listData = bookOfflineDao.loadAllBookOffline();

            // set up data
            adapter = new RecyclerListViewAdapter(getActivity(), listData, new CustomItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    BookOffline book = (BookOffline) adapter.getItem(position);
                    Intent intent = new Intent(getActivity(), ReadingActivity.class);
                    intent.putExtra("BOOK", book);
                    startActivity(intent);
                }
                @Override
                public void onItemLongClick(View v, final int position) {
                    showDialog(getActivity().getApplicationContext());
                }
            });
            recyclerView.setAdapter(adapter);
        } catch (Exception ex) {
            MZLog.d(Log.getStackTraceString(ex));
        }
    }

    public static void showDialog(Context activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Your Message")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
}
