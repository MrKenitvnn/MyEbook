package ebook.ken.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ebook.ken.activity.R;
import ebook.ken.activity.SectionActivity;
import ebook.ken.adapter.FragmentBookStoreAdapter;
import ebook.ken.objects.BookOnline;
import ebook.ken.objects.SectionOnline;
import ebook.ken.utils.JsonHandler;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyUtils;
import ebook.ken.utils.MyApp;
import ebook.ken.utils.VolleyRequest;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by admin on 5/26/2015.
 */

public class BookStoreFragment extends Fragment {

    private View view;

    @Bind(R.id.lvStore)
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
        ButterKnife.bind(this, view);

        // events
        lvStore.setOnItemClickListener(lvStoreItemClick);
        lvStore.setOnRefreshListener(lvStoreRefresh);

        setHasOptionsMenu(true);
        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        loadDataFromServer();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
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
                if (MyUtils.isOnline(MyApp.getAppContext())) {
                    requestBookByPage(0);
                    tvSectionName.setText("Thể loại");
                } else {
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
                if (MyUtils.isOnline(MyApp.getAppContext())) {
                    pbRefresh.setVisibility(View.VISIBLE);// show progressbar
                    // set list empty
                    adapter = new FragmentBookStoreAdapter(getActivity(), new ArrayList<BookOnline>());
                    lvStore.setAdapter(adapter);

                    requestBookBySection(result.getSectionId());
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
        if (MyUtils.isOnline(MyApp.getAppContext())) {
            // load first page of book store
            if (MyApp.listBookOnlineFirstPage == null) {
                requestBookByPage(0);
            } else {
                adapter = new FragmentBookStoreAdapter(getActivity(), MyApp.listBookOnlineFirstPage);
                lvStore.setAdapter(adapter);
                tvSectionName.setText("Thể Loại");
            }

            // load section
            if (MyApp.listSection == null) {
                requestSection();
            }
        } else {
            tvIsOnline.setVisibility(View.VISIBLE);
            if (MyApp.listBookOnlineFirstPage == null) {
                lvStore.onRefreshComplete();
            }
        }
    }//end-func loadDataFromServer


    /**
     * volley request
     */
    private void requestSection() {
        JsonArrayRequest req = new JsonArrayRequest(JsonHandler.URL_LOAD_SECTION,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            MZLog.d(response.toString());
                            MyApp.listSection = JsonHandler.makeSectionFromJsonArray(response);
                        } catch (Exception e) {
                            MZLog.d("ERROR : BookStoreFragment : requestSection");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        MZLog.d("BookStoreFragment : requestSection");
                    }
                });
        VolleyRequest.getInstance().addToRequestQueue(req, MyApp.getAppContext());
    }

    private void requestBookBySection(int sectionId) {
        JsonArrayRequest req = new JsonArrayRequest(JsonHandler.BASE_URL + "load_book.php?section_id=" + sectionId,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            MZLog.d(response.toString());

                            pbRefresh.setVisibility(View.GONE);
                            tvIsOnline.setVisibility(View.GONE);
                            lvStore.onRefreshComplete();

                            MyApp.listBookBySection = JsonHandler.listBookFromJsonArray(response);
                            adapter = new FragmentBookStoreAdapter(getActivity(), MyApp.listBookBySection);
                            lvStore.setAdapter(adapter);

                        } catch (Exception e) {
                            MZLog.d("ERROR : BookStoreFragment : requestBookByPage");
                        }
                    }
                }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            try {
                                pbRefresh.setVisibility(View.GONE);
                                tvIsOnline.setVisibility(View.VISIBLE);
                                tvIsOnline.setText("Server not found.");
                            } catch (Exception e) {
                                MZLog.d("ERROR : BookStoreFragment : requestBookBySection");
                            }
                        }
                    });
        VolleyRequest.getInstance().addToRequestQueue(req, MyApp.getAppContext());
    }

    private void requestBookByPage (int page) {
        pbRefresh.setVisibility(View.VISIBLE);// show progressbar
        tvIsOnline.setVisibility(View.GONE);// hide message no internet access

        JsonArrayRequest req = new JsonArrayRequest(JsonHandler.BASE_URL + "load_book.php?page=" + page,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            MZLog.d(response.toString());

                            pbRefresh.setVisibility(View.GONE);
                            tvIsOnline.setVisibility(View.GONE);

                            MyApp.listBookOnlineFirstPage = JsonHandler.listBookFromJsonArray(response);
                            adapter = new FragmentBookStoreAdapter(getActivity(), MyApp.listBookOnlineFirstPage);
                            lvStore.setAdapter(adapter);

                            lvStore.onRefreshComplete();
                        } catch (Exception e) {
                            MZLog.d("ERROR : BookStoreFragment : requestBookByPage");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        try {
                            pbRefresh.setVisibility(View.GONE);
                            tvIsOnline.setVisibility(View.VISIBLE);
                            tvIsOnline.setText("Server not found.");
                            lvStore.onRefreshComplete();
                        } catch (Exception e) {
                            MZLog.d("ERROR : BookStoreFragment : requestBookByPage");
                        }
                    }
                });
        VolleyRequest.getInstance().addToRequestQueue(req, MyApp.getAppContext());
    }
}