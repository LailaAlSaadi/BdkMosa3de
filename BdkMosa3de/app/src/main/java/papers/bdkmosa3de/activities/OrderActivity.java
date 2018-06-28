package papers.bdkmosa3de.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;
import java.util.UUID;

import papers.bdkmosa3de.R;
import papers.bdkmosa3de.common.ItemDao;
import papers.bdkmosa3de.model.Order;

import static org.springframework.util.StringUtils.isEmpty;

@EActivity(R.layout.activity_order)
public class OrderActivity extends AppCompatActivity
        implements
        View.OnClickListener {

    ItemDao itemDao = new ItemDao();

    @ViewById(R.id.areas)
    public Spinner areasSpinner;

    @ViewById(R.id.in_det)
    public EditText detailsEditText;

    @ViewById(R.id.service_title)
    TextView serviceTitle;

    TextView txtDate, txtFromTime, txtToTime, txtLocMap;
    private int mYear, mMonth, mDay, mHour, mMinute;

    Bundle savedInstanceState;

    @AfterViews
    public void saveInst() {
        if (savedInstanceState != null) {

            serviceTitle.setText(savedInstanceState.getString("setServiceType"));
            areasSpinner.setSelection(Integer.parseInt(savedInstanceState.getString("setArea")));
            txtDate.setText(savedInstanceState.getString("setDate"));
            txtFromTime.setText(savedInstanceState.getString("setFromTime"));
            txtToTime.setText(savedInstanceState.getString("setToTime"));
            detailsEditText.setText(savedInstanceState.getString("setDetails"));
        }
    }
    Dialog dialog;

    @AfterViews
    protected void pre() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        dialog = new Dialog(this);

        dialog.setContentView(R.layout.activity_maps);
        dialog.setTitle("Game Paused");

        Intent intent = getIntent();
        int key = intent.getIntExtra("key", 0);

        serviceTitle.setText(getString(MainActivity.desc[key]));

        txtDate = findViewById(R.id.in_date);
        txtFromTime = findViewById(R.id.from_time);
        txtToTime = findViewById(R.id.to_time);
        txtLocMap = findViewById(R.id.locMap);


        txtDate.setOnClickListener(this);
        txtFromTime.setOnClickListener(this);
        txtToTime.setOnClickListener(this);
        txtLocMap.setOnClickListener(this);

    }


    @Click
    public void orderNow() {
        if (!isValidInput()) return;
        Order order = new Order();
        order.setServiceType(serviceTitle.getText().toString());
        order.setArea(areasSpinner.getSelectedItem().toString());
        order.setDate(txtDate.getText().toString());
        order.setFromTime(txtFromTime.getText().toString());
        order.setToTime(txtToTime.getText().toString());

        order.setDetails(detailsEditText.getText().toString());
        order.setUid(UUID.randomUUID().toString());
        itemDao.addOrder(order);
        Toast.makeText(getApplicationContext(), getString(R.string.order_done), Toast.LENGTH_LONG).show();
        (new Handler()).postDelayed(this::finish, 1000);

    }

    private boolean isValidInput() {
        if (isEmpty(detailsEditText.getText().toString())) {
            Toast.makeText(this, getString(R.string.please_complete), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void onClick(View v) {

        if (v == txtDate) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year, monthOfYear, dayOfMonth) -> txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year), mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == txtFromTime) {

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> txtFromTime.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
            timePickerDialog.show();
        }
        if (v == txtToTime) {

            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute) -> txtToTime.setText(hourOfDay + ":" + minute), mHour, mMinute, false);
            timePickerDialog.show();
        }


        if (v == txtLocMap) {

                    dialog.show();
            }


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState = new Bundle();
        savedInstanceState.putString("setServiceType", serviceTitle.getText().toString());
        savedInstanceState.putString("setArea", areasSpinner.getSelectedItem().toString());
        savedInstanceState.putString("setDate", txtDate.getText().toString());
        savedInstanceState.putString("setFromTime", txtFromTime.getText().toString());
        savedInstanceState.putString("setToTime", txtToTime.getText().toString());
        savedInstanceState.putString("setDetails", detailsEditText.getText().toString());

        super.onSaveInstanceState(savedInstanceState);
    }
}
