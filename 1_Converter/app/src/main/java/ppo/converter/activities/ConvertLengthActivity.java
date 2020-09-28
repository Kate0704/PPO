package ppo.converter.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import ppo.converter.ConvertLengthFragment;
import ppo.converter.R;
import ppo.converter.UnitPickerListDialog;

public class ConvertLengthActivity extends AppCompatActivity {

    FragmentManager manager = getSupportFragmentManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convert_length);

        final TextView listbtn = findViewById(R.id.listbtn);

        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UnitPickerListDialog myDialogFragment = new UnitPickerListDialog();

                manager = getSupportFragmentManager();
                listbtn.setTextColor(getResources().getColor(R.color.colorAccent));

                myDialogFragment.show(manager, "myDialog");
            }
        });

        Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        ActionBar ab = getSupportActionBar();

        assert ab != null;
        ab.setDisplayHomeAsUpEnabled(true);



    }
}
