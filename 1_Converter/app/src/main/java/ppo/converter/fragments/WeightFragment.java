package ppo.converter.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ppo.converter.R;


public class WeightFragment extends Fragment {

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weight, container, false);
    }
}
