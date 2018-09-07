package studio.smartters.mowardsurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import studio.smartters.mowardsurvey.others.Constants;

public class LoginActivity extends AppCompatActivity {
    private EditText numberText,passText;
    private ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        numberText=findViewById(R.id.login_phone);
        passText=findViewById(R.id.login_password);
    }

    public void goHome(View view) {
        String number=numberText.getText().toString().trim();
        String pass=passText.getText().toString().trim();
        if(TextUtils.isEmpty(number) || TextUtils.isEmpty(pass)){
            Toast.makeText(this, "You must fill both the fields", Toast.LENGTH_SHORT).show();
        }else{
            p=new ProgressDialog(this);
            p.setMessage("Please wait while we are logging you in");
            p.setTitle("Please Wait");
            p.setCancelable(false);
            p.setCanceledOnTouchOutside(false);
            p.show();
            LoginTask lt=new LoginTask();
            lt.execute(Constants.URL+"loginServey?user="+number+"&pass="+pass);
        }
    }
    private class LoginTask extends AsyncTask<String,Void,String>{

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
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            JSONObject json=new JSONObject();
            try {
                json.put("status",0);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return json.toString();

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            p.dismiss();
            try {
                JSONObject json=new JSONObject(s);
                if(json.getInt("status")==1){
                    SharedPreferences preferences=getSharedPreferences("login",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putBoolean("login",true);
                    editor.apply();
                    editor.putString("id",json.getString("id"));
                    editor.apply();
                    editor.putString("booth_id",json.getString("booth"));
                    editor.apply();
                    startActivity(new Intent(LoginActivity.this,HomeActivity.class));
                    finish();
                }else{
                    Toast.makeText(LoginActivity.this, "Some Error occurred", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(getSharedPreferences("login",MODE_PRIVATE).getBoolean("login",false)){
            startActivity(new Intent(LoginActivity.this,HomeActivity.class));
            finish();
        }
    }
}