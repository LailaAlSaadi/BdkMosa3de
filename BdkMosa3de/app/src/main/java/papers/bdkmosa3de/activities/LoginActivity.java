package papers.bdkmosa3de.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.List;

import papers.bdkmosa3de.R;
import papers.bdkmosa3de.common.ItemDao;
import papers.bdkmosa3de.model.User;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {

    @ViewById(R.id.userEmail)
    TextView userEmailText;

    @ViewById(R.id.userPassword)
    TextView userPasswordText;

    ItemDao itemDao = new ItemDao();
    List<User> userList;

    @AfterViews
    public void hideKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        userList = itemDao.getUsers();

//        User user = new User();
//        user.setEmail("email");
//        user.setGender("F");
//        user.setPassword("password");
//        user.setPhoneNumber("123");
//        user.setUsername("username");
//        user.setUid("0");
//        itemDao.addEntry(user);
    }

    @Click
    public void signUpNow() {
        Intent i = new Intent(LoginActivity.this, SignUpActivity_.class);
        startActivity(i);
    }

    @Click
    public void loginNow() {
        String email = userEmailText.getText().toString();
        String password = userPasswordText.getText().toString();
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Invalid email or password", Toast.LENGTH_SHORT).show();
            return;
        }
        String uid;
        for (User user : userList) {
            if (email.equals(user.email) && password.equals(user.password)) {
                uid = user.uid;

                SharedPreferences sharedpreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("uid", uid);
                editor.apply();


                Intent i = new Intent(LoginActivity.this, MainActivity_.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
                finish();
               return;
            }
        }

        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
    }


}
