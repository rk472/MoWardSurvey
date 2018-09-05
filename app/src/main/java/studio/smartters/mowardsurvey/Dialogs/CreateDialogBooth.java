package studio.smartters.mowardsurvey.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import studio.smartters.mowardsurvey.BoothActivity;
import studio.smartters.mowardsurvey.R;


public class CreateDialogBooth extends Dialog {
    private EditText et_name,et_desig,et_phone;
    private Button btn_create;
    private BoothActivity activity = BoothActivity.getInstance();
    private Context c;
    public CreateDialogBooth(@NonNull final Context context) {
        super(context);
        setContentView(R.layout.modal_add);
        et_name = findViewById(R.id.modal_name);
        et_desig = findViewById(R.id.modal_designation);
        et_phone = findViewById(R.id.modal_contact);
        btn_create = findViewById(R.id.modal_btn);
        c=context;
        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=et_name.getText().toString().trim();
                String desig = et_desig.getText().toString().trim();
                String phone = et_phone.getText().toString().trim();
                if(TextUtils.isEmpty(name)||TextUtils.isEmpty(phone)||TextUtils.isEmpty(desig)) {
                    Toast.makeText(context, "Fields can't be empty..", Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        });
    }
}
