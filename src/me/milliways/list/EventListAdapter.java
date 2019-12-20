package me.milliways.list;

import java.util.List;

import me.milliways.R;
import me.milliways.data.PlaceRepository;
import me.milliways.entity.Event;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class EventListAdapter extends ArrayAdapter<Event>{
	private final int resource;

    public EventListAdapter(Context context, int resource, List<Event> items) {
        super(context, resource, items);
        this.resource = resource;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LinearLayout projectView;

        Event event = getItem(position);

        if(convertView == null) {
            projectView = new LinearLayout(getContext());
            LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi.inflate(resource, projectView, true);
        } else {
            projectView = (LinearLayout) convertView;
        }

        TextView top = (TextView) projectView.findViewById(R.id.line_title);
        TextView bottom = (TextView) projectView.findViewById(R.id.line_description);
        
        top.setText("[" + getContext().getResources().getString(PlaceRepository.getPlaceById(event.place).type.getLocalizedNameId()) + "] Evento " + event.title);
        bottom.setText(event.shortDescription);

        return projectView;
    }
}