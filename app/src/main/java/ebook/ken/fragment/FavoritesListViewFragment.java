package ebook.ken.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.adapter.FragmentFavoritesListViewAdapter;
import ebook.ken.adapter.FragmentHomeListViewAdapter;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.Vars;

public class FavoritesListViewFragment extends Fragment {


	// UI
	private View view;
	private ListView lvFavorites;

	// list data book offline
	private List<BookOffline> listData;

	// adapter
	private FragmentFavoritesListViewAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_favorites_listview,
				container, false);

		// init controls
		lvFavorites = (ListView) view.findViewById(R.id.lvFavorites);

		// events


		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
			//get all book
			listData = Vars.listAllBookFavorites;

			// create adapter
			adapter = new FragmentFavoritesListViewAdapter(getActivity(), listData);

			// set data list view
			lvFavorites.setAdapter(adapter);

		}catch (Exception ex){
			Log.d(">>> ken <<<", Log.getStackTraceString(ex));
		}
	}

}
