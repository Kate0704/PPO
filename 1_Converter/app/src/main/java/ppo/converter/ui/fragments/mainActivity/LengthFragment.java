package ppo.converter.ui.fragments.mainActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ppo.converter.databinding.FragmentLengthBinding;
import ppo.converter.ui.activities.ConvertLengthActivity;
import ppo.converter.viewModels.MainActivityViewModel;


public class LengthFragment extends Fragment implements View.OnClickListener {

    MainActivityViewModel viewModel;

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        FragmentLengthBinding binding = FragmentLengthBinding.inflate(getLayoutInflater());
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);

        binding.lengthButton.setOnClickListener(this);
        binding.areaButton.setOnClickListener(this);
        binding.volumeButton.setOnClickListener(this);
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(getActivity(), ConvertLengthActivity.class);
        viewModel.setTitle(view);
        startActivity(intent);
    }
}
