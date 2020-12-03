package ppo.converter.viewModels;

import android.view.View;

import androidx.lifecycle.ViewModel;

import org.jetbrains.annotations.NotNull;

import ppo.converter.R;

public class MainActivityViewModel extends ViewModel {

    private static String title;

    public String getTitle() { return title; }

    public void setTitle(@NotNull View view) {
        if (view.getId() == R.id.length_button)
            title = "Length converter";
        else if (view.getId() == R.id.area_button)
            title = "Area converter";
        else title = "Volume converter";
    }
}
