package studio.smartters.mowardsurvey;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

import studio.smartters.mowardsurvey.adapter.DataAdapter;
import studio.smartters.mowardsurvey.others.Constants;

public class ViewDataActivity extends AppCompatActivity {
    private EditText etSearch;
    private String id;
    private RecyclerView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);
        etSearch = findViewById(R.id.search_name_text);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        list=findViewById(R.id.view_survey_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        id=getIntent().getExtras().getString("id");
        refresh("");
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                refresh(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }
    void refresh(String name){
        GetDataTask gt=new GetDataTask();
        gt.execute(Constants.URL+"getAllDataByFamily?id="+id+"&name="+name);
    }

    public void goBack(View view) {
        finish();
    }

    public void clearText(View view) {
        etSearch.setText("");
    }

    @SuppressLint("StaticFieldLeak")
    private class GetDataTask extends AsyncTask<String,Void,String>{

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
                    Snackbar.make(etSearch, "No data found", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                List<JSONObject> jsonList = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    jsonList.add(arr.getJSONObject(i));
                    Log.e("arr", arr.getJSONObject(i).toString());
                }
                DataAdapter d = new DataAdapter(jsonList, ViewDataActivity.this);
                list.setAdapter(d);
            } catch(JSONException e){
                    Toast.makeText(ViewDataActivity.this, s, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
