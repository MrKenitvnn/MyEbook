package ebook.ken.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ebook.ken.activity.R;
import ebook.ken.objects.SectionOnline;


public class SectionAdapter extends ArrayAdapter<SectionOnline>{

	
	public SectionAdapter(Context context, List<SectionOnline> lstData) {
		super(context, 0, lstData);
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		// Get the data item for this position
		SectionOnline item = getItem(position);
		
		// Check if an existing view is being reused, otherwise inflate the view
		if(convertView == null){
			convertView = LayoutInflater.from(getContext())
							.inflate(R.layout.item_section, parent, false);
		}
		
		// Lookup view for data population
		TextView tvName = (TextView) convertView.findViewById(R.id.tvSectionName);
	    
		// Populate the data into the template view using the data object	       
		tvName.setText(item.getSectionName());

		// Return the completed view to render on screen		
		return convertView;
	}
	
	
}
