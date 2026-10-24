package za.co.smartpantry;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.*;

public class IngredientFormActivity extends Activity {
    DatabaseHelper db; long id=-1; EditText name, qty, unit, expiry;
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b); db=new DatabaseHelper(this); id=getIntent().getLongExtra("id",-1);
        LinearLayout root=new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(22,22,22,22);
        TextView title=new TextView(this); title.setText(id<0?"Add Ingredient":"Edit Ingredient"); title.setTextSize(24); root.addView(title);
        name=field(root,"Ingredient name (e.g. tomato)"); qty=field(root,"Quantity (number)"); unit=field(root,"Unit (e.g. pieces, g, ml)"); expiry=field(root,"Expiry date (optional, YYYY-MM-DD)");
        if(id>=0){ Ingredient x=db.getIngredient(id); if(x!=null){name.setText(x.name);qty.setText(String.valueOf(x.quantity));unit.setText(x.unit);expiry.setText(x.expiry);} }
        Button save=new Button(this); save.setText("Save"); root.addView(save);
        save.setOnClickListener(v -> save());
        setContentView(root);
    }
    EditText field(LinearLayout root,String hint){ EditText e=new EditText(this);e.setSingleLine(true);e.setHint(hint);root.addView(e);return e; }
    void save(){
        String n=name.getText().toString().trim(), q=qty.getText().toString().trim(), u=unit.getText().toString().trim();
        if(n.isEmpty()||q.isEmpty()||u.isEmpty()){Toast.makeText(this,"Name, quantity and unit are required.",Toast.LENGTH_LONG).show();return;}
        double amount; try{amount=Double.parseDouble(q);if(amount<=0)throw new NumberFormatException();}catch(NumberFormatException e){qty.setError("Enter a number greater than zero");return;}
        if(id<0) db.addIngredient(n,amount,u,expiry.getText().toString()); else db.updateIngredient(id,n,amount,u,expiry.getText().toString());
        finish();
    }
}
