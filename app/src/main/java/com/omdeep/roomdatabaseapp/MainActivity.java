package com.omdeep.roomdatabaseapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Toast;

import com.omdeep.roomdatabaseapp.databinding.ActivityMainBinding;
import com.omdeep.roomdatabaseapp.databinding.ActivityUserInputBinding;
import com.omdeep.roomdatabaseapp.databinding.DialogUpdateRoomBinding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemLongClickListener {
    ActivityMainBinding binding;
//    private ActivityUserInputBinding insert;
    private UserDao dao;
    private UserInputActivity inputActivity;
    private CustomListAdapterForRoomListview adapter;
    private List<User> userList = new ArrayList<>();
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        super.onCreate(savedInstanceState);
        setContentView(binding.getRoot());

        registerForContextMenu(binding.listView);

        dao = AppDatabase.getInstance(MainActivity.this).userDao();
        new GetDatAsyncTask().execute();

        binding.listView.setOnItemLongClickListener(this);

binding.floatingButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        startActivity(new Intent(MainActivity.this, UserInputActivity.class));
    }
});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_create_profile:
                ActivityUserInputBinding binding = ActivityUserInputBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(this);
                dialog.setContentView(binding.getRoot());
                dialog.setCancelable(false);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
//                binding.etFname.setText(user.getFirstName());
//                binding.etLname.setText(user.getLastName());
//                binding.btnInsert.setOnClickListener(new View.OnClickListener() {
////                    @Override
//                    public void onClick(View view) {
//                        new UserInputActivity.InsertUserAsyncTask().execute(Collections.singletonList(user)));
//                    }
//                });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        int id1 = position;
        user = userList.get(position);
        return false;
    }

    class GetDatAsyncTask extends AsyncTask<Void, Void, List<User>> {
        ProgressDialog dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
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
            dialog.dismiss();
            userList.addAll(users);
            adapter = new CustomListAdapterForRoomListview(userList, MainActivity.this);
            binding.listView.setAdapter(adapter);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.profile_context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.item_delete_record:
                new DeleteAsynTask().execute();
                break;

            case R.id.item_update_record:
                DialogUpdateRoomBinding binding = DialogUpdateRoomBinding.inflate(getLayoutInflater());
                Dialog dialog = new Dialog(this);
                dialog.setContentView(binding.getRoot());
                dialog.setCancelable(false);
                dialog.show();
                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                binding.etFname.setText(user.getFirstName());
                binding.etLname.setText(user.getLastName());
                binding.etMnumber.setText(user.getMobileNumber());
                binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new UpdateAsynTask().execute();
                        dialog.dismiss();
                    }
                });
                break;
        }
        return super.onContextItemSelected(item);
    }

    class DeleteAsynTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... strings) {
            String msg = "";
            int i = dao.deleteUser(user);
            if (i>0){
                msg = i+" data deleted;";
            }else {
                msg = "Something went wrong";
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }

    class UpdateAsynTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... strings) {
            String msg = "";
            int i = dao.updateUser(user);
            if (i>0){
                msg = i+" data updated;";
            }else {
                msg = "Something went wrong";
            }
            return msg;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }


}