package com.example.lab3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {


    int row = 1;
    int column = 1 ;

    TextView lvlView;
    TextView leftView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Button button = findViewById(R.id.button_buld);
        Button button1 = findViewById(R.id.button_ex);
        TableLayout tableLayout = findViewById(R.id.table);
        EditText editRow = findViewById(R.id.row);
        EditText editColumn = findViewById(R.id.column);
        lvlView = findViewById(R.id.text_lvl);
        leftView = findViewById(R.id.text_left);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    row =Integer.parseInt(editRow.getText().toString());
                }catch (Exception e){
                    row = 1;
                }

                try {
                    column =Integer.parseInt(editColumn.getText().toString());
                }catch (Exception e){
                    column = 1;
                }

                if(row == column)
                    createTable(tableLayout , row , column , null);
                else
                    Toast.makeText(getBaseContext() , "Матрица смежности не может быть построена" , Toast.LENGTH_SHORT).show();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] b = {{0,1,0,0,1,0,0}
                        ,{0,1,0,1,0,0,0}
                        ,{0,0,0,1,0,0,0}
                        ,{0,0,0,0,1,0,0}
                        ,{0,0,0,0,1,0,0}
                        ,{0,0,1,0,0,0,1}
                        ,{0,0,0,1,0,1,0}};

                editColumn.setText(b.length + "");
                editRow.setText(b[0].length + "");

                row =Integer.parseInt(editRow.getText().toString());
                column =Integer.parseInt(editColumn.getText().toString());




                createTable(tableLayout ,  7 , 7  , b);
            }
        });

        Button buttonCalculation = findViewById(R.id.button_calculation);
        buttonCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int arr[][] = readTable(row , column);
                int i =0 ;
                String decom = "";
                for(Set<Integer> it : decomposition(arr)){
                    decom += "G" + i++ + "(";
                    for(int num : it){
                        decom += num + " ";
                    }
                    decom += ") " + findDug(numberDug(arr) , it);

                    decom +="\n";
                }
                lvlView.setText(decom);

                leftView.setText(rights(arr));
            }
        });
    }

    static String rights(int[][] arr){
        String s = "";
        for(int i = 0 ; i < arr.length;i++){
            s += "G(" + (i+1) +")=" + rights(arr[i]) + "\n";
        }
        return s;
    }

    static String rights(int[] arr){
        String s = "";
        for(int i = 0; i < arr.length;i++){
            if(arr[i] == 1) s+= i + 1 +" ";
        }
        if(s.equals("")) return "{нет вершин}";
        return "{"+s+"}";
    }

    private String findDug(int[][] arr , Set<Integer> s){
        if(s.size() == 1)
            return "";

        String out = "Дуги (";

        for(int i : s){
            for(int j : s){
                if(arr[i][j] != 0) out += " " + arr[i][j];
            }
        }
        return out + ")";
    }



    private int[][] numberDug(int[][] arr){
        int[][] e  = new int[arr.length][arr.length];
        int num =1;
        for(int i = 0;  i < arr.length;i++){
            for (int j = 0; j < arr.length;j++){
                if(arr[i][j] == 1)e[i][j] = num++;
                else e[i][j] = 0;
            }
        }

        return e;
    }

    ArrayList<Set<Integer>> decomposition(int[][] arr){
        ArrayList<Set<Integer>> arcs = new ArrayList<>();


        Set<Integer> saveH = new HashSet<>();

        for(int i = 0;i < arr.length;i++){
            if(!saveH.contains(i)){

                Set<Integer> a = subGraf(arr , i);
                arcs.add(a);
                saveH.addAll(a);
            }

        }

        return arcs;
    }
    Set<Integer> subGraf(int[][] arr , int n){
        Set<Integer> q = findQ(arr , n);
        Set<Integer> r = findR(arr , n);

        return getIntersection(q , r);
    }

    Set<Integer> getIntersection(Set<Integer> a , Set<Integer> b){
        Set<Integer> intersection = new HashSet<>();

        for(int n : a){
            if(b.contains(n)) intersection.add(n);
        }

        return intersection;
    }

    Set<Integer> findQ(int[][] a, int find){
        Set<Integer> set = new HashSet<>();
        set.add(find);

        return findQ(a , set);
    }
    Set<Integer> findQ(int[][] a ,  Set<Integer> find){
        return findR(t(a) , find);
    }

    Set<Integer> findR(int[][] a , int find){
        Set<Integer> set = new HashSet<>();
        set.add(find);

        return findR(a , set);
    }
    Set<Integer> findR(int[][] a , Set<Integer> find){
        Set<Integer> out = find;

        boolean flag = true;

        while (flag) {
            Set<Integer> iteration = new HashSet<>(out);

            flag = false;
            for (int n : out) {
                Set<Integer> iter = getLine(a, n);
                for (int ni : iter) {
                    if (iteration.add(ni))
                        flag = true;
                }

            }

            out.addAll(iteration);
        }

        return out;

    }

    Set<Integer> getLine(int[][] arr, int n ){
        Set<Integer> out = new HashSet<>();

        for(int i =0 ; i< arr.length;i++){
            if(arr[n][i] == 1) out.add(i);
        }

        return out;
    }


    static int[][] t(int[][] arr){
        int[][] tM = new int[arr.length][arr.length];

        for(int i = 0 ; i < arr.length;i++){
            for(int j = 0 ; j < arr.length;j++){
                tM[i][j] = arr[j][i];
            }
        }

        return tM;
    }

    private int[][] readTable(int row , int colums){
        int arr[][] = new int[row][colums];

        for (int i = 0 ; i < row; i++){
            for(int j = 0; j < colums;j++){
                String text = ((EditText) findViewById(i *100 + j)).getText().toString();

                try {
                    arr[i][j] = Integer.parseInt(text);
                }catch (Exception e){
                    arr[i][j] = 0;
                }
            }
        }

        return arr;
    }

    private void createTable(TableLayout tableLayout , int row , int colums , int[][] arr){
        tableLayout.removeAllViews();

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tableRow.addView(new TextView(this));

        for(int i = 0; i < row; i++){
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(18);
            textView.setText((i + 1) + "  ");

            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);

        for (int i = 0; i < row; i++){


            tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            TextView textView = new TextView(this);
            textView.setText((i+1) + " ");
            textView.setTextSize(18);

            tableRow.addView(textView);

            for(int j = 0; j < colums;j++){
                EditText editText = new EditText(this);
                editText.setId(i *100 + j);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                if(arr != null && arr.length >= i && arr[0].length >= j)
                    editText.setText(arr[i][j] + "");
                tableRow.addView(editText);
            }
            tableLayout.addView(tableRow);
        }
    }

    private void createTable(TableLayout tableLayout, int[][] arr , int[] newOrder){
        tableLayout.removeAllViews();

        int row = arr.length;
        int colums = arr[0].length;

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tableRow.addView(new TextView(this));

        for(int i = 0; i < row; i++){
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(18);
            textView.setText((i + 1) + "(" + newOrder[i] + ") ");

            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);

        for (int i = 0; i < row; i++){


            tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            TextView numberTV = new TextView(this);
            numberTV.setText((i + 1) + "(" + newOrder[i] + ") ");
            numberTV.setTextSize(18);

            tableRow.addView(numberTV);

            for(int j = 0; j < colums;j++){
                TextView cell = new TextView(this);
                cell.setText(arr[i][j] + "");
                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                cell.setTextSize(18);
                tableRow.addView(cell);
            }
            tableLayout.addView(tableRow);
        }
    }
}