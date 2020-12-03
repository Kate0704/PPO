package ppo.converter.ui.fragments.mainActivity;

import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ppo.converter.R;
import ppo.converter.databinding.FragmentWeightBinding;


public class WeightFragment extends Fragment {

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        FragmentWeightBinding binding = FragmentWeightBinding.inflate(getLayoutInflater());
        Typeface typeFaceFont = Typeface.createFromAsset(getActivity().getAssets(), "fa_solid.ttf");
        String text = binding.weightFragmentText.getText().toString()
                + getString(R.string.fa_pastafarianism_solid) + " "
                + getString(R.string.fa_street_view_solid) + " "
                + getString(R.string.fa_khanda_solid);
        binding.weightFragmentText.setText(text);
        binding.weightFragmentText.setTypeface(typeFaceFont);
        return binding.getRoot();
    }
}
