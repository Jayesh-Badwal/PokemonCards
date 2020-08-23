package com.example.pokemoncards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ListView listAllPokemons;
    List<Attributes> all = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listAllPokemons = findViewById(R.id.allPokemons);
        loadData();
    }

    void loadData() {
        Request req = new Request.Builder().url("https://api.pokemontcg.io/v1/cards").get().build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(req).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if(!response.isSuccessful()) {
                    Toast.makeText(MainActivity.this, "Error in request", Toast.LENGTH_SHORT).show();
                }
                String respFromAPI = response.body().string();
                Log.d("RawData", respFromAPI);
                try {
                    extractData(respFromAPI);
                } catch (Exception e) {
                    Log.d("Error", "Not in json format");
                }
            }
        });
    }

    void extractData(String respFromAPI) throws Exception{
        Log.d("Mydata", respFromAPI);
        JSONObject obj = new JSONObject(respFromAPI);
        JSONArray array = obj.getJSONArray("cards");
        for(int  i = 0; i < array.length(); i++) {
            JSONObject jsonObject = array.getJSONObject(i);
            String a = jsonObject.toString();
            Log.d("msg", a);
            String name = jsonObject.getString("name");
            String id = jsonObject.getString("id");
            Log.d("AllPokemons", name + " " + id);
            Attributes singlePokemon = new Attributes(name, id);
            all.add(singlePokemon);
        }
        Asynchronous asynk  = new Asynchronous();
        asynk.execute();
    }

    private class Asynchronous extends AsyncTask<String ,Void,List<Attributes>> {
        @Override
        protected List<Attributes> doInBackground(String... strings) {
            return all;
        }
        @Override
        protected void onPostExecute(List<Attributes> pokemonClass) {
            final PokemonArrayAdapter pokemonAdapter = new PokemonArrayAdapter(MainActivity.this , pokemonClass);
            listAllPokemons.setAdapter(pokemonAdapter);
            listAllPokemons.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Attributes pokemon = (Attributes) pokemonAdapter.getItem(i);
                    Log.d("SelectedPokemon", pokemon.name + " " + pokemon.id);
                    Intent intent = new Intent(MainActivity.this , PokemonData.class);
                    intent.putExtra("id" , pokemon.id);
                    intent.putExtra("name", pokemon.name);
                    startActivity(intent);
                }
            });
        }
    }
}