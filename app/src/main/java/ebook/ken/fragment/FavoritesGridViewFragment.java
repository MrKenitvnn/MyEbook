package ebook.ken.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.activity.ReadingActivity;
import ebook.ken.adapter.FragmentFavoritesGridViewAdapter;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyApp;

public class FavoritesGridViewFragment extends Fragment {

	private View view;
	private GridView gvFavorites;

	// list data book offline
	private List<BookOffline> listData;

	// adapter
	public static FragmentFavoritesGridViewAdapter adapter;


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
		gvFavorites.setOnItemClickListener(gvFavoritesEvent);

		return view;

	}// end-func onCreateView


	@Override
	public void onResume() {

		super.onResume();
		try{
			//get all book
			listData = MyApp.listAllBookFavorites;

			// create adapter
			adapter = new FragmentFavoritesGridViewAdapter(getActivity(), listData);

			// set data list view
			gvFavorites.setAdapter(adapter);

		} catch (Exception ex){
			MZLog.d(Log.getStackTraceString(ex));
		}

	}// end-func onResume


	////////////////////////////////////////////////////////////////////////////////
	// TODO events

	AdapterView.OnItemClickListener gvFavoritesEvent = new AdapterView.OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			BookOffline book = (BookOffline) adapter.getItem(position);
			Intent intent = new Intent(getActivity(), ReadingActivity.class);
			intent.putExtra("BOOK", book);
			startActivity(intent);
		}
	};

}
