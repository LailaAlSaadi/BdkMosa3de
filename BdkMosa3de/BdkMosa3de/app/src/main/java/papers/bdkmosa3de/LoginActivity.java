package papers.bdkmosa3de;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @AfterViews
    public void hideKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }
    @Click
    public void signUpNow(){
        Intent i = new Intent(LoginActivity.this, SignUpActivity_.class);
        startActivity(i);

    }

}
