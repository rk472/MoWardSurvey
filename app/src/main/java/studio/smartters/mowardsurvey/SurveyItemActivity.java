package studio.smartters.mowardsurvey;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import studio.smartters.mowardsurvey.adapter.SurveyAdapter;

public class SurveyItemActivity extends AppCompatActivity {
    private int number;
    private RecyclerView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_item);
        String jsonData = getIntent().getExtras().getString("json_data");
        Log.i("MyData",jsonData);
        try {
            JSONObject json=new JSONObject(jsonData);
            number=Integer.parseInt(json.getString("member_no"));
            list=findViewById(R.id.survey_item_list);
            list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            list.setHasFixedSize(true);
            SurveyAdapter a=new SurveyAdapter(this,number);
            list.setAdapter(a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.save_survey, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            Toast.makeText(this, "Save Clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}
