package za.co.smartpantry;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "smart_pantry.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE pantry (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity REAL NOT NULL, unit TEXT NOT NULL, expiry TEXT)");
        db.execSQL("CREATE TABLE recipes (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT UNIQUE NOT NULL, ingredients TEXT NOT NULL, method TEXT NOT NULL)");
        seedRecipes(db);
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Add explicit migration statements here when the schema changes.
    }

    private void seedRecipes(SQLiteDatabase db) {
        String[][] data = {
            {"Tomato Egg Scramble","egg,tomato,salt,oil","Beat eggs with salt. Chop tomato. Cook tomato in oil, add eggs and scramble until set."},
            {"Garlic Pasta","pasta,garlic,oil,salt","Boil pasta. Warm chopped garlic in oil, toss with drained pasta and salt."},
            {"Potato Hash","potato,onion,oil,salt","Dice potato and onion. Pan-fry in oil until tender and lightly browned."},
            {"Banana Oat Bowl","banana,oats,milk","Slice banana. Heat oats with milk until soft and top with banana."},
            {"Cheese Toast","bread,cheese,butter","Butter bread, add cheese and toast in a pan or oven until melted."},
            {"Simple Pancakes","flour,egg,milk,sugar","Mix flour, egg, milk and sugar into a smooth batter. Cook small portions in a lightly oiled pan."},
            {"Onion Omelette","egg,onion,salt,oil","Beat eggs with salt. Cook chopped onion in oil, pour in eggs and cook until firm."},
            {"Carrot Soup","carrot,onion,water,salt","Chop carrot and onion. Simmer in water with salt until soft, then mash or blend."},
            {"Apple Oat Porridge","apple,oats,milk","Cook oats in milk. Dice apple and stir through until warmed."},
            {"Tuna Sandwich","bread,tuna,mayonnaise","Mix tuna with mayonnaise and spread between slices of bread."},
            {"Rice and Egg Bowl","rice,egg,oil,salt","Cook rice. Fry or scramble egg in oil and serve over rice with salt."},
            {"Garlic Butter Bread","bread,garlic,butter","Mix crushed garlic into softened butter, spread on bread and toast until golden."},
            {"Spinach Omelette","egg,spinach,salt,oil","Cook spinach briefly in oil. Add beaten salted eggs and cook until set."},
            {"Tomato Cheese Toast","bread,tomato,cheese","Top bread with sliced tomato and cheese. Toast until the cheese melts."},
            {"Peanut Banana Toast","bread,peanut butter,banana","Spread peanut butter on bread and top with banana slices."},
            {"Cucumber Salad","cucumber,tomato,salt,oil","Slice cucumber and tomato. Toss with a little oil and salt."},
            {"Lemon Water","lemon,water","Squeeze lemon into water and stir."},
            {"Sweet Potato Mash","sweet potato,butter,salt","Boil sweet potato until soft, drain and mash with butter and salt."},
            {"Chicken Rice","chicken,rice,onion,salt","Cook rice. Cook chicken and onion thoroughly in a pan, season with salt and serve together."},
            {"Bean Tomato Stew","beans,tomato,onion,salt","Cook chopped onion and tomato until soft. Add cooked beans and salt, simmer."}
        };
        for (String[] r : data) {
            ContentValues v = new ContentValues(); v.put("name", r[0]); v.put("ingredients", r[1]); v.put("method", r[2]);
            db.insertWithOnConflict("recipes", null, v, SQLiteDatabase.CONFLICT_IGNORE);
        }
    }

    public long addIngredient(String name, double qty, String unit, String expiry) {
        ContentValues v = new ContentValues(); v.put("name", clean(name)); v.put("quantity", qty); v.put("unit", unit.trim()); v.put("expiry", expiry.trim());
        return getWritableDatabase().insert("pantry", null, v);
    }
    public int updateIngredient(long id, String name, double qty, String unit, String expiry) {
        ContentValues v = new ContentValues(); v.put("name", clean(name)); v.put("quantity", qty); v.put("unit", unit.trim()); v.put("expiry", expiry.trim());
        return getWritableDatabase().update("pantry", v, "_id=?", new String[]{String.valueOf(id)});
    }
    public int deleteIngredient(long id) { return getWritableDatabase().delete("pantry", "_id=?", new String[]{String.valueOf(id)}); }

    public List<Ingredient> getIngredients() {
        List<Ingredient> result = new ArrayList<>();
        try (Cursor c = getReadableDatabase().rawQuery("SELECT _id,name,quantity,unit,COALESCE(expiry,'') FROM pantry ORDER BY name COLLATE NOCASE", null)) {
            while (c.moveToNext()) result.add(new Ingredient(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4)));
        }
        return result;
    }
    public Ingredient getIngredient(long id) {
        try (Cursor c = getReadableDatabase().rawQuery("SELECT _id,name,quantity,unit,COALESCE(expiry,'') FROM pantry WHERE _id=?", new String[]{String.valueOf(id)})) {
            if (c.moveToFirst()) return new Ingredient(c.getLong(0), c.getString(1), c.getDouble(2), c.getString(3), c.getString(4));
        }
        return null;
    }
    public List<Recipe> getRecipes() {
        List<Recipe> result = new ArrayList<>();
        try (Cursor c = getReadableDatabase().rawQuery("SELECT name,ingredients,method FROM recipes ORDER BY name", null)) {
            while (c.moveToNext()) result.add(new Recipe(c.getString(0), c.getString(1), c.getString(2)));
        }
        return result;
    }

    public List<Recipe> getStrictMatches() {
        List<Recipe> matches = new ArrayList<>();
        List<Ingredient> pantry = getIngredients();
        for (Recipe recipe : getRecipes()) {
            boolean allPresent = true;
            for (String required : recipe.ingredientList()) {
                String req = normalize(required);
                boolean found = false;
                for (Ingredient item : pantry) {
                    if (normalize(item.name).equals(req) && item.quantity >= 1.0) { found = true; break; }
                }
                if (!found) { allPresent = false; break; }
            }
            if (allPresent) matches.add(recipe);
        }
        return matches;
    }

    private String clean(String s) { return s.trim().toLowerCase(Locale.ROOT); }
    private String normalize(String s) {
        String n = s.trim().toLowerCase(Locale.ROOT);
        if (n.endsWith("ies") && n.length() > 4) return n.substring(0, n.length()-3) + "y";
        if (n.endsWith("s") && !n.endsWith("ss") && n.length() > 3) return n.substring(0, n.length()-1);
        return n;
    }
}
