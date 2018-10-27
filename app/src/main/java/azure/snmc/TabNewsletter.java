package azure.snmc;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class TabNewsletter extends Fragment {
    private WebView mywebView = null;
    private ProgressBar progressBarDonate;
    private TextView tvDonateProgressWait;
    FrameLayout netDonate;
    String primeDiv = "td-container";//td-ss-main-content
    Elements ele;
    String[] idsToHide = {"td-ss-main-sidebar", "td-header-top-menu-full"};


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_newletter, container, false);
        mywebView = v.findViewById(R.id.webView);
        mywebView.setVisibility(View.INVISIBLE);

        progressBarDonate = v.findViewById(R.id.progressBarDonate);
        progressBarDonate.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);
        progressBarDonate.setVisibility(View.VISIBLE);

        tvDonateProgressWait = v.findViewById(R.id.tvDonateProgressWait);
        tvDonateProgressWait.setVisibility(View.VISIBLE);

        TextView tvDonateNet = v.findViewById(R.id.tvDonateNoNet);
        tvDonateNet.setVisibility(View.GONE);//

        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();//fixme may produce java.lang.Null
        if (ni == null || (!ni.isConnected())) {/*if no net*/
            progressBarDonate.setVisibility(View.INVISIBLE);
            tvDonateProgressWait.setVisibility(View.INVISIBLE);
            tvDonateNet.setVisibility(View.VISIBLE);
        } else {

            mywebView.getSettings().setJavaScriptEnabled(true);
            mywebView.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageFinished(WebView view, String url) {//not reachng here

                //todo tried to remove header foorter but only stopping scroll midway at the end of page
//                mywebView.loadUrl("javascript:(function() { " +
//              "try{document.getElementsByClassName('td-footer-bottom-full')[0].style.display='none';}catch(e){}" +//ending
//              "try{document.getElementsByClassName('td-header-top-menu-full')[0].style.display='none';}catch(e){}" +//top
//              "try{document.getElementsByClassName('td-ss-main-sidebar')[0].style.display='none';}catch(e){}" +//top
//                "})()");
              mywebView.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");////to make it visible later - step 1
                }
            }
            );

            mywebView.addJavascriptInterface(new TabNewsletter.myJavaScriptInterface(), "CallToAnAndroidFunction");//to make it visible later - step 2
            // Enable Javascript
//            WebSettings webSettings = mywebView.getSettings();
//            webSettings.setJavaScriptEnabled(true);

            mywebView.loadUrl("http://newsletter.snmc.ca/");
            mywebView.setVisibility(View.VISIBLE);

            progressBarDonate.setVisibility(View.INVISIBLE);
            tvDonateProgressWait.setVisibility(View.INVISIBLE);//TODO
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

    }
}
