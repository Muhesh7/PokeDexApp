package com.example.pokedex;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TypesAdapter extends RecyclerView.Adapter<TypesAdapter.MyViewHolder>{
    Context mContext;
    ArrayList<mainType> mModels;
    ArrayList<mainType> mSearch;
    recyclerViewClick mRecyclerViewClick;
    @NonNull
    @Override
    public TypesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, null);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TypesAdapter.MyViewHolder holder, int position) {
        holder.text.setText(mModels.get(position).getPokemon().getName());
        int p = mModels.get(position).getPokemon().getNumber();
        Picasso.with(mContext).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + p + ".png")
                .into(holder.img);
    }

    public TypesAdapter(Context context,recyclerViewClick recyclerViewClick) {
        mContext = context;
        mRecyclerViewClick = recyclerViewClick;
        mModels = new ArrayList<>();
    }
    public void pokemonAdapter(ArrayList<mainType> poke)
    {
        mModels=poke;
        mSearch=new ArrayList<>(poke);
    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
        TextView text;
        public MyViewHolder(@NonNull View view) {
            super(view);
            img =view.findViewById(R.id.imagee);
            text = view.findViewById(R.id.desc);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRecyclerViewClick.onItemClick(getAdapterPosition());

                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mRecyclerViewClick.onLongerClick(getAdapterPosition());
                    return true;
                }
            });

        }
    }
}
