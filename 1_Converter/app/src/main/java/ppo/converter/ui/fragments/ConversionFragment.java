package ppo.converter.ui.fragments;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ppo.converter.R;
import ppo.converter.databinding.FragmentConversionBinding;
import ppo.converter.ui.activities.ConvertLengthActivity;
import ppo.converter.ui.custom.UnitPickerListDialog;
import ppo.converter.viewModels.ConversionViewModel;
import ppo.converter.viewModels.ModelFactory;

public class ConversionFragment extends Fragment implements View.OnClickListener {

    private FragmentConversionBinding binding;
    private ConversionViewModel mViewModel;
    static UnitPickerListDialog myDialogFragment;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentConversionBinding.inflate(getLayoutInflater());
        String t = ConvertLengthActivity.title;
        mViewModel = ViewModelProviders.of(getActivity(), new ModelFactory(getResources(), t)).get(ConversionViewModel.class);
        String unit = mViewModel.getUnits()[0];
        binding.listbtn1.setText(unit.substring(unit.lastIndexOf(" ") + 1));
        binding.listbtn2.setText(unit.substring(unit.lastIndexOf(" ") + 1));
        binding.unit1.setText(unit.substring(0, unit.lastIndexOf(" ")));
        binding.unit2.setText(unit.substring(0, unit.lastIndexOf(" ")));
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        binding.text1.setTextColor(getResources().getColor(R.color.colorAccent));
        myDialogFragment = new UnitPickerListDialog(mViewModel.getUnits());

        binding.listbtn1.setOnClickListener(this);
        binding.listbtn2.setOnClickListener(this);

        final MutableLiveData<String> value = mViewModel.getInputValue();
        value.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(!value.getValue().equals("")) {
                    binding.text1.setText(value.getValue());
                    convert();
                }
            }
        });

        final MutableLiveData<String> unit = myDialogFragment.getUnitName();
        unit.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                String val = unit.getValue();
                if(!val.equals("")) {
                    TextView unitChanged = mViewModel.getUnitChanged();
                    unitChanged.setText(val.substring(val.lastIndexOf(" ") + 1));
                    if (unitChanged.getId() == R.id.listbtn1)
                        binding.unit1.setText(val.substring(0, val.lastIndexOf(" ")));
                    else binding.unit2.setText(val.substring(0, val.lastIndexOf(" ")));
                    convert();
                }
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == 404 && !(this.isDetached())) {
            binding.listbtn1.setTextColor(getResources().getColor(R.color.colorText));
            binding.listbtn2.setTextColor(getResources().getColor(R.color.colorText));
        }
    }

    private void showDialog() {
        if(getActivity() != null) {
            myDialogFragment.setTargetFragment(ConversionFragment.this, 404);
            myDialogFragment.show(getActivity().getSupportFragmentManager(), "myDialog");
        }
    }



    void convert(){
        double val = Double.parseDouble(binding.text1.getText().toString());
        String from = binding.unit1.getText().toString() + " " + binding.listbtn1.getText().toString();
        String to = binding.unit2.getText().toString() + " " + binding.listbtn2.getText().toString();
        String result = mViewModel.convert(val, from, to);
        binding.text2.setText(result);
    }

    @Override
    public void onClick(View v) {
        mViewModel.unitChanged((TextView)v);
        ((TextView)v).setTextColor(getResources().getColor(R.color.colorAccent));
        showDialog();
    }
}
