package com.example.firebasepushnotify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Signup extends AppCompatActivity {
    String[] areas_id;
    String[] areas_name;
    Spinner spinner_area;
    //SharedPreferences sharedPreferences;
    Button button_register;
    int a = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        EditText editText = findViewById(R.id.editText);
        spinner_area = findViewById(R.id.spinner);
        button_register = findViewById(R.id.button_Register);
        getAreasForSpin();
        SharedPreferences sharedPreferences
                = getSharedPreferences("MySharedPref",
                MODE_PRIVATE);
        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String area_name = spinner_area.getSelectedItem().toString();

                FirebaseMessaging.getInstance().subscribeToTopic(area_name.toString())
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                String msg = "Subscribed to " + area_name;
                                if (!task.isSuccessful()) {
                                    msg = "Failed";
                                }
                                Log.d("tag", msg);
                                Toast.makeText(Signup.this, msg, Toast.LENGTH_SHORT).show();
                            }
                        });

                String user_name = editText.getText().toString();
                Intent intent = new Intent(Signup.this,MainActivity.class);


                intent.putExtra("name",user_name);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", user_name);
                editor.commit();
                startActivity(intent);


            }
        });

    }

    private void getAreasForSpin()
    {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(FetchAreaApi.base_url).addConverterFactory(GsonConverterFactory.create()).build();

        FetchAreaApi api = retrofit.create(FetchAreaApi.class);
        Call<List<Areas>> call = api.getAreas();
        call.enqueue(new Callback<List<Areas>>() {
            @Override
            public void onResponse(Call<List<Areas>> call, Response<List<Areas>> response) {
                List<Areas> areasList = response.body();

                //Creating an String array for the ListView
                areas_name = new String[areasList.size()];
                areas_id = new String[areasList.size()];

                //looping through all the heroes and inserting the names inside the string array
                for (int i = 0; i < areasList.size(); i++) {
                    areas_name[i] = areasList.get(i).getName();
                    areas_id[i] = areasList.get(i).getId();
                }
                Log.d("tag",areas_id[0]);
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(Signup.this,android.R.layout.simple_spinner_item, areas_name);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_area.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<Areas>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
