package studio.smartters.mowardsurvey.ViewHolder;

import android.app.DatePickerDialog;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;

import studio.smartters.mowardsurvey.R;

public class ServeyViewHolder extends RecyclerView.ViewHolder {
    private EditText nameText,numberText,votertext,adharText,relationText,serveyDOB,serveyDOM;
    private Spinner selectGender,selectBloodGroup,selectMaritialStatus;
    private View v;
    private CheckBox no_adhar,no_voter;
    private TextView positionText;
    private Button saveButton;
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
}
