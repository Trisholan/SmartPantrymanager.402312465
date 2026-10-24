package za.co.smartpantry;

import java.util.Arrays;
import java.util.List;

public class Recipe {
    public final String name, ingredients, method;
    public Recipe(String name, String ingredients, String method) {
        this.name = name; this.ingredients = ingredients; this.method = method;
    }
    public List<String> ingredientList() { return Arrays.asList(ingredients.split(",")); }
}
