package ebook.ken.fragment;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import ebook.ken.activity.R;
import ebook.ken.dao.BookOfflineDao;
import ebook.ken.dao.ChapterDao;
import ebook.ken.objects.BookOffline;
import ebook.ken.objects.BookOnline;
import ebook.ken.utils.BookOfflineHandler;
import ebook.ken.utils.FileHandler;
import ebook.ken.utils.JsonHandler;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyApp;


/**
 * Created by admin on 5/26/2015.
 */

public class BookStoreDetailFragment extends Fragment {

    private View view;
    private Button btnDownload;
    private TextView tvAuthorStoreDetail, tvDescription;
    private RatingBar ratingBar;

    private BookOfflineDao bookOfflineDao;
    private ChapterDao chapterDao;
    private SharedPreferences sharedPreferences;
    private BookOnline itemBook;

    /**
     * fragment life cycle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_book_store_detail, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // init controls
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        btnDownload = (Button) view.findViewById(R.id.btnDownload);
        tvDescription = (TextView) view.findViewById(R.id.tvDescription);
        tvAuthorStoreDetail = (TextView) view.findViewById(R.id.tvAuthorStoreDetail);

        // set text
        tvAuthorStoreDetail.setText(MyApp.currentBookDetail.getBookAuthor());
        tvDescription.setText(MyApp.currentBookDetail.getBookDesciption());

        // events
        btnDownload.setOnClickListener(btnDownloadEvent);
        ratingBar.setOnRatingBarChangeListener(onRating);

        // enable options menu
        setHasOptionsMenu(true);

        // open dao
        bookOfflineDao = new BookOfflineDao(getActivity());
        chapterDao = new ChapterDao(getActivity());

        itemBook = MyApp.currentBookDetail;

        return view;
    }


    /**
     * events
     */
    OnClickListener btnDownloadEvent = new OnClickListener() {
        @Override
        public void onClick(View v) {
            new AsyncDownLoadBook().execute();
        }
    };

    OnRatingBarChangeListener onRating = new OnRatingBarChangeListener() {
        @Override
        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
            // send rate value to server
            int user_id = sharedPreferences.getInt("user_id", 0);
            if (user_id != 0) {
                new AsyncRating().execute(String.valueOf(itemBook.getBookId()), String.valueOf(user_id), String.valueOf(rating));
            } else {
                Toast.makeText(getActivity(), "Server Error!", Toast.LENGTH_SHORT).show();
            }
        }
    };

    /**
     * option menu
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        MenuItem item = menu.findItem(R.id.search);
        item.setVisible(false);

        super.onCreateOptionsMenu(menu, inflater);
    }

    /**
     * async task
     */
    private ProgressDialog pDialog;

    // Progress dialog type (0 - for Horizontal progress bar)
    public static final int progress_bar_type = 0;

    /**
     * Showing Dialog
     */
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

    protected void dismissDialog(int id) {
        switch (id) {
            case progress_bar_type:
                pDialog.dismiss();
                break;
        }
    }


    /**
     * Asyntask for download book
     */
    class AsyncDownLoadBook extends AsyncTask<Void, String, Boolean> {

        BookOffline bookOffline = new BookOffline();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(progress_bar_type);
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            String epubPath = itemBook.getBookFilePath();

            // download file
            try {
                // check book is exists
                if (bookOfflineDao.checkBookOfflineByIdOnline(itemBook.getBookId())) {
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


                    // step 1: create folder of book
                    String bookFolder = String.valueOf(bookOfflineDao.getLastId());
                    String bookFolderPath = FileHandler.createBookFolder(bookFolder);
                    String pathExtract = FileHandler.ROOT_PATH + itemBook.getBookFilePath();

                    // step 2: extract file
                    FileHandler.doUnzip(FileHandler.ROOT_PATH + itemBook.getBookFilePath(), bookFolderPath);

                    // step 3: delete file epub
                    FileHandler.deleteFileFromSdcard(pathExtract);

                    // step 4: find ncx, opf, cover path
                    ncxFilePath = FileHandler.getNcxFilePath(FileHandler.EPUB_PATH + bookFolder);
                    opfFilePath = FileHandler.getContentFilePath(FileHandler.EPUB_PATH + bookFolder);
                    coverFilePath = FileHandler.getCoverFilePath(FileHandler.EPUB_PATH + bookFolder);

                    // step 5: write data to database
                    bookOffline.setBookIdOnline(itemBook.getBookId())
                            .setBookFilePath(itemBook.getBookFilePath())
                            .setBookAuthor(itemBook.getBookAuthor())
                            .setBookName(itemBook.getBookName());
                    bookOffline.setBookFolder(bookFolder)
                            .setBookFolderPath(bookFolderPath)
                            .setBookNcxPath(ncxFilePath)
                            .setBookOpfPath(opfFilePath)
                            .setBookCoverPath(coverFilePath);

                    bookOfflineDao.addBookOffline(bookOffline);
                    //  ghi chapter
                    chapterDao.addListChapter(BookOfflineHandler
                            .listEpubChapterData(bookOffline));

                } catch (Exception ex) {
                    MZLog.d( Log.getStackTraceString(ex));
                }
            } catch (Exception ex) {
                MZLog.d(Log.getStackTraceString(ex));
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

            if (result) {
                Toast.makeText(getActivity(), "Tải thành công: " + MyApp.currentBookDetail.getBookName(), Toast.LENGTH_SHORT).show();

                // TODO note: put to update download total's book plus = 1
                new AsyncPlusDownload().execute(bookOffline);
            } else {
                Toast.makeText(getActivity(), "Cuốn sách này đã có!", Toast.LENGTH_SHORT).show();
            }
        }
    }// end-async AsyncDownLoadBook


    /**
     * Asynctask for plus value download
     */
    class AsyncPlusDownload extends AsyncTask<BookOffline, Void, Void> {

        @Override
        protected Void doInBackground(BookOffline... params) {
            try {
                sendAddPlusDownload(params[0].getBookIdOnline());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private void sendAddPlusDownload(int book_id) throws Exception {

        String url = "http://mrkenitvnn.esy.es/api/includes/add_download.php";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        /*con.setRequestProperty("User-Agent", USER_AGENT);*/
        /*con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");*/

        String urlParameters = "book_id=" + book_id;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();
    }


    /**
     * send rate
     */
    class AsyncRating extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            try {
                sendRating(Integer.parseInt(params[0]), Integer.parseInt(params[1]), params[2]);
            } catch (Exception e) {
                MZLog.d(e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(MyApp.getAppContext(), "Thank you for your rating!", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * @param book_id
     * @param user_id
     * @param rate
     * @throws Exception
     */
    private void sendRating(int book_id, int user_id, String rate) throws Exception {

        String url = "http://mrkenitvnn.esy.es/api/includes/add_rate.php";
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add reuqest header
        con.setRequestMethod("POST");
        /*con.setRequestProperty("User-Agent", USER_AGENT);*/
        /*con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");*/

        String urlParameters = "book_id=" + book_id + "&user_id=" + user_id + "&rate=" + rate;

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        MZLog.d("rate-response: " + response.toString());
    }


}
