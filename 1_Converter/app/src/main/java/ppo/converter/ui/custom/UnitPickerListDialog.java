package ppo.converter.ui.custom;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.MutableLiveData;

import ppo.converter.R;
import ppo.converter.databinding.FragmentCustomDialogBinding;

@SuppressWarnings("rawtypes")
public class UnitPickerListDialog extends DialogFragment {

    String[] units;
    FragmentCustomDialogBinding binding;
    View view;
    public MutableLiveData<String> unit = new MutableLiveData<String>("");

    public MutableLiveData<String> getUnitName() {return unit;}

    public UnitPickerListDialog(String[] units) {
        super();
        this.units = units;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCustomDialogBinding.inflate(getLayoutInflater());
        view = binding.getRoot();
        binding.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                unit.setValue(units[position]);
                onDismiss(getDialog());
            }
        });
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDismiss(getDialog());
            }
        });
        return view;
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Intent intent = new Intent();
        intent.putExtra("DIALOG_DISMISSED", 404);
        if (getTargetFragment().getActivity() != null)
            getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        ArrayAdapter adapter = new ArrayAdapter(getContext(), R.layout.list_item, units);
        binding.listView.setAdapter(adapter);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);
        setAppearance();
    }

    void setAppearance(){
        Window window = getDialog().getWindow();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            window.setLayout(getResources().getDimensionPixelSize(R.dimen.dialog_width), getResources().getDimensionPixelSize(R.dimen.dialog_height_land));
        else window.setLayout(getResources().getDimensionPixelSize(R.dimen.dialog_width), getResources().getDimensionPixelSize(R.dimen.dialog_height));
        window.setGravity(Gravity.BOTTOM);
        view.setBackgroundResource(R.drawable.dialog_shape);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }


    //////////////////////////////////////////////////////////////////
    /////////////// ДЛЯ ПОДДЕРЖКИ ПОВОРОТА ЭКРАНА ////////////////////
    //////////////////////////////////////////////////////////////////

    @Override
    public void onDestroyView() {
        if (getDialog() != null && getRetainInstance())
            getDialog().setOnDismissListener(null);
        super.onDestroyView();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
    //////////////////////////////////////////////////////////////////
}