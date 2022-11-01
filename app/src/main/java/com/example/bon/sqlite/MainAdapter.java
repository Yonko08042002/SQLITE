package com.example.bon.sqlite;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    //
    Activity activity;
    JSONArray jsonArray;

    DatabaseHelper databaseHelper;

    //
    public  MainAdapter(Activity activity,JSONArray jsonArray){
        this.activity =activity;
        this.jsonArray=jsonArray;
    }
    //
    public void  updateArray(JSONArray jsonArray){
        this.jsonArray =jsonArray;

    }

    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_main,parent,false);
        //Initialize
        databaseHelper =new DatabaseHelper(view.getContext());

        return  new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        try {
            //
            JSONObject object =jsonArray.getJSONObject(position);
            //
            holder.tvText.setText(object.getString("text"));
            //
            holder.tvDate.setText(object.getString("date"));
        }catch (JSONException e){
            e.printStackTrace();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    JSONObject object =jsonArray.getJSONObject(
                            holder.getAdapterPosition()
                    );
                    String sID =object.getString("id");
                    String sText =object.getString("text");

                    //
                    Dialog dialog =new Dialog(activity);
                    //
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(
                            Color.TRANSPARENT
                    ));
                    //Set view
                    dialog.setContentView((R.layout.dialog_main));
                    //Display dialog
                    dialog.show();
                    //
                    EditText editText =dialog.findViewById(R.id.edit_text);
                    Button btUpdate =dialog.findViewById(R.id.bt_submit);
                    //
                    editText.setText(sText);
                    //
                    btUpdate.setText("Update");

                    //
                    btUpdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //
                            String sText = editText.getText().toString().trim();
                            //
                            databaseHelper.update(sID,sText);
                            //
                            updateArray(databaseHelper.getArray());
                            //
                            notifyItemChanged(holder.getAdapterPosition());
                            //
                            dialog.dismiss();
                        }
                    });
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                int position =holder.getAdapterPosition();
                //
                try {
                    //
                    JSONObject object =jsonArray.getJSONObject(position);
                    //
                    String sID =object.getString("id");
                    //
                    AlertDialog.Builder builder =new AlertDialog.Builder(activity);
                    //
                    builder.setTitle("Confirm");
                    //
                    builder.setMessage("Are you sure to delete?");
                    //
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //
                            databaseHelper.delete(sID);
                            //
                            jsonArray.remove(position);
                            //
                            notifyItemRemoved(position);
                            notifyItemRangeChanged(position, jsonArray.length());
                        }
                    });
                    //
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                //
                builder.show();
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        //

        return jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //
        TextView tvText,tvDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //
            tvText =itemView.findViewById(R.id.tv_text);
            tvDate =itemView.findViewById(R.id.tv_date);
        }
    }
}
