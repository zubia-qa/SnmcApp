package azure.snmc;

//import android.app.Fragment;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.webkit.WebViewFragment;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Collections;

public class TabServices extends Fragment {
    private WebView mywebView;
    private ProgressBar progressBarServices;
    private TextView tvServicesProgressWait;
    FrameLayout netServices;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_services1, container, false);
        mywebView = (WebView) v.findViewById(R.id.webView);
        mywebView.setVisibility(View.INVISIBLE);

        //added this for back button


        mywebView.setFocusableInTouchMode(true);
        mywebView.requestFocus();
        mywebView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {


                //fixme
                if( keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    if (mywebView != null) {
                        if (mywebView.canGoBack()) {
                            mywebView.goBack();
                        } else {
                            getActivity().finish();
                        }
                    }
                    Toast.makeText(getActivity(), "Going to previous page", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });


        progressBarServices = (ProgressBar)v.findViewById(R.id.progressBarS);
        progressBarServices.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        progressBarServices.setVisibility(View.VISIBLE);

        tvServicesProgressWait=(TextView)v.findViewById(R.id.tvSProgressWait);
        tvServicesProgressWait.setVisibility(View.VISIBLE);

        netServices = (FrameLayout) v.findViewById(R.id.flServices);
        netServices.setVisibility(View.INVISIBLE);//

        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || (! ni.isConnected())){
            netServices.setVisibility(View.VISIBLE);//Displays message of no internet
            progressBarServices.setVisibility(View.INVISIBLE);
            tvServicesProgressWait.setVisibility(View.INVISIBLE);
        }
        else {
//hide some elements from webpage
            mywebView.getSettings().setJavaScriptEnabled(true);
            mywebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mywebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mywebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url)
                // hide element by class name // webview.loadUrl("javascript:(function() { " +
                //       "document.getElementsByClassName('your_class_name')[0].style.display='none'; })()");
                // hide element by id //webview.loadUrl("javascript:(function() { " +
                //      "document.getElementById('your_id')[0].style.display='none';})()");
                {
                    mywebView.loadUrl("javascript:(function() { " +

                            "try{document.getElementById('header').style.display='none';}catch(e){}" +//top part
                            "try{document.getElementById('footer').style.display='none';}catch(e){}" +//ending stuff like location etc
                            "})()");
                    mywebView.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");////to make it visible later - step 1
                }
            });

            mywebView.addJavascriptInterface(new TabServices.myJavaScriptInterface(), "CallToAnAndroidFunction");//to make it visible later - step 2

            // Enable Javascript
            WebSettings webSettings = mywebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            mywebView.loadUrl("http://www.snmc.ca/services/");
//        Force links and redirects to open in the WebView instead of in a browser
//        mywebView.setWebViewClient(new WebViewClient());//already being called to remove elements
        }
        return v;
    }

    public class myJavaScriptInterface {
        @JavascriptInterface
        public void setVisible() {
            getActivity().runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mywebView.setVisibility(View.VISIBLE);
                    progressBarServices.setVisibility(View.INVISIBLE);
                    tvServicesProgressWait.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

//        public static void onBackPressed() {
//        if (mywebView.canGoBack()) {
//                mywebView.goBack();
//            } else {
//                super.getActivity().onBackPressed();
//            }
//        }


//        public boolean canGoBack() {
//            return mywebView.canGoBack();
////        return this.mywebView != null && this.mywebView.canGoBack();
//        }
//
//
//        public void goBack() {
////        if (this.mywebView != null) {
////            this.mywebView.goBack();
////        }
//            mywebView.goBack();
//        }

}








// String address = "http://www.snmc.ca/services/";
//private TextView textView, textView2, textView3, textView4;
//ViewGroup insertPoint;
//LinearLayout child;
//FrameLayout netEvent;
//public TabServices() {
//}
//
//public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//return inflater.inflate(R.layout.fragment_services, container, false);//todo
//}
//
//public void onStart() {
//super.onStart();
//insertPoint = (ViewGroup) getActivity().findViewById(R.id.flServices);
//
//netEvent = (FrameLayout) getActivity().findViewById(R.id.flServices);
//netEvent.setVisibility(View.INVISIBLE);
//
//ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
//NetworkInfo ni = cm.getActiveNetworkInfo();
//if (ni == null || (! ni.isConnected())){
//netEvent.setVisibility(View.VISIBLE);//Displays message of no internet
////Todo : if connect to internet should refresh
//}
//else
//new PostTask().execute();
//}
//
//private class PostTask extends AsyncTask<String, Void, String> {
//
//private String title = "";
//private String description = "";
//private String content = "";
//private String link = "";
//ArrayList arrayList;
//
//@Override
//protected String doInBackground(String... params) {
//
//try {
//arrayList = new ArrayList();
//
//Document doc1 = Jsoup.connect(address).timeout(10000).get();
//
////                Elements elements = doc1.select(".block_content h3");//returns 8 elements
////                 for (Iterator<Element> iterator = elements.iterator(); iterator.hasNext(); ) {
////                     Element element = iterator.next();
////                     for (Element strong : element.select("strong")) {
////                         description = strong.toString();
////                         String myArray[] = {description};
////                         arrayList.add(myArray);//fixme null pointer reference
//
//for (Element element : doc1.select(".block_content")) {//// TODO: 2018-01-11 not for only one block content
//for (Element hTags : element.select("h3")) {
//Elements h3Tags = hTags.select("strong");
//description =  h3Tags.text();
//
//String myArray[] = {description};
//arrayList.add(myArray);
//}}
//} catch (Exception e) {
//
//String result = "Sorry, something went wrong. Please, send feedback";
//return result;
//}
//return null;//?// TODO: 2018-01-11
//}
//
//@Override
//protected void onPostExecute(String result) {
//
//if (arrayList != null && (getActivity() != null)) {//?
//Collections.reverse(arrayList);
//
//for (int i = 0; i < arrayList.size(); i++) {
//
//LayoutInflater vi = getActivity().getLayoutInflater();//todo null object - layoutinflater when rotated quickly
//child = (LinearLayout) vi.inflate(R.layout.event_view, null);
//
//textView = (TextView) child.findViewById(R.id.textview1);
//
////                  addLayout(title, description, link, content);
//String[] strArray = (String[])arrayList.get(i);
//textView.setText(strArray[0]);
//textView.setTextColor(0xff2a949e);
//insertPoint.addView(child, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT));
//}
//} else {
//netEvent.setVisibility(View.VISIBLE);
//}
//}
//}
//}
