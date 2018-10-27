package azure.snmc;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity1 extends AppCompatActivity{// extends android.support.v4.app.FragmentActivity or AppCompatActivity


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        initUI();

    }


    private void initUI() {
        final ViewPager viewPager = findViewById(R.id.vp_horizontal_ntb);
//        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_vertical_ntb);



        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 5;
            }//todo tabs

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                final View view = LayoutInflater.from(
                        getBaseContext()).inflate(R.layout.fragment_prayer, null, false);

                final TextView txtPage = view.findViewById(R.id.textHour);
                txtPage.setText(String.format("Page #%d", position));

                container.addView(view);
                return view;
            }
        });


        final String[] colors = getResources().getStringArray(R.array.default_preview);
        final NavigationTabBar navigationTabBar = findViewById(R.id.ntb_horizontal);
//        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.vp_vertical_ntb);//todo for vertical tabs
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_prayer2),
                        Color.parseColor(colors[0]))
                        .title("Prayer")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_event1),
                        Color.parseColor(colors[1]))
                        .title("Events")
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_service1),
                        Color.parseColor(colors[4]))
                        .title("Services")
                        .build()
        );

        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_donate),
                        Color.parseColor(colors[2]))//.                        badgeTitle("123")
                        .title("Donate")
                        .build()

        );
        models.add(
        new NavigationTabBar.Model.Builder(
                getResources().getDrawable(R.drawable.ic_news3),
                Color.parseColor(colors[1]))//.                        badgeTitle("123")
                .title("Newsletter")
                .build()
        );//TODO tabs

        navigationTabBar.setBgColor(Color.parseColor("#167576"));
        navigationTabBar.setActiveColor(Color.parseColor("#ffffff"));
        navigationTabBar.setInactiveColor(Color.parseColor("#E4E4E4"));
        navigationTabBar.setModels(models);

        TabPrayer tab1 = new TabPrayer();//todo tabprayercsv or not
        TabEventExpandable tab2 = new TabEventExpandable();
        TabServiceExpandable tab5 = new TabServiceExpandable();
        TabDonate tab3 = new TabDonate();
        TabNewsletter tab4 = new TabNewsletter();//TODO tabs

        List<Fragment> fragmentList = new ArrayList<>();

        fragmentList.add(tab1);
        fragmentList.add(tab2);
        fragmentList.add(tab5);
        fragmentList.add(tab3);
        fragmentList.add(tab4);//todo extra tab

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
        if (id == R.id.action_contact) {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"info@snmc.com"});

            try {
                startActivity(Intent.createChooser(i, "Send Mail to SNMC"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(MainActivity1.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        if (id == R.id.action_rate) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=azure.snmc")));
//            Toast.makeText(MainActivity1.this, "Action clicked", Toast.LENGTH_LONG).show();
            return true;
        }

        if (id == R.id.sendFeedback) {
            Intent Email = new Intent(Intent.ACTION_SEND);
            Email.setType("text/email");
            Email.putExtra(Intent.EXTRA_EMAIL, new String[] { "feedback.osb@gmail.com" });
            Email.putExtra(Intent.EXTRA_SUBJECT, "SNMC App Feedback");
            startActivity(Intent.createChooser(Email, "Send Feedback:"));
            return true;
        }
        if (id == R.id.share) {
            try {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_SUBJECT, "SNMC App");
                String sAux = "\nLet me recommend you this application\n\n";
                sAux = sAux + "https://play.google.com/store/apps/details?id=azure.snmc \n\n";//todo put my app

                i.putExtra(Intent.EXTRA_TEXT, sAux);
                startActivity(Intent.createChooser(i, "Choose one"));
            } catch(Exception e) {
                //e.toString();
            }
            return true;
        } if (id == R.id.disclaimer) {
            showDialogMenu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void showDialogMenu(){
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.AlertDialogCustom));
        builder.setTitle("DISCLAIMER");
        builder.setMessage(R.string.disclaimer_text);
        builder.setNeutralButton("BACK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int item) {

            }
        });

builder.create();
builder.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    };
}

 class DashPagerActivity  extends FragmentPagerAdapter {
     private List<Fragment> fragmentList;

     public DashPagerActivity(FragmentManager fm, List fragmentList) {
         super(fm);
         this.fragmentList = fragmentList;
     }

     @Override
     public Fragment getItem(int arg0) {
         return fragmentList.get(arg0);
     }

     @Override
     public int getCount() {
         return fragmentList.size();
     }


 }

