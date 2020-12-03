package ppo.converter.ui.fragments.conversion;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import ppo.converter.R;
import ppo.converter.databinding.FragmentConversionBinding;
import ppo.converter.ui.activities.ConvertLengthActivity;
import ppo.converter.ui.custom.UnitPickerListDialog;
import ppo.converter.viewModels.ConversionViewModel;
import ppo.converter.viewModels.ModelFactory;

public class ConversionFragment extends Fragment implements View.OnClickListener, View.OnLongClickListener {

    FragmentConversionBinding binding;
    ConversionViewModel mViewModel;
    UnitPickerListDialog myDialogFragment;
    TextView valueSelected, unitSelected;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentConversionBinding.inflate(getLayoutInflater());
        mViewModel = ViewModelProviders.of(getActivity(), new ModelFactory(getResources(), ConvertLengthActivity.title)).get(ConversionViewModel.class);
        initViews();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        myDialogFragment = new UnitPickerListDialog(mViewModel.getUnits());
        setListeners();

        final MutableLiveData<String> value = mViewModel.getInputValue();
        value.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if(!value.getValue().equals("")) {
                    valueSelected.setText(value.getValue());
                    convert();
                }
            }
        });

        final MutableLiveData<String> unit = myDialogFragment.getUnitName();
        unit.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                if (!(unit.getValue().equals(""))) {
                    unitSelected.setText(mViewModel.getUnitShort(unit.getValue()));
                    if (unitSelected == binding.listbtn1)
                        binding.unit1.setText(mViewModel.getUnitFull(unit.getValue()));
                    else
                        binding.unit2.setText(mViewModel.getUnitFull(unit.getValue()));
                    convert();
                }
            }
        });
    }

    void convert(){
        double val = Double.parseDouble(valueSelected.getText().toString());
        String from = binding.unit1.getText().toString() + " " + binding.listbtn1.getText().toString();
        String to = binding.unit2.getText().toString() + " " + binding.listbtn2.getText().toString();
        if (valueSelected == binding.text1)
            binding.text2.setText(mViewModel.convert(val, from, to));
        else
            binding.text1.setText(mViewModel.convert(val, to, from));
    }

    void initViews() {
        binding.listbtn1.setText(mViewModel.getUnitShort(mViewModel.getUnits()[0]));
        binding.listbtn2.setText(mViewModel.getUnitShort(mViewModel.getUnits()[0]));
        binding.unit1.setText(mViewModel.getUnitFull(mViewModel.getUnits()[0]));
        binding.unit2.setText(mViewModel.getUnitFull(mViewModel.getUnits()[0]));
        valueSelected = binding.text1;
        binding.text1.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    void setListeners() {
        binding.listbtn1.setOnClickListener(this);
        binding.listbtn2.setOnClickListener(this);
        binding.text1.setOnClickListener(this);
        binding.text2.setOnClickListener(this);
        binding.text1.setOnLongClickListener(this);
        binding.text2.setOnLongClickListener(this);
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

    @Override
    public void onClick(View v) {
        if (v == binding.listbtn1 || v == binding.listbtn2) {
            unitSelected = ((TextView) v);
            showDialog();
        }
        else {
            valueSelected.setTextColor(getResources().getColor(R.color.colorText));
            valueSelected = ((TextView) v);
            mViewModel.inputValue(valueSelected);
        }
        ((TextView)v).setTextColor(getResources().getColor(R.color.colorAccent));
    }

    @Override
    public boolean onLongClick(View v) {
        ClipData clip = ClipData.newPlainText("clip", ((TextView) v).getText().toString());
        ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(getActivity(), "Text copied", Toast.LENGTH_SHORT)
                .show();
        return false;
    }
}
