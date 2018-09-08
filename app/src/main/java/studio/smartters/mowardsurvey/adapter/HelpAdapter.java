package studio.smartters.mowardsurvey.adapter;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import studio.smartters.mowardsurvey.R;
import studio.smartters.mowardsurvey.ViewHolder.HelpViewHolder;


public class HelpAdapter extends RecyclerView.Adapter<HelpViewHolder>{

    private List<JSONObject> list;
    private AppCompatActivity a;

    public HelpAdapter(List<JSONObject> list, AppCompatActivity a) {
        this.list = list;
        this.a = a;
    }

    @NonNull
    @Override
    public HelpViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(a).inflate(R.layout.help_row,parent,false);
        return new HelpViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HelpViewHolder holder, int position) {
        try {
            holder.setName(list.get(position).getString("name"));
            holder.setAddress(list.get(position).getString("address"));
            holder.setCall(list.get(position).getString("phone"));
            holder.setClick(list.get(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
