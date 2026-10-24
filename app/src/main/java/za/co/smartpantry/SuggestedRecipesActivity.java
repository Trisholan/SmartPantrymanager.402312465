package za.co.smartpantry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.*;
import java.util.List;

public class SuggestedRecipesActivity extends Activity {
    DatabaseHelper db; ListView list; List<Recipe> recipes;
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); db=new DatabaseHelper(this);
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(16,16,16,16);
        TextView title=new TextView(this);title.setText("Suggested Recipes");title.setTextSize(24);root.addView(title);
        TextView rule=new TextView(this);rule.setText("Only recipes with every required ingredient in your pantry are shown.");rule.setPadding(0,8,0,12);root.addView(rule);
        list=new ListView(this);root.addView(list,new LinearLayout.LayoutParams(-1,0,1));
        list.setOnItemClickListener((p,v,pos,id)->{Intent i=new Intent(this,RecipeDetailActivity.class);i.putExtra("name",recipes.get(pos).name);startActivity(i);});
        setContentView(root);
    }
    @Override protected void onResume(){super.onResume();refresh();}
    void refresh(){recipes=db.getStrictMatches(); if(recipes.isEmpty()){list.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,new String[]{"No recipes match your pantry yet — add more ingredients."}));}else{String[] names=new String[recipes.size()];for(int i=0;i<recipes.size();i++)names[i]=recipes.get(i).name;list.setAdapter(new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,names));}}
}
