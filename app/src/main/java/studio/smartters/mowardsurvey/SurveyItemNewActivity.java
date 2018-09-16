package studio.smartters.mowardsurvey;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Objects;

import studio.smartters.mowardsurvey.POJO.Survey;

public class SurveyItemNewActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static SurveyItemNewActivity inst;
    private int numberOfMember=0;
    private PlaceholderFragment[] fragments;
    public Survey data[];
    private ProgressDialog p;
    private ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_item_new);
        inst=this;

        String jsonData = Objects.requireNonNull(getIntent().getExtras()).getString("json_data");
        JSONObject json;
        try {
            json = new JSONObject(jsonData);
            numberOfMember=Integer.parseInt(json.getString("member_no"));
            data=new Survey[numberOfMember];
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fragments=new PlaceholderFragment[numberOfMember];
        for(int i=0;i<fragments.length;i++){
            fragments[i]=new PlaceholderFragment(i,numberOfMember);
        }
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Enter Member Details");
        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(numberOfMember);
        mViewPager.setAdapter(mSectionsPagerAdapter);



    }
    public static SurveyItemNewActivity getInstance(){
        return inst;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_survey_item_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            if(canSave()){
                upload();
            }else{
                Toast.makeText(this, "You must save all the fields before uploading..", Toast.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    public static class PlaceholderFragment extends Fragment {
        public PlaceholderFragment(){}
        @SuppressLint("ValidFragment")
        public PlaceholderFragment(int number,int total) {
            pageNumber=number;
            totalNumber=total;
        }
        private int pageNumber,totalNumber;
        private EditText nameText,numberText,votertext,adharText,relationText,serveyDOB,serveyDOM;
        private Spinner selectGender,selectBloodGroup,selectMaritialStatus;
        private CheckBox no_adhar,no_voter;
        private TextView positionText;
        private Button saveButton;
        private SurveyItemNewActivity inst;
        @SuppressLint("SetTextI18n")
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View v = inflater.inflate(R.layout.fragment_survey_item_new, container, false);
            nameText=v.findViewById(R.id.survey_name);
            numberText=v.findViewById(R.id.survey_contact);
            votertext=v.findViewById(R.id.survey_voter);
            adharText=v.findViewById(R.id.survey_adhar);
            relationText=v.findViewById(R.id.survey_relation);
            selectGender=v.findViewById(R.id.survey_gender_select);
            selectBloodGroup=v.findViewById(R.id.survey_blood_select);
            selectMaritialStatus=v.findViewById(R.id.survey_marital_select);
            positionText=v.findViewById(R.id.position_text);
            serveyDOB=v.findViewById(R.id.survey_dob);
            serveyDOM=v.findViewById(R.id.survey_dom);
            saveButton=v.findViewById(R.id.save_btn);
            no_adhar=v.findViewById(R.id.survey_no_adhar);
            no_voter=v.findViewById(R.id.survey_no_voter);
            inst=SurveyItemNewActivity.getInstance();
            positionText.setText((pageNumber+1)+"/"+totalNumber);
            selectMaritialStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position==1) {
                        serveyDOM.setEnabled(true);
                    }
                    else{
                        serveyDOM.setText("");
                        serveyDOM.setEnabled(false);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            no_adhar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        adharText.setEnabled(false);
                    }else{
                        adharText.setEnabled(true);
                    }
                }
            });
            no_voter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        votertext.setEnabled(false);
                    }else{
                        votertext.setEnabled(true);
                    }
                }
            });
            serveyDOB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String dob=serveyDOB.getText().toString().trim();
                    int year,month,day;
                    if(TextUtils.isEmpty(dob)) {
                        Calendar c = Calendar.getInstance();
                        year=c.get(Calendar.YEAR);
                        month=c.get(Calendar.MONTH);
                        day=c.get(Calendar.DAY_OF_MONTH);
                    }else{
                         year=Integer.parseInt(dob.split("/")[2]);
                         month=Integer.parseInt(dob.split("/")[1])-1;
                         day=Integer.parseInt(dob.split("/")[0]);
                    }
                    DatePickerDialog d=new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            serveyDOB.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        }
                    },year,month,day);
                    d.show();
                }
            });
            selectGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String dob=serveyDOB.getText().toString();
                    if(!TextUtils.isEmpty(dob)){
                        int year=Integer.parseInt(dob.split("/")[2]);
                        int month=Integer.parseInt(dob.split("/")[1]);
                        int day=Integer.parseInt(dob.split("/")[0]);
                        int age=getAge(year,month-1,day);
                        if(!((age>=21 && selectGender.getSelectedItemPosition()==0) || (age>=18 && (selectGender.getSelectedItemPosition()==1 || selectGender.getSelectedItemPosition()==2)))){
                            selectMaritialStatus.setSelection(0);
                            selectMaritialStatus.setEnabled(false);
                        }else{
                            selectMaritialStatus.setEnabled(true);
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            serveyDOB.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    int year=Integer.parseInt(s.toString().split("/")[2]);
                    int month=Integer.parseInt(s.toString().split("/")[1]);
                    int day=Integer.parseInt(s.toString().split("/")[0]);
                    int age=getAge(year,month-1,day);
                    if(!((age>=21 && selectGender.getSelectedItemPosition()==0) || (age>=18 && (selectGender.getSelectedItemPosition()==1 || selectGender.getSelectedItemPosition()==2)))){
                        selectMaritialStatus.setSelection(0);
                        selectMaritialStatus.setEnabled(false);
                    }else{
                        selectMaritialStatus.setEnabled(true);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
            serveyDOM.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar c=Calendar.getInstance();
                    DatePickerDialog d=new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            serveyDOM.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        }
                    },c.get(Calendar.YEAR),c.get(Calendar.MONTH),c.get(Calendar.DAY_OF_MONTH));
                    d.show();
                }
            });
            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name=nameText.getText().toString().trim();
                    String gender=selectGender.getSelectedItem().toString();
                    String bloodGroup=selectBloodGroup.getSelectedItem().toString();
                    String maritialStatus=selectMaritialStatus.getSelectedItem().toString();
                    String dob=serveyDOB.getText().toString();
                    String dom=serveyDOM.getText().toString();
                    String adhar=adharText.getText().toString().trim();
                    String voter=votertext.getText().toString().trim();
                    String relation=relationText.getText().toString().trim();
                    String phone=numberText.getText().toString().trim();
                    Boolean noAdhar=no_adhar.isChecked();
                    Boolean noVoter=no_voter.isChecked();
                    if(TextUtils.isEmpty(name) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(relation) || TextUtils.isEmpty(phone) ){
                        Toast.makeText(inst, "You must fill all the fields", Toast.LENGTH_SHORT).show();
                    }else if(maritialStatus.equalsIgnoreCase("married") && TextUtils.isEmpty(dom)){
                        Toast.makeText(inst, "You must fill the date of marriage", Toast.LENGTH_SHORT).show();
                    }else if(phone.length()<10){
                        Toast.makeText(inst, "Invalid phone number", Toast.LENGTH_SHORT).show();
                    }else{
                        Survey servey=new Survey();
                        servey.setAdhar(adhar);
                        servey.setBloodGroup(bloodGroup);
                        servey.setDOB(dob);
                        servey.setDOM(dom);
                        servey.setGender(gender);
                        servey.setMaritialStatus(maritialStatus);
                        servey.setName(name);
                        servey.setVoter(voter);
                        servey.setRelation(relation);
                        servey.setNoAdhar(noAdhar);
                        servey.setNoVoter(noVoter);
                        servey.setPhone(phone);
                        inst.data[pageNumber]=servey;
                        saveButton.setText("update");
                        Toast.makeText(inst, "Saved Successfully", Toast.LENGTH_SHORT).show();
                        SurveyItemNewActivity.getInstance().mViewPager.setCurrentItem(SurveyItemNewActivity.getInstance().mViewPager.getCurrentItem()+1);
                    }
                }
            });
            return v;
        }
        private int getAge(int year, int month, int day){
            Calendar dob = Calendar.getInstance();
            Calendar today = Calendar.getInstance();
            dob.set(year, month, day);
            int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
            if (today.get(Calendar.MONTH) < dob.get(Calendar.MONTH)){
                if(today.get(Calendar.DAY_OF_MONTH) < dob.get(Calendar.DAY_OF_MONTH))
                    age--;
            }
            return age;
        }
        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            numberText.requestFocus();
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return numberOfMember;
        }
    }
    private void upload() {
        try {
            p=new ProgressDialog(this);
            p.setMessage("Uploading your data to database");
            p.setTitle("Wait");
            p.setCanceledOnTouchOutside(false);
            p.setCancelable(false);
            p.show();
            JSONArray arr=new JSONArray();
            for(Survey s:data){
                JSONObject json=new JSONObject();

                json.put("name",s.getName());
                json.put("gender",s.getGender());
                json.put("blood group",s.getBloodGroup());
                json.put("maritial status",s.getMaritialStatus());
                json.put("dob",s.getDOB());
                json.put("dom",s.getDOM());
                json.put("adhar",s.getAdhar());
                json.put("voter",s.getVoter());
                json.put("relation",s.getRelation());
                json.put("phone",s.getPhone());
                json.put("no adhar",s.getNoAdhar());
                json.put("no voter",s.getNoVoter());
                arr.put(json);

            }
            final JSONObject final1=new JSONObject();
            final1.put("members",arr);
            JSONObject json=new JSONObject(Objects.requireNonNull(getIntent().getExtras()).getString("json_data"));
            final1.put("family",json);

           UploadTask ut=new UploadTask();

            ut.execute("http://205.147.101.127:8084/MoWord/survey?data="+final1.toString());
            //Log.e("url","http://192.168.42.50:8084/servey?data="+URLEncoder.encode(ar.toString()));
        } catch (JSONException e) {
            e.printStackTrace();
            p.dismiss();
        }
    }

    boolean canSave(){
        for(Survey s:data){
            if(s==null){
                return false;
            }
        }
        return  true;
    }
    @SuppressLint("StaticFieldLeak")
    private class UploadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url=new URL(strings[0]);
                URLConnection con=url.openConnection();
                InputStream is=con.getInputStream();
                InputStreamReader ir=new InputStreamReader(is);
                int data=ir.read();
                StringBuilder res= new StringBuilder();
                while(data!=-1){
                    res.append((char) data);
                    data=ir.read();
                }
                return res.toString();
            } catch (IOException e) {
                return "Unable to reach server !";
            }

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            Log.e("res",s);
            Toast.makeText(SurveyItemNewActivity.this, s, Toast.LENGTH_SHORT).show();
            if(s.equals("Successfully Submitted")){
                finish();
            }
        }
    }
}
