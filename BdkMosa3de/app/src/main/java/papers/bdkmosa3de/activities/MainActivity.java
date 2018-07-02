package papers.bdkmosa3de.activities;

import android.app.Activity;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;

import com.viewpagerindicator.CirclePageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import papers.bdkmosa3de.R;
import papers.bdkmosa3de.common.ImageAdapter;
import papers.bdkmosa3de.common.ImageModel;
import papers.bdkmosa3de.common.SlidingImage_Adapter;

@EActivity(R.layout.activity_main)
public class MainActivity extends Activity implements NavigationView.OnNavigationItemSelectedListener{


    public int[] images = {R.drawable.chef
            , R.drawable.cosmetic
            , R.drawable.sink
            , R.drawable.saw
            , R.drawable.clean
            , R.drawable.socket
            , R.drawable.deed
            , R.drawable.build
            , R.drawable.delivery
            , R.drawable.design
            , R.drawable.farming
            , R.drawable.mokayf
            , R.drawable.satalite
            , R.drawable.video
            , R.drawable.events
    };


    public static int[] desc = {
            R.string.chef
            , R.string.cosmetic
            , R.string.sink
            , R.string.saw
            , R.string.clean
            , R.string.socket
            , R.string.deed
            , R.string.build
            , R.string.delivery
            , R.string.design
            , R.string.farming
            , R.string.mokayf
            , R.string.satalite
            , R.string.video
            , R.string.events
    };


    @ViewById(R.id.pager)
    public static ViewPager mPager;

    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<ImageModel> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.page1, R.drawable.page2,
            R.drawable.page3};


    @ViewById(R.id.services)
    GridView gridView;

    @AfterViews
    public void slideShow() {
        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();
        gridView.setAdapter(new ImageAdapter(this, images,desc));

    }

    private ArrayList<ImageModel> populateList() {

        ArrayList<ImageModel> list = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init() {
        mPager = findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImage_Adapter(MainActivity.this, imageModelArrayList));
        CirclePageIndicator indicator = findViewById(R.id.indicator);
        indicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        indicator.setRadius(5 * density);
        NUM_PAGES = imageModelArrayList.size();

        final Handler handler = new Handler();
        final Runnable Update = () -> {
            if (currentPage == NUM_PAGES) {
                currentPage = 0;
            }
            mPager.setCurrentItem(currentPage++, true);
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2000, 4000);

        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }


        @SuppressWarnings("StatementWithEmptyBody")
        @Override
        public boolean onNavigationItemSelected(MenuItem item) {
            // Handle navigation view item clicks here.
            int id = item.getItemId();

            if (id == R.id.nav_camera) {
                // Handle the camera action
            } else if (id == R.id.nav_gallery) {

            } else if (id == R.id.nav_slideshow) {

            } else if (id == R.id.nav_manage) {

            } else if (id == R.id.nav_share) {

            } else if (id == R.id.nav_send) {

            }

            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
        }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
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
