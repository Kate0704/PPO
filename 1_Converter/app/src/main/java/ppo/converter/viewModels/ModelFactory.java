package ppo.converter.viewModels;

import android.content.res.Resources;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final Resources res;
    private final String title;

    public ModelFactory(Resources res, String title) {
        super();
        this.res = res;
        this.title = title;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == ConversionViewModel.class) return (T) new ConversionViewModel(res, title);
        return null;
    }
}
