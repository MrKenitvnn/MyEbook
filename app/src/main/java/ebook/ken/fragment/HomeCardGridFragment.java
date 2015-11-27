package ebook.ken.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import java.io.File;
import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.activity.ReadingActivity;
import ebook.ken.adapter.HomeRecyclerGridViewAdapter;
import ebook.ken.dao.BookFavoriteDao;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.listener.CustomItemClickListener;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.FileHandler;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyApp;

/**
 * Created by ken on 04/11/2015.
 */
public class HomeCardGridFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;

    //  data access object
    private static BookOfflineDao bookOfflineDao;
    // list data book offline
    private List<BookOffline> listData;
    // adapter
    public static HomeRecyclerGridViewAdapter adapter;

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
            adapter = new HomeRecyclerGridViewAdapter(getActivity(), listData, new CustomItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    BookOffline book = adapter.getItem(position);
                    Intent intent = new Intent(getActivity(), ReadingActivity.class);
                    intent.putExtra("BOOK", book);
                    startActivity(intent);
                }

                @Override
                public void onItemLongClick(View v, int position) {
                    BookOffline book = adapter.getItem(position);
                    showDialog(view, position, book.getBookName());
                }
            });

        recyclerView.setAdapter(adapter);

        } catch (Exception ex){
            MZLog.d(Log.getStackTraceString(ex));
        }

    }


    public static void showDialog(final View view, final int position, String bookName) {

        AlertDialog dialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(MyApp.getAppContext());
        builder.setTitle("Xóa sách");
        builder.setIcon(R.drawable.ic_action_delete);

        builder.setMessage("Bạn muốn xóa " + bookName + " ?");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                try {
                    BookFavoriteDao bookFavoriteDao = new BookFavoriteDao(MyApp.getAppContext());
                    BookOffline book = adapter.getItem(position);

                    int resultDel = bookOfflineDao.deleteBookOffline(book.getBookId());
                    bookFavoriteDao.delBookFavorite(book.getBookId());

                    FileHandler.deleteFolderFromSdcard(new File(FileHandler.ROOT_PATH + book.getBookFolder()));

                    if (resultDel > 0) {
                        // TODO note del a book in gridview
//                        adapter.eventDelABook(position);
                    } else {
                        MZLog.d("ERROR: HomeCardListFragment : showDialog");
                    }
                } catch (Exception e) {
                    MZLog.d("ERROR: HomeCardListFragment : showDialog");
                }

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    view.animate().alpha(0).setDuration(ANIMATION_DURATION)
//                            .withEndAction(new Runnable() {
//                                @Override
//                                public void run() {
//                                }
//                            });
//                }
            }
        });
        dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
    }
}
