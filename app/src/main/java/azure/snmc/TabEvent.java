package azure.snmc;

//import android.app.Fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.safety.Whitelist;
import org.jsoup.select.Elements;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

public class TabEvent extends Fragment {

    String address = "http://www.snmc.ca/feed";
    String address1 = "http://www.snmc.ca/snmc-events-4/";
    private TextView textView, textView2, textView3;
    ViewGroup insertPoint;
    LinearLayout child;
    FrameLayout netEvent;
    ImageButton btnCalendar;
    public TabEvent() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_event, container, false);
    }

    public void onStart() {
        super.onStart();
        //// TODO: 2018-01-11 add progress bar 
        insertPoint = (ViewGroup) getActivity().findViewById(R.id.eventll);

        netEvent = (FrameLayout) getActivity().findViewById(R.id.flEvent);
        netEvent.setVisibility(View.INVISIBLE);

        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || (! ni.isConnected())){
            netEvent.setVisibility(View.VISIBLE);//Displays message of no internet
            //Todo : if connect to internet should refresh
        }
        else
            new PostTask().execute();
    }

    public static void onBackPressed() {//TODO
    }


    private class PostTask extends AsyncTask<String, Void, String> {

        private String title = "";
        private String description = "";
        private String content = "";
        private String link = "";
        ArrayList arrayList;

        @Override
        protected String doInBackground(String... params) {

            try {
                arrayList = new ArrayList();

                Document doc = Jsoup.connect(address).timeout(10000).get();
//                doc.outputSettings(new Document.OutputSettings().prettyPrint(false));//makes html() preserve linebreaks and spacing
//                doc.select("p").prepend("\\n");
//                result1= doc1.select("[class=recent_posts]").first().text().replaceAll("\\\\n", "\n");

                for (Element item : doc.select("item")) {

//                    if (item.getTitle().equals("tag which you want")){item.add(feedItem}//Todo for sorting

                    title = item.select("title").first().text();//TODO final String link etc
                    description = item.select("description").first().text()+"...";
                    link = item.select("link").first().text();
                    content = item.select("content|encoded").first().text();
                    content = content.replaceAll("<p>", "\n\n").replaceAll("<[^>]*>", "").replaceAll("&#8217;",                 "");//last one for inshallah

                    String myArray[] = {title, description, link, content};


/* TODO FOR RSS FEED DATE for organizing and adding
                    Calendar c = Calendar.getInstance();
                    Date today = c.getTime();

                    String rssDateString = "02/28/2019";
                    SimpleDateFormat yourDateFormat = new SimpleDateFormat("mm/dd/yyyy");

                    Date rssDate = yourDateFormat.parse(rssDateString);
                    if (rssDate.after(today)) {System.out.println("After");}
                    else{System.out.println("Before");}
                    */
                    arrayList.add(myArray);
                }
            } catch (Exception e) {

                String result = "Sorry, something went wrong. Please, send feedback";
                return result;
            }

            try {
                Document doc1 = Jsoup.connect(address1).timeout(10000).get();
                        for (Element table : doc1.select("div.block_content")) {//// TODO: 2018-01-11 not for only one block content
                            for (Element row : table.select("tr")) {
                                Elements tds = row.select("td");
                                title = tds.get(1).text();
                                description =  tds.get(0).text();
                                link =  tds.get(2).text();
                                content =  tds.get(2).text();

                    String myArray[] = {title, description, link, content};
                    arrayList.add(myArray);
                         }
                        }
            } catch (Exception e) {
                String result = "Sorry, something went wrong. Please, send feedback";
                return result;
            }
            return null;//?// TODO: 2018-01-11
        }

        @Override
        protected void onPostExecute(String result) {
        insertPoint.removeAllViews();//TODO added to avoid duplicates
            if (arrayList != null && (getActivity() != null)) {//?
                Collections.reverse(arrayList);

                for (int i = 0; i < arrayList.size(); i++) {

                    LayoutInflater vi = getActivity().getLayoutInflater();//todo null object - layoutinflater when rotated quickly
                    child = (LinearLayout) vi.inflate(R.layout.event_view, null);

                    textView = (TextView) child.findViewById(R.id.textview1);
                    textView2 = (TextView) child.findViewById(R.id.textview2);
                    textView3 = (TextView) child.findViewById(R.id.textview3);
//                    textView4 = (TextView) child.findViewById(R.id.textview4);
                    btnCalendar=(ImageButton) child.findViewById(R.id.btnCal);

//                  addLayout(title, description, link, content);
                    final String[] strArray;
                    strArray = (String[])arrayList.get(i);
                    textView.setText(strArray[0]);
                    textView.setTextColor(0xff2a949e);
                    textView2.setText(strArray[1]);
                    textView2.setTextColor(0xff2a949e);
                    textView3.setText(strArray[2]);
                    textView3.setTextColor(0xff2a949e);
//                    textView4.setText(strArray[3]);
                    btnCalendar.setOnClickListener(new View.OnClickListener() {

                        public void onClick(View v) {
                            Calendar cal = Calendar.getInstance();
                            if (Build.VERSION.SDK_INT >= 14) {

                                Intent intent = new Intent(Intent.ACTION_INSERT)
                                        .setData(CalendarContract.Events.CONTENT_URI)
                                        .putExtra(CalendarContract.Events.ALL_DAY, false)
                                        .putExtra(CalendarContract.Events.RRULE, "")//"FREQ=WEEKLY","FREQ=WEEKLY;BYDAY=SU","FREQ=MONTHLY;BYSETPOS=2;BYDAY=SU"
                                        .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTimeInMillis())
                                        .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTimeInMillis() + 60 * 60 * 1000)
                                        .putExtra(CalendarContract.Events.TITLE, strArray[0])
                                        .putExtra(CalendarContract.Events.DESCRIPTION, strArray[1])
                                        .putExtra(CalendarContract.Events.EVENT_LOCATION, "SNMC Mosque, 3020 Woodroffe Avenue");
                                startActivity(intent);
                            }

                            else {
                                Intent intent = new Intent(Intent.ACTION_EDIT);
                                intent.setType("vnd.android.cursor.item/event");
                                intent.putExtra("beginTime", cal.getTimeInMillis());
                                intent.putExtra("allDay", false);
                                intent.putExtra("rrule", "FREQ=WEEKLY");
                                intent.putExtra("endTime", cal.getTimeInMillis()+60*60*1000);
                                intent.putExtra("title", strArray[0]);
                                intent.putExtra("description", strArray[1]);
                                intent.putExtra("eventLocation", "SNMC Mosque");
                                startActivity(intent);
                            }
                        }
                    });

//                  insert into main view (or instead of just // insertPoint.addView(child);)
                    insertPoint.addView(child, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                }//TODO adds multiple times if wait long enough
            } else {
                netEvent.setVisibility(View.VISIBLE);//// FIXME: 2018-01-11 
            }
        }

    }
    
}