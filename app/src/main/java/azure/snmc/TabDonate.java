package azure.snmc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.KeyEvent;
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

public class TabDonate extends Fragment {
    WebView mywebView = null;
    private ProgressBar progressBarDonate;
    private TextView tvDonateProgressWait;
    FrameLayout netDonate;


//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        return inflater.inflate(R.layout.fragment_donate, container, false);
////        ((MainActivity) getActivity()).setOnBackPressedListener(this);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_donate, container, false);
        mywebView = (WebView) v.findViewById(R.id.webView);
        mywebView.setVisibility(View.INVISIBLE);

        progressBarDonate = (ProgressBar)v.findViewById(R.id.progressBarDonate);
        progressBarDonate.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.MULTIPLY);

        progressBarDonate.setVisibility(View.VISIBLE);

        tvDonateProgressWait=(TextView)v.findViewById(R.id.tvDonateProgressWait);
        tvDonateProgressWait.setVisibility(View.VISIBLE);

        netDonate = (FrameLayout) v.findViewById(R.id.flDonate);
        netDonate.setVisibility(View.INVISIBLE);//

        ConnectivityManager cm = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null || (! ni.isConnected())){
            netDonate.setVisibility(View.VISIBLE);//Displays message of no internet
            progressBarDonate.setVisibility(View.INVISIBLE);
            tvDonateProgressWait.setVisibility(View.INVISIBLE);
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
//                        "document.getElementsByClassName('subscribe')[0].style.display='none'; " +
//                        "try{document.getElementById('footer_blocks').style.display='none';}catch(e){}" +
                            "try{document.getElementById('header').style.display='none';}catch(e){}" +//top part
                            "try{document.getElementById('footer').style.display='none';}catch(e){}" +//ending stuff like location etc
//                            "try{document.getElementById('widgets').style.display='none';}catch(e){}" +//tweet etc
//                            "try{document.getElementsByClassName('center_column')[0].style.display='none';}catch(e){}" +//news popular post
//                            "try{document.getElementsByClassName('left_column')[0].style.display='none';}catch(e){}" +//time for next event + main items

//                        "document.getElementsByClassName('footer_blocks')[0].style.display='none'; " +
//                        "document.getElementsByClassName('footer')[0].style.display='none'; " +
//                        "document.getElementsByClassName('latest_sermon')[0].style.display='none'; " +
//                        "document.getElementsByClassName('search')[0].style.display='none'; " +
//                        "document.getElementsByClassName('info')[0].style.display='none'; " +
//                        "document.getElementsByClassName('buttons')[0].style.display='none'; " +
//                        "document.getElementsByClassName('title')[0].style.display='none'; " +
//                        "document.getElementsByClassName('wrapper')[0].style.display='none'; " +

                            "})()");
                    mywebView.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");////to make it visible later - step 1

                }
            });

            mywebView.addJavascriptInterface(new myJavaScriptInterface(), "CallToAnAndroidFunction");//to make it visible later - step 2


            // Enable Javascript
            WebSettings webSettings = mywebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            mywebView.loadUrl("http://www.snmc.ca/donation/");
//            mywebView.loadUrl("http://www.snmc.ca/");
//        Force links and redirects to open in the WebView instead of in a browser
//        mywebView.setWebViewClient(new WebViewClient());//already being called to remove elements
        }
        return v;


    }

    public static void onBackPressed() {
//        if (mywebView.getVisibility() == View.VISIBLE) {
//            mywebView.setVisibility(View.GONE);
//        } else {
//            getActivity().finish();
//        }
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

    public boolean canGoBack() {
        return this.mywebView != null && this.mywebView.canGoBack();
    }

    public void goBack() {
        if (this.mywebView != null) {
            this.mywebView.goBack();
        }
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:

                // back button is pressed.. Do your stuff here
                if (mywebView != null) {
                    if (mywebView.canGoBack()) {
                        mywebView.goBack();
                    }
                }

                return true;
        }
        return false;
    }
}

