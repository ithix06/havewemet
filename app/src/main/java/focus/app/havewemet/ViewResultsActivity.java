package focus.app.havewemet;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

public class ViewResultsActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private static String TAG = "ViewResultsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "NEW ACTIVITY");

        setContentView(R.layout.activity_view_results);

        Intent intent = getIntent();
        String filename = (String) intent.getStringExtra("results");

        FileInputStream inputStream;

        StringBuilder text = new StringBuilder();

        final String folder = Environment.getExternalStorageDirectory() + File.separator  + "havewemetoutput";
        File file = new File(folder, "results.txt");



        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
            }
            br.close();
        } catch (Exception e) {
            Log.i(TAG, "EXCEPTION READING IN FILE: " + e);
        }

        Log.i(TAG, "LOADING: " + text.toString());
        LocationComparer locationComparer = LocationComparer.buildFrom(text.toString());
        TextView textView = (TextView) findViewById(R.id.results_status_text_6);
        textView.setText("Results Size: " + locationComparer.getResults().size());

        TextView resultOne = (TextView) findViewById(R.id.results_status_text_7);
        resultOne.setText(locationComparer.getResults().keySet().iterator().next() + " - " + locationComparer.getResults().entrySet().iterator().next());

        Log.i(TAG, "finishedd!!!!!!!!!!!!!!!!!!: ");

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(new String[]{"hi","what"});    
        mRecyclerView.setAdapter(mAdapter);
    }


}
