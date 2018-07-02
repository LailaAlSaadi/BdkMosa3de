package papers.bdkmosa3de.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.springframework.util.ObjectUtils;

import papers.bdkmosa3de.R;

@EActivity(R.layout.activity_splash)
public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;

    @ViewById(R.id.needHelpAr)
    TextView needHelp;

    @AfterViews
    public void preFont() {
        needHelp.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/ar_font.ttf"));
    }

    @AfterViews
    public void startSplash() {
        new Handler().postDelayed(() -> {


            SharedPreferences sharedpreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);
            String uid = sharedpreferences.getString("uid", null);


            if (uid == null) {
                Intent i = new Intent(SplashScreen.this, LoginActivity_.class);
                startActivity(i);
            }
            else {
                Intent i = new Intent(SplashScreen.this, MainActivity_.class);
                startActivity(i);

            }

            finish();
        }, SPLASH_TIME_OUT);
    }

}