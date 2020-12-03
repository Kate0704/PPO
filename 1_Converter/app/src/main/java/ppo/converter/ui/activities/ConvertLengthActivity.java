package ppo.converter.ui.activities;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import ppo.converter.databinding.ActivityConvertLengthBinding;
import ppo.converter.viewModels.ConversionViewModel;
import ppo.converter.viewModels.MainActivityViewModel;

public class ConvertLengthActivity extends AppCompatActivity{

    ConversionViewModel mViewModel;
    public static String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityViewModel mainViewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        title = mainViewModel.getTitle();
        ActivityConvertLengthBinding binding = ActivityConvertLengthBinding.inflate(getLayoutInflater());
        binding.title.setText(title);

        setSupportActionBar(binding.toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setContentView(binding.getRoot());
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewModel = ViewModelProviders.of(this).get(ConversionViewModel.class);
    }

    public void numOnClick(View view) {
        mViewModel.inputValue(view);
    }

    public void AC(View view) {
        mViewModel.AC();
    }

    public void delete(View view) {
        mViewModel.delete();
    }
}
