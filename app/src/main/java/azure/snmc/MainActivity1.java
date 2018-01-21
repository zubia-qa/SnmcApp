package azure.snmc;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity1 extends android.support.v4.app.FragmentActivity   {// extends android.support.v4.app.FragmentActivity or AppCompatActivity


    private WebView mywebView;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;
    int check=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initUI();
    }


    private void initUI() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);



//        viewPager.setAdapter(new PagerAdapter() {
//            @Override
//            public int getCount() {
//                return 5;
//            }
//
//            @Override
//            public boolean isViewFromObject(final View view, final Object object) {
//                return view.equals(object);
//            }
//
//            @Override
//            public void destroyItem(final View container, final int position, final Object object) {
//                ((ViewPager) container).removeView((View) object);
//            }
//
//            @Override
//            public Object instantiateItem(final ViewGroup container, final int position) {
//                final View view = LayoutInflater.from(
//                        getBaseContext()).inflate(R.layout.fragment_prayer, null, false);
//
//                final TextView txtPage = (TextView) view.findViewById(R.id.textHour);
//                txtPage.setText(String.format("Page #%d", position));
//
//                container.addView(view);
//                return view;
//            }
//        });


        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_add_calendar),
                        Color.parseColor(colors[0]))
                        .title("Prayer")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_add_calendar),
                        Color.parseColor(colors[1]))
                        .title("Events")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_add_calendar),
                        Color.parseColor(colors[2]))//.                        badgeTitle("123")
                        .title("Donate")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_add_calendar),
                        Color.parseColor(colors[3]))
                        .title("Youth")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_add_calendar),
                        Color.parseColor(colors[4]))
                        .title("Services")
                        .build()
        );

        navigationTabBar.setBgColor(Color.parseColor("#424242"));
        navigationTabBar.setActiveColor(Color.parseColor("#ffffff"));
        navigationTabBar.setInactiveColor(Color.parseColor("#9E9E9E"));
        navigationTabBar.setModels(models);

        TabPrayer tab1 = new TabPrayer();
        TabEvent tab2 = new TabEvent();
        TabDonate tab3 = new TabDonate();
        TabYouth tab4 = new TabYouth();
        TabServices tab5 = new TabServices();
        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(tab1);
        fragmentList.add(tab2);
        fragmentList.add(tab3);
        fragmentList.add(tab4);
        fragmentList.add(tab5);
        DashPagerActivity fragmentPageAdapter =new DashPagerActivity(getSupportFragmentManager(),fragmentList);
        viewPager.setAdapter(fragmentPageAdapter);
        navigationTabBar.setViewPager(viewPager, 0);


        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });


        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
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
}

 class DashPagerActivity  extends FragmentPagerAdapter {
     private List<Fragment> fragmentList;

     public DashPagerActivity(FragmentManager fm, List fragmentList) {
         super(fm); // TODO Auto-generated constructor stub
         this.fragmentList = fragmentList;
     }

     @Override
     public Fragment getItem(int arg0) {
         // TODO Auto-generated constructor stub
         return fragmentList.get(arg0);
     }

     @Override
     public int getCount() {
         return fragmentList.size();
     }


 }

