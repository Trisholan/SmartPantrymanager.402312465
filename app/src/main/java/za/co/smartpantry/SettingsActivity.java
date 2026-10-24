package za.co.smartpantry;

import android.app.Activity;
import android.os.Bundle;
import android.widget.*;

public class SettingsActivity extends Activity {
    @Override protected void onCreate(Bundle b) {
        super.onCreate(b);
        LinearLayout root=new LinearLayout(this);root.setOrientation(LinearLayout.VERTICAL);root.setPadding(22,22,22,22);
        TextView title=new TextView(this);title.setText("Settings");title.setTextSize(24);root.addView(title);
        TextView about=new TextView(this);about.setText("Smart Pantry Manager\n\nDatabase: SQLite, stored locally on this device.\n\nThe app does not use GPS, maps or location services.\n\nStrict recipe rule: all listed ingredients must exist in the pantry with a quantity of at least 1.0 in the app's current simplified ingredient model.");about.setTextSize(16);about.setPadding(0,20,0,20);root.addView(about);
        CheckBox alerts=new CheckBox(this);alerts.setText("Enable expiry reminders (display preference only)");alerts.setChecked(getPreferences(0).getBoolean("alerts",false));alerts.setOnCheckedChangeListener((v,checked)->getPreferences(0).edit().putBoolean("alerts",checked).apply());root.addView(alerts);
        setContentView(root);
    }
}
