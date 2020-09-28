package ppo.converter.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import ppo.converter.R;
import ppo.converter.activities.ConvertLengthActivity;


public class LengthFragment extends Fragment{

    View v;

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_length, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        LinearLayout l_btn = v.findViewById(R.id.length_button);
        l_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConvertLengthActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout a_btn = v.findViewById(R.id.area_button);
        a_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConvertLengthActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout v_btn = v.findViewById(R.id.volume_button);
        v_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ConvertLengthActivity.class);
                startActivity(intent);
            }
        });
    }
}
