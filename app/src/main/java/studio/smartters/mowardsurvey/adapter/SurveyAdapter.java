package studio.smartters.mowardsurvey.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import studio.smartters.mowardsurvey.R;
import studio.smartters.mowardsurvey.ViewHolder.ServeyViewHolder;

public class SurveyAdapter extends RecyclerView.Adapter<ServeyViewHolder>{
    private int size;
    private Context c;
    public SurveyAdapter(Context c, int size){
        this.size=size;
        this.c=c;
    }
    @NonNull
    @Override
    public ServeyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.survey_row,parent,false);
        return new ServeyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ServeyViewHolder holder, int position) {
        holder.setPosition(size);
        holder.setClick(position);
    }

    @Override
    public int getItemCount() {
        return size;
    }
}
