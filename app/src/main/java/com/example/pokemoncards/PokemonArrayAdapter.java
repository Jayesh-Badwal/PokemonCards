package com.example.pokemoncards;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class PokemonArrayAdapter extends ArrayAdapter {

    public PokemonArrayAdapter(Context context, List<Attributes> resource) {
        super(context,0, resource);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView =convertView;
        if(listItemView==null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.pokemon_list_adapter, parent, false);
        }
        Attributes currPokemon = (Attributes) getItem(position);
        Log.d("ArrayAdapter", currPokemon.name + " " + currPokemon.id);
        TextView textView = listItemView.findViewById(R.id.PokemonListAdapter);
        textView.setText(currPokemon.name);
        return listItemView;
    }

}
