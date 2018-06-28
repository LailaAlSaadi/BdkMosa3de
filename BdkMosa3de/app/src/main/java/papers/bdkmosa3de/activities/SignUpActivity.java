package papers.bdkmosa3de.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import papers.bdkmosa3de.R;
import papers.bdkmosa3de.common.ItemDao;
import papers.bdkmosa3de.model.User;

import static org.springframework.util.StringUtils.isEmpty;


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
        if (!isValidInput()) return;
        User user = new User();
        user.setEmail(String.valueOf(userEmailText.getText()));
        user.setGender(genderValue);
        user.setPassword(String.valueOf(userPasswordText.getText()));
        user.setPhoneNumber(String.valueOf(userPhoneNumberText.getText()));
        user.setUsername(String.valueOf(userNameText.getText()));
        user.setUid(getID());
//        user.setBirthdate(getBirthdate());
        itemDao.addEntry(user);


        SharedPreferences sharedpreferences = getSharedPreferences("sp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("uid",user.uid);
        editor.apply();

        Intent i = new Intent(SignUpActivity.this, MainActivity_.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();

    }

    private boolean isValidInput() {
        if (isEmpty(userEmailText.getText()) ||
                isEmpty(userPasswordText.getText()) ||
                isEmpty(userPhoneNumberText.getText()) ||
                isEmpty(userNameText.getText()) || isEmpty(genderValue)) {
            Toast.makeText(this, getString(R.string.please_complete), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!userCPasswordText.getText().toString().equals(userPasswordText.getText().toString())) {
            Toast.makeText(this, getString(R.string.unmatched_password), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (userPhoneNumberText.getText().length() != 10) {
            Toast.makeText(this, getString(R.string.invalid_phone), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!isValidEmail(userEmailText.getText())){
            Toast.makeText(this, getString(R.string.invalid_email), Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }


    //    public String getBirthdate() {
//        return birthdateIn.getDayOfMonth() + "-" + (birthdateIn.getMonth() + 1) + "-" + birthdateIn.getYear();
//    }
//
    private static final String EMAIL_REGEX = "^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
    private static Pattern pattern= Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
    private Matcher matcher;

    public boolean isValidEmail(CharSequence email) {
        matcher = pattern.matcher(email);
        return matcher.matches();
    }
    @AfterViews
    public void genderRadio() {
        genderRdGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                int selectedId = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = findViewById(selectedId);
                if (radioButton.getText().equals("Female")) genderValue = "F";
                else if (radioButton.getText().equals("Male")) genderValue = "M";
            }
        });
    }

    public String getID() {
        return UUID.randomUUID().toString();
    }


//    @AfterViews
//    public void checkDup() {
//    }
}
