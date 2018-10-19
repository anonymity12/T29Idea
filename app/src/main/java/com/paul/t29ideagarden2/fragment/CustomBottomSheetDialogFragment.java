package com.paul.t29ideagarden2.fragment;

/**
 * Created by paul on 2018/10/19
 * last modified at 23:12.
 * Desc:
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.paul.t29ideagarden2.R;

public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {
    Button sub_button;
    EditText sub_et;
    String finish_descrip;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_bottom_sheet, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sub_button = view.findViewById(R.id.sub_button);
        sub_et = view.findViewById(R.id.sub_et);
        
        sub_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish_descrip = sub_et.getText().toString();
                // TODO: 2018/10/19 插入到数据库，关联的monk下 
            }
        });
    }
}