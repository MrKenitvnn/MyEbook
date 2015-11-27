package ebook.ken.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.myui.imageloader.ImageLoader;
import ebook.ken.objects.BookOnline;
import ebook.ken.utils.JsonHandler;


public class FragmentBookStoreAdapter extends ArrayAdapter<BookOnline> {

    private LayoutInflater mInflater;
    public ImageLoader imageLoader;

    private static class ViewHolder {
        private ImageView ivCover;
        private TextView tvNameBookOnline, tvAuthorBookOnline, tvDownloadTotal;
        private RatingBar ratingBar;
    }

    /**
     * constructor
     */
    public FragmentBookStoreAdapter(Context context, List<BookOnline> listData) {
        super(context, R.layout.item_book_store, listData);
        imageLoader = new ImageLoader(context);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     * get item view
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder viewHolder;
        BookOnline itemBook = getItem(position);// get item by position

        if (view == null) {
            viewHolder = new ViewHolder();
            view = mInflater.inflate(R.layout.item_book_store, parent, false);

            viewHolder.ivCover = (ImageView) view.findViewById(R.id.ivCoverBookStore);
            viewHolder.ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
            viewHolder.tvDownloadTotal = (TextView) view.findViewById(R.id.tvDownloadTotal);
            viewHolder.tvNameBookOnline = (TextView) view.findViewById(R.id.tvNameBookStore);
            viewHolder.tvAuthorBookOnline = (TextView) view.findViewById(R.id.tvAuthorBookOnline);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvDownloadTotal.setText(String.valueOf(itemBook.getBookTotalDownload()));
        viewHolder.tvNameBookOnline.setText(itemBook.getBookName());
        viewHolder.tvAuthorBookOnline.setText(itemBook.getBookAuthor());
        viewHolder.ratingBar.setRating(itemBook.getBookRate());
        imageLoader.DisplayImage(JsonHandler.BASE_URL + itemBook.getBookCoverPath(), viewHolder.ivCover);

        return view;
    }
}
