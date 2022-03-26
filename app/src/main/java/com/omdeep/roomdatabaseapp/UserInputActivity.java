package com.omdeep.roomdatabaseapp;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_insert:
                User user = new User(binding.etFname.getText().toString(), binding.etLname.getText().toString(),
                        binding.etMobileno.getText().toString());
                new InsertUserAsyncTask().execute(Collections.singletonList(user));
                binding.btnInsert.setVisibility(view.GONE);
                binding.btnCancel.setVisibility(view.GONE);
                binding.btnGetData.setVisibility(view.VISIBLE);
                break;

            case R.id.btn_get_data:
//                new GetDatAsyncTask().execute();
                startActivity(new Intent(UserInputActivity.this, MainActivity.class));
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
}