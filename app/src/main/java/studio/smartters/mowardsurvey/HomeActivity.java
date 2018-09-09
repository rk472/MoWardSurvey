package studio.smartters.mowardsurvey;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import studio.smartters.mowardsurvey.Fragments.ImageFragment;
import studio.smartters.mowardsurvey.Fragments.VideoFragment;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SectionsPagerAdapter mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        ViewPager mViewPager = findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,SurveyMainActivity.class));
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_booth) {
            startActivity(new Intent(this,BoothActivity.class));
            return true;
        }else if (id == R.id.action_help){
            startActivity(new Intent(this,HelpRequestActivity.class));
            return true;
        }else if(id == R.id.action_dataview){
            startActivity(new Intent(this,ViewDataActivity.class));
            return true;
        }else if (id == R.id.action_noadhar){
            startActivity(new Intent(this,NoAdharActivity.class));
            return true;
        }else if(id == R.id.action_novoter){
            startActivity(new Intent(this,NoVoterActivity.class));
            return true;
        }else if(id == R.id.action_logout){
            SharedPreferences s=getSharedPreferences("login",MODE_PRIVATE);
            SharedPreferences.Editor e=s.edit();
            e.putBoolean("login",false);
            e.apply();
            finishAffinity();
            startActivity(new Intent(this,LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
        public Fragment getItem(int position) {
            Fragment f=null;
            switch (position){
                case 0:
                    f = new ImageFragment();
                    break;
                case 1:
                    f = new VideoFragment();
                    break;
            }
            return f;
        }
        @Override
        public int getCount() {
            return 2;
        }
    }
}
