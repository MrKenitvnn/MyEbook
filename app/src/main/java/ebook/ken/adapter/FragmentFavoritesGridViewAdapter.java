package ebook.ken.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ebook.ken.activity.R;
import ebook.ken.objects.BookOffline;


public class FragmentFavoritesGridViewAdapter extends BaseAdapter {

	private class ViewHolder {
		public ImageView imageView;
		public TextView tvEpubBookName;
	}

	private static List<BookOffline> mLocations;
	private ArrayList<BookOffline> arraylist;
	private LayoutInflater mInflater;

	/**
	 * @return the mLocations
	 */
	public static List<BookOffline> getmLocations() {
		return mLocations;
	}

	/**
	 * @param mLocations
	 *            the mLocations to set
	 */
	public static void setmLocations(List<BookOffline> mLocations) {
		FragmentFavoritesGridViewAdapter.mLocations = mLocations;
	}


	public FragmentFavoritesGridViewAdapter(Context context, List<BookOffline> locations) {
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mLocations = locations;
		this.arraylist = new ArrayList<BookOffline>();
		this.arraylist.addAll(locations);
	}


	@Override
	public int getCount() {
		if (mLocations != null) {
			return mLocations.size();
		}

		return 0;
	}


	@Override
	public Object getItem(int position) {
		if (mLocations != null && position >= 0 && position < getCount()) {
			return mLocations.get(position);
		}

		return null;
	}


	@Override
	public long getItemId(int position) {
		if (mLocations != null && position >= 0 && position < getCount()) {
			return mLocations.get(position).getBookId();
		}

		return 0;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view = convertView;
		ViewHolder viewHolder;

		if (view == null) {
			view = mInflater.inflate(R.layout.item_book_gridview, parent,
					false);

			viewHolder = new ViewHolder();
			viewHolder.imageView = (ImageView) view
					.findViewById(R.id.grid_image);
			viewHolder.tvEpubBookName = (TextView) view
					.findViewById(R.id.grid_label);

			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}

		// create an instance of EpubBook
		BookOffline locationModel = mLocations.get(position);

		if (locationModel.getBookCoverPath() != null) { // when doesn't have cover
			File imgFile = new File(locationModel.getBookCoverPath());
			if (imgFile.exists()) {

				// viewHolder.imageView.setImageBitmap(myBitmap);
				Uri uri = Uri.fromFile(imgFile);
				viewHolder.imageView.setImageURI(uri);
			}
		} else {
			Drawable myDrawable = view.getResources().getDrawable(
					R.drawable.default_book_cover);
			viewHolder.imageView.setImageDrawable(myDrawable);
		}// end-if

		viewHolder.tvEpubBookName.setText(locationModel.getBookName());

		return view;
	}


	// event del a book
	public void eventDelABook(int id) {
		mLocations.remove(id);
		notifyDataSetChanged();
	}


	// Filter
	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		mLocations.clear();
		if (charText.length() == 0) {
			mLocations.addAll(arraylist);
		} else {
			for (BookOffline wp : arraylist) {
				if (wp.getBookName().toLowerCase(Locale.getDefault())
						.contains(charText)
						|| wp.getBookAuthor()
								.toLowerCase(Locale.getDefault())
								.contains(charText)) {
					mLocations.add(wp);
				}
			}
		}
		notifyDataSetChanged();
	}


	public void closeSearch() {
		// mLocations.addAll(arraylist);
		notifyDataSetChanged();
	}

}
