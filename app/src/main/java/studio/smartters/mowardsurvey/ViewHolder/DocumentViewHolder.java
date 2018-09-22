package studio.smartters.mowardsurvey.ViewHolder;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import studio.smartters.mowardsurvey.R;

public class DocumentViewHolder extends RecyclerView.ViewHolder {
    private TextView nameText;
    private View v;
    private ImageView deleteDoc;
    public DocumentViewHolder(View itemView) {
        super(itemView);
        v=itemView;
        nameText=v.findViewById(R.id.document_name);
        deleteDoc=v.findViewById(R.id.delete_document);
    }
    public void setText(String name){
        nameText.setText(name);
    }
    public void setClick(final AppCompatActivity a, final String url){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(url), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                a.startActivity(intent);
            }
        });
    }
}
