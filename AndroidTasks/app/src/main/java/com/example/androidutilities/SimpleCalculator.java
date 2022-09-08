package com.example.androidutilities;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;
import java.text.NumberFormat;


public class SimpleCalculator extends Fragment implements View.OnClickListener {
    private String mString = "";
    private TextView showCalc;
    private Button mButton0,mButton1,mButton2,mButton3,mButton4,mButton5,mButton6,mButton7,mButton8,mButton9,
                    mButtonPlus,mButtonMinus,mButtonDiv,mButtonMulti,mButtonEvaluate,mButtonClear,mButtonDelete;
    private CalculatorData mData = new CalculatorData();

    public SimpleCalculator() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simple_calculator, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        showCalc = view.findViewById(R.id.textview);

        mButton0 = view.findViewById(R.id.button0);
        mButton0.setOnClickListener(this);
        mButton1 = view.findViewById(R.id.button1);
        mButton1.setOnClickListener(this);
        mButton2 = view.findViewById(R.id.button2);
        mButton2.setOnClickListener(this);
        mButton3 = view.findViewById(R.id.button3);
        mButton3.setOnClickListener(this);
        mButton4 = view.findViewById(R.id.button4);
        mButton4.setOnClickListener(this);
        mButton5 = view.findViewById(R.id.button5);
        mButton5.setOnClickListener(this);
        mButton6 = view.findViewById(R.id.button6);
        mButton6.setOnClickListener(this);
        mButton7 = view.findViewById(R.id.button7);
        mButton7.setOnClickListener(this);
        mButton8 = view.findViewById(R.id.button8);
        mButton8.setOnClickListener(this);
        mButton9 = view.findViewById(R.id.button9);
        mButton9.setOnClickListener(this);

        mButtonPlus = view.findViewById(R.id.buttonAdd);
        mButtonPlus.setOnClickListener(this);
        mButtonMinus = view.findViewById(R.id.buttonSub);
        mButtonMinus.setOnClickListener(this);
        mButtonMulti = view.findViewById(R.id.buttonMulti);
        mButtonMulti.setOnClickListener(this);
        mButtonDiv = view.findViewById(R.id.buttonDiv);
        mButtonDiv.setOnClickListener(this);
        mButtonClear = view.findViewById(R.id.buttonClear);
        mButtonClear.setOnClickListener(this);
        mButtonDelete = view.findViewById(R.id.buttonDelete);
        mButtonDelete.setOnClickListener(this);
        mButtonEvaluate = view.findViewById(R.id.buttonEvaluate);
        mButtonEvaluate.setOnClickListener(this);


        /*mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String test = mEditText.getText().toString();
                Expression expression = new ExpressionBuilder(test).build();
                double result = expression.evaluate() c;
                Log.i("testing",String.valueOf(result));
            }
        });*/
        if (savedInstanceState != null) {
            String temp = savedInstanceState.getString("CALC_DATA");
            showCalc.setText(temp);
        }
    }

    @Override
    public void onClick(View v) {
        int viewID = v.getId();
        switch (viewID) {
            case R.id.button1:
                mData.appendString("1");
                showCalc.setText(mData.getString());
                break;
            case R.id.button2:
                mData.appendString("2");
                showCalc.setText(mData.getString());
                break;
            case R.id.button3:
                mData.appendString("3");
                showCalc.setText(mData.getString());
                break;
            case R.id.button4:
                mData.appendString("4");
                showCalc.setText(mData.getString());
                break;
            case R.id.button5:
                mData.appendString("5");
                showCalc.setText(mData.getString());
                break;
            case R.id.button6:
                mData.appendString("6");
                showCalc.setText(mData.getString());
                break;
            case R.id.button7:
                mData.appendString("7");
                showCalc.setText(mData.getString());
                break;
            case R.id.button8:
                mData.appendString("8");
                showCalc.setText(mData.getString());
                break;
            case R.id.button9:
                mData.appendString("9");
                showCalc.setText(mData.getString());
                break;
            case R.id.button0:
                mData.appendString("0");
                showCalc.setText(mData.getString());
                break;
            case R.id.buttonAdd:
                mData.appendString("+");
                showCalc.setText(mData.getString());
                break;
            case R.id.buttonSub:
                mData.appendString("-");
                showCalc.setText(mData.getString());
                break;
            case R.id.buttonMulti:
                mData.appendString("*");
                showCalc.setText(mData.getString());
                break;
            case R.id.buttonDiv:
                mData.appendString("/");
                showCalc.setText(mData.getString());
                break;
            case R.id.buttonEvaluate:
                //showCalc.setText(String.valueOf(mData.evaluateExpression()));
                //mData.setString(String.valueOf(mData.evaluateExpression()));
                NumberFormat format = new DecimalFormat("0.#");
                showCalc.setText(format.format(mData.evaluateExpression()));
                mData.setString(format.format(mData.evaluateExpression()));
                break;
            case R.id.buttonClear:
                mData.clearScreen();
                showCalc.setText(mData.getString());
                break;
            case R.id.buttonDelete:
                if (TextUtils.isEmpty(mData.getString())){
                    Toast.makeText(v.getContext(),"Empty expression",Toast.LENGTH_SHORT).show();
                } else {
                    showCalc.setText(mData.deleteLast());
                }
                break;
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putString("CALC_DATA",mData.getString());
        super.onSaveInstanceState(outState);
    }
}
