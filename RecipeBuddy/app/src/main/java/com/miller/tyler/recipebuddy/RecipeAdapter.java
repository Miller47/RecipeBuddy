package com.miller.tyler.recipebuddy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
        View view = inflater.inflate(R.layout.recipe_list, parent, false);

        //Display recipe name and descrption
        RecipeData recipeData = recipeDataList.get(position);
        TextView name = (TextView) view.findViewById(R.id.name_text);
        name.setText(recipeData.getName());

        TextView time = (TextView) view.findViewById(R.id.cook_time);


        int dur = recipeData.getTotalTime();
        int min = dur / 60;
        int sec = dur % 60;

        if (dur == 0) {
            time.setText("N/A");
        } else {

            String cookTime = "Time to Cook: " + min + ":" + sec;
            time.setText(cookTime);
        }

        TextView des =  (TextView) view.findViewById(R.id.source_text);
        des.setText(recipeData.getDescription());

        ImageView imageView = (ImageView) view.findViewById(R.id.recipe_image);

        Picasso.with(getContext())
                .load(recipeData.getImageUrl())
                .into(imageView);

        return view;
    }
}
