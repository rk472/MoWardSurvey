package studio.smartters.mowardsurvey.adapter;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import studio.smartters.mowardsurvey.R;
import studio.smartters.mowardsurvey.ViewHolder.PhotoViewHolder;


public class PhotoAdapter extends RecyclerView.Adapter<PhotoViewHolder> {
    private Context c;
    private List name,path;
    public PhotoAdapter(Context c, List name,List path) {
        this.c = c;
        this.name=name;
        this.path=path;
    }


    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.image_row,parent,false);
        return new PhotoViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        holder.setName(name.get(position).toString());
        holder.setImg(path.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return name.size();
    }
}

