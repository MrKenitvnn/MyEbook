package ebook.ken.activity;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import ebook.ken.dao.Database;
import ebook.ken.fragment.BookStoreFragment;
import ebook.ken.fragment.FavoritesFragment;
import ebook.ken.fragment.HomeCardGridFragment;
import ebook.ken.fragment.HomeCardListFragment;
import ebook.ken.fragment.HomeFragment;
import ebook.ken.fragment.InfoFragment;
import ebook.ken.gcm.QuickstartPreferences;
import ebook.ken.gcm.RegistrationIntentService;
import ebook.ken.utils.FileHandler;
import ebook.ken.utils.MZLog;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

public class MainActivity extends MaterialNavigationDrawer implements SearchView.OnQueryTextListener{

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static String textSearch = "";
    private SearchView mSearchView;

    private SharedPreferences prefs = null;
    private Database database;
    GoogleCloudMessaging gcm;


    /**
     *  activity life cycle
     */
    @Override
    public void init(Bundle bundle) {

        try {
            // set the header image
            this.setDrawerHeaderImage(R.drawable.mat3);

            // set section
            this.addSection(newSection("Home", R.drawable.ic_home, new HomeFragment()));
            this.addSection(newSection("Favorites", R.drawable.ic_communities, new FavoritesFragment()));
            this.addSection(newSection("Book Store", R.drawable.ic_pages, new BookStoreFragment()));
            this.addSection(newSection("Group", R.drawable.ic_whats_hot, new InfoFragment()));

            // get shared preferences for create app folder
            prefs = getSharedPreferences("ebook.ken.activity", MODE_PRIVATE);

        } catch (Exception ex){
            MZLog.d(Log.getStackTraceString(ex));
        }
    }// end-func init


    @Override
    protected void onStart() {
        super.onStart();
        try {
            this.setHomeAsUpIndicator(R.drawable.ic_action_back_white);

            // check is first run app to create app folder
            if (prefs.getBoolean("firstRun", true)) {
                // create root folder
                FileHandler.createRootFolder();
                // create database
                database = new Database(this);
                // put first run
                prefs.edit().putBoolean("firstRun", false).commit();
            }// end-if

            /**
             * TODO GCM
             */
            boolean state = prefs.getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
            if (checkPlayServices() && !state ) {
                // Start IntentService to register this application with GCM.
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }

        } catch (Exception ex){
            MZLog.d(Log.getStackTraceString(ex));
        }
    }// end-func onStart


    @Override
    public void onHomeAsUpSelected() {
        // when the back arrow is selected this method is called

    }// end-func onHomeAsUpSelected

    /**
     * option menu
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        mSearchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        setupSearchView();

        return true;

    }// end-func onCreateOptionsMenu

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    /**
     *  setup search
     */
    private void setupSearchView() {

        mSearchView.setIconifiedByDefault(true);
        mSearchView.refreshDrawableState();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        if (searchManager != null) {}

        mSearchView.setOnQueryTextListener(this);
    }

    /**
     * hardware
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Fragment current = (Fragment) getCurrentSection().getTargetFragment();
        if (current instanceof HomeFragment
                || current instanceof FavoritesFragment) {
            MainActivity.this.finish();
        }
    }

    /**
     * check play services
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                MZLog.i("This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    /**
     * search
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Fragment fmHomeContent = getSupportFragmentManager().findFragmentById(R.id.fmHomeContent);

        if (fmHomeContent instanceof HomeCardListFragment) {
            HomeCardListFragment.adapter.filter(newText);
        } else if (fmHomeContent instanceof HomeCardGridFragment) {

        }

        return true;
    }

}
