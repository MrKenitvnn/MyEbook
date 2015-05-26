package ebook.ken.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ebook.ken.fragment.BookStoreFragment;
import ebook.ken.fragment.FavoritesFragment;
import ebook.ken.fragment.HomeFragment;
import ebook.ken.utils.FileHandler;
import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;


public class MainActivity extends MaterialNavigationDrawer {


    private SharedPreferences prefs = null;


    ////////////////////////////////////////////////////////////////////////////////
    // Todo activity life cycle

    @Override
    public void init(Bundle bundle) {

        // set the header image
        this.setDrawerHeaderImage(R.drawable.mat3);

        // set section
        this.addSection(newSection("Home", R.drawable.ic_action_back_white, new HomeFragment()));
        this.addSection(newSection("Favorites", R.drawable.ic_action_back_white, new FavoritesFragment()));
        this.addSection(newSection("Book Store", R.drawable.ic_action_back_white, new BookStoreFragment()));

        // get shared preferences for create app folder
        prefs = getSharedPreferences("ebook.ken.activity", MODE_PRIVATE);
    }


    @Override
    protected void onStart() {

        super.onStart();
        this.setHomeAsUpIndicator(R.drawable.ic_action_back_white);

        // check is first run app to create app folder
        if (prefs.getBoolean("firstrun", true)) {
            // create root folder
            FileHandler.createRootFolder();
            prefs.edit().putBoolean("firstrun", false).commit();
        }

    }// end-func onStart


    @Override
    public void onHomeAsUpSelected() {
        // when the back arrow is selected this method is called
    }


    ////////////////////////////////////////////////////////////////////////////////
    // Todo function inner




    ////////////////////////////////////////////////////////////////////////////////
    // Todo option menu


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
