package studio.smartters.mowardsurvey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

public class PersonDataActivity extends AppCompatActivity {
    private EditText etName,etHead,etAddress,etRelation,etContact,etGen,etBlood,etDom,etDob,etMarriage,etAdhar,etVoter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_data);
        etName = findViewById(R.id.person_name);
        etHead = findViewById(R.id.person_head_name);
        etAddress = findViewById(R.id.person_address);
        etRelation = findViewById(R.id.person_relation);
        etContact = findViewById(R.id.person_contact);
        etGen = findViewById(R.id.person_gender);
        etBlood = findViewById(R.id.person_blood);
        etDom = findViewById(R.id.person_dom);
        etDob = findViewById(R.id.person_dob);
        etMarriage = findViewById(R.id.person_marital_status);
        etAdhar = findViewById(R.id.person_adhar);
        etVoter = findViewById(R.id.person_voter);
        String jsonData = getIntent().getExtras().getString("json_data");
        try {
            setData(new JSONObject(jsonData));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void setData(JSONObject s){
        try {
            etName.setText(s.getString("pname"));
            etHead.setText(s.getString("phead"));
            etAddress.setText(s.getString("paddress"));
            etRelation.setText(s.getString("prelation"));
            etContact.setText(s.getString("pcontact"));
            etGen.setText(s.getString("pgender"));
            etBlood.setText(s.getString("pblood"));
            etDom.setText(s.getString("pdom"));
            etDob.setText(s.getString("pdob"));
            etMarriage.setText(s.getString("pmarriage"));
            etAdhar.setText(s.getString("padhar"));
            etVoter.setText(s.getString("pvoter"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void goBack(View view) {
        finish();
    }
}
