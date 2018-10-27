package azure.snmc;

//import android.app.Fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

public class TabYouth extends Fragment {

    private final String address = "http://www.snmc.ca/youth/";
    private ViewGroup insertPoint;
    private FrameLayout netEvent;
    public TabYouth() {
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
                return inflater.inflate(R.layout.fragment_event_expand, container, false);
    }

    public void onStart() {
        super.onStart();

//        netEvent = getActivity().findViewById(R.id.llayout);
//        netEvent.setVisibility(View.INVISIBLE);

        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;//may fix ni giving sometimes null
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || (! ni.isConnected())){
            netEvent.setVisibility(View.VISIBLE);//Displays message of no internet
        }
        else
            new PostTask().execute();
    }

    public static void onBackPressed() {
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

                Document doc1 = Jsoup.connect(address).timeout(10000).get();

//                Elements elements = doc1.select(".block_content h3");//returns 8 elements
//                 for (Iterator<Element> iterator = elements.iterator(); iterator.hasNext(); ) {
//                     Element element = iterator.next();
//                     for (Element strong : element.select("strong")) {
//                         description = strong.toString();
//                         String myArray[] = {description};
//                         arrayList.add(myArray);

                for (Element element : doc1.select(".block_content")) {
                    for (Element hTags : element.select("h3")) {
                                Elements h3Tags = hTags.select("strong");
                                description =  h3Tags.text();

                    String myArray[] = {description};
                    arrayList.add(myArray);
                     }}
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if (arrayList != null && (getActivity() != null)) {//?
                Collections.reverse(arrayList);

                for (int i = 0; i < arrayList.size(); i++) {

                    LayoutInflater vi = getActivity().getLayoutInflater();//todo null object - layoutinflater when rotated quickly
                    LinearLayout child = (LinearLayout) vi.inflate(R.layout.event_view, null);

                    TextView textView = child.findViewById(R.id.titletv);

//                  addLayout(title, description, link, content);
                    String[] strArray = (String[])arrayList.get(i);
                    textView.setText(strArray[0]);
                    textView.setTextColor(0xff2a949e);

                    insertPoint.addView(child, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
                }
            } else {
                netEvent.setVisibility(View.VISIBLE);
            }
        }

    }
    
}