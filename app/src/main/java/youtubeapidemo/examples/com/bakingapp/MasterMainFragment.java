package youtubeapidemo.examples.com.bakingapp;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by 1515012 on 13-07-2017.
 */

public class MasterMainFragment extends Fragment {//implements RecipeAdapter.ListItemClickListener{

    private static final String TAG = MasterMainFragment.class.getSimpleName();
    public static ProgressDialog progressDialog;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    private ArrayList<Recipes> arrayList;
    private RecipeAdapter recipeAdapter;
    private static final int VERTICAL_ITEM_SPACE = 48;

    public MasterMainFragment() {

    }




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_master_list, container, false);
        ButterKnife.bind(this, rootView);

        makeJsonArrayRequest();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        arrayList = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(recipeAdapter);

            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setTitle(getString(R.string.progress_dialog));
            progressDialog.setMessage(getString(R.string.please_wait));

            progressDialog.show();
            recyclerView.addItemDecoration(new VerticalSpaceItemDecoration(VERTICAL_ITEM_SPACE));

        //     recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this,R.drawable.divider));
     /*   recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG,view.getId()+"");
              //  listItemClickListener.onListItemClick(view.getId());
            }
        });*/
        return rootView;
    }

    /**
     * Method to make json array request where response starts with [
     */
    private void makeJsonArrayRequest() {
        String mUrlBaking = "https://go.udacity.com/android-baking-app-json";
        JsonArrayRequest req = new JsonArrayRequest(mUrlBaking,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject person = (JSONObject) response.get(i);
                                String dish_Name = person.getString("name");
                                int id = person.getInt("id");
                                int servings = person.getInt("servings");
                                arrayList.add(new Recipes(id, dish_Name, servings));
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.i(TAG, "Error: " + e.getMessage());

                        }
                        finally {
                            if (progressDialog.isShowing())
                                progressDialog.dismiss();
                        }


                        recipeAdapter.changeData(arrayList);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        MySingleton.getInstance(getActivity()).addToRequestQueue(req);
    }




    /*

    @Override
    public void onListItemClick(int clickedItemIndex) {
        Intent intent = new Intent(getActivity(), IngredientActivity.class);
        intent.putExtra(getString(R.string.LIST_KEY), clickedItemIndex);
        startActivity(intent);
    }
*/


}
