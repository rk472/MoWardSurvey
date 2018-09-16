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
import studio.smartters.mowardsurvey.ViewHolder.FamilyDataViewHolder;

public class FamilyDataAdapter extends RecyclerView.Adapter<FamilyDataViewHolder> {
    private List<JSONObject> arr;
    private AppCompatActivity a;
    public FamilyDataAdapter(List<JSONObject> arr, AppCompatActivity a){
        this.arr=arr;
        this.a=a;
    }
    @NonNull
    @Override
    public FamilyDataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(a).inflate(R.layout.family_row,parent,false);
        return new FamilyDataViewHolder(v);
    }
    @Override
    public void onBindViewHolder(@NonNull FamilyDataViewHolder holder, int position) {
        try {
            holder.setAddress(arr.get(position).getString("phead"));
            holder.setNumber(arr.get(position).getString("phead"));
            holder.setName(arr.get(position).getString("pname"));
            holder.setCall(arr.get(position).getString("pcontact"),a);
            holder.setClick(arr.get(position).toString(),a);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    public int getItemCount() {
        return arr.size();
    }
}
