package ppo.converter.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ppo.converter.R;
import ppo.converter.databinding.FragmentMoneyBinding;


public class TimeFragment extends Fragment {

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        FragmentMoneyBinding binding = FragmentMoneyBinding.inflate(getLayoutInflater());
        Typeface typeFaceFont = Typeface.createFromAsset(getActivity().getAssets(), "fa_solid.ttf");
        String text = binding.timeFragmentText.getText().toString()
                + getString(R.string.fa_grin_beam_solid) + " "
                + getString(R.string.fa_hand_holding_heart_solid) + " "
                + getString(R.string.fa_sun_solid);
        binding.timeFragmentText.setText(text);
        binding.timeFragmentText.setTypeface(typeFaceFont);
        return binding.getRoot();
    }

}
