package ebook.ken.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.activity.ReadingActivity;
import ebook.ken.adapter.FragmentHomeListViewAdapter;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.FileHandler;
import ebook.ken.utils.MZLog;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubReader;


public class HomeListViewFragment extends Fragment implements AdapterView.OnItemLongClickListener{

	// UI
	private View view;
	private ListView lvHome;

	//  data access object
	private BookOfflineDao bookOfflineDao;

	static final int ANIMATION_DURATION = 400;
	// list data book offline
	private List<BookOffline> listData;

	// adapter
	public static FragmentHomeListViewAdapter adapter;


	/**
	 * fragment life cycle
	 */

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		 view = inflater.inflate(R.layout.fragment_home_listview, container, false);

		// init controls
//		lvHome = (ListView) view.findViewById(R.id.lvHome);

		// events
		lvHome.setOnItemClickListener(lvHomeEvent);
		lvHome.setOnItemLongClickListener(this);

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
			adapter = new FragmentHomeListViewAdapter(getActivity(), listData);

			// set data list view
			lvHome.setAdapter(adapter);

		}catch (Exception ex){
			MZLog.d(Log.getStackTraceString(ex));
		}
	}


	////////////////////////////////////////////////////////////////////////////////
	// TODO events

	OnItemClickListener lvHomeEvent = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

			BookOffline book = (BookOffline) adapter.getItem(position);
			Intent intent = new Intent(getActivity(), ReadingActivity.class);
			intent.putExtra("BOOK", book);
			startActivity(intent);
		}
	};
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, final View view,
								   final int position, long id) {
		AlertDialog dialog;
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity().getApplicationContext());

		builder.setTitle("Delete a book");
		builder.setIcon(R.drawable.icon_view_grid);

		builder.setMessage("Do you want to delete this book?");
		builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});

		builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				view.animate().alpha(0).setDuration(ANIMATION_DURATION)
						.withEndAction(new Runnable() {
							@Override
							public void run() {
								Book book = (Book) adapter
										.getItem(position);

								// TODO note: delete book
//								if (bookOfflineDao.deleteBookOffline(Integer
//										.parseInt(book.get)) > 0) {
//									FragmentBooks.favoriteDao
//											.delEpubFavorite(Integer
//													.parseInt(book
//															.getEpubFolder()));
//									FileHandler.deleteBookFolder(new File(
//											FileHandler.rootPath
//													+ book.getEpubFolder()));
//									adapter.eventDelABook(position);
//								} else {
//									Log.d("delete epub book",
//											"failed FragmentBooks_ListView line 117");
//								}
							}
						});
			}
		});
		dialog = builder.create();
		dialog.show();

		return true;
	}// end-func onItemLongClick

}
