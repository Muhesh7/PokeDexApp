package com.example.pokedex;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class recyclerAdapter extends RecyclerView.Adapter<recyclerAdapter.MyViewHolder> implements Filterable {

    Context mContext;
    ArrayList<pokemon> mModels;
    ArrayList<pokemon> mSearch;
    int flags[]={R.drawable.kanto,R.drawable.johto,R.drawable.hoenn,R.drawable.sinnoh
            ,R.drawable.unova,R.drawable.kalas,R.drawable.alola};
    recyclerViewClick mRecyclerViewClick;
    pokemon mPokemon;
    int[] colors;
    int f;


    @Override
    public Filter getFilter() {
        return exFilter;
    }
private Filter exFilter=new Filter() {
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        ArrayList<pokemon> filteredList =new ArrayList<>();
        if(constraint==null|| constraint.length()==0)
        {
          filteredList.addAll(mSearch);

        }
        else {
            String filterPattern =constraint.toString().toLowerCase().trim();
            for(pokemon item : mSearch)
            {
                if(item.getName().toLowerCase().contains(filterPattern))
                {
                    filteredList.add(item);
                }
            }
        }
        FilterResults filterResults =new FilterResults();
        filterResults.values=filteredList;
        return filterResults;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        mModels.clear();
        mModels.addAll((List) results.values);
        notifyDataSetChanged();

    }
};

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView img;
            TextView text;
            TextView text2;

        public MyViewHolder(View view) {
            super(view);
            if(f==1||f==3) {
                img = view.findViewById(R.id.imagee);
                text = view.findViewById(R.id.desc);
                if (f == 3) {
                    text2 = view.findViewById(R.id.desc2);
                    text2.setText("Region");
                   int width=mContext.getResources().getDisplayMetrics().widthPixels;
                    int height=mContext.getResources().getDisplayMetrics().heightPixels;
                   Log.d("screeen",Integer.toString(width));
                    img.getLayoutParams().width=(width*10)/24;
                    img.getLayoutParams().height=(height*100)/224;
                }
            }
            else if(f==2){
                text = view.findViewById(R.id.maintype);
            }
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


    public recyclerAdapter(Context c, recyclerViewClick recyclerViewClick,int f) {
        mModels = new ArrayList<>();
        mSearch=new ArrayList<>(mModels);
        this.mContext=c;
        this.f=f;
        this.mRecyclerViewClick=recyclerViewClick;
        colors = mContext.getResources().getIntArray(R.array.colors);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = null;
        if(f==1||f==3)
        {  itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview, null);}
        else  if(f==2)
        { itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cardview2, null);
        }

        return new MyViewHolder(itemView);
    }
    public  void pokemonAdapter(ArrayList<pokemon> poke)
    {
       mModels=poke;
       mSearch=new ArrayList<>(poke);
    }
    public void clearApplications() {
        int size = this.mModels.size();
        if (size > 0) {
            for (int i = 0; i < size; i++) {
                mModels.remove(0);
            }

            this.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
 if(f==1) {
     holder.text.setText(mModels.get(position).getName());
     int p = mModels.get(position).getNumber();
     Picasso.with(mContext).load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/" + p + ".png")
             .fit().into(holder.img);
 }
 else  if(f==2){

            holder.text.setText(mModels.get(position).getName());
            holder.text.setBackgroundColor(colors[position]);
        }
 else if(f==3)
 {holder.text.setText(mModels.get(position).getName());
   holder.img.setImageResource(flags[position]);
     Picasso.with(mContext).load(flags[position]).fit().into(holder.img);}

    }

    @Override
    public int getItemCount() {
        return mModels.size();
    }

}