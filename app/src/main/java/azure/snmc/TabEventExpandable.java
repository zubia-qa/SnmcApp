package azure.snmc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.text.DateFormat;
import java.util.Objects;


public class TabEventExpandable extends Fragment {

    private ExpandableListView expListView;//Child View
    private ArrayList eventsDescriptionList; //List of event description
    private String address = "http://www.snmc.ca/feed"; //Alternate address "http://www.snmc.ca/snmc-events-4/";
    private String testAddress = "http://192.168.0.11/?feed=rss2";
    private String address1 = "http://www.snmc.ca/snmc-events-4/";//key events
    String address2 = "http://www.snmc.ca/snmc-weekly-events/";//weekly events
    private ProgressBar progressBarEvent;
    FrameLayout netEvent;
    private ArrayList<String> categories;
    private int previousGroup = 0;
    private int lastExpandedPosition = -1;

    private TextView tvEventProgressWait;
    InputStream is;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_event_expand, container, false);
        expListView = root.findViewById(R.id.lvExp);
        ViewGroup insertPoint = root.findViewById(R.id.eventll);

        progressBarEvent = root.findViewById(R.id.progressBarE);
        progressBarEvent.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);


        tvEventProgressWait = root.findViewById(R.id.tvSProgressWait);
        tvEventProgressWait.setVisibility(View.GONE);//

        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();//fixme maygive nullpointer
        if (ni == null || (!ni.isConnected())) {//when no internet
            progressBarEvent.setVisibility(View.INVISIBLE);
            tvEventProgressWait.setVisibility(View.VISIBLE);
        }
        progressBarEvent.setVisibility(View.VISIBLE);

        try {
            is = getActivity().getAssets().open("rss1.html");//fixme error crash:java.lang.NullPointerException: Attempt to invoke interface method 'android.os.IBinder com.android.internal.app.IAppOpsCallback.asBinder()' on a null object reference at android.os.Parcel.readException - Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.res.AssetManager android.support.v4.app.FragmentActivity.getAssets()' on a null object reference
            //moving it to oncreate to fix
        } catch (IOException e) {
            Log.e("Exception", "File read failed: " + e.toString());
        }

        //Initialize list of categories
        categories = new ArrayList<>();
        categories.add("Alert");
        categories.add("Weekly Events");
        categories.add("Senior's Events");
        categories.add("Youth Events");
        categories.add("Sister's Events");
        categories.add("Key Events");
        categories.add("All");

        //Execute async task to build entity description list from RSS Feed
        new PostTask().execute();
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }


    private class PostTask extends AsyncTask<String, Void, ArrayList> {//todo static or leaks may occur


        protected ArrayList doInBackground(String... params) {
            Document doc;

            try {
                eventsDescriptionList = new ArrayList();
                try {
                    doc = Jsoup.connect(address).timeout(10000).get();//todo  testaddress or address
                } catch (Exception e) {
                    doc = null;
                }

                //Process each item and add to event description list
                if (doc != null) {
                    for (Element item : doc.select("item")) {
                        String title = item.select("title").first().text();
                        String description = item.select("description").first().text() + "...";
                        String content = item.select("content|encoded").first().text();

                        description = content.replaceAll("<p>", "\n\n").replaceAll("<[^>]*>", "").replaceAll("&#8217;", "").replaceAll("&#8230;", "").replaceAll("&nbsp;", "");//8217 for inshallah
//                    content = description.replaceAll("<p>", "\n\n").replaceAll("<[^>]*>", "").replaceAll("&#8217;", "").replaceAll("&#8230;", "");//8217 one for inshallah

                        String link = item.select("link").first().text();
                        String cat = item.select("category").first().text();

                        EventDescription eventDescription = new EventDescription(true, title, description, link, content, cat);//fixme crash nullpointer maybe jsoup
                        eventsDescriptionList.add(eventDescription);//fixme unchecked call to add (E) as ....arraylist
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            Document doc1;
            try {
                try {
                    doc1 = Jsoup.connect(address1).timeout(10000).get();
                } catch (Exception e) {
                    doc1 = null;
                }

                if (doc1 != null) {
                    for (Element table : doc1.select("div.block_content")) {
                        for (Element row : table.select("tr")) {
                            Elements tds = row.select("td");
                            String title = tds.get(1).text();
                            String description = tds.get(0).text();
                            String link = tds.get(2).text();
                            String content = "";
                            String cat = "Key Events";

                            EventDescription eventDescription = new EventDescription(true, title, description, link, content, cat);
                            eventsDescriptionList.add(eventDescription);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
//                    if (item.getTitle().equals("tag which you want")){item.add(feedItem}//Todo for sorting


            try {
                if (is != null) {
                    Document doc2;
                    try {
                        doc2 = Jsoup.parse(is, "ISO-8859-1", testAddress, Parser.xmlParser());
                    }
                    catch(Exception e){
                        doc2 = null;
                    };
                    // fixme nullpointer from above line 135 - (old comment-UTF8 doesn't give links, need to use xmlparser as not html)
                    if (doc2!=null){
                        for (Element item : doc2.select("item")) {

                            String title = item.select("title").first().text();
                            String description = item.select("description").first().text();
                            String link = item.select("link").first().text();
                            String cat = item.select("category").first().text();
                            String content = item.select("content|encoded").first().text();

                            description = content.replaceAll("<p>", "\n\n").replaceAll("<[^>]*>", "").replaceAll("&#8217;", "").replaceAll("&#8230;", "").replaceAll("&nbsp;", "");//8217 for inshallah
//                    content = content.replaceAll("<p>", "\n\n").replaceAll("<[^>]*>", "").replaceAll("&#8217;", "").replaceAll("&#8230;", "").replaceAll("&nbsp;", "");;//8217 one for inshallah

                            String today;
                            long event_begin, event_end;

                            String begin = item.select("snmc|startTime").first().text();
                            String end = item.select("snmc|endTime").first().text();

                            if (begin.isEmpty() || (begin == null))//todo whatif not empty but still not right format
                            {
                                begin = "12:00";
                                end = "13:00";
                            }
                            else if (end.isEmpty() || (end == null)) {
                                end = begin;
                            }

                            String freq = item.select("snmc|freq").first().text();
                            String step = item.select("snmc|step").first().text();
                            String day = item.select("snmc|day").first().text();
                            String date = item.select("snmc|date").first().text();
                            String rrule = "FREQ=" + freq.toUpperCase() + ";BYDAY=" + day.toUpperCase() + ";BYSETPOS=" + step;//for RRULE calendar repeat
//                        if (!date.isEmpty()){//if date available
//                            today=new SimpleDateFormat("yyyy-MM-dd").format(date);//giving error
//                            String modifiedStartDate = today + " " + begin;
//                            String modifiedEndDate = today + " " + end;
//                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//                            Date event_b = sdf.parse(modifiedStartDate);
//                            Date event_e = sdf.parse(modifiedEndDate);
//                            event_begin = event_b.getTime();
//                            event_end = event_e.getTime();
//                        }//todo
//
//                        else {//if no date string
                            Date todayDate = new Date();//gives today date
                            today = new SimpleDateFormat("yyyy-MM-dd").format(todayDate);

//                      new NextWeekDay();//TODO put next week day if day!=null and date!=null else today's date

                            String modifiedStartDate = today + " " + begin;
                            String modifiedEndDate = today + " " + end;
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            Date event_b = sdf.parse(modifiedStartDate);
                            Date event_e = sdf.parse(modifiedEndDate);
                            event_begin = event_b.getTime();
                            event_end = event_e.getTime();
//                        }

                            //todo if no time etc then it doesn't add the item , if one missing skips all rss feed
                            EventDescription eventDescription = new EventDescription(true, title, description, link, content, cat, event_begin, event_end, rrule);
                            eventsDescriptionList.add(eventDescription);
                        }
                    }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }

            finally {//for file reading//fixme finally block cannot complete normally
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
//                    Elements elements ;
//                    try {
//                        Document doc3 = Jsoup.connect(address2).timeout(10000).get();
//                          elements = doc3.select("div.block_content");{
//
//                            for (Iterator<Element> iterator = elements.iterator(); iterator.hasNext();)
//                            {
//                                Element element = iterator.next();
//                                Elements headings2 = elements.select("h2");
//
//                                String title = "";
//                                String description= "";
//                                String link= "";
//                                String content= "";
//                                String cat= "Weekly Events";
//
//                                for (Element heading2 : headings2) {
//                                     title = heading2.text();
//                                     link = "";//element.select("strong").text()
//                                     description = element.select("p ~h2 ~p").text();
//                                     content = "";
//
//                                    EventDescription eventDescription = new EventDescription(true, title, description, link, content, cat);
//                                    eventsDescriptionList.add(eventDescription);  }
//                            }
//
//                         }
//                            }
//
//                    catch (IOException e) {
//                        e.printStackTrace();
//                    }
            }
            return eventsDescriptionList;
        }

        @Override
        protected void onPostExecute(ArrayList result) {

//          tvEventProgressWait.setVisibility(View.INVISIBLE);
            List<String> listDataHeader;
            HashMap<String, List<EventDescription>> listDataChild;
            ExpandableListAdapter listAdapter;

            if (eventsDescriptionList != null && (getActivity() != null)) {//?
                listDataHeader = new ArrayList<>();
                listDataChild = new HashMap<>();
                // Adding category header and child events description
                for (String header : categories) {
                    List<EventDescription> eventDescList = new ArrayList<>();
                    for (Object objEventDescription : eventsDescriptionList) {
                        EventDescription evDesc = (EventDescription) objEventDescription;
                        if ((header.compareToIgnoreCase("All") == 0)|| evDesc.GetCategory().equals(header))
                            eventDescList.add((EventDescription) objEventDescription);
                    }

                    if (eventDescList.size() != 0) {
                        String title = header + " (" + eventDescList.size() + ")";
                        listDataHeader.add(title);
                        listDataChild.put(title, eventDescList);
                    }
                }
                progressBarEvent.setVisibility(View.INVISIBLE);

                listAdapter = new ExpandableListAdapter(getActivity(), listDataHeader, listDataChild);
                expListView.setAdapter(listAdapter);

                /*collapses other groups when expand one but*/
                expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                    @Override
                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                        Boolean shouldExpand = (!expListView.isGroupExpanded(groupPosition));
                        expListView.collapseGroup(lastExpandedPosition);

                        if (shouldExpand){
                            //generateExpandableList();
                            expListView.expandGroup(groupPosition);
                            expListView.setSelectionFromTop(groupPosition, 0);
                        }
                        lastExpandedPosition = groupPosition;
                        return true;
                    }
                });
            }
        }
    }
}
