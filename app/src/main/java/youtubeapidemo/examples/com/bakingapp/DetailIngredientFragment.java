package youtubeapidemo.examples.com.bakingapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
 * Created by 1515012 on 15-07-2017.
 */

public class DetailIngredientFragment extends Fragment {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.cook_custom_button)
    Button cookingButton;


    public static final String TAG = DetailIngredientFragment.class.getSimpleName();
    private IngredientAdapter ingredientAdapter;
    private int pos;
    private ArrayList<String> arrayList;
    private ArrayList<Description> descriptionArrayList;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_detail_ingredient, container, false);
        ButterKnife.bind(this, root);
        /*    progressDialog = new ProgressDialog(getActivity());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setCancelable(false);
            progressDialog.setIndeterminate(false);
            progressDialog.setTitle(getString(R.string.progress_dialog));
            progressDialog.setMessage(getString(R.string.please_wait));
            progressDialog.show();*/

        Intent intent = getActivity().getIntent();

        arrayList = new ArrayList<>();

        descriptionArrayList = new ArrayList<>();
        if (intent != null) {
            pos = intent.getIntExtra(getString(R.string.LIST_KEY), 0);
        } else {

            pos = getArguments().getInt("position");
        }
        makeJsonArrayRequest();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        ingredientAdapter = new IngredientAdapter(getActivity(), arrayList);
        recyclerView.setAdapter(ingredientAdapter);
        if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            RecyclerView.OnItemTouchListener disabler = new RecyclerViewDisabler();
            recyclerView.addOnItemTouchListener(disabler);
        }
        //   if(getActivity().getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE)
        /*    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.FILL_PARENT);

        params.gravity = Gravity.TOP;

        cooingButton.setLayoutParams(params);*/

        cookingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DescriptionActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", descriptionArrayList);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        return root;
    }


    private void makeJsonArrayRequest() {
        String mUrlBaking = "https://go.udacity.com/android-baking-app-json";
        JsonArrayRequest req = new JsonArrayRequest(mUrlBaking,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        String ingredientsRequirent = null;
                        try {
                            JSONObject recipe = (JSONObject) response.get(pos);
                            JSONArray ingredients = recipe.getJSONArray("ingredients");
                            for (int j = 0; j < ingredients.length(); j++) {
                                JSONObject ingr = (JSONObject) ingredients.get(j);
                                ingredientsRequirent = ingr.getString("ingredient") + " - " +
                                        ingr.getInt("quantity") + " " + ingr.getString("measure");
                                Log.i(TAG, ingredientsRequirent);
                                arrayList.add(ingredientsRequirent);
                            }
                            JSONArray steps = recipe.getJSONArray("steps");
                            for (int i = 0; i < steps.length(); i++) {
                                JSONObject step = (JSONObject) steps.get(i);
                                int id = step.getInt("id");
                                String shortDescription = step.getString("shortDescription");
                                String describe = step.getString("description");
                                String videoURL = step.getString("videoURL");
                                descriptionArrayList.add(new Description(id, shortDescription, describe, videoURL));
                            }
                            ingredientAdapter.changeData(arrayList);
                       /*      if (progressDialog.isShowing())
                                    progressDialog.dismiss();*/


                        } catch (JSONException e) {

                            e.printStackTrace();
                            Log.i(TAG, "Error: " + e.getMessage());
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });

        MySingleton.getInstance(getActivity()).addToRequestQueue(req);

    }


    public class RecyclerViewDisabler implements RecyclerView.OnItemTouchListener {

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            return true;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
}
