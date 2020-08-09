package com.zzy.calculater;

import android.graphics.Color;
import android.net.wifi.p2p.WifiP2pManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    private Button delete;
    private Button point;
    private Button finish;
    private TextView textview;
    private Stack<String> numStack;
    private Stack<String> symbleStack;
    private boolean start;
    private String num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        numStack = new Stack<String>();
        symbleStack = new Stack<String>();
        start = true;
        num = "";
        textview = findViewById(R.id.textview);
        finish = findViewById(R.id.button_20);
        delete = findViewById(R.id.button_15);
        point = findViewById(R.id.button_13);
        number n = new number();
        symble s = new symble();
        addButton(R.id.button_1, n);
        addButton(R.id.button_2, n);
        addButton(R.id.button_3, n);
        addButton(R.id.button_5, n);
        addButton(R.id.button_6, n);
        addButton(R.id.button_7, n);
        addButton(R.id.button_9, n);
        addButton(R.id.button_10, n);
        addButton(R.id.button_11, n);
        addButton(R.id.button_14, n);

        addButton(R.id.button_16, s);
        addButton(R.id.button_17, s);
        addButton(R.id.button_18, s);
        addButton(R.id.button_19, s);
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textview.append(".");
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = textview.getText().toString();
                if(s.equals(""))
                {
                    textview.setText(null);
                }
                else
                {
                    textview.setText(s.substring(0,s.length()-1));

                }
            }
        });
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = textview.getText().toString();
                String x;
                int[] index = new int[s.length()];
                index[0] = -1;
                int j = 1;
                for(int i = 0;i<s.length();i++)
                {
                    x = s.substring(i,i+1);
                    if(x.equals("+") || x.equals("-") || x.equals("*") || x.equals("/"))
                    {
                        index[j] = i;
                     //   String m = String.valueOf(index[j]);
                     //   Log.d("Activiti.this",m+"  "+String.valueOf(i));
                        numStack.push(s.substring(index[j-1]+1,index[j]));
                        j++;
                        if (x.equals("+") || x.equals("-"))//因为+-的优先级低，所以应放在栈底
                        {
                            while (!symbleStack.empty())//当符号栈不为空，先清空符号栈
                            {
                                cleanStack();
                            }
                        }
                        symbleStack.push(x);
                    }
                    if(i == s.length()-1)
                    {
                        numStack.push(s.substring(index[j-1]+1,s.length()));
                    }
                }
                while(!symbleStack.empty())
                {
                    cleanStack();
                }
                num = numStack.pop();
                textview.setText(num);
                start = false;
            }
        });
    }

    public void addButton(int id, View.OnClickListener listener)//定义一个一步添加button和listener的方法
    {
        Button button = findViewById(id);
        button.setOnClickListener(listener);
    }

    public double calculate(double a, double b, String sym)
    {
        switch (sym) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                return a / b;
            default:
                return 0;
        }
    }

    public void cleanStack()
    {
        String sym = symbleStack.pop();
        double b = Double.parseDouble(numStack.pop());
        double a = Double.parseDouble(numStack.pop());
        double c = calculate(a, b, sym);
        numStack.push(String.valueOf(c));
    }
    class number implements View.OnClickListener//事件，把数字压入栈
    {
        @Override
        public void onClick(View v) {
            Button button = (Button) v;//获得当前Button
            //  Log.d("MainActivity.this",num);
           if(start == false)
            {
                textview.setText(null);
                start = true;
                num = "";
            }
            textview.append(button.getText().toString());
        }
    }

    class symble implements View.OnClickListener//事件，把符号压入栈
    {

        @Override
        public void onClick(View v) {
            Button button = (Button) v;//获得当前Button
            String sym = button.getText().toString();//获得button的text
            textview.append(sym);
            if(start == false && !num.equals(null))
                start = true;
        }
    }
}
