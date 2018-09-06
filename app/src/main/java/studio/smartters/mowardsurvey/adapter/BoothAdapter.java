package studio.smartters.mowardsurvey.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import studio.smartters.mowardsurvey.R;
import studio.smartters.mowardsurvey.ViewHolder.MemberViewHolder;

public class BoothAdapter extends RecyclerView.Adapter<MemberViewHolder> {
    private List<String> names,desigs,ids,phones;
    private Context c;
    public BoothAdapter(List<String> names,List<String> desigs,List<String> ids,List<String> phones,Context c){
        this.names=names;
        this.c=c;
        this.desigs=desigs;
        this.ids=ids;
        this.phones=phones;
    }
    @NonNull
    @Override
    public MemberViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.booth_row,parent,false);
        return new MemberViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberViewHolder holder, int position) {
        holder.setName(names.get(position));
        holder.setDesig(desigs.get(position));
        holder.call(phones.get(position));
        holder.remove(ids.get(position));
    }

    @Override
    public int getItemCount() {
        return names.size();
    }
}
