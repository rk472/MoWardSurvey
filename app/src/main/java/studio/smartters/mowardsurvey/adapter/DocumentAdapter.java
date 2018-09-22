package studio.smartters.mowardsurvey.adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import studio.smartters.mowardsurvey.R;
import studio.smartters.mowardsurvey.ViewHolder.DocumentViewHolder;
import studio.smartters.mowardsurvey.others.Constants;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentViewHolder> {
    private List<String> names, ids;
    private AppCompatActivity a;
    public DocumentAdapter(List<String> names, List<String> ids, AppCompatActivity a){
        this.a=a;
        this.ids=ids;
        this.names=names;
    }
    @NonNull
    @Override
    public DocumentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(a).inflate(R.layout.document_row,parent,false);
        return new DocumentViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentViewHolder holder, int position) {
        holder.setText(names.get(position).substring(0,names.get(position).length()-4));
        holder.setClick(a, Constants.URL+"documents/"+names.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }
}
