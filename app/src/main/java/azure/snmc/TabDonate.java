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
    private WebView mywebView = null;
    private ProgressBar progressBarDonate;
    private TextView tvDonateProgressWait;
    FrameLayout netDonate;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_donate, container, false);
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
//hide some elements from webpage
            mywebView.getSettings().setJavaScriptEnabled(true);
            mywebView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
            mywebView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            mywebView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url)
                {
                    mywebView.loadUrl("javascript:(function() { " +
                            "try{document.getElementById('header').style.display='none';}catch(e){}" +//top part
                            "try{document.getElementById('footer').style.display='none';}catch(e){}" +//ending stuff like location
                            "})()");
                    mywebView.loadUrl("javascript: window.CallToAnAndroidFunction.setVisible()");////to make it visible later - step 1

                }
            });

            mywebView.addJavascriptInterface(new myJavaScriptInterface(), "CallToAnAndroidFunction");//to make it visible later - step 2
            // Enable Javascript
            WebSettings webSettings = mywebView.getSettings();
            webSettings.setJavaScriptEnabled(true);

            mywebView.loadUrl("http://www.snmc.ca/donation/");

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

                // back button is pressed.
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

