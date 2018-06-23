package papers.bdkmosa3de;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_splash)
public class SplashScreen extends AppCompatActivity {

    @ViewById (R.id.needHelpAr)
    TextView needHelp;


    private static int SPLASH_TIME_OUT = 3000;

    @AfterViews
    public void preFont(){
        needHelp.setTypeface( Typeface.createFromAsset(getAssets(), "fonts/ar_font.ttf"));
    }
    @AfterViews
    public void startSplash() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, LoginActivity_.class);
                startActivity(i);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

}