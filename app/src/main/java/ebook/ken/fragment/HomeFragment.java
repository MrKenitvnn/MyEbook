package ebook.ken.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.adapter.FragmentHomeListViewAdapter;
import ebook.ken.dao.BookFavoriteDao;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.MyUtils;
import ebook.ken.utils.Vars;

/**
 * Created by admin on 5/26/2015.
 */
public class HomeFragment extends Fragment {

    private View view;
    private ImageView ivChangeStyle;

    public static BookFavoriteDao bookFavoriteDao;



    ////////////////////////////////////////////////////////////////////////////////
    // Todo fragment life cycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        // init controls
        ivChangeStyle = (ImageView) view.findViewById(R.id.ivChangeStyle);

        // events
        ivChangeStyle.setOnClickListener(ivChangeStyleEvent);

        // create favorite dao
        bookFavoriteDao = new BookFavoriteDao(getActivity());

        return view;
    }// end-func onCreateView


    @Override
    public void onStart() {
        super.onStart();

        // load list favorites
        Vars.listAllFavorites = bookFavoriteDao.loadAllFavorites();

        // set first fragment
        if (Vars.isInListView) {

            MyUtils.navigationToView((FragmentActivity) getActivity(),
                    new HomeListViewFragment(), R.id.fmBooksContent);
            // change image of imageview
            MyUtils.setImageIsGridView(getActivity(), ivChangeStyle);
        } else {

            MyUtils.navigationToView((FragmentActivity) getActivity(),
                    new HomeGridViewFragment(), R.id.fmBooksContent);
            // change image of imageview
            MyUtils.setImageIsListView(getActivity(), ivChangeStyle);
        }// end-if

    }// end-func onStart


    @Override
    public void onResume() {
        super.onResume();

    }


    ////////////////////////////////////////////////////////////////////////////////
    // Todo events

    OnClickListener ivChangeStyleEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                if(Vars.isInListView){ // if in listview and wanna to gridview

                    Vars.isInListView = false;
                    // replace fragment of books
                    MyUtils.navigationToView( (FragmentActivity) getActivity(),
                            new HomeGridViewFragment(),
                            R.id.fmBooksContent);

                    // change image of imageview
                    MyUtils.setImageIsListView(getActivity(), ivChangeStyle);

                } else { // if in gridview and wanna to listview

                    Vars.isInListView = true;

                    // replace fragment of books
                    MyUtils.navigationToView( (FragmentActivity) getActivity(),
                            new HomeListViewFragment(),
                            R.id.fmBooksContent);

                    // change image of imageview
                    MyUtils.setImageIsGridView(getActivity(), ivChangeStyle);

                }// end-if
            } catch(Exception ex){
                Log.d(">>> ken <<<", ex.getMessage());
                Vars.isInListView = true;
            }
        }
    };//end-event ivChangeStyleEvent







}
