package youtubeapidemo.examples.com.bakingapp;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

/* Activity displays all available Recipies*/

public class MainActivity extends AppCompatActivity implements RecipeAdapter.ListItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(Configuration.ORIENTATION_LANDSCAPE==getResources().getConfiguration().orientation) {
            DetailIngredientFragment detailIngredientFragment = new DetailIngredientFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.detail_ingredient_fragment,
                    detailIngredientFragment).commit();
        }
    }
    @Override
    public void onListItemClick(int clickedItemIndex) {
        if(Configuration.ORIENTATION_PORTRAIT==getResources().getConfiguration().orientation) {
            Intent intent = new Intent(MainActivity.this, IngredientActivity.class);
            intent.putExtra(getString(R.string.LIST_KEY), clickedItemIndex);
            startActivity(intent);
        }
        else
        {
            DetailIngredientFragment detailIngredientFragment=new DetailIngredientFragment();
            Bundle args=new Bundle();
            args.putInt("position",clickedItemIndex);
            detailIngredientFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_ingredient_fragment,
                    detailIngredientFragment).commit();

        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
