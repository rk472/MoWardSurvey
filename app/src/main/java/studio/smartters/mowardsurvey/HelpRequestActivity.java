package studio.smartters.mowardsurvey;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import studio.smartters.mowardsurvey.adapter.HelpAdapter;
import studio.smartters.mowardsurvey.others.Constants;

public class HelpRequestActivity extends AppCompatActivity {
    @SuppressLint("StaticFieldLeak")
    private static HelpRequestActivity inst;
    private RecyclerView list;
    private String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_request);
        inst=this;
        list=findViewById(R.id.help_search_name_list);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        list.setHasFixedSize(true);
        id=getSharedPreferences("login",MODE_PRIVATE).getString("id","0");
        refresh();
    }
    public void refresh(){
        GetDataTask gt=new GetDataTask();
        gt.execute(Constants.URL+"getHelpBySurveyMan?id="+id);
    }
    public static HelpRequestActivity getInstance(){
        return inst;
    }
    @SuppressLint("StaticFieldLeak")
    private class GetDataTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
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
            try {
                JSONArray arr = new JSONArray(s);
                if (arr.length() == 0)
                    Snackbar.make(list, "No data found", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                List<JSONObject> jsonList = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    jsonList.add(arr.getJSONObject(i));
                    Log.e("arr", arr.getJSONObject(i).toString());
                }

                HelpAdapter d = new HelpAdapter(jsonList, HelpRequestActivity.this);
                list.setAdapter(d);

            } catch(JSONException e){
                Toast.makeText(HelpRequestActivity.this, s, Toast.LENGTH_SHORT).show();
            }

        }
    }

}
