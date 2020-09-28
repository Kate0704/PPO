package ppo.converter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class UnitPickerListDialog extends DialogFragment {

    DBHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;
    ListView listView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

//        final String[] catNames = new String[]{
//                "Рыжик", "Барсик", "Мурзик", "Мурка", "Васька",
//                "Томасина", "Барсик", "Мурзик", "Мурка", "Васька",
//                "Томасина", "Барсик", "Мурзик", "Мурка", "Васька",
//                "Томасина", "Барсик", "Мурзик", "Мурка", "Васька",
//                "Томасина", "Барсик", "Мурзик", "Мурка", "Васька",
//                "Томасина"
//        };

        View v = inflater.inflate(R.layout.fragment_custom_dialog, container, false);
        listView = v.findViewById(R.id.listView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        if (getActivity() != null) {
            databaseHelper = new DBHelper(getActivity().getApplicationContext());
            databaseHelper.create_db();
        }

        Button btn_cancel = v.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

        db = databaseHelper.open();
        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("select * from "+ DBHelper.TABLE, null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {DBHelper.COLUMN_NAME, DBHelper.COLUMN_CUT};
        boolean f = userCursor.moveToFirst();
        int d = userCursor.getCount();
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(getContext(), R.layout.list_item,
                userCursor, headers, new int[]{android.R.id.text1, android.R.id.text2}, 0);
        listView.setAdapter(userAdapter);

//        final ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
//                android.R.layout.simple_list_item_1, catNames);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

//        listView.setAdapter(adapter);
        getView().setBackgroundResource(R.drawable.dialog_shape);
        if(getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        builder.setView(getView());

        Dialog dialog = getDialog();
        assert dialog != null;
        Window window = dialog.getWindow();
        assert window != null;

        int width = getResources().getDimensionPixelSize(R.dimen.dialog_width);
        int height = getResources().getDimensionPixelSize(R.dimen.dialog_height);

        window.setLayout(width, height);
        window.setGravity(Gravity.BOTTOM);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        TextView txt = getActivity().findViewById(R.id.listbtn);
        txt.setTextColor(getResources().getColor(R.color.colorText));
        db.close();
        userCursor.close();
    }
}