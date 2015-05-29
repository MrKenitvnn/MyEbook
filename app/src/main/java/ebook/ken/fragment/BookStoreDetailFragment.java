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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import ebook.ken.activity.R;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.dao.ChapterDAO;
import ebook.ken.objects.Book;
import ebook.ken.objects.BookOffline;
import ebook.ken.objects.BookOnline;
import ebook.ken.utils.BookOfflineHandler;
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
    private ChapterDAO chapterDAO;



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
        chapterDAO     = new ChapterDAO(getActivity());

        return view;
    }


    ///////////////////////////////////////////////////////////////////////////////////
    // TODO events

    OnClickListener btnDownloadEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncDownLoadBook().execute();
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
                pDialog.setMessage("Downloading. Please wait...");
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


    class AsyncDownLoadBook extends AsyncTask<Void, String, Boolean>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            BookOnline itemBook = Vars.currentBookDetail;
            String epubPath = itemBook.getBookFilePath();

            // download file
            try{
                // check book is exists
                if( bookOfflineDao.checkBookOfflineByIdOnline(itemBook.getBookId()) ){
                    return false;
                }

                // get url
                URL url = new URL(JsonHandler.BASE_URL + epubPath);

                // connect
                URLConnection connectionEpub = url.openConnection();
                connectionEpub.connect();

                // total length of file
                int length = connectionEpub.getContentLength();

                // download file
                InputStream input = new BufferedInputStream(url.openStream());

                // Output stream
                OutputStream output = new FileOutputStream(FileHandler.ROOT_PATH + itemBook.getBookFilePath());

                byte data[] = new byte[1024];

                int count;
                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // writing data to file
                    output.write(data, 0, count);

                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / length));
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

                try {
                    String ncxFilePath = "";
                    String opfFilePath = "";
                    String coverFilePath = "";

//                    BookOnline itemBook = Vars.currentBookDetail;
                    BookOffline bookOffline = new BookOffline();

                    // step 1: create folder of book
                    String bookFolder       = String.valueOf(bookOfflineDao.getLastId());
                    String bookFolderPath   = FileHandler.createBookFolder(bookFolder);
                    String pathExtract      = FileHandler.ROOT_PATH + itemBook.getBookFilePath();

                    // step 2: extract file
                    FileHandler.doUnzip(FileHandler.ROOT_PATH + itemBook.getBookFilePath(), bookFolderPath);

                    // step 3: delete file epub
                    FileHandler.deleteFileFromSdcard(pathExtract);

                    // step 4: find ncx, opf, cover path
                    ncxFilePath   = FileHandler.getNcxFilePath(FileHandler.EPUB_PATH + bookFolder);
                    opfFilePath   = FileHandler.getContentFilePath(FileHandler.EPUB_PATH + bookFolder);
                    coverFilePath = FileHandler.getCoverFilePath(FileHandler.EPUB_PATH + bookFolder);

                    // step 5: write data to database
                    bookOffline.setBookIdOnline(itemBook.getBookId())
                                .setBookFilePath(itemBook.getBookFilePath())
                                .setBookAuthor(itemBook.getBookAuthor())
                                .setBookName(itemBook.getBookName());
                    bookOffline .setBookFolder(bookFolder)
                                .setBookFolderPath(bookFolderPath)
                                .setBookNcxPath(ncxFilePath)
                                .setBookOpfPath(opfFilePath)
                                .setBookCoverPath(coverFilePath);

                    bookOfflineDao.addBookOffline(bookOffline);
                    //  ghi chapter
                    chapterDAO.addListChapter(BookOfflineHandler
                            .listEpubChapterData(bookOffline));

                } catch (Exception ex) {
                    Log.d(">>> ken <<<", Log.getStackTraceString(ex));
                }

            } catch (Exception ex){
                Log.d(">>> ken <<<", Log.getStackTraceString(ex));
                return false;
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
                Toast.makeText(getActivity(), "Tải thành công: " + Vars.currentBookDetail.getBookName(), Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText( getActivity(), "Cuốn sách này đã có!", Toast.LENGTH_SHORT ).show();
            }

        }
    }// end-async AsyncDownLoadBook








}
