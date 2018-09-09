package studio.smartters.mowardsurvey;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
    @SuppressLint("StaticFieldLeak")
    private class LoginTask extends AsyncTask<String,Void,String>{

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
                    Toast.makeText(LoginActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(LoginActivity.this, s, Toast.LENGTH_SHORT).show();
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