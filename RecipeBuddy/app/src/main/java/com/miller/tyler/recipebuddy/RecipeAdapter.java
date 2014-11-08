package com.miller.tyler.recipebuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Tyler on 11/8/14.
 */
public class RecipeAdapter extends ArrayAdapter<RecipeData> {

    private Context context;
    private List<RecipeData> recipeDataList;

    public RecipeAdapter(Context context, int resource, List<RecipeData> objects) {
        super(context, resource, objects);
        this.context = context;
        this.recipeDataList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(android.R.layout.simple_list_item_2, parent, false);

        //Display brew name and descrption
        RecipeData recipeData = recipeDataList.get(position);
        TextView name = (TextView) view.findViewById(android.R.id.text1);
        name.setText(recipeData.getName());

        TextView des =  (TextView) view.findViewById(android.R.id.text2);
        des.setText(recipeData.getDescription());

        return view;
    }
}
