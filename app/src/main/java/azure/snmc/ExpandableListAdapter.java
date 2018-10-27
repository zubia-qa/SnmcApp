package azure.snmc;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class ExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<EventDescription>> _listDataChild;

    public ExpandableListAdapter(Context context, List<String> listDataHeader,
                                 HashMap<String, List<EventDescription>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }


    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final EventDescription eventDescription = (EventDescription) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.event_view, null);
        }
        TextView titletv = convertView.findViewById(R.id.titletv);
        TextView descriptiontv = convertView.findViewById(R.id.descriptiontv);
        TextView linktv = convertView.findViewById(R.id.linktv);

        titletv.setText(eventDescription.GetTitle());
        descriptiontv.setText(eventDescription.GetDescription());
        linktv.setText(eventDescription.GetLink());

        manageCalendarButton(eventDescription, convertView);


        return convertView;
    }

    private void manageCalendarButton(final EventDescription eventDescription, View convertView) {
        ImageButton btnCalendar = convertView.findViewById(R.id.btnCal);
        if (eventDescription.IsEvent()) {

            btnCalendar.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
//                    Calendar cal = Calendar.getInstance();

                        Intent intent = new Intent(Intent.ACTION_INSERT)
                                .setData(CalendarContract.Events.CONTENT_URI)
                                .putExtra(CalendarContract.Events.ALL_DAY, false)
                                .putExtra(CalendarContract.Events.RRULE, eventDescription.GetRrule())//"FREQ=WEEKLY;BYDAY=SU","FREQ=MONTHLY;BYSETPOS=2;BYDAY=SU"
                                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, eventDescription.GetBegin())
                                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, eventDescription.GetEnd())
                                .putExtra(CalendarContract.Events.TITLE, eventDescription.GetTitle())
                                .putExtra(CalendarContract.Events.DESCRIPTION, eventDescription.GetDescription())
                                .putExtra(CalendarContract.Events.EVENT_LOCATION, "SNMC Mosque, 3020 Woodroffe Avenue");
                        _context.startActivity(intent);

                }
            });
        } else
            btnCalendar.setVisibility(View.INVISIBLE);
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {

        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.list_group, null);
        }

        TextView lblListHeader = convertView
                .findViewById(R.id.lblListHeader);

//        Typeface typeface = ResourcesCompat.getFont(_context, R.font.amiko_bold);
//        lblListHeader.setTypeface(typeface);
        lblListHeader.setAllCaps(true);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}