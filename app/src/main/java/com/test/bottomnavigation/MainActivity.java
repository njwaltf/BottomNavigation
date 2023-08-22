package com.test.bottomnavigation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bottomNav;
    FrameLayout content;
    private String JSON_URL = "";
    List<ResepModel> resepModels;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        resepModels =  new ArrayList<>();
        recyclerView = findViewById(R.id.recView);

        GetData getData =  new GetData();
        getData.execute();
        bottomNav =  findViewById(R.id.bottomNav);
        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.mainHome){
                    openFragment(new Home());
                    return true;
                }
                if (item.getItemId() == R.id.fav){
                    openFragment(new Favorite());
                    return true;
                }
                if (item.getItemId() == R.id.setting){
                    openFragment(new Setting());
                    return true;
                }
                return false;
            }
        });
        openFragment(new Home());
    }

    public class GetData extends AsyncTask<String ,String ,String>{

        @Override
        protected String doInBackground(String... strings) {
            String current = "";
            try{
                URL url;
                HttpURLConnection urlConnection = null;

                try{
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    InputStream is  = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);

                    int data = isr.read();
                    while (data != -1){
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;

                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } finally {
                    if (urlConnection != null){
                        urlConnection.disconnect();
                    }
                }

            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("resep");
                for (int i = 0;i < jsonArray.length();i++){
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);

                    ResepModel model =  new ResepModel();
                    model.setId(jsonObject1.getString("id"));
                    model.setId(jsonObject1.getString("author"));
                    resepModels.add(model);

                }

            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            PutDataIntoRecylerView(resepModels);
        }
    }

    private void PutDataIntoRecylerView(List<ResepModel> resepList){
        Adaptery adaptery = new Adaptery(this,resepList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adaptery);
    }

    Boolean openFragment (Fragment fragment){
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.content,fragment).commit();
            return true;
        }
        return false;
    }
}