package studio.smartters.mowardsurvey;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
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
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import studio.smartters.mowardsurvey.Dialogs.CreateDialogBooth;
import studio.smartters.mowardsurvey.adapter.BoothAdapter;
import studio.smartters.mowardsurvey.others.Constants;

public class BoothActivity extends AppCompatActivity {
    private static BoothActivity inst;
    private List<String> names,desigs,phones,ids;
    private RecyclerView list;
    private String id;
    private EditText messageText;
    public ProgressDialog p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booth);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inst = this;
        id=getSharedPreferences("login",MODE_PRIVATE).getString("id","0");
        messageText=findViewById(R.id.message_booth);
        list=findViewById(R.id.member_list);
        list.setHasFixedSize(true);
        list.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        refresh();
    }
    public String getid(){
        return id;
    }
    public void refresh(){
        GetMembersTask gt=new GetMembersTask();
        gt.execute(Constants.URL+"getBoothMembers?booth="+id);

    }
    public static BoothActivity getInstance(){
        return inst;
    }
    public void addMember(View view) {
        final CreateDialogBooth dialog= new CreateDialogBooth(this);
        dialog.show();
    }

    public void message(View view) {
        String msg=messageText.getText().toString();
        if(TextUtils.isEmpty(msg)){
            Toast.makeText(inst, "Can't send blank message", Toast.LENGTH_SHORT).show();
        }else{
            if(phones.size()==0){
                Toast.makeText(inst, "No members to message..please add the members first.", Toast.LENGTH_SHORT).show();
            }else {
                p=new ProgressDialog(this);
                p.setMessage("Sending message");
                p.setCancelable(false);
                p.setCanceledOnTouchOutside(false);
                String num = "";
                num += phones.get(0);
                for (int i = 1; i < phones.size(); i++) {
                    num += ("," + phones.get(i));
                }
                MessageTask ot = new MessageTask();
                ot.execute("http://sms.forcesms.info/sendSMS?username=jtbabu&message=Mo Ward Survey Alert ! From: MoWard Surveyor,"+ msg + "&sendername=JAVATC&smstype=TRANS&numbers=" + num + "&apikey=261819f9-0181-4274-941b-c8602ef5225d");
            }
        }
    }

    private class GetMembersTask extends AsyncTask<String,Void,String> {

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
            try {
                JSONArray arr=new JSONArray(s);
                names=new ArrayList<>();
                ids=new ArrayList<>();
                phones=new ArrayList<>();
                desigs=new ArrayList<>();
                for(int i=0;i<arr.length();i++){
                    JSONObject json=arr.getJSONObject(i);
                    names.add(json.getString("name"));
                    ids.add(json.getString("id"));
                    phones.add(json.getString("phone"));
                    desigs.add(json.getString("desig"));
                    BoothAdapter b=new BoothAdapter(names,desigs,ids,phones,BoothActivity.this);
                    list.setAdapter(b);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    class MessageTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url=new URL(strings[0]);
                URLConnection con=url.openConnection();
                InputStream is=con.getInputStream();
                return "res";
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "err";
        }

        @Override
        protected void onPostExecute(String s) {
            p.dismiss();
            super.onPostExecute(s);
            if(s.equalsIgnoreCase("err")){
                Toast.makeText(BoothActivity.this, "Some Error Occurred..Try again later", Toast.LENGTH_SHORT).show();
            }else{
                messageText.setText("");
                Toast.makeText(BoothActivity.this, "Message sent Successfully", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
