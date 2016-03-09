package ebook.ken.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import ebook.ken.activity.R;
import ebook.ken.listener.CustomItemClickListener;
import ebook.ken.myui.imageloader.ImageLoader;
import ebook.ken.objects.BookOnline;
import ebook.ken.utils.JsonHandler;

/**
 * Created by ken on 11/27/2015.
 */
public class SearchResultAdapter  extends RecyclerView.Adapter<SearchResultAdapter.ListViewHolder> {

    private List<BookOnline> listBookOnline = null;
    Context context;
    CustomItemClickListener listener;
    public ImageLoader imageLoader;

    public SearchResultAdapter(Context mContext, List<BookOnline> mListBookOnline, CustomItemClickListener listener) {
        this.context = mContext;
        this.listBookOnline = mListBookOnline;
        imageLoader = new ImageLoader(context);
        this.listener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cardview_book_store, parent, false);
        final ListViewHolder mViewHolder = new ListViewHolder(itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, mViewHolder.getPosition());
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                listener.onItemLongClick(v, mViewHolder.getPosition());
                return true;
            }
        });
        return mViewHolder;
    }


    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {

        final BookOnline item = listBookOnline.get(position);
        holder.tvNameBookStore.setText(item.getBookName());
        holder.tvAuthorBookOnline.setText(item.getBookAuthor());
        holder.tvDownloadTotal.setText(item.getBookTotalDownload() + "");
        holder.ratingBar.setRating(item.getBookRate());
        imageLoader.DisplayImage(JsonHandler.BASE_URL + item.getBookCoverPath(), holder.ivCoverBookStore);
    }

    @Override
    public int getItemCount() {
        if (!listBookOnline.isEmpty()) {
            return listBookOnline.size();
        }
        return 0;
    }

    public BookOnline getItem(int position) {
        return listBookOnline.get(position);
    }

    /**
     * inner class ViewHolder
     */
    public class ListViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.tvNameBookStore)
        TextView tvNameBookStore;

        @Bind(R.id.tvAuthorBookOnline)
        TextView tvAuthorBookOnline;

        @Bind(R.id.tvDownloadTotal)
        TextView tvDownloadTotal;

        @Bind(R.id.ratingBar)
        RatingBar ratingBar;

        @Bind(R.id.ivCoverBookStore)
        ImageView ivCoverBookStore;

        public ListViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
