package com.example.pokemoncards;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

public class PokemonData extends AppCompatActivity {

    String name;
    String id;
    TextView head;
    ImageView img;
    TextView rank;
    TextView hp;
    TextView evolvesFrom;
    TextView set;
    TextView pokedex;
    TextView types;
    TextView type;
    TextView value;

    String eachrank, eachhp, eachimage, eachevolvesFrom, eachset, eachtype, eachweaktype, eachweakvalue, eachpokedex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_data);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");

        head = findViewById(R.id.pokemonName);
        head.setText(name);

        loadData();
    }

    void loadData() {
        Request request = new Request.Builder().url("https://api.pokemontcg.io/v1/cards/" + id).get().build();
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Toast.makeText(PokemonData.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) throws IOException {
                if (!response.isSuccessful()) {
                    return;
                }
                String respFromApi = response.body().string();
                Log.d("PokemonData", respFromApi);
                try {
                    extractData(respFromApi);
                } catch (Exception e) {
                    Log.d("Error", e.getMessage());
                }
            }
        });
    }

    void extractData(String respFromApi) throws Exception {
        JSONObject initial = new JSONObject(respFromApi);
        JSONObject eachPokemon = initial.getJSONObject("card");
        Log.d("JSONCHECK", eachPokemon.toString());

        if(eachPokemon.has("number")) eachrank = eachPokemon.getString("number");
        else eachrank = "---";
        if(eachPokemon.has("hp")) eachhp = eachPokemon.getString("hp");
        else eachhp = "---";
        if(eachPokemon.has("imageUrlHiRes")) eachimage = eachPokemon.getString("imageUrlHiRes");
        if(eachPokemon.has("evolvesFrom")) eachevolvesFrom = eachPokemon.getString("evolvesFrom");
        else eachevolvesFrom = "---";
        if(eachPokemon.has("set")) eachset = eachPokemon.getString("set");
        else eachset = "---";
        if(eachPokemon.has("nationalPokedexNumber")) eachpokedex = String.valueOf(eachPokemon.getInt("nationalPokedexNumber"));
        else eachpokedex = "---";
        if(eachPokemon.has("types")) {
            String types = eachPokemon.getJSONArray("types").toString();
            String type = "";
            for(int j = 0; j < types.length(); j++) {
                if((types.charAt(j) >= 'a' && types.charAt(j) <= 'z') || (types.charAt(j) >= 'A' && types.charAt(j) <= 'Z'))
                    type = type.concat(String.valueOf(types.charAt(j)));
                else if(types.charAt(j) == ',')
                    type.concat(", ");
            }
            eachtype = type;
        } else eachtype = "---";
        if(eachPokemon.has("weaknesses")) {
            JSONArray types = eachPokemon.getJSONArray("weaknesses");
            JSONObject weaknesses = types.getJSONObject(0);
            eachweaktype = weaknesses.getString("type");
            eachweakvalue = weaknesses.getString("value");
        } else {
            eachweakvalue = "---";
            eachweaktype = "---";
        }

        String test = eachrank + " " + eachhp + " " + eachimage;
        Log.d("Test1", test);

        Asynchronous asynk  = new Asynchronous();
        asynk.execute();
    }
    private class Asynchronous extends AsyncTask<String ,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            return "";
        }
        @Override
        protected void onPostExecute(String None) {
            Log.d("Inside","in");
            img = findViewById(R.id.image);
            Picasso.get().load(eachimage).into(img);

            rank = findViewById(R.id.rank);
            rank.setText("Rank : " + eachrank);

            hp = findViewById(R.id.hp);
            hp.setText("Hp : " + eachhp);

            set = findViewById(R.id.set);
            set.setText("Set : " + eachset);

            evolvesFrom = findViewById(R.id.evolvesFrom);
            evolvesFrom.setText("Evolves from : " + eachevolvesFrom);

            pokedex = findViewById(R.id.pokedex);
            pokedex.setText("National Pokedex Number : " + eachpokedex);

            types = findViewById(R.id.types);
            types.setText("Types : " + eachtype);

            type = findViewById(R.id.type);
            type.setText("Type : " + eachweaktype);

            value = findViewById(R.id.value);
            value.setText("Value : " + eachweakvalue);

        }
    }
}