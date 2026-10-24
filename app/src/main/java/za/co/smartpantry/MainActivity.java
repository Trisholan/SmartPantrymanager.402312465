package za.co.smartpantry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout root = new LinearLayout(this); root.setOrientation(LinearLayout.VERTICAL); root.setPadding(28,40,28,28);
        root.setGravity(Gravity.CENTER_HORIZONTAL); root.setBackgroundColor(0xFFF5F7F2);
        TextView title = new TextView(this); title.setText("Smart Pantry Manager"); title.setTextSize(27); title.setTypeface(null, Typeface.BOLD); title.setTextColor(0xFF245B3A);
        root.addView(title);
        TextView intro = new TextView(this); intro.setText("Cook with what you already have. Reduce food waste one meal at a time."); intro.setTextSize(16); intro.setPadding(0,18,0,24);
        root.addView(intro);
        addButton(root, "My Pantry", PantryActivity.class);
        addButton(root, "Suggested Recipes", SuggestedRecipesActivity.class);
        addButton(root, "Settings", SettingsActivity.class);
        setContentView(root);
    }
    private void addButton(LinearLayout root, String label, Class<?> target) {
        Button button = new Button(this); button.setText(label); button.setOnClickListener(v -> startActivity(new Intent(this, target)));
        root.addView(button, new LinearLayout.LayoutParams(-1, -2));
    }
}
