package ebook.ken.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ebook.ken.activity.R;
import ebook.ken.dao.BookFavoriteDao;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyUtils;
import ebook.ken.utils.MyApp;

/**
 * Created by admin on 5/26/2015.
 */
public class FavoritesFragment extends Fragment {

    private View view;
    private ImageView ivChangeStyle;

    public static BookFavoriteDao bookFavoriteDao;


    ////////////////////////////////////////////////////////////////////////////////
    // Todo fragment life cycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // init controls
        ivChangeStyle = (ImageView) view.findViewById(R.id.ivChangeStyle);

        // setup dao
        bookFavoriteDao = new BookFavoriteDao(getActivity());

        // set list book favorites
        MyApp.listAllBookFavorites = bookFavoriteDao.loadAllBookOfFavorites();

        // events
        ivChangeStyle.setOnClickListener(ivChangeStyleEvent);

        // enable option menu
        setHasOptionsMenu(true);

        return view;
    }// end-func onCreateView


    @Override
    public void onStart() {
        super.onStart();

        // set first fragment
        if (MyApp.isInListView) {

            MyUtils.navigationToView((FragmentActivity) getActivity(),
                    new FavoritesListViewFragment(), R.id.fmFavoriteContent);
        } else {

            MyUtils.navigationToView((FragmentActivity) getActivity(),
                    new FavoritesGridViewFragment(), R.id.fmFavoriteContent);
        }// end-if

    }// end-func onStart


    ////////////////////////////////////////////////////////////////////////////////
    // Todo events

    View.OnClickListener ivChangeStyleEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                if(MyApp.isInListView){ // if in listview and wanna to gridview

                    MyApp.isInListView = false;
                    // replace fragment of books
                    MyUtils.navigationToView( (FragmentActivity) getActivity(),
                                              new FavoritesGridViewFragment(),
                                              R.id.fmFavoriteContent);

                } else { // if in gridview and wanna to listview

                    MyApp.isInListView = true;

                    // replace fragment of books
                    MyUtils.navigationToView( (FragmentActivity) getActivity(),
                                              new  FavoritesListViewFragment(),
                                              R.id.fmFavoriteContent);

                }// end-if
            } catch(Exception ex){
                MZLog.d(Log.getStackTraceString(ex));
                MyApp.isInListView = true;
            }
        }
    };//end-event ivChangeStyleEvent


    ////////////////////////////////////////////////////////////////////////////////
    // Todo option menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }


    ////////////////////////////////////////////////////////////////////////////////
    // Todo async task



}
