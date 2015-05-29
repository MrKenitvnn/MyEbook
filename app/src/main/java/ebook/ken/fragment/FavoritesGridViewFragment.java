package ebook.ken.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.adapter.FragmentFavoritesGridViewAdapter;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.Vars;

public class FavoritesGridViewFragment extends Fragment {

	private View view;
	private GridView gvFavorites;

	// list data book offline
	private List<BookOffline> listData;

	// adapter
	private FragmentFavoritesGridViewAdapter adapter;


	////////////////////////////////////////////////////////////////////////////////
	// TODO fragment life cycle

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_favorites_gridview,
				container, false);

		// init controls
		gvFavorites = (GridView) view.findViewById(R.id.gvFavorites);

		// events

		return view;

	}// end-func onCreateView


	@Override
	public void onResume() {

		super.onResume();
		try{
			//get all book
			listData = Vars.listAllBookFavorites;

			// create adapter
			adapter = new FragmentFavoritesGridViewAdapter(getActivity(), listData);

			// set data list view
			gvFavorites.setAdapter(adapter);

		} catch (Exception ex){
			Log.d(">>> ken <<<", Log.getStackTraceString(ex));
		}

	}// end-func onResume


	////////////////////////////////////////////////////////////////////////////////
	// TODO events


}
