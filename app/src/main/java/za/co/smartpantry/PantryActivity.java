package za.co.smartpantry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import java.util.List;

public class PantryActivity extends Activity {
    DatabaseHelper db; ListView list; ArrayAdapter<Ingredient> adapter; List<Ingredient> items;
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); db = new DatabaseHelper(this);
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(16,16,16,16);
        TextView title = new TextView(this); title.setText("My Pantry"); title.setTextSize(24); root.addView(title);
        Button add = new Button(this); add.setText("Add ingredient"); add.setOnClickListener(v -> startActivity(new Intent(this, IngredientFormActivity.class))); root.addView(add);
        list = new ListView(this); root.addView(list, new LinearLayout.LayoutParams(-1,0,1));
        TextView hint = new TextView(this); hint.setText("Tap an item to edit. Long-press an item to delete."); root.addView(hint);
        list.setOnItemClickListener((p,v,pos,id) -> { Intent i = new Intent(this, IngredientFormActivity.class); i.putExtra("id", items.get(pos).id); startActivity(i); });
        list.setOnItemLongClickListener((p,v,pos,id) -> { Ingredient item=items.get(pos); new android.app.AlertDialog.Builder(this).setTitle("Delete ingredient?").setMessage("Delete "+item.name+"?").setNegativeButton("Cancel",null).setPositiveButton("Delete",(d,w)->{db.deleteIngredient(item.id); refresh();}).show(); return true; });
        setContentView(root);
    }
    @Override protected void onResume() { super.onResume(); if(db!=null) refresh(); }
    void refresh() { items=db.getIngredients(); adapter=new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items); list.setAdapter(adapter); }
}
