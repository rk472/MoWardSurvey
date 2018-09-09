package studio.smartters.mowardsurvey.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import studio.smartters.mowardsurvey.R;
import studio.smartters.mowardsurvey.adapter.VideoAdapter;
import studio.smartters.mowardsurvey.others.Constants;

public class VideoFragment extends Fragment {
    private RequestQueue r;
    private List<String> name,path;
    private RecyclerView list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View v;
    private LinearLayout ln,ll;
    private  boolean loaded = false;
    private TextView tvLoad;
    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_video, container, false);
        list=v.findViewById(R.id.video_list);
        ln=v.findViewById(R.id.video_error);
        ll=v.findViewById(R.id.video_load);
        tvLoad = v.findViewById(R.id.load_text);
        tvLoad.setText("Please Wait Loading");
        swipeRefreshLayout=v.findViewById(R.id.swipe_video);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refresh();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        Cache c=new DiskBasedCache(Objects.requireNonNull(getActivity()).getCacheDir(),1024*1024);
        Network n=new BasicNetwork(new HurlStack());
        r=new RequestQueue(c,n);
        r.start();
        refresh();
        return v;
    }
    @SuppressLint("SetTextI18n")
    private void refresh(){
        loaded = false;
        if(isNetworkAvailable()) {
            tvLoad.setText("Please Wait Loading");
            ll.setVisibility(View.VISIBLE);
            ln.setVisibility(View.GONE);
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(!loaded){
                        tvLoad.setText("Slow Internet Detected\nPlease Wait a Little Longer");
                    }
                }
            },5000);
            String url = Constants.URL + "getVideos";
            JsonArrayRequest j = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    name = new ArrayList<>();
                    path = new ArrayList<>();
                    int count = 0;
                    while (count < response.length()) {
                        try {
                            JSONObject j = response.getJSONObject(count);
                            name.add(j.getString("desc"));
                            path.add(j.getString("url"));
                            count++;
                        } catch (JSONException e) {
                            //Toast.makeText(getActivity(),e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                    VideoAdapter p = new VideoAdapter(getActivity(), name, path);
                    list.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
                    list.setHasFixedSize(true);
                    list.setAdapter(p);
                    list.setVisibility(View.VISIBLE);
                    ln.setVisibility(View.GONE);
                    ll.setVisibility(View.GONE);
                    loaded = true;
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            r.add(j);
        }else{
            list.setVisibility(View.GONE);
            ll.setVisibility(View.GONE);
            ln.setVisibility(View.VISIBLE);
        }
    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) v.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connectivityManager != null;
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

}
