package com.miller.tyler.recipebuddy;


import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ConverterFragment extends Fragment {

    private Spinner mUnits;
    private TextView mResult;
    private EditText mUserValue;
    private Button mConvert;
    private double mUnitValue;
    private double mValueToConvert;


    public ConverterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_converter, container, false);

        mUnits = (Spinner) rootView.findViewById(R.id.spinner);
        mResult = (TextView) rootView.findViewById(R.id.result);
        mUserValue = (EditText) rootView.findViewById(R.id.editText);
        mConvert = (Button) rootView.findViewById(R.id.convert_button);

        ArrayAdapter adapter = ArrayAdapter.createFromResource(getActivity(), R.array.convert_to, R.layout.spinner_item);
        mUnits.setAdapter(adapter);

        mUnits.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        mUnitValue = 0.33333333334;
                        break;
                    case 1:
                        mUnitValue = 0.0625;
                        break;
                    case 2:
                        mUnitValue = 0.5;
                        break;
                    case 3:
                        mUnitValue = 0.5;
                        break;
                    case 4:
                        mUnitValue = 0.25;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                if (mUserValue.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "Please enter a Value!", Toast.LENGTH_LONG).show();

                } else {
                    mValueToConvert = Double.parseDouble(mUserValue.getText().toString());
                    String result = String.valueOf(mUnitValue * mValueToConvert);
                    mResult.setText(result);
                    mUserValue.setText(null);

                }
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }




}
