package studio.smartters.mowardsurvey.ViewHolder;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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

public class MemberViewHolder extends RecyclerView.ViewHolder {
    private TextView nameText, desigText;
    private ImageView phone, delete;
    private View v;
    private BoothActivity activity=BoothActivity.getInstance();
    public MemberViewHolder(View itemView) {
        super(itemView);
        nameText = itemView.findViewById(R.id.member_name);
        v = itemView;
        desigText = itemView.findViewById(R.id.member_desig);
        phone = itemView.findViewById(R.id.member_call);
        delete = itemView.findViewById(R.id.member_delete);
    }

    public void setName(String name) {
        nameText.setText(name);
    }

    public void setDesig(String desig) {
        desigText.setText(desig);
    }

    public void call(final String number) {
        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:" + number));
                if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) v.getContext(),new String[]{Manifest.permission.CALL_PHONE},1);
                    return;
                }
                v.getContext().startActivity(intent);
            }
        });
    }
    public void remove(final String id){
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RemoveTask rt=new RemoveTask();
                activity.p=new ProgressDialog(v.getContext());
                activity.p.setTitle("Please wait");
                activity.p.setMessage("please wait while we are adding the Member");
                activity.p.setCanceledOnTouchOutside(false);
                activity.p.setCancelable(false);
                activity.p.show();
                rt.execute(Constants.URL+"removeBoothMembers?id="+id);
            }
        });

    }
    private class RemoveTask extends AsyncTask<String,Void,String> {

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
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            activity.p.dismiss();
            try {
                Log.e("err",s);
                JSONObject json=new JSONObject(s);
                if(json.getBoolean("status")){
                    Toast.makeText(v.getContext(), "removed Successfully", Toast.LENGTH_SHORT).show();
                    activity.refresh();
                }else{
                    Toast.makeText(v.getContext(), "Some error occurred...try again later..", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
