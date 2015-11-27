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
import ebook.ken.listener.CustomItemClickListener;
import ebook.ken.myui.imageloader.ImageLoader;
import ebook.ken.objects.BookOffline;


/**
 * Created by ken on 04/11/2015.
 */
public class HomeRecyclerGridViewAdapter extends RecyclerView.Adapter<HomeRecyclerGridViewAdapter.ListViewHolder> {

    private Context context;
    List<BookOffline> listBookOnline = null;
    public ImageLoader imageLoader;
    CustomItemClickListener listener;

    public HomeRecyclerGridViewAdapter(Context mContext, List<BookOffline> mListBookOnline, CustomItemClickListener listener) {
        this.context = mContext;
        this.listBookOnline = mListBookOnline;
        imageLoader = new ImageLoader(context);
        this.listener = listener;
    }

    @Override
    public ListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_gridview, parent, false);
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
            Drawable myDrawable = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                myDrawable = context.getDrawable(R.drawable.default_book_cover);
            }
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
     * event del a book
     */
    public void eventDelABook(int id) {
        listBookOnline.remove(id);
        notifyDataSetChanged();
    }
    /**
     * inner class ViewHolder
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
