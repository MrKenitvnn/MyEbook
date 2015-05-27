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

import ebook.ken.adapter.SectionAdapter;
import ebook.ken.objects.SectionOnline;
import ebook.ken.utils.Vars;


public class SectionActivity extends Activity{
	
	// define something
	public static final int 
						REQUEST_CODE = 0x1,
						RESULT_OK = 0x0,
						RESULT_CANCELED = 0x2;
	
	public static final String
						RESULT = "section_id";	

	// intent for return main activity
	Intent returnIntent;
	
	// controls
	private Button btnCloseSection;
	private ListView lvSection;
	
	// variable in activity
	SectionAdapter adapter;
	
	
	////////////////////////////////////////////////////////////////////////////////
	// TODO activity life cycle
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_section);
		
		// init controls
		btnCloseSection = (Button) findViewById(R.id.btnCloseSection);
		lvSection = (ListView) findViewById(R.id.lvSection);
		
		// events
		btnCloseSection.setOnClickListener(setOnClickListenerEvent);
		lvSection.setOnItemClickListener(lvSectionItemClick);
		
	}//end-func onCreate

	@Override
	protected void onResume() {

		super.onResume();
		// set data for listView section
		if(Vars.listSection != null){
			adapter = new SectionAdapter(this, Vars.listSection);
			lvSection.setAdapter(adapter);
		}

	}// end-func onResume


	////////////////////////////////////////////////////////////////////////////////
	// TODO events

	OnClickListener setOnClickListenerEvent = new OnClickListener() {
		
		@Override
		public void onClick(View v) {

			returnIntent = new Intent();
			setResult(RESULT_CANCELED, returnIntent);
			finish();

		}
	};// end-func setOnClickListenerEvent
	
	
	OnItemClickListener lvSectionItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			// get object section click
			SectionOnline item = adapter.getItem(position);

			// setup intent
			returnIntent = new Intent();
			returnIntent.putExtra(RESULT, item);
			setResult(RESULT_OK, returnIntent);
			finish();

		}
	};// end-event lvSectionItemClick
	
	
	////////////////////////////////////////////////////////////////////////////////
	// TODO 
	
	
}
