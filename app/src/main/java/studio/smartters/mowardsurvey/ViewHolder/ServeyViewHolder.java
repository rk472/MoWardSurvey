package studio.smartters.mowardsurvey.ViewHolder;

import android.app.DatePickerDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import studio.smartters.mowardsurvey.POJO.Servey;
import studio.smartters.mowardsurvey.R;
import studio.smartters.mowardsurvey.SurveyItemActivity;

public class ServeyViewHolder extends RecyclerView.ViewHolder {
    private EditText nameText,numberText,votertext,adharText,relationText,serveyDOB,serveyDOM;
    private Spinner selectGender,selectBloodGroup,selectMaritialStatus;
    private View v;
    private CheckBox no_adhar,no_voter;
    private TextView positionText;
    private Button saveButton;
    private SurveyItemActivity inst;
    public ServeyViewHolder(View itemView) {
        super(itemView);
        v=itemView;
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
        inst=SurveyItemActivity.getInstance();
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

    }

    public void setPosition(int size){
        positionText.setText((getAdapterPosition()+1)+"/"+size);
    }
    public void setClick(final int position){
        MyClickListener listener= new MyClickListener();
        listener.updatePosition(position);
        saveButton.setOnClickListener(listener);
    }
    private class MyClickListener implements View.OnClickListener {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }
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
                inst.data[getAdapterPosition()]=servey;
                saveButton.setText("update");
                Toast.makeText(inst, "Saved Successfully", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
