package azure.snmc;

//import android.app.Fragment;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.lang.Boolean.TRUE;

public class TabPrayer extends Fragment {

    private TextView fajrAtv, fajrItv, zuhrItv, asarItv, meghribItv, ishaItv, zuhrAtv, asarAtv, meghribAtv, ishaAtv, datetv, azaantv,prayertextJumamah1,prayertextJumamah2;
    private String currentDate;
    Date EndTime,CurrentTime = null;

    private TextView txtHour, txtMinute,txtNextName;
    private Handler handler;
    private Runnable runnable;
    List<String> array, arrayIqama;
    private SimpleDateFormat timeFormat;

    public TabPrayer() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_prayer, container, false);
        return view;
    }

    public static TabPrayer newInstance(String text) {

        Bundle args = new Bundle();
        args.putString("msg", text);

        TabPrayer fragment = new TabPrayer();
        fragment.setArguments(args);
        return fragment;
    }

    public void onStart() {
        super.onStart();
        SimpleDateFormat curFormater = new SimpleDateFormat("EEEE MMM dd, yyyy");
        currentDate = curFormater.format(new Date());

        datetv = (TextView) getActivity().findViewById(R.id.datetv);
        fajrAtv = (TextView) getActivity().findViewById(R.id.fajrA);
        fajrItv = (TextView) getActivity().findViewById(R.id.fajrI);
        zuhrAtv = (TextView) getActivity().findViewById(R.id.zuhrA);
        zuhrItv = (TextView) getActivity().findViewById(R.id.zuhrI);
        asarAtv = (TextView) getActivity().findViewById(R.id.asarA);
        asarItv = (TextView) getActivity().findViewById(R.id.asarI);
        meghribAtv = (TextView) getActivity().findViewById(R.id.meghribA);
        meghribItv = (TextView) getActivity().findViewById(R.id.meghribI);
        ishaItv = (TextView) getActivity().findViewById(R.id.ishaI);
        ishaAtv = (TextView) getActivity().findViewById(R.id.ishaA);
        azaantv = (TextView) getActivity().findViewById(R.id.azaanHeader);
        prayertextJumamah1 = (TextView) getActivity().findViewById(R.id.jummahPrayerTV1);
        prayertextJumamah2 = (TextView) getActivity().findViewById(R.id.jummahPrayerTV2);
        prayertextJumamah1.setTextColor(Color.WHITE);
        prayertextJumamah2.setTextColor(Color.WHITE);

        txtHour = (TextView) getActivity().findViewById(R.id.textHour);
        txtMinute = (TextView) getActivity().findViewById(R.id.textMinutes);
        txtNextName = (TextView) getActivity().findViewById(R.id.tvNextPrayerName);

        timeFormat = new SimpleDateFormat("h:mm a");

        DbHelper myDbHelper = new DbHelper(getActivity().getApplicationContext());

        try {
            myDbHelper.createDataBase();
        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
            throw sqle;
        }


        array = myDbHelper.getAllPrayersList();//        Toast.makeText(getActivity(), "array=" + array, Toast.LENGTH_SHORT).show(); //help for debugging
        if (!array.isEmpty()) {//if database has data

            datetv.setText(currentDate);
            fajrAtv.setText(array.get(1));
            fajrItv.setText(array.get(2));
            zuhrAtv.setText(array.get(3));
            zuhrItv.setText(array.get(4));
            asarAtv.setText(array.get(5));
            asarItv.setText(array.get(6));
            meghribAtv.setText(array.get(7));
            meghribItv.setText(array.get(8));
            ishaAtv.setText(array.get(9));
            ishaItv.setText(array.get(10));
            prayertextJumamah1.setText("1st Prayer: " + array.get(11));
            prayertextJumamah2.setText("2nd Prayer: " + array.get(12));
//            jumaahSubstring = "WINTER: 1st-12:00 PM   2nd-1:15PM \n SUMMER: 1st-1:00PM   2nd-2:15PM";

        } else {//if database didn't fetch an array will try in posttask
            azaantv.setVisibility(View.INVISIBLE);//masking column heading as online no data for azaan todo: dont need this
        }

           nextPrayerTime();

    }


    private void nextPrayerTime() {
        boolean nextDay = false;
        arrayIqama = new ArrayList<>();//iqama times list for that day
        arrayIqama.add("12:00 AM");//todo hack way of finding time after next day starts
        arrayIqama.add(array.get(2));//Fajr
        arrayIqama.add(array.get(4));//zuhr
        arrayIqama.add(array.get(6));//Asr
        arrayIqama.add(array.get(8));//Meghrib
        arrayIqama.add(array.get(10));//Isha
        arrayIqama.add(array.get(2));//todo hack way of finding time in between isha and next fajr

        try {
            Date StartTime = null;
            CurrentTime = timeFormat.parse(timeFormat.format(new Date()));

            for (int i = 0; i < arrayIqama.size()-1; i++) {

                StartTime = timeFormat.parse(arrayIqama.get(i).toString());
                EndTime = timeFormat.parse(arrayIqama.get(i+1).toString());
                if(nextDay) {//If next day times then add day
                    StartTime = AddDays(StartTime,1);
                    EndTime = AddDays(EndTime,1);
                }
                else if(EndTime.before(StartTime)) {//If end before start then end next day
                    nextDay = true;
                    EndTime = AddDays(EndTime,1);
                }

                if (CurrentTime.after(StartTime) && CurrentTime.before(EndTime)) {
    //              todo find prayer name i0=Fajr, i1=Zuhr...etc
                    countDownStart();
                break;
               }
               else{}//todo crash if after isha
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
    private Date AddDays(Date dt, int days){
        Calendar cd =  Calendar.getInstance();
        cd.setTime(dt);
        cd.add(Calendar.DATE,1);
        return cd.getTime();
    }


    public void countDownStart() {
            handler = new Handler();
            runnable = new Runnable() {

                @Override
                public void run() {
                    handler.postDelayed(this, 1000);
                    try {

//                      if (!CurrentTime.after(EndTime)) {//fixme-not working with this condition
                        CurrentTime = timeFormat.parse(timeFormat.format(new Date()));
                        long diff = EndTime.getTime()-CurrentTime.getTime();
//                        if(diff<=0){}
//                        else {
                            long hours = diff / (60 * 60 * 1000);
                            diff -= hours * (60 * 60 * 1000);
                            long minutes = diff / (60 * 1000);
                            txtHour.setText("" + String.format("%02d", hours));
                            txtMinute.setText("" + String.format("%02d", minutes));
//                        }
//                        } else {//// FIXME: 2018-01-16  not needed, something else maybe, also need to stop any leak if any
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }//todo resume run countdown+ when time is 0 at fajr dont display 24

            };
            handler.postDelayed(runnable, 1 * 1000);
        }

}
