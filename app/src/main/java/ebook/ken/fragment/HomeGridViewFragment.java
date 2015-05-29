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
import ebook.ken.adapter.FragmentHomeGridViewAdapter;
import ebook.ken.adapter.FragmentHomeListViewAdapter;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.objects.BookOffline;

public class HomeGridViewFragment extends Fragment {

	private View view;
	private GridView gvHome;

	//  data access object
	private BookOfflineDao bookOfflineDao;

	// list data book offline
	private List<BookOffline> listData;

	// adapter
	private FragmentHomeGridViewAdapter adapter;


	////////////////////////////////////////////////////////////////////////////////
	// TODO fragment life cycle

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 view = inflater.inflate(R.layout.fragment_home_gridview,
				container, false);

		// init controls
		gvHome = (GridView) view.findViewById(R.id.gvHome);

		// create dao
		bookOfflineDao = new BookOfflineDao(getActivity());

		// events


		return view;

	}// end-func onCreateView


	@Override
	public void onResume() {

		super.onResume();
		try{
			// create dao
			bookOfflineDao = new BookOfflineDao(getActivity());
			//get all book
			listData = bookOfflineDao.loadAllBookOffline();
			// create adapter
			adapter = new FragmentHomeGridViewAdapter(getActivity(), listData);
			// set data list view
			gvHome.setAdapter(adapter);

		} catch (Exception ex){
			Log.d(">>> ken <<<", Log.getStackTraceString(ex));
		}

	}// end-func onResume


	////////////////////////////////////////////////////////////////////////////////
	// TODO events


}
