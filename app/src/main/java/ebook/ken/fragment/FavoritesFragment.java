package ebook.ken.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import ebook.ken.activity.R;
import ebook.ken.utils.MyUtils;
import ebook.ken.utils.Vars;

/**
 * Created by admin on 5/26/2015.
 */
public class FavoritesFragment extends Fragment {

    private View view;
    private ImageView ivChangeStyle;


    ////////////////////////////////////////////////////////////////////////////////
    // Todo fragment life cycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_favorites, container, false);

        // init controls
        ivChangeStyle = (ImageView) view.findViewById(R.id.ivChangeStyle);

        // events
        ivChangeStyle.setOnClickListener(ivChangeStyleEvent);

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        // set first fragment
        if (Vars.isInListView) {

            MyUtils.navigationToView((FragmentActivity) getActivity(),
                    new FavoritesListViewFragment(), R.id.fmBooksContent);
            // change image of imageview
            MyUtils.setImageIsGridView(getActivity(), ivChangeStyle);
        } else {

            MyUtils.navigationToView((FragmentActivity) getActivity(),
                    new FavoritesGridViewFragment(), R.id.fmBooksContent);
            // change image of imageview
            MyUtils.setImageIsListView(getActivity(), ivChangeStyle);
        }// end-if

    }// end-func onStart

    ////////////////////////////////////////////////////////////////////////////////
    // Todo events

    View.OnClickListener ivChangeStyleEvent = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try{
                if(Vars.isInListView){ // if in listview and wanna to gridview

                    Vars.isInListView = false;
                    // replace fragment of books
                    MyUtils.navigationToView( (FragmentActivity) getActivity(),
                                              new FavoritesGridViewFragment(),
                                              R.id.fmBooksContent);

                    // change image of imageview
                    MyUtils.setImageIsListView(getActivity(), ivChangeStyle);

                } else { // if in gridview and wanna to listview

                    Vars.isInListView = true;

                    // replace fragment of books
                    MyUtils.navigationToView( (FragmentActivity) getActivity(),
                                              new  FavoritesListViewFragment(),
                                              R.id.fmBooksContent);

                    // change image of imageview
                    MyUtils.setImageIsGridView(getActivity(), ivChangeStyle);

                }// end-if
            } catch(Exception ex){
                Log.d(">>> ken <<<", Log.getStackTraceString(ex));
                Vars.isInListView = true;
            }
        }
    };//end-event ivChangeStyleEvent

}
