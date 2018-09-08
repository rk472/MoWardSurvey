package studio.smartters.mowardsurvey;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class SurveyMainActivity extends AppCompatActivity {
    private EditText etMember,etHOFName,etPin,etPhone,etAddress,etOther;
    private CheckBox cbAtala,cbUjjwala,cbSukanya,cbSurakhya,cbOther;
    private TextInputLayout otherCont;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_main);
        otherCont = findViewById(R.id.other_container);
        otherCont.setVisibility(View.INVISIBLE);
        etAddress = findViewById(R.id.family_address);
        etHOFName = findViewById(R.id.hof_name);
        etPhone = findViewById(R.id.family_phone);
        etOther = findViewById(R.id.other_schemes);
        etPin = findViewById(R.id.family_pin);
        etMember = findViewById(R.id.survey_family_member);
        cbAtala = findViewById(R.id.atala_ck);
        cbOther = findViewById(R.id.other_ck);
        cbSukanya = findViewById(R.id.sukanya_ck);
        cbUjjwala = findViewById(R.id.ujjwala_ck);
        cbSurakhya = findViewById(R.id.surakhya_ck);
        cbOther.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    otherCont.setVisibility(View.VISIBLE);
                }else{
                    otherCont.setVisibility(View.INVISIBLE);
                }
            }
        });
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mMember = etMember.getText().toString().trim();
                final String mName = etHOFName.getText().toString().trim();
                final String mPhone = etPhone.getText().toString().trim();
                final String mPin = etPin.getText().toString().trim();
                final String mAddress = etAddress.getText().toString().trim();
                final String mOther = etOther.getText().toString().trim();
                final String booth_id=getSharedPreferences("login",MODE_PRIVATE).getString("booth_id","0");
                final String uid=getSharedPreferences("login",MODE_PRIVATE).getString("id","0");
                if(!(mMember.equals("")||mName.equals("")||mAddress.equals("")||mPhone.equals("")||mPin.equals(""))) {
                    if(Integer.parseInt(mMember)<1){
                        Snackbar.make(view, "Members must be more than 0", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }else {
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("name", mName);
                            jsonObject.put("pin", mPin);
                            jsonObject.put("phone", mPhone);
                            jsonObject.put("address", mAddress);
                            jsonObject.put("member_no", mMember);
                            jsonObject.put("other_scheme_name", cbOther.isChecked() ?mOther:"");
                            jsonObject.put("atala_scheme", cbAtala.isChecked() ? 1 : 0);
                            jsonObject.put("ujjwala_scheme", cbUjjwala.isChecked() ? 1 : 0);
                            jsonObject.put("sukanya_scheme", cbSukanya.isChecked() ? 1 : 0);
                            jsonObject.put("surakhya_scheme", cbSurakhya.isChecked() ? 1 : 0);
                            jsonObject.put("other_scheme", cbOther.isChecked() ? 1 : 0);
                            jsonObject.put("booth_id",booth_id);
                            jsonObject.put("uid",uid);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        final String jsonData = jsonObject.toString();
                        new AlertDialog.Builder(SurveyMainActivity.this)
                                .setTitle("Alert")
                                .setMessage(R.string.continue_text)
                                .setPositiveButton("Yes, Sure", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Intent ii = new Intent(SurveyMainActivity.this, SurveyItemActivity.class);
                                        ii.putExtra("json_data", jsonData);
                                        startActivity(ii);
                                        finish();
                                    }
                                }).setNegativeButton("No, Don't", null).show();
                    }
                }else{
                    Snackbar.make(view, "Please Fill All Fields.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}