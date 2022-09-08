package com.example.androidutilities;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class CalculatorData {
    private String mString = "";

    CalculatorData(){
    }

    public void appendString(String s){
        mString = mString + s;
    }

    public String getString() {
        return mString;
    }

    public void setString(String s) {
        mString = s;
    }

    public void clearScreen(){
        mString = "";
    }

    public double evaluateExpression(){
        Expression expression = new ExpressionBuilder(mString).build();
        return expression.evaluate();
    }

    public String deleteLast(){
        mString = mString.substring(0,mString.length()-1);
        return mString;
    }
}
