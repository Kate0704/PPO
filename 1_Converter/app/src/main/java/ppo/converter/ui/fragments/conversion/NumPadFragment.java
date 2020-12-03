package ppo.converter.ui.fragments.conversion;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import ppo.converter.databinding.NumPadFragmentBinding;

public class NumPadFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        NumPadFragmentBinding binding = NumPadFragmentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }
}
