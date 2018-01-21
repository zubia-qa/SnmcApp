
package azure.snmc;

import java.util.Locale;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentPagerAdapter;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements ActionBar.TabListener {


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;

    private WebView mywebView;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    int check=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);



        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
//        mywebView = (WebView) findViewById(R.id.webView);


        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_prayer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
//            return PlaceholderFragment.newInstance(position + 1);

//            if (position == 0){ // if the position is 0 we are returning the First tab
//            else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
//            {
//                TabEvent tab2 = new TabEvent();
//                return tab2;
//            }

            switch (position) {

                case 0:
                    TabPrayer tab1 = new TabPrayer();
                    return tab1;
                case 1:
                    TabEvent tab2 = new TabEvent();
                    return tab2;
                case 2:
                    TabDonate tab3 = new TabDonate();
                    return tab3;
                case 3:
                    TabYouth tab4 = new TabYouth();
                    return tab4;
                case 4:
                    TabServices tab5 = new TabServices();//TODO add for new tab
                    return tab5;

            }
            return null;
        }

//            return MainActivity.PlaceholderFragment.newInstance(position + 1);
//            switch(position){
//                case 0: Fragment prayer = TabPrayer.newInstance(position);
//                    return prayer;
//
//            }
//            return null;


        @Override
        public int getCount() {
            // Show 5 total pages.
            return 5;//// TODO: 2018-01-11 when add more tabs and add name to strings value
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_section1).toUpperCase(l);
                case 1:
                    return getString(R.string.title_section2).toUpperCase(l);
                case 2:
                    return getString(R.string.title_section3).toUpperCase(l);
                case 3:
                    return getString(R.string.title_section4).toUpperCase(l);//// TODO: 2018-01-11 add new tabs name here
                case 4:
                    return getString(R.string.title_section5).toUpperCase(l);
            }
            return null;
        }
    }

//    @Override
//    public void onBackPressed() {
//        switch (check) {
//            case 0:
//                finish();
//                break;
//            case 1:
//                TabEvent.onBackPressed();
//                break;
//            case 2:
//                TabDonate.onBackPressed();
//                break;
//            case 3:
//                TabYouth.onBackPressed();
//                break;
//            case 4:
//                TabServices.onBackPressed();
//                break;
//        }
//    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.activity_main, container, false);
            return rootView;
        }


    }


//    @Override
//    public void onBackPressed() {
//
//        if (getFragmentManager().getBackStackEntryCount() > 1){
//            getFragmentManager().popBackStackImmediate();
//            getFragmentManager().beginTransaction().commit();
//        } else {
//            //handle finish
//            finish(); // Closes app
//        }
//    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0 ){
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

}



//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//
//                // back button is pressed.. Do your stuff here
//                if (mywebView != null) {
//                    if (mywebView.canGoBack()) {
//                        mywebView.goBack();
//                    } else {
//                       finish();
//                    }
//                }
//                return true;
//        }
//        return false;
//    }

//    @Override
//    public void onBackPressed() {
//
//        Intent start = new Intent(MainActivity.this,MainActivity.class);
//        startActivity(start);
//        finishActivity(0);
//    }


//    @Override
//    public void onBackPressed() {
//            if (mywebView.canGoBack()) {
//                mywebView.goBack();
//            } else {
//                super.onBackPressed();
//            }
//        }

