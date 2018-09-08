package studio.smartters.mowardsurvey.ViewHolder;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import studio.smartters.mowardsurvey.HelpDetailsActivity;
import studio.smartters.mowardsurvey.HelpRequestActivity;
import studio.smartters.mowardsurvey.R;


public class HelpViewHolder extends RecyclerView.ViewHolder {
    private View v;
    private TextView nameText, addressText;
    private ImageButton callButton;

    public HelpViewHolder(View itemView) {
        super(itemView);
        v = itemView;
        nameText = v.findViewById(R.id.search_name);
        addressText = v.findViewById(R.id.search_address);
        callButton = v.findViewById(R.id.search_call);
    }

    public void setName(String name) {
        nameText.setText(name);
    }

    public void setAddress(String address) {
        addressText.setText(address);
    }

    public void setCall(final String number) {
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_CALL);
                i.setData(Uri.parse("tel" + number));
                if (ActivityCompat.checkSelfPermission(HelpRequestActivity.getInstance(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                HelpRequestActivity.getInstance().startActivity(i);
            }
        });
    }
    public void setClick(final String json){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(HelpRequestActivity.getInstance(), HelpDetailsActivity.class);
                i.putExtra("json_data",json);
                HelpRequestActivity.getInstance().startActivity(i);
            }
        });
    }
}
