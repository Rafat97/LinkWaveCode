package com.example.linkwave;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IndevidualPlaceAdapter extends RecyclerView.Adapter<IndevidualPlaceAdapter.MyViewHolder> {


    List<String> AllItems;
    List<PlaceInformation> AllItemsPlaceClass;
    Context myContext;

   /* public IndevidualPlaceAdapter(List<String> allItems, Context myContext) {
        AllItems = allItems;
        this.myContext = myContext;
    }
*/
    public IndevidualPlaceAdapter(List<PlaceInformation> allItemsPlaceClass, Context myContext) {
        AllItemsPlaceClass = allItemsPlaceClass;
        this.myContext = myContext;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

        TextView myTextView;
        public MyViewHolder(View v) {
            super(v);
            myTextView = (TextView) v.findViewById(R.id.PlaceName);

        }
    }




    @NonNull
    @Override
    public IndevidualPlaceAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.individual_place,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final IndevidualPlaceAdapter.MyViewHolder holder, final int position) {
        //String val = this.AllItems.get(position);
        PlaceInformation place = this.AllItemsPlaceClass.get(position);

        holder.myTextView.setText(place.getName());
        holder.myTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(myContext,PlaceDetailsActivity.class);
                it.putExtra("ItemClickedPosition",position);
                myContext.startActivity(it);
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.AllItemsPlaceClass.size();
    }
}
