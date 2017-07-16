package youtubeapidemo.examples.com.bakingapp;

/**
 * Created by 1515012 on 09-07-2017.
 */

public class Recipes {
    private int id, servings;
    private String dish_Name;


    public Recipes(int id, String dish_name, int servings) {
        this.id = id;
        this.dish_Name = dish_name;
        this.servings = servings;
    }

    public int getId() {
        return id;
    }

    public int getServings() {
        return servings;
    }

    public String getDish_Name() {
        return dish_Name;
    }


}
