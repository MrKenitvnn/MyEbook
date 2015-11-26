package ebook.ken.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.melnykov.fab.FloatingActionButton;

import ebook.ken.activity.R;
import ebook.ken.dao.BookFavoriteDao;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyUtils;
import ebook.ken.utils.MyApp;

/**
 * Created by admin on 5/26/2015.
 */
public class HomeFragment extends Fragment {

    private View view;
    private FloatingActionButton ivChangeStyle;

    public static BookFavoriteDao bookFavoriteDao;

    /**
     * fragment life cycle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_home, container, false);

        // init controls
        ivChangeStyle = (FloatingActionButton) view.findViewById(R.id.ivChangeStyle);

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
        MyApp.listAllFavorites = bookFavoriteDao.loadAllFavorites();

        // set first fragment
        if (MyApp.isInListView) {
            MyUtils.navigationToView((FragmentActivity) getActivity(),
                    new HomeCardListFragment(), R.id.fmHomeContent);
        } else {
            MyUtils.navigationToView((FragmentActivity) getActivity(),
                    new HomeCardGridFragment(), R.id.fmHomeContent);
        }

    }// end-func onStart

    @Override
    public void onResume() {
        super.onResume();

    }

    /**
     * events
     */
    OnClickListener ivChangeStyleEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                if (MyApp.isInListView) { // if in listview and wanna to gridview
                    MyApp.isInListView = false;
                    // replace fragment of books
                    MyUtils.navigationToView((FragmentActivity) getActivity(),
                            new HomeCardGridFragment(),
                            R.id.fmHomeContent);
                } else { // if in gridview and wanna to listview
                    MyApp.isInListView = true;
                    // replace fragment of books
                    MyUtils.navigationToView((FragmentActivity) getActivity(),
                            new HomeCardListFragment(),
                            R.id.fmHomeContent);
                }// end-if
            } catch (Exception ex) {
                MZLog.d(Log.getStackTraceString(ex));
                MyApp.isInListView = true;
            }
        }
    };

}
