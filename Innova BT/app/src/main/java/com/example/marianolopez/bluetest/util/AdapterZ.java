package com.example.marianolopez.bluetest.util;
import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


public class AdapterZ extends ArrayAdapter{
    ArrayList coleccion;
    Activity context;
    ViewHolderZ holder;
    int layout;
    public AdapterZ(Activity context, ArrayList colection, int layout, ViewHolderZ view) {
        super(context, layout, colection);
        this.coleccion = colection;
        this.context=context;
        this.layout = layout;
        this.holder = view;
    }

    public View getView(int position, View convertView,ViewGroup parent){
        //Log.w("ADAPTER","Inflater");
        LayoutInflater inflater = context.getLayoutInflater();
        //Log.w("ADAPTER","layout");
        View item = inflater.inflate(layout, null);
        //Log.w("ADAPTER","fill");
        holder.fill(item, coleccion, position);
        return (item);
    }

}