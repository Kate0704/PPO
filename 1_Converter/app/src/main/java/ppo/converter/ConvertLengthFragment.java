package ppo.converter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class ConvertLengthFragment extends Fragment {

    FragmentManager manager;
    FragmentTransaction transaction;

    @Override public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_conversion_activity, container, false);


        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Button listbtn = view.findViewById(R.id.listbtn);

        listbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UnitPickerListDialog myDialogFragment = new UnitPickerListDialog();

//                Dialog dialog = myDialogFragment.getDialog();
//                if (dialog != null)
//                {
////            int width = ViewGroup.LayoutParams.MATCH_PARENT;
////            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
//                    if(dialog.getWindow() != null) {
//                        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
//                        params.x = 50;
//                        params.y = 50;
//                        dialog.getWindow().setAttributes(params);
//
////                dialog.getWindow().setLayout(width, height);
//                        dialog.getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
//                    }
//                }
                manager = getActivity().getSupportFragmentManager();
//                transaction = manager.beginTransaction();
//                transaction.add(R.id.frame, myDialogFragment);
//                transaction.commit();


                myDialogFragment.show(manager, "myDialog");
//                ListView listView = findViewById(R.id.listView);
//                View v = findViewById(R.id.select_)
//
//
//
//                listView.setAdapter(adapter);
            }
        });
    }
}
