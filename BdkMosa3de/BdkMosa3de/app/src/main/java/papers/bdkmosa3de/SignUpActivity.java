package papers.bdkmosa3de;

import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.firebase.database.Transaction;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_signup)
public class SignUpActivity extends AppCompatActivity {

    ItemDao itemDao = new ItemDao();


    @ViewById(R.id.userName)
    TextView userNameText;

    @ViewById(R.id.userEmail)
    TextView userEmailText;

    @ViewById(R.id.userPassword)
    TextView userPasswordText;

    @ViewById(R.id.userCPassword)
    TextView userCPasswordText;

    @ViewById(R.id.userPhoneNumber)
    TextView userPhoneNumberText;

//    @ViewById(R.id.birthdateIn)
//    DatePicker birthdateIn;


    @ViewById(R.id.femal_maleRadio)
    RadioGroup genderRdGroup;

    @ViewById(R.id.femaleRadio)
    RadioButton femaleRd;
    private String genderValue;

    @AfterViews
    public void hideKeyboard() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Click
    public void signupNow() {
        User user = new User();
        user.setEmail(String.valueOf(userEmailText.getText()));
        user.setGender(genderValue);
        user.setPassword(String.valueOf(userPasswordText.getText()));
        user.setPhoneNumber(String.valueOf(userPhoneNumberText.getText()));
        user.setUsername(String.valueOf(userNameText.getText()));
//        user.setBirthdate(getBirthdate());
        itemDao.addEntry(user);
    }


//    public String getBirthdate() {
//        return birthdateIn.getDayOfMonth() + "-" + (birthdateIn.getMonth() + 1) + "-" + birthdateIn.getYear();
//    }
//
    @AfterViews
    public void genderRadio() {
        genderRdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(selectedId);
                genderValue = radioButton.getText().equals("Female") ? "F" : "M";
            }
        });
    }

//    @AfterViews
//    public void checkDup() {
//    }
}
