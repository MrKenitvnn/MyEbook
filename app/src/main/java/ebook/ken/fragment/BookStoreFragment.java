package ebook.ken.fragment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import ebook.ken.activity.R;
import ebook.ken.activity.SectionActivity;
import ebook.ken.adapter.FragmentBookStoreAdapter;
import ebook.ken.objects.BookOnline;
import ebook.ken.objects.SectionOnline;
import ebook.ken.utils.JsonHandler;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyUtils;
import ebook.ken.utils.MyApp;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by admin on 5/26/2015.
 */

public class BookStoreFragment extends Fragment {

    private View view;
    PullToRefreshListView lvStore;

    @Bind(R.id.tvIsOnline)
    TextView tvIsOnline;

    @Bind(R.id.tvSectionName)
    TextView tvSectionName;

    @Bind(R.id.pbRefresh)
    ProgressBar pbRefresh;

    @Bind(R.id.llSection)
    LinearLayout llSection;

    private FragmentBookStoreAdapter adapter;

    /**
     * fragment life cycle
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_book_store, container, false);
        lvStore = (PullToRefreshListView) view.findViewById(R.id.lvStore);

        ButterKnife.bind(this, view);
        // events
        lvStore.setOnItemClickListener(lvStoreItemClick);
        lvStore.setOnRefreshListener(lvStoreRefresh);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadDataFromServer();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * events
     */
    OnRefreshListener lvStoreRefresh = new OnRefreshListener() {
        @Override
        public void onRefresh(PullToRefreshBase pullToRefreshBase) {
            try {

                if (MyUtils.isOnline(getActivity())) {
                    new AsyncLoadBookByFirstPage().execute();
                    tvSectionName.setText("Thể loại");
                    MyApp.isInSection = false;
                } else {
                    lvStore.onRefreshComplete();
                    // thong bao khong co ket noi mang
                    tvIsOnline.setVisibility(View.VISIBLE);
                }
            } catch (Exception ex) {
                MZLog.d(Log.getStackTraceString(ex));
            }
        }
    };

    AdapterView.OnItemClickListener lvStoreItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            try {
                // get item by position click
                BookOnline item = adapter.getItem(position - 1);

                // set book current
                MyApp.currentBookDetail = item;

                // start child fragment
                ((MaterialNavigationDrawer) getActivity()).setFragmentChild(new BookStoreDetailFragment(), item.getBookName());
            } catch (Exception ex) {
                MZLog.d(Log.getStackTraceString(ex));
            }
        }
    };

    @OnClick(R.id.llSection)
    void llSection() {
        try {
            if (MyApp.listSection != null) {
                Intent i = new Intent(getActivity(), SectionActivity.class);
                startActivityForResult(i, SectionActivity.REQUEST_CODE);
            }
        } catch (Exception ex) {
            MZLog.d(Log.getStackTraceString(ex));
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == SectionActivity.RESULT_OK) {
                SectionOnline result = (SectionOnline) data.getSerializableExtra(SectionActivity.RESULT);
                MyApp.currentSection = result;

                // set text for current section
                tvSectionName.setText(result.getSectionName());

                // load book by section
                if (MyUtils.isOnline(getActivity())) {
                    new AsyncLoadBookBySection().execute(result.getSectionId());
                } else {
                    Toast.makeText(getActivity(), "Hiện không có kết nối internet", Toast.LENGTH_SHORT).show();
                }
            }

            if (resultCode == SectionActivity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } catch (Exception ex) {
            MZLog.d(Log.getStackTraceString(ex));
        }
    }

    /**
     * function
     */
    private void loadDataFromServer() {

        if (MyUtils.isOnline(getActivity())) {

            // load section
            if (MyApp.listSection == null) {
                new AsyncLoadSection().execute();
            }

            // load first page of book store
            if (MyApp.listBookOnlineFirstPage == null) {
                new AsyncLoadBookByFirstPage().execute();
            } else {
                adapter = new FragmentBookStoreAdapter(getActivity(), MyApp.listBookOnlineFirstPage);
                lvStore.setAdapter(adapter);
                tvSectionName.setText("Thể Loại");
            }
        } else {
            tvIsOnline.setVisibility(View.VISIBLE);
            if (MyApp.listBookOnlineFirstPage == null) {
                lvStore.onRefreshComplete();
            }
        }

    }//end-func loadDataFromServer

    /**
     * async task
     */

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
            MyApp.listSection = result;
        }
    }// end-async AsyncLoadSection


    private class AsyncLoadBookByFirstPage extends AsyncTask<Void, Void, List<BookOnline>> {

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
                MZLog.d(Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<BookOnline> result) {

            pbRefresh.setVisibility(View.GONE);
            lvStore.onRefreshComplete();

            if (result != null) {
                MyApp.listBookOnlineFirstPage = result;
                adapter = new FragmentBookStoreAdapter(getActivity(), result);
                lvStore.setAdapter(adapter);
            }
        }
    }// end-async AsyncLoadBookByFirstPage


    private class AsyncLoadBookBySection extends AsyncTask<Integer, Void, List<BookOnline>> {
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
                return MyApp.listBookBySection = JsonHandler
                        .getBookOnline(JsonHandler.GET_BOOK_BY_SECTION, "" + params[0]);

            } catch (JSONException e) {
                MZLog.d(Log.getStackTraceString(e));
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<BookOnline> result) {

            try {
                pbRefresh.setVisibility(View.GONE);
                lvStore.onRefreshComplete();

                // state list on view book of section
                MyApp.isInSection = true;

                if (result != null) {
                    adapter = new FragmentBookStoreAdapter(getActivity(), result);
                    lvStore.setAdapter(adapter);
                }// end-if
            } catch (Exception ex) {
                MZLog.d(Log.getStackTraceString(ex));
            }
        }
    }// end-async AsyncLoadBookByFirstPage
}
