package com.example.bon.sqlite;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Initialize variable
    RecyclerView recyclerView;
    FloatingActionButton btAdd;

    DatabaseHelper  databaseHelper;
    MainAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView =findViewById(R.id.recycler_view);
        btAdd =findViewById(R.id.bt_add);

        databaseHelper =new DatabaseHelper(getApplicationContext());
        //
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //
        adapter =new MainAdapter(this,databaseHelper.getArray());
        //
        recyclerView.setAdapter(adapter);
        //
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                Dialog dialog = new Dialog(MainActivity.this);
                //
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                        Color.TRANSPARENT
                ));
                //Set view
                dialog.setContentView(R.layout.dialog_main);
                //
                dialog.show();

                //
                EditText editText =dialog.findViewById(R.id.edit_text);
                Button btSubmit =dialog.findViewById(R.id.bt_submit);

                //
                btSubmit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String sText = editText.getText().toString().trim();
                        //
                        String sDate =new SimpleDateFormat("dd MMM yyyy",
                                Locale.getDefault()).format(new Date());
                        //
                        databaseHelper.insert(sText,sDate);
                        //
                        adapter.updateArray(databaseHelper.getArray());
                        //
                        dialog.dismiss();
                    }
                });
                //
                btAdd.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(
                                MainActivity.this
                        );
                        //
                        builder.setTitle("Confirm");
                        //
                        builder.setMessage("Are you sure to delete all?");
                        //
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //
                                databaseHelper.truncate();
                                //
                                adapter.updateArray(databaseHelper.getArray());
                                //
                                recyclerView.setAdapter(adapter);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Close Dialog
                                dialogInterface.dismiss();
                            }
                        });
                        builder.show();
                        return false;
                    }
                });
            }
        });
    }
}