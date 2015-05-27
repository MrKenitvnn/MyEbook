package ebook.ken.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import ebook.ken.activity.R;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.objects.Book;
import ebook.ken.objects.BookOffline;
import ebook.ken.objects.BookOnline;
import ebook.ken.utils.FileHandler;
import ebook.ken.utils.JsonHandler;
import ebook.ken.utils.Vars;

;

/**
 * Created by admin on 5/26/2015.
 */

public class BookStoreDetailFragment extends Fragment {

    private View view;
    private Button btnDownload;
    private TextView tvAuthorStoreDetail, tvDescription;

    private BookOfflineDao bookOfflineDao;



    ///////////////////////////////////////////////////////////////////////////////////
    //TODO fragment life cycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_book_store_detail, container, false);

        // init controls
        btnDownload         = (Button) view.findViewById(R.id.btnDownload);
        tvAuthorStoreDetail = (TextView) view.findViewById(R.id.tvAuthorStoreDetail);
        tvDescription       = (TextView) view.findViewById(R.id.tvDescription);

        // set text
        tvAuthorStoreDetail.setText(Vars.currentBookDetail.getBookAuthor());
        tvDescription.setText(Vars.currentBookDetail.getBookDesciption());

        // events
        btnDownload.setOnClickListener(btnDownloadEvent);

        // enable options menu
        setHasOptionsMenu(true);

        // open dao
        bookOfflineDao = new BookOfflineDao(getActivity());

        return view;
    }


    ///////////////////////////////////////////////////////////////////////////////////
    // TODO events

    OnClickListener btnDownloadEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncDownLoadBook().execute(Vars.currentBookDetail);
        }
    };


    ///////////////////////////////////////////////////////////////////////////////////
    //TODO option menu

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }


    ///////////////////////////////////////////////////////////////////////////////////
    // TODO async task

    // Progress Dialog
    private ProgressDialog pDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    /**
     * Showing Dialog
     * */
    protected Dialog showDialog(int id) {
        switch (id) {
            case progress_bar_type: // we set this to 0
                pDialog = new ProgressDialog(getActivity());
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(false);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }

    protected void dismissDialog(int id){
        switch (id) {
            case progress_bar_type:
                pDialog.dismiss();
                break;
        }
    }


    class AsyncDownLoadBook extends AsyncTask<BookOnline, String, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected Boolean doInBackground(BookOnline... params) {

            BookOnline itemBook = params[0];

            String epubPath = itemBook.getBookCoverPath();
            String coverPath = itemBook.getBookFilePath();

            // download file
            try{
                // check book is exists
                if( bookOfflineDao.checkBookOfflineByIdOnline(itemBook.getBookId()) ){
                    return false;
                }

                // get url
                URL urlEpub = new URL(JsonHandler.BASE_URL + epubPath);
                URL urlCover = new URL(JsonHandler.BASE_URL + coverPath);

                // connect
                URLConnection connectionEpub = urlEpub.openConnection();
                connectionEpub.connect();

//                URLConnection connectionCover = urlEpub.openConnection();
//                connectionCover.connect();

                // total length of file
//                int length = connectionEpub.getContentLength() + connectionCover.getContentLength();
                int length = connectionEpub.getContentLength();

                // download file
                InputStream inputEpub = new BufferedInputStream(urlEpub.openStream());
//                InputStream inputCover = new BufferedInputStream(urlCover.openStream());

                // Output stream
                OutputStream outputEpub = new FileOutputStream(FileHandler.ROOT_PATH + itemBook.getBookFilePath());
//                OutputStream outputCover = new FileOutputStream(FileHandler.ROOT_PATH + itemBook.getBookCoverPath());

                byte dataCover[] = new byte[1024];
                byte dataEpub[] = new byte[1024];

                int countCover, countEpub;
                long total = 0;


                while ((countEpub = inputEpub.read(dataEpub)) != -1) {
                    total += countEpub;
                    // writing data to file
                    outputEpub.write(dataEpub, 0, countEpub);

                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / length));
                }

//                while ((countCover = inputCover.read(dataCover)) != -1) {
//                    // writing data to file
//                    outputCover.write(dataCover, 0, countCover);
//                }

                // flushing output
                outputEpub.flush();

                // closing streams
                outputEpub.close();
                inputEpub.close();

                // write data to database
                BookOffline bookOffline = new BookOffline();

                bookOffline.setBookIdOnline(itemBook.getBookId())
                            .setBookFilePath(itemBook.getBookFilePath())
                            .setBookCoverPath(itemBook.getBookCoverPath())
                            .setBookAuthor(itemBook.getBookAuthor())
                            .setBookName(itemBook.getBookName());

                bookOfflineDao.addBookOffline(bookOffline);

            } catch (Exception ex){
                Log.d(">>> ken <<<", Log.getStackTraceString(ex));
            }

            return true;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(values[0]));
        }

        @Override
        protected void onPostExecute(Boolean result) {

            // dismiss the dialog after the file was downloaded
            dismissDialog(progress_bar_type);

            if( result ){
                Toast.makeText( getActivity(), "Tải thành công: " + Vars.currentBookDetail.getBookName(), Toast.LENGTH_SHORT ).show();
            } else {
                Toast.makeText( getActivity(), "Cuốn sách này đã có!", Toast.LENGTH_SHORT ).show();
            }

        }
    }// end-async AsyncDownLoadBook








}
