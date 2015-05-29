package ebook.ken.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.adapter.FragmentHomeListViewAdapter;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.FileHandler;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;


public class HomeListViewFragment extends Fragment {

	// define variable

	// UI
	private View view;
	private ListView lvHome;

	//  data access object
	private BookOfflineDao bookOfflineDao;

	// list data book offline
	private List<BookOffline> listData;

	// adapter
	private FragmentHomeListViewAdapter adapter;


	////////////////////////////////////////////////////////////////////////////////
	// TODO fragment life cycle

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 view = inflater.inflate(R.layout.fragment_home_listview, container, false);

		// init controls
		lvHome = (ListView) view.findViewById(R.id.lvHome);


		// events
		lvHome.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			}
		});

		return view;
	}// end-func onCreateView


	@Override
	public void onStart() {
		super.onStart();
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
			adapter = new FragmentHomeListViewAdapter(getActivity(), listData);

			// set data list view
			lvHome.setAdapter(adapter);

		}catch (Exception ex){
			Log.d(">>> ken <<<", Log.getStackTraceString(ex));
		}
	}


	////////////////////////////////////////////////////////////////////////////////
	// TODO events





}
