package com.example.prakharagarwal.automaticirrigation;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * A placeholder fragment containing a simple view.
 */
public class AddPresetActivityFragment extends android.app.DialogFragment  implements DatePickerDialog.OnDateSetListener{

    static View rootview;
    static int flag=0;
    public AddPresetActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fragment_add_preset, container, false);
        Button btn1=(Button)rootview.findViewById(R.id.btn1);
        Button btn2=(Button)rootview.findViewById(R.id.btn2);
        Button btn3=(Button)rootview.findViewById(R.id.btn3);
        Button btn4=(Button)rootview.findViewById(R.id.btn4);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=1;
                new AddPresetActivityFragment().show(getFragmentManager(),"Select Date");
            }

        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag=3;
                new AddPresetActivityFragment().show(getFragmentManager(),"Select Date");
            }
        });
        return inflater.inflate(R.layout.fragment_add_preset, container, false);
    }



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm+1, dd);
        }
        public void populateSetDate(int year, int month, int day) {
            if(flag==1){
                Button dob=(Button) rootview.findViewById(R.id.btn1);
                dob.setText(day+"-"+month+"-"+year);
            }else   if(flag==3){
                Button dob = (Button) rootview.findViewById(R.id.btn3);
                dob.setText(day + "-" + month + "/" + year);

            }
        }


}
