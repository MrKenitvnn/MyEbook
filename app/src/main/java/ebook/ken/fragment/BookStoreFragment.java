package ebook.ken.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.activity.SectionActivity;
import ebook.ken.adapter.FragmentBookStoreAdapter;
import ebook.ken.objects.BookOnline;
import ebook.ken.objects.SectionOnline;
import ebook.ken.utils.JsonHandler;
import ebook.ken.utils.MyUtils;
import ebook.ken.utils.Vars;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by admin on 5/26/2015.
 */

public class BookStoreFragment extends Fragment {

    private View view;
    private PullToRefreshListView lvStore;
    private LinearLayout llSection;
    private TextView tvSectionName, tvIsOnline;
    private ProgressBar pbRefresh;

    private FragmentBookStoreAdapter adapter;


    ////////////////////////////////////////////////////////////////////////////////
    // TODO fragment life cycle

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_book_store, container, false);

        // init controls
        lvStore         = (PullToRefreshListView) view.findViewById(R.id.pull_to_refresh_listview);
        llSection		= (LinearLayout) view.findViewById(R.id.llSection);
        tvSectionName	= (TextView) view.findViewById(R.id.tvSectionName);
        tvIsOnline      = (TextView) view.findViewById(R.id.tvIsOnline);
        pbRefresh       = (ProgressBar) view.findViewById(R.id.pbRefresh);

        // events
        llSection.setOnClickListener(llSectionEvent);
        // Set a listener to be invoked when the list should be refreshed.
//        lvStore.setPullRotateImage(getResources().getDrawable(R.drawable.rotate_img));
        lvStore.setOnItemClickListener(lvStoreEvent);
        lvStore.setOnRefreshListener(lvStoreRefresh);

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();

        // load data from server
        loadDataFromServer();

    }// end-func onStart


    ////////////////////////////////////////////////////////////////////////////////
    // TODO events

    OnRefreshListener lvStoreRefresh = new OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase pullToRefreshBase) {
            try {

                if (MyUtils.isOnline(getActivity())) {
                    new AsyncLoadBookByFirstPage().execute();
                    tvSectionName.setText("Thể loại");
                    Vars.isInSection = false;
                } else {
                    lvStore.onRefreshComplete();
                    // thong bao khong co ket noi mang
                    tvIsOnline.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex){
                Log.d(">>> ken <<<", Log.getStackTraceString(ex));
            }
        }
    };// end-event lvStoreRefresh


    OnItemClickListener lvStoreEvent = new OnItemClickListener(){

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                // get item by position click
                BookOnline item = adapter.getItem(position - 1);

                // set book current
                Vars.currentBookDetail = item;

                // start child fragment
                ((MaterialNavigationDrawer) getActivity()).setFragmentChild(new BookStoreDetailFragment(), item.getBookName());
            } catch ( Exception ex){
                Log.d(">>> ken <<<", Log.getStackTraceString(ex));
            }
        }
    };// end-event lvStoreEvent


    OnClickListener llSectionEvent = new OnClickListener() {

        @Override
        public void onClick(View v) {
            try{
                if( Vars.listSection!= null ){
                    Intent i = new Intent(getActivity(), SectionActivity.class);
                    startActivityForResult(i, SectionActivity.REQUEST_CODE);
                }
            } catch(Exception ex) {
                Log.d(">>> ken <<<", Log.getStackTraceString(ex));
            }
        }
    };// end-event llSectionEvent


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try{
            if(resultCode == SectionActivity.RESULT_OK){
                SectionOnline result = (SectionOnline) data.getSerializableExtra(SectionActivity.RESULT);
                Vars.currentSection = result;

                // set text for current section
                tvSectionName.setText(result.getSectionName());

                // load book by section
                if( MyUtils.isOnline(getActivity()) ){
                    new AsyncLoadBookBySection().execute(result.getSectionId());
                } else {
                    Toast.makeText(getActivity(), "Hiện không có kết nối internet", Toast.LENGTH_SHORT).show();
                }

                Log.d(">>> ken <<<", "" + result.getSectionId());
            }

            if (resultCode == SectionActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }catch(Exception ex){
            Log.d(">>> ken <<<", Log.getStackTraceString(ex));
        }
    };// end-event onActivityResult


    ////////////////////////////////////////////////////////////////////////////////
    // TODO function

    private void loadDataFromServer(){

        if(MyUtils.isOnline(getActivity())){

            // load section
            if( Vars.listSection == null){
                new AsyncLoadSection().execute();
            }

            // load first page of book store
            if( Vars.listBookOnlineFirstPage == null ){
                new AsyncLoadBookByFirstPage().execute();
            } else {
                if(Vars.isInSection){
                    adapter = new FragmentBookStoreAdapter(getActivity(), Vars.listBookBySection);
                    lvStore.setAdapter(adapter);
                    tvSectionName.setText(Vars.currentSection.getSectionName());
                } else {
                    adapter = new FragmentBookStoreAdapter(getActivity(), Vars.listBookOnlineFirstPage);
                    lvStore.setAdapter(adapter);
                    Vars.isInSection = false;
                    tvSectionName.setText("Thể Loại");
                }
            }
        } else {
            tvIsOnline.setVisibility(View.VISIBLE);
            lvStore.onRefreshComplete();
        }

    }//end-func loadDataFromServer


    ///////////////////////////////////////////////////////////////////////////////////
    //TODO async task

    private class AsyncLoadSection extends AsyncTask<Void, Void, List<SectionOnline>> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected List<SectionOnline> doInBackground(Void... params) {
            // get list section online in backgound
            return JsonHandler.getSectionOnline();
        }

        @Override
        protected void onPostExecute(List<SectionOnline> result) {
            // set list section to Vars
            Vars.listSection = result;
        }
    }// end-async AsyncLoadSection


    private class AsyncLoadBookByFirstPage extends AsyncTask<Void, Void, List<BookOnline>>{

        @Override
        protected void onPreExecute() {
            // show progressbar
            pbRefresh.setVisibility(View.VISIBLE);
            // hide message no internet access
            tvIsOnline.setVisibility(View.GONE);
        }

        @Override
        protected List<BookOnline> doInBackground(Void... params) {
            try {
                return JsonHandler.getBookOnline(JsonHandler.GET_BOOK_BY_PAGE,
                        String.valueOf(0));
            } catch (JSONException e) {
                Log.d(">>> ken <<<", "load book: " + Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<BookOnline> result) {

            pbRefresh.setVisibility(View.GONE);
            lvStore.onRefreshComplete();

            if(result != null){
                Vars.listBookOnlineFirstPage = result;
                adapter = new FragmentBookStoreAdapter(getActivity(), result);
                lvStore.setAdapter(adapter);
            }// end-if
        }
    }// end-async AsyncLoadBookByFirstPage


    private class AsyncLoadBookBySection extends AsyncTask<Integer, Void, List<BookOnline>>{

        @Override
        protected void onPreExecute() {
            // show progressbar
            pbRefresh.setVisibility(View.VISIBLE);
            // hide message no internet access
            tvIsOnline.setVisibility(View.GONE);

            // set list empty
            adapter = new FragmentBookStoreAdapter(getActivity(), new ArrayList<BookOnline>());
            lvStore.setAdapter(adapter);
        }

        @Override
        protected List<BookOnline> doInBackground(Integer... params) {
            try {
                return Vars.listBookBySection = JsonHandler
                                                .getBookOnline(JsonHandler.GET_BOOK_BY_SECTION, "" + params[0]);

            } catch (JSONException e) {
                Log.d(">>> ken <<<", "Book store by section: " + Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<BookOnline> result) {

            try {

                pbRefresh.setVisibility(View.GONE);
                lvStore.onRefreshComplete();

                // state list on view book of section
                Vars.isInSection = true;

                if (result != null) {
                    adapter = new FragmentBookStoreAdapter(getActivity(), result);
                    lvStore.setAdapter(adapter);
                }// end-if

            } catch (Exception ex){
                Log.d(">>> ken <<<", "Book store by section: " + Log.getStackTraceString(ex));
            }
        }
    }// end-async AsyncLoadBookByFirstPage



}
