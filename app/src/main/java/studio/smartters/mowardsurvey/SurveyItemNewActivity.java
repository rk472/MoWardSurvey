package studio.smartters.mowardsurvey;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.TextUtils;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;

import studio.smartters.mowardsurvey.POJO.Servey;

public class SurveyItemNewActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private static SurveyItemNewActivity inst;
    private ViewPager mViewPager;
    private int numberOfMember=0;
    private PlaceholderFragment[] fragments;
    public Servey data[];
    private ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_item_new);
        inst=this;

        String jsonData = getIntent().getExtras().getString("json_data");
        JSONObject json= null;
        try {
            json = new JSONObject(jsonData);
            numberOfMember=Integer.parseInt(json.getString("member_no"));
            data=new Servey[numberOfMember];
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fragments=new PlaceholderFragment[numberOfMember];
        for(int i=0;i<fragments.length;i++){
            fragments[i]=new PlaceholderFragment(i,numberOfMember);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setOffscreenPageLimit(numberOfMember);
        mViewPager.setAdapter(mSectionsPagerAdapter);



    }
    public static SurveyItemNewActivity getInstance(){
        return inst;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_survey_item_new, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
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
        private View v;
        private CheckBox no_adhar,no_voter;
        private TextView positionText;
        private Button saveButton;
        private SurveyItemNewActivity inst;
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
                    if(position==0) {
                        serveyDOM.setEnabled(true);
                        serveyDOM.setText("");
                    }
                    else{
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
                    Calendar c=Calendar.getInstance();
                    DatePickerDialog d=new DatePickerDialog(v.getContext(), new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            serveyDOB.setText(dayOfMonth+"/"+(month+1)+"/"+year);
                        }
                    },c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH));
                    d.show();
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
                    },c.get(Calendar.YEAR),c.get(Calendar.MONTH)+1,c.get(Calendar.DAY_OF_MONTH));
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
                    if(TextUtils.isEmpty(name) || TextUtils.isEmpty(dob) || TextUtils.isEmpty(relation) || TextUtils.isEmpty(phone) ||(maritialStatus.equals("married") && TextUtils.isEmpty(dom))){
                        Toast.makeText(inst, "You must fill all the fields", Toast.LENGTH_SHORT).show();
                    }else{
                        Servey servey=new Servey();
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
                    }
                }
            });
            return v;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return fragments[position];
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
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
            for(Servey s:data){
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
            JSONObject json=new JSONObject(getIntent().getExtras().getString("json_data"));
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
        for(Servey s:data){
            if(s==null){
                return false;
            }
        }
        return  true;
    }
    private class UploadTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            try{
                URL url=new URL(strings[0]);
                URLConnection con=url.openConnection();
                InputStream is=con.getInputStream();
                InputStreamReader ir=new InputStreamReader(is);
                int data=ir.read();
                String res="";
                while(data!=-1){
                    res+=(char)data;
                    data=ir.read();
                }
                return res;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Some unknown error occurred";
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
