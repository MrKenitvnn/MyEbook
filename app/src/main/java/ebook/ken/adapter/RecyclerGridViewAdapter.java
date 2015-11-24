package ebook.ken.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.myui.imageloader.ImageLoader;
import ebook.ken.objects.BookOffline;
import ebook.ken.objects.BookOnline;
import ebook.ken.utils.JsonHandler;


/**
 * Created by ken on 04/11/2015.
 */
public class RecyclerGridViewAdapter extends RecyclerView.Adapter<RecyclerGridViewAdapter.ListViewHolder> {

    private Context context;
    List<BookOffline> listBookOnline = null;
    public ImageLoader imageLoader;

    public RecyclerGridViewAdapter(Context mContext, List<BookOffline> mListBookOnline) {
        this.context = mContext;
        this.listBookOnline = mListBookOnline;
        imageLoader = new ImageLoader(context);
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_gridview, parent, false);
        return new ListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ListViewHolder holder, int position) {
        BookOffline item = listBookOnline.get(position);
        holder.tvCardLabel.setText(item.getBookName());
        if (item.getBookCoverPath() != null) {
            File imgFile = new File(item.getBookCoverPath());
            if (imgFile.exists()) {
                Uri uri	  = Uri.fromFile(imgFile);
                holder.ivCardImage.setImageURI(uri);
            }
        } else {
            Resources res = context.getResources();
            Drawable myDrawable = context.getDrawable(R.drawable.default_book_cover);
            holder.ivCardImage.setImageDrawable(myDrawable);
        }// end-if;
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

        protected ImageView ivCardImage;
        protected TextView tvCardLabel;

        public ListViewHolder(View itemView) {
            super(itemView);
            ivCardImage = (ImageView) itemView.findViewById(R.id.card_image);
            tvCardLabel = (TextView) itemView.findViewById(R.id.card_label);
        }
    }
}
