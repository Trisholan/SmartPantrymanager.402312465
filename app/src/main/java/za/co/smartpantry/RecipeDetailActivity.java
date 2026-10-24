package za.co.smartpantry;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;
import java.util.List;

public class RecipeDetailActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); DatabaseHelper db=new DatabaseHelper(this); String target=getIntent().getStringExtra("name"); Recipe selected=null;
        List<Recipe> recipes=db.getRecipes(); for(Recipe r:recipes)if(r.name.equals(target)){selected=r;break;}
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(22,22,22,22);
        if(selected!=null){TextView title=new TextView(this);title.setText(selected.name);title.setTextSize(25);root.addView(title);
            TextView ingredients=new TextView(this);ingredients.setText("Required ingredients\n"+selected.ingredients.replace(",", "\n"));ingredients.setTextSize(17);ingredients.setPadding(0,20,0,20);root.addView(ingredients);
            TextView method=new TextView(this);method.setText("Method\n"+selected.method);method.setTextSize(17);root.addView(method);
        } else {TextView error=new TextView(this);error.setText("Recipe not found.");root.addView(error);}
        setContentView(root);
    }
}
