package ebook.ken.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ebook.ken.activity.R;
import ebook.ken.fragment.HomeFragment;
import ebook.ken.listener.CustomItemClickListener;
import ebook.ken.myui.imageloader.ImageLoader;
import ebook.ken.objects.BookOffline;
import ebook.ken.utils.MyApp;


/**
 * Created by ken on 04/11/2015.
 */
public class RecyclerListViewAdapter extends RecyclerView.Adapter<RecyclerListViewAdapter.ListViewHolder> {

    private static List<BookOffline> listBookOnline = null;
    private List<BookOffline> listBookTmp = null;
    public ImageLoader imageLoader;
    Context context;
    CustomItemClickListener listener;


    public RecyclerListViewAdapter(Context mContext, List<BookOffline> mListBookOnline, CustomItemClickListener listener) {
        this.context = mContext;

        this.listBookOnline = mListBookOnline;
        this.listBookTmp = new ArrayList<BookOffline>();
        this.listBookTmp.addAll(this.listBookOnline);
        this.listener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_listview, parent, false);
        final ListViewHolder mViewHolder = new ListViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition());
            }
        });
        itemView.setOnLongClickListener(new View.OnLongClickListener(){

            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClick(v, mViewHolder.getPosition());
                return true;
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final ListViewHolder holder, int position) {
        final BookOffline item = listBookOnline.get(position);
        holder.tvBooksName.setText(item.getBookName());
        holder.tvBooksAuthor.setText(item.getBookAuthor());
        if (item.getBookCoverPath() != null) {
            File imgFile = new File(item.getBookCoverPath());
            if (imgFile.exists()) {
                Uri uri	  = Uri.fromFile(imgFile);
                holder.ivBooksCoverList.setImageURI(uri);
            }
        } else {
            Resources res = context.getResources();
            Drawable myDrawable = context.getDrawable(R.drawable.default_book_cover);
            holder.ivBooksCoverList.setImageDrawable(myDrawable);
        }// end-if;

        // with any book, loop though items of list favorites to show checkbox
        for (int i = 0; i < MyApp.listAllFavorites.size(); i++) {
            if( item.getBookId() == MyApp.listAllFavorites.get(i).getBookOfflineId() ){
                holder.cbFavorite.setChecked(true);
            }
        }
        holder.cbFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (holder.cbFavorite.isChecked()) {
                        HomeFragment.bookFavoriteDao.addBookFavorite(item.getBookId());
                        MyApp.listAllFavorites = HomeFragment.bookFavoriteDao.loadAllFavorites();
                    } else {
                        HomeFragment.bookFavoriteDao.delBookFavorite(item.getBookId());
                        MyApp.listAllFavorites = HomeFragment.bookFavoriteDao.loadAllFavorites();
                    }
                } catch(Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (!listBookOnline.isEmpty()) {
            return listBookOnline.size();
        }
        return 0;
    }

    public BookOffline getItem (int position) {
        return listBookOnline.get(position);
    }

    /**
     * TODO: inner class ViewHolder
     */
    public class ListViewHolder extends RecyclerView.ViewHolder {

        protected ImageView ivBooksCoverList;
        protected TextView tvBooksName, tvBooksAuthor;
        protected CheckBox cbFavorite;

        public ListViewHolder(View itemView) {
            super(itemView);
            ivBooksCoverList = (ImageView) itemView.findViewById(R.id.ivBooksCoverList);
            tvBooksName = (TextView) itemView.findViewById(R.id.tvBooksName);
            tvBooksAuthor = (TextView) itemView.findViewById(R.id.tvBooksAuthor);
            cbFavorite = (CheckBox) itemView.findViewById(R.id.cbFavorite);
        }
    }

    /**
     * TODO lọc khi nhập ký tự trên searchview
     */
    public void filter(String charText) {
        charText	= charText.toLowerCase(Locale.getDefault());
        listBookOnline	.clear();
        if (charText.length() == 0) {
            listBookOnline.addAll(listBookTmp);
        } else {
            for (BookOffline wp : listBookTmp) {
                if (wp.getBookName().toLowerCase(Locale.getDefault()).contains(Character.toString(charText.charAt(charText.length()-1)))
                        || wp.getBookAuthor().toLowerCase(Locale.getDefault()).contains(charText)) {
                    listBookOnline.add(wp);
                }// end-if
            }// end-for
        }
        notifyDataSetChanged();
    }// end-func filter

    public void closeSearch() {
        // mLocations.addAll(arraylist);
        notifyDataSetChanged();
    }
}
