package studio.smartters.mowardsurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import studio.smartters.mowardsurvey.POJO.Servey;
import studio.smartters.mowardsurvey.adapter.SurveyAdapter;
import studio.smartters.mowardsurvey.others.Constants;

public class SurveyItemActivity extends AppCompatActivity {
    private int number;
    private RecyclerView list;
    public Servey data[];
    private static SurveyItemActivity inst;
    private ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_item);
        String jsonData = getIntent().getExtras().getString("json_data");
        inst=this;
        Log.i("MyData",jsonData);
        try {
            JSONObject json=new JSONObject(jsonData);
            number=Integer.parseInt(json.getString("member_no"));
            list=findViewById(R.id.survey_item_list);
            list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
            list.setHasFixedSize(true);
            SurveyAdapter a=new SurveyAdapter(this,number);
            list.setAdapter(a);
            data=new Servey[number];
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public static SurveyItemActivity getInstance(){
        return inst;
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
            if(canSave()){
                upload();
            }else{
                Toast.makeText(this, "You must save all the fields before uploading..", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
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
    private class UploadTask extends AsyncTask<String,Void,String>{
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
            Toast.makeText(SurveyItemActivity.this, s, Toast.LENGTH_SHORT).show();
            if(s.equals("Successfully Submitted")){
                finish();
            }
        }
    }
}
