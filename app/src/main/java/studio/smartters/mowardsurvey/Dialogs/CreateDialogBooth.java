package studio.smartters.mowardsurvey.Dialogs;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import studio.smartters.mowardsurvey.BoothActivity;
import studio.smartters.mowardsurvey.R;
import studio.smartters.mowardsurvey.others.Constants;


public class CreateDialogBooth extends Dialog {
    private EditText et_name,et_desig,et_phone;
    private Button btn_create;
    private BoothActivity activity = BoothActivity.getInstance();
    private Context c;
    public CreateDialogBooth(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.modal_add);
        et_name = findViewById(R.id.modal_name);
        et_desig = findViewById(R.id.modal_designation);
        et_phone = findViewById(R.id.modal_contact);
        btn_create = findViewById(R.id.modal_btn);
        final String id=activity.getid();
        c=context;
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=et_name.getText().toString().trim();
                String desig = et_desig.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(desig)) {
                    Toast.makeText(context, "Fields can't be empty..", Toast.LENGTH_SHORT).show();
                }else if(phone.length()<10){
                    Toast.makeText(context, "Invalid phone number", Toast.LENGTH_SHORT).show();
                }else{
                    activity.p=new ProgressDialog(c);
                    activity.p.setTitle("Please wait");
                    activity.p.setMessage("please wait while we are adding the Member");
                    activity.p.setCanceledOnTouchOutside(false);
                    activity.p.setCancelable(false);
                    activity.p.show();
                    AddBoothMember at=new AddBoothMember();
                    at.execute(Constants.URL+"addBoothMembers?booth="+id+"&name="+name+"&phone="+phone+"&desig="+desig);
                }
            }
        });
    }
    private class AddBoothMember extends AsyncTask<String,Void,String> {

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
            } catch (IOException e) {
                return "Unable to reach server !";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            activity.p.dismiss();
            try {
                JSONObject json=new JSONObject(s);
                if(json.getBoolean("status")){
                    Toast.makeText(c, "Added Successfully", Toast.LENGTH_SHORT).show();
                    activity.refresh();
                    dismiss();
                }else{
                    Toast.makeText(c, "can't add the member", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(activity, s, Toast.LENGTH_SHORT).show();
            }
        }
    }

}
