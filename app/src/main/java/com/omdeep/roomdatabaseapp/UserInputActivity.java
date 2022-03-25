package com.omdeep.roomdatabaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.omdeep.roomdatabaseapp.databinding.ActivityUserInputBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UserInputActivity extends AppCompatActivity implements View.OnClickListener{
    private ActivityUserInputBinding binding;
    private UserDao dao;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityUserInputBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());
        dao = AppDatabase.getInstance(UserInputActivity.this).userDao();

        binding.btnInsert.setOnClickListener(this);
        binding.btnGetData.setOnClickListener(this);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.profile_options_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.item_create_profile:
//                Dialog dialog = new Dialog(this);
//                dialog.setCancelable(false);
//                dialog.show();
//                Window window = dialog.getWindow();
//                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
////                binding.etFname.setText(user.getFirstName());
////                binding.etLname.setText(user.getLastName());
//               binding.btnInsert.setOnClickListener(new View.OnClickListener() {
//                   @Override
//                   public void onClick(View view) {
//                       switch (view.getId()) {
//                           case R.id.btn_insert:
//                               User user = new User(binding.etFname.getText().toString(), binding.etLname.getText().toString(),
//                                       binding.etMobileno.getText().toString());
//                               new InsertUserAsyncTask().execute(Collections.singletonList(user));
//
//                               break;
//                           case R.id.btn_get_data:
//                               startActivity(new Intent(UserInputActivity.this, MainActivity.class));
////                               new GetDatAsyncTask().execute();
//                               break;
//                       }
//                   }
//               });
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
//                ActivityUserInputBinding binding1 = ActivityUserInputBinding.inflate(getLayoutInflater());
//                Dialog dialog = new Dialog(this);
//                dialog.setContentView(binding.getRoot());
//                dialog.setCancelable(false);
//                dialog.show();
//                Window window = dialog.getWindow();
//                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                User user = new User(binding.etFname.getText().toString(), binding.etLname.getText().toString(),
                        binding.etMobileno.getText().toString());
                new InsertUserAsyncTask().execute(Collections.singletonList(user));
                binding.btnInsert.setVisibility(view.GONE);
                binding.btnCancel.setVisibility(view.GONE);
                binding.btnGetData.setVisibility(view.VISIBLE);
                break;

            case R.id.btn_get_data:
                startActivity(new Intent(UserInputActivity.this, MainActivity.class));
                new GetDatAsyncTask().execute();
                break;
        }

    }
    class InsertUserAsyncTask extends AsyncTask<List<User>, Void, List<Long>> {

        @Override
        protected List<Long> doInBackground(List<User>... lists) {
            List<User> userList = lists[0];
            ArrayList<User> arrayList = new ArrayList<>(userList);
            List<Long> insertedRows = dao.insertData(arrayList.toArray(arrayList.toArray(new User[]{})));
            return insertedRows;
        }

        @Override
        protected void onPostExecute(List<Long> longs) {
            super.onPostExecute(longs);
            Utility.showLongToast(UserInputActivity.this, longs.size()+" data inserted...");
        }
    }

    class GetDatAsyncTask extends AsyncTask<Void, Void, List<User>>{
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UserInputActivity.this);
            dialog.setMessage("Please wait...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected List<User> doInBackground(Void... voids) {
            List<User> userList = dao.getAllData();
            return userList;
        }

        @Override
        protected void onPostExecute(List<User> users) {
            super.onPostExecute(users);
            Log.d("TAG:", ""+users.size());
        }
    }
}