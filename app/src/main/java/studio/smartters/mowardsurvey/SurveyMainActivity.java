package studio.smartters.mowardsurvey;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class SurveyMainActivity extends AppCompatActivity {
    private EditText etMember;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_main);

        etMember = findViewById(R.id.survey_family_member);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String mMember = etMember.getText().toString();
                if(!mMember.equals("")) {
                    new AlertDialog.Builder(SurveyMainActivity.this)
                            .setTitle("Alert")
                            .setMessage(R.string.continue_text)
                            .setPositiveButton("Yes, Sure", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent ii = new Intent(SurveyMainActivity.this,SurveyItemActivity.class);
                                    ii.putExtra("numMember",mMember);
                                    startActivity(ii);
                                    finish();
                                }
                            }).setNegativeButton("No, Don't",null).show();
                }else{
                    Snackbar.make(view, "Please Fill No of Family members.", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });
    }
}