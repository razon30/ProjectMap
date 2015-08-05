package com.example.razon30.projectmap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by razon30 on 05-08-15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolderBoxOffice> {

    private ArrayList<String> listPlaces = new ArrayList<>();
    private LayoutInflater layoutInflater;
    Context context;
    String name;

    public RecyclerAdapter(Context context, ArrayList<String> listPlaces,String name) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.listPlaces = listPlaces;
        this.name = name;
        notifyDataSetChanged();

    }

    @Override
    public ViewHolderBoxOffice onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.recycler_item, parent, false);
        ViewHolderBoxOffice viewHolder = new ViewHolderBoxOffice(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolderBoxOffice holder, int position) {
        String place = listPlaces.get(position);
        holder.placeTV.setText(place);

    }


    @Override
    public int getItemCount() {
        return listPlaces.size();
    }

    public class ViewHolderBoxOffice extends RecyclerView.ViewHolder {

        TextView placeTV;

        public ViewHolderBoxOffice(View itemView) {
            super(itemView);
            placeTV = (TextView) itemView.findViewById(R.id.tv);
        }
    }


}
