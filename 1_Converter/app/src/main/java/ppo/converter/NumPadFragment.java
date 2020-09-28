package ppo.converter;

import androidx.lifecycle.ViewModel;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ppo.converter.R;

public class NumPadFragment extends Fragment {

    private NumPadViewModel mViewModel;

    public static NumPadFragment newInstance() {
        return new NumPadFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.num_pad_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new NumPadViewModel();
        // TODO: Use the ViewModel
    }

}
