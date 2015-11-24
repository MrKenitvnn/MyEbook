package ebook.ken.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.activity.ReadingActivity;
import ebook.ken.adapter.FragmentFavoritesListViewAdapter;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyApp;

public class FavoritesListViewFragment extends Fragment {


	// UI
	private View view;
	private ListView lvFavorites;

	// list data book offline
	private List<BookOffline> listData;

	// adapter
	public static FragmentFavoritesListViewAdapter adapter;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.fragment_favorites_listview,
				container, false);

		// init controls
		lvFavorites = (ListView) view.findViewById(R.id.lvFavorites);

		// events
		lvFavorites.setOnItemClickListener(lvFavoriteEvent);

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
			//get all book
			listData = MyApp.listAllBookFavorites;

			// create adapter
			adapter = new FragmentFavoritesListViewAdapter(getActivity(), listData);

			// set data list view
			lvFavorites.setAdapter(adapter);

		}catch (Exception ex){
			MZLog.d(Log.getStackTraceString(ex));
		}
	}

	////////////////////////////////////////////////////////////////////////////////
	// TODO events

	AdapterView.OnItemClickListener lvFavoriteEvent = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			BookOffline book = (BookOffline) adapter.getItem(position);
			Intent intent = new Intent(getActivity(), ReadingActivity.class);
			intent.putExtra("BOOK", book);
			startActivity(intent);
		}
	};

}
