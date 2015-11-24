package ebook.ken.fragment;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONException;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.adapter.FragmentBookStoreAdapter;
import ebook.ken.objects.BookOnline;
import ebook.ken.objects.SectionOnline;
import ebook.ken.utils.JsonHandler;
import ebook.ken.utils.MZLog;
import ebook.ken.utils.MyApp;
import ebook.ken.utils.MyUtils;
import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;

/**
 * Created by ken on 23/11/2015.
 */
public class BookStoreTabHostFragment extends Fragment implements MaterialTabListener {

    private View view;
    private MaterialTabHost mTabHost;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;
    private FragmentBookStoreAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bookstore_tabhost, container, false);

        mTabHost = (MaterialTabHost) view.findViewById(R.id.materialTabHost);
        mViewPager = (ViewPager) view.findViewById(R.id.viewPager);

        mAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabHost.setSelectedNavigationItem(position);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

//        for (int i = 0; i < mAdapter.getCount(); i++) {
        MaterialTab materialTab = mTabHost.newTab();
        materialTab.setText("TẤT CẢ");
        materialTab.setTabListener(this);
        mTabHost.addTab(materialTab);
//        }
        // load data from server
        loadDataFromServer();
    }// end-func onStart

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mViewPager.setCurrentItem(materialTab.getPosition());
        int currentTab = mViewPager.getCurrentItem();
        MZLog.d("onTabSelected page: " + currentTab  + "--materialTab: " + materialTab.getPosition() );
    }


    @Override
    public void onTabReselected(MaterialTab materialTab) {
        int currentTab = mViewPager.getCurrentItem();
        MZLog.d("onTabReselected page: " + currentTab  + "--materialTab: " + materialTab.getPosition() );
    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {
        int currentTab = mViewPager.getCurrentItem();
        MZLog.d("onTabUnselected page: " + currentTab  + "--materialTab: " + materialTab.getPosition() );
    }

    /**
     * load data from server
     */
    private void loadDataFromServer() {
        if (MyUtils.isOnline(MyApp.getAppContext())) {
            // load first page

            // load section
            if (MyApp.listSection == null) {
                new AsyncLoadSection().execute();
            }

        } else {

        }
    }

    private class AsyncLoadBookByFirstPage extends AsyncTask<Void, Void, List<BookOnline>> {

        @Override
        protected void onPreExecute() {
            // show progressbar
//            pbRefresh.setVisibility(View.VISIBLE);
            // hide message no internet access
//            tvIsOnline.setVisibility(View.GONE);
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
//            pbRefresh.setVisibility(View.GONE);
//            lvStore.onRefreshComplete();

            if (result != null) {
                MyApp.listBookOnlineFirstPage = result;
                adapter = new FragmentBookStoreAdapter(getActivity(), result);
//                lvStore.setAdapter(adapter);
            }
        }
    }// end-async AsyncLoadBookByFirstPage

    /**
     * async task load section
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
            if (result != null) {
                for (int i = 0; i < MyApp.listSection.size(); i++) {
                    mTabHost.addTab(mTabHost.newTab().setText(MyApp.listSection.get(i).getSectionName()).setTabListener(BookStoreTabHostFragment.this));
                    mTabHost.notifyDataSetChanged();
                    mAdapter.setCount(mAdapter.getCount() + 1);
                }
            }
        }
    }

    /**
     * View pager adapter
     */
    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int count = 1;
        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {
            DummyFragment dummyFragment = new DummyFragment();
            return dummyFragment;
        }

        @Override
        public int getCount() {
            return count;
        }

        public void setCount(int newCount) {
            count = newCount;
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return MyApp.getAppContext().getResources().getStringArray(R.array.tabs)[position];
        }
    }
}
