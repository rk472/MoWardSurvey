package studio.smartters.mowardsurvey;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import studio.smartters.mowardsurvey.others.Constants;


public class HelpDetailsActivity extends AppCompatActivity {
    private TextView nameText, bodyText, addressText;
    private JSONObject json;
    private EditText etReview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_details);
        nameText = findViewById(R.id.help_name);
        bodyText = findViewById(R.id.help_body);
        addressText = findViewById(R.id.help_address);
        etReview = findViewById(R.id.help_review);
        String data = getIntent().getExtras().getString("json_data");
        try {
            json = new JSONObject(data);
            nameText.setText(json.getString("name"));
            bodyText.setText(json.getString("body"));
            addressText.setText(json.getString("address"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void call(View v) {
        Intent i = new Intent(Intent.ACTION_CALL);
        try {
            i.setData(Uri.parse("tel:" + json.getString("phone")));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            startActivity(i);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void resolve(View v){
        String revData = etReview.getText().toString().trim();
        if (revData.length()>0) {
            ResolveTask rt = new ResolveTask();
            try {
                rt.execute(Constants.URL + "changeHelpStatus?id=" + json.getString("id")+"&review="+revData);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Snackbar.make(v, "Enter review before resolving.", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        }
    }
    private class ResolveTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            try {
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
                //Toast.makeText(SearchNameActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                //Toast.makeText(SearchNameActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
            return "no";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(HelpDetailsActivity.this, s, Toast.LENGTH_SHORT).show();
            if(s.equals("Successfully resolved")){
                HelpRequestActivity.getInstance().refresh();
                finish();
            }
        }
    }
}
