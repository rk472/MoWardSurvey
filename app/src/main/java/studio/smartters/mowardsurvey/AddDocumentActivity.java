package studio.smartters.mowardsurvey;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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

import studio.smartters.mowardsurvey.adapter.DocumentAdapter;
import studio.smartters.mowardsurvey.others.Constants;

public class AddDocumentActivity extends AppCompatActivity {
    private RecyclerView list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private FloatingActionButton fab;
    private LinearLayout ln;
    private static AddDocumentActivity inst;
    private ProgressDialog uploading;
    private String selectedPath;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_document);
        list=findViewById(R.id.document_list);
        ln=findViewById(R.id.document_error);
        fab=findViewById(R.id.add_document);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent = new Intent();
                intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select a Document "), 1);*/
            }
        });
        swipeRefreshLayout=findViewById(R.id.swipe_document);
        inst=this;
        list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        list.setHasFixedSize(true);
        refresh();
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    public static AddDocumentActivity getInstance(){
        return inst;
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    public void refresh(){
        if(isNetworkAvailable()) {
            AddDocumentTask ad=new AddDocumentTask();
            ad.execute(Constants.URL+"getDocuments");
        }else{
            ln.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            Uri selectedDocUri = data.getData();
            assert selectedDocUri != null;

                selectedPath=getPath(getApplicationContext(),selectedDocUri);



        }
    }
    public  String getPath(Context context, Uri uri)  {
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] projection = { "_data" };
            Cursor cursor = null;

            try {
                cursor = context.getContentResolver().query(uri, projection, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    return cursor.getString(column_index);
                }
            } catch (Exception e) {
                // Eat it
            }
        }
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return "err";
    }

    private class AddDocumentTask extends AsyncTask<String,Void,String> {

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
                return "Can't reach server !";
            }
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray arr=new JSONArray(s);
                List<String> names=new ArrayList<>();
                List<String> ids=new ArrayList<>();
                for(int i=0;i<arr.length();i++){
                    JSONObject json=arr.getJSONObject(i);
                    names.add(json.getString("name"));
                    ids.add(json.getString("id"));
                    DocumentAdapter documentAdapter=new DocumentAdapter(names,ids,AddDocumentActivity.inst);
                    list.setAdapter(documentAdapter);
                    ln.setVisibility(View.GONE);
                }
            } catch (JSONException e) {
                Toast.makeText(AddDocumentActivity.this, s, Toast.LENGTH_SHORT).show();
            }
        }
    }
    private class DBTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                URLConnection con= url.openConnection();
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
                e.printStackTrace();
            }
            return "Unknown error occurred";
        }

        @Override
        protected void onPostExecute(String s) {
            uploading.dismiss();
            super.onPostExecute(s);
            Toast.makeText(AddDocumentActivity.this, s, Toast.LENGTH_SHORT).show();
            Log.i("err",s);
            if(s.equals("Successfully uploaded")){
                refresh();
            }
        }
    }
}
