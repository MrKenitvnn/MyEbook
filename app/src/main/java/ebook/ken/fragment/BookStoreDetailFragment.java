package ebook.ken.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import ebook.ken.activity.R;
import ebook.ken.utils.Vars;

;

/**
 * Created by admin on 5/26/2015.
 */

public class BookStoreDetailFragment extends Fragment {

    private View view;
    private Button btnDownload;
    private TextView tvAuthorStoreDetail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_book_store_detail, container, false);

        // init controls
        btnDownload = (Button) view.findViewById(R.id.btnDownload);
        tvAuthorStoreDetail = (TextView) view.findViewById(R.id.tvAuthorStoreDetail);

        // set text
        tvAuthorStoreDetail.setText(Vars.currentBookDetail.getBookAuthor());

        // events
        btnDownload.setOnClickListener(btnDownloadEvent);

        return view;
    }


    // TODO events

    OnClickListener btnDownloadEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), Vars.currentBookDetail.getBookFilePath(), Toast.LENGTH_SHORT).show();
        }
    };
}
