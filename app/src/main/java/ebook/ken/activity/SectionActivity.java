package ebook.ken.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import ebook.ken.adapter.SectionAdapter;
import ebook.ken.objects.SectionOnline;
import ebook.ken.utils.MyApp;


public class SectionActivity extends Activity {

    public static final int
            REQUEST_CODE = 0x1,
            RESULT_OK = 0x0,
            RESULT_CANCELED = 0x2;

    public static final String
            RESULT = "section_id";

    Intent returnIntent; // intent for return main activity

    // controls
    @Bind(R.id.btnCloseSection) Button btnCloseSection;
    @Bind(R.id.lvSection) ListView lvSection;

    SectionAdapter adapter; // variable in activity

    /**
     * activity life cycle
     **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_section);

        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // set data for listView section
        if (MyApp.listSection != null) {
            adapter = new SectionAdapter(this, MyApp.listSection);
            lvSection.setAdapter(adapter);
        }
    }

    /**
     * events
     */

    @OnClick(R.id.btnCloseSection)
    void btnCloseSectionClick() {
        returnIntent = new Intent();
        setResult(RESULT_CANCELED, returnIntent);
        finish();
    }

    @OnItemClick(R.id.lvSection)
    void lvSectionItemClick(int position) {
        // get object section click
        SectionOnline item = adapter.getItem(position);

        // setup intent
        returnIntent = new Intent();
        returnIntent.putExtra(RESULT, item);
        setResult(RESULT_OK, returnIntent);
        finish();
    }
}