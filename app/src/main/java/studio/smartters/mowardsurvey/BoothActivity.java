package studio.smartters.mowardsurvey;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import studio.smartters.mowardsurvey.Dialogs.CreateDialogBooth;

public class BoothActivity extends AppCompatActivity {
    private static BoothActivity inst;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booth);
        inst = this;
    }
    public static BoothActivity getInstance(){
        return inst;
    }
    public void addMember(View view) {
        final CreateDialogBooth dialog= new CreateDialogBooth(this);
        dialog.show();
    }
}
