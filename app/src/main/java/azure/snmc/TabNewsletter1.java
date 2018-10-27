package azure.snmc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.select.Elements;

public class TabNewsletter1 extends Fragment {
    private WebView mywebView = null;
    private ProgressBar progressBarDonate;
    private TextView tvDonateProgressWait;

    String primeDiv="td-container";//td-ss-main-content
    Elements ele;
    String[] idsToHide = { "td-ss-main-sidebar", "td-header-top-menu-full" };

//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_donate, container, false);
////        ((MainActivity1) getActivity()).setOnBackPressedListener(this);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_newletter, container, false);
        mywebView = v.findViewById(R.id.webView);
        mywebView.setVisibility(View.INVISIBLE);

        progressBarDonate = v.findViewById(R.id.progressBarDonate);
        progressBarDonate.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        progressBarDonate.setVisibility(View.VISIBLE);

        tvDonateProgressWait= v.findViewById(R.id.tvDonateProgressWait);
        tvDonateProgressWait.setVisibility(View.VISIBLE);

        TextView tvDonateNet = v.findViewById(R.id.tvDonateNoNet);
        tvDonateNet.setVisibility(View.GONE);//
        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();//fixme may produce java.lang.Null
        if (ni == null || (! ni.isConnected())){/*if no net*/
            progressBarDonate.setVisibility(View.INVISIBLE);
            tvDonateProgressWait.setVisibility(View.INVISIBLE);
            tvDonateNet.setVisibility(View.VISIBLE);
            }
else {


            mywebView.getSettings().setJavaScriptEnabled(true);
            mywebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
//
                    mywebView.loadUrl("javascript:(function() { " +
                            "try{document.getElementsByClassName('td-footer-bottom-full')[0].style.display='none';}catch(e){}" +//ending stuff like location etc
                            "try{document.getElementsByClassName('td-header-top-menu-full')[0].style.display='none';}catch(e){}" +//top part
                            "try{document.getElementsByClassName('td-ss-main-sidebar')[0].style.display='none';}catch(e){}" +//top part
                            "})()");

                    mywebView.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");////to make it visible later - step 1
                         }
            });


//                    super.onPageFinished(view, url);
//                    //run 'disableSection' for all divs to hide/disable
//                    for (String s : idsToHide) {
//                        String surveyId = s;
//                        mywebView.loadUrl("javascript:disableSection('" + surveyId + "');");
//                    }
//                }
//            });


            mywebView.addJavascriptInterface(new myJavaScriptInterface(), "CallToAnAndroidFunction");//to make it visible later - step 2
            // Enable Javascript
            WebSettings webSettings = mywebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            mywebView.loadUrl("http://newsletter.snmc.ca/");
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
                    progressBarDonate.setVisibility(View.INVISIBLE);
                    tvDonateProgressWait.setVisibility(View.INVISIBLE);
                }
            });
        }

    }}








//        private class  LoadData extends AsyncTask<Void, Void, WebView> {
//            String html=new String();
//            Document doc = null;
//            @Override
//            protected WebView doInBackground(Void... params) {
//
//                try {
//
//                    doc = Jsoup.connect("http://newsletter.snmc.ca/").timeout(50000).get();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                 ele = doc.select("div.td-ss-main-content");
//                return mywebView;
//            }
//            @Override
//            protected void onPostExecute(WebView result) {
//
//                super.onPostExecute(result);
//                mywebView.loadData(ele.toString(), "text/html", "utf-8");
//                mywebView.setVisibility(View.VISIBLE);
//                progressBarDonate.setVisibility(View.INVISIBLE);
//                tvDonateProgressWait.setVisibility(View.INVISIBLE);
//            }















//hide some elements from webpage

//            mywebView.getSettings().setJavaScriptEnabled(true);
//            mywebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
//            mywebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//            mywebView.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    mywebView.loadUrl("javascript:(function() { " +
//                            "try{document.getElementById('td-footer-bottom-full').style.display='none';}catch(e){}" +//ending stuff like location etc
//                            "try{document.getElementsByClassName('td-header-top-menu-full').style.display='none';}catch(e){}" +//top part
//                            "try{document.getElementsByClassName('td-ss-main-sidebar').style.display='none';}catch(e){}" +//top part
//                            "try{document.getElementsByClassName('td-container').style.display='none';}catch(e){}" +//top part
//                            "})()");
//
//                    mywebView.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");////to make it visible later - step 1
//
//                }
//            });
//
//            mywebView.addJavascriptInterface(new myJavaScriptInterface(), "CallToAnAndroidFunction");//to make it visible later - step 2
//            // Enable Javascript
//            WebSettings webSettings = mywebView.getSettings();
//            webSettings.setJavaScriptEnabled(true);
//
////                    super.onPageFinished(view, url);
////                    //run 'disableSection' for all divs to hide/disable
////                    for (String s : idsToHide) {
////                        String surveyId = s;
////                        mywebView.loadUrl("javascript:disableSection('" + surveyId + "');");
////                    }
////                }
////            });
//
//            mywebView.loadUrl("http://newsletter.snmc.ca/");
////        Force links and redirects to open in the WebView instead of in a browser
////        mywebView.setWebViewClient(new WebViewClient());//already being called to remove elements
//        }
//        return v;
//    }
//}

