package azure.snmc;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class TabServiceExpandable extends Fragment {

    ExpandableListView expListView;


    // --Commented out by Inspection (2018-02-07 2:44 PM):private ArrayList<Object> childItems = new ArrayList<Object>();

    private int lastExpandedPosition=-1;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_event, container, false);

        View root = inflater.inflate(R.layout.fragment_service_expand, container, false);
        expListView = root.findViewById(R.id.lvExp);

        ViewGroup insertPoint = root.findViewById(R.id.eventll);
        prepareListData();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private void prepareListData() {
        List<String> listDataHeader = new ArrayList<>();
        HashMap<String, List<EventDescription>> listDataChild = new HashMap<>();

        // Adding child data
//        listDataHeader.add("Facilities Booking");
//        listDataHeader.add("Family Counseling");
//        listDataHeader.add("Marriage Services");
//        listDataHeader.add("Sickness and Funeral Services");
//        listDataHeader.add("Sisters");
//        listDataHeader.add("Seniors");
//        listDataHeader.add("Youth Activities");
//        listDataHeader.add("SNMC Al-Huda School");
//        listDataHeader.add("Second Language Program");
//        listDataHeader.add("SNMC Scholarship");
//        listDataHeader.add("Islam 101 for  New Muslim  and Interested  Canadians ");



//        String[]  ch_facilities= {getResources().getString(R.string.rentalT),getResources().getString(R.string.rentalD),getResources().getString(R.string.rentalL)};
//        String[] ch_family = {getResources().getString(R.string.familyT), getResources().getString(R.string.familyD), getResources().getString(R.string.familyL)};
//        String[] ch_marriage = {getResources().getString(R.string.matchmakingT), getResources().getString(R.string.matchmakingD), getResources().getString(R.string.matchmakingL)};
//        String[] ch_funeral = {getResources().getString(R.string.funeralT), getResources().getString(R.string.funeralD), getResources().getString(R.string.funeralL)};
//        String[] ch_sister = {getResources().getString(R.string.sisterT), getResources().getString(R.string.sisterD), getResources().getString(R.string.sisterL)};
//        String[] ch_senior = {getResources().getString(R.string.seniorsT), getResources().getString(R.string.seniorsD), getResources().getString(R.string.seniorsL)};
//        String[] ch_youth = {getResources().getString(R.string.youthT), getResources().getString(R.string.youthD), getResources().getString(R.string.youthL)};
//        String[] ch_huda = {getResources().getString(R.string.alhudaT), getResources().getString(R.string.alhudaD), getResources().getString(R.string.alhudaL)};
//        String[] ch_language = {getResources().getString(R.string.languageT), getResources().getString(R.string.languageD), getResources().getString(R.string.languageL)};
//        String[] ch_scholarship = {getResources().getString(R.string.scholarshipT), getResources().getString(R.string.scholarshipD), getResources().getString(R.string.scholarshipL)};
//        String[] ch_convert = {getResources().getString(R.string.convertsT), getResources().getString(R.string.convertsD), getResources().getString(R.string.convertsL)};
       ArrayList<Integer> serviceNamesArray = new ArrayList<>();
        serviceNamesArray.add(R.array.facility);
        serviceNamesArray.add(R.array.funeral);
        serviceNamesArray.add(R.array.marriage);
        serviceNamesArray.add(R.array.sister);
        serviceNamesArray.add(R.array.senior);
        serviceNamesArray.add(R.array.youth);
        serviceNamesArray.add(R.array.alhuda);
        serviceNamesArray.add(R.array.language);
        serviceNamesArray.add(R.array.scholarship);
        serviceNamesArray.add(R.array.convert);

        for (int header : serviceNamesArray) {
            String[] facilityArray = getResources().getStringArray(header);
            listDataHeader.add(facilityArray[0]);
            for (String aFacilityArray : facilityArray) {
                String title = facilityArray[0];
                String description = facilityArray[1];
                String link = facilityArray[2];

                List<EventDescription> facility = new ArrayList<>();
                EventDescription evDesc = new EventDescription(false, title, description, link, "");
                listDataChild.put(facilityArray[0], facility);
                facility.add(evDesc);
            }
        }

        ExpandableListAdapter listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);

      //collapse rest when one expands
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                Boolean shouldExpand = (!expListView.isGroupExpanded(groupPosition));
                expListView.collapseGroup(lastExpandedPosition);

                if (shouldExpand){
                    //generateExpandableList();
                    expListView.expandGroup(groupPosition);
//                    expListView.setSelectionFromTop(groupPosition, 0);
                            expListView.smoothScrollToPositionFromTop(groupPosition,0,150);

                }
                lastExpandedPosition = groupPosition;
                return true;

            }
        });
    }
}