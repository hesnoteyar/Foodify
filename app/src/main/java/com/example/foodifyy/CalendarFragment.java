package com.example.foodifyy;

import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.util.Date;
import java.util.List;

public class CalendarFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private List<Date> dates;

    public CalendarFragment() {
        // Required empty public constructor
    }

    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_calendar, container, false);

        final TextView resultTextView = v.findViewById(R.id.resultTextView);
        EditText search = v.findViewById(R.id.search);
        Button searchButton = v.findViewById(R.id.searchButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchTerm = search.getText().toString(); // Replace with your actual search term
                new FetchFoodInfoTask(resultTextView).execute(searchTerm);
            }
        });

        return v;
    }

    private static class FetchFoodInfoTask extends AsyncTask<String, Void, FoodInfo> {
        private TextView resultTextView;

        public FetchFoodInfoTask(TextView resultTextView) {
            this.resultTextView = resultTextView;
        }

        @Override
        protected FoodInfo doInBackground(String... params) {
            String searchTerm = params[0];
            return FoodNutrientsApi.getFoodInfo(searchTerm);
        }

        @Override
        protected void onPostExecute(FoodInfo foodInfo) {
            if (foodInfo != null) {
                resultTextView.setText(String.format("Fats: %.1f g \nProtein: %.1f g \nCalories: %.1f kcal",
                        foodInfo.getFats(), foodInfo.getProtein(), foodInfo.getCalories()));
            } else {
                resultTextView.setText("No data found");
                Log.e("FoodNutrientsApi", "API response is null");
            }
        }
    }
}
