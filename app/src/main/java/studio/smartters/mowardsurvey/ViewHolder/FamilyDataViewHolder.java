package studio.smartters.mowardsurvey.ViewHolder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import studio.smartters.mowardsurvey.PersonDataActivity;
import studio.smartters.mowardsurvey.R;
import studio.smartters.mowardsurvey.ViewDataActivity;

public class FamilyDataViewHolder extends RecyclerView.ViewHolder {
    private TextView nameText, addressText, fnoText;
    private ImageButton callButton;
    private View v;

    public FamilyDataViewHolder(View itemView) {
        super(itemView);
        v = itemView;
        nameText = v.findViewById(R.id.family_name);
        addressText = v.findViewById(R.id.family_address);
        fnoText = v.findViewById(R.id.family_no);
        callButton = v.findViewById(R.id.member_call);
    }

    public void setName(String name) {
        nameText.setText(name);
    }

    public void setAddress(String name) {
        addressText.setText(name);
    }

    public void setNumber(String name) {
        fnoText.setText(name);
    }

    public void setCall(final String number, final AppCompatActivity a) {
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(a, Manifest.permission.CALL_PHONE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(a,new String[]{Manifest.permission.CALL_PHONE},1);
                }else{
                    Intent i = new Intent(Intent.ACTION_CALL);
                    i.setData(Uri.parse("tel:" + number));
                    a.startActivity(i);
                }

            }
        });
    }
    public void setClick(final String json, final AppCompatActivity a){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(a,ViewDataActivity.class);
                i.putExtra("json_data",json);
                a.startActivity(i);
            }
        });

    }
}
