package focus.app.havewemet;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;

public class GatherInfo extends AppCompatActivity {

    private static final String TAG = "HAVE-WE-MET";
    public final static String EXTRA_MESSAGE = "result-message";

    // Progress Dialog
    private ProgressDialog pDialog;
    public static final int PROGRESS_BAR_TYPE = 0;

    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    //Status View
    private TextView statusTextView;

    private Button mViewResultsButton;

    public final String filename = "results";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "start program");

        setContentView(R.layout.activity_gather_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.statusTextView = (TextView) findViewById(R.id.status_text);

        mViewResultsButton = (Button) findViewById(R.id.view_results_button);
        mViewResultsButton.setText("View Past Results");

        mViewResultsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GatherInfo.this, MapsActivity.class);
                intent.putExtra("results", filename);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);

        verifyStoragePermissions(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Calculate
        final EditText personOneLocationPathEditText = (EditText) findViewById(R.id.file_path_one_edit_text);
        final EditText personTwoLocationPathEditText = (EditText) findViewById(R.id.file_path_two_edit_text);

        Button haveWeMetButton = (Button) findViewById(R.id.have_we_met_button);

        haveWeMetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String personOneFilePath = personOneLocationPathEditText.getText().toString();
                String personTwoFilePath = personTwoLocationPathEditText.getText().toString();

                runHaveWeMetBefore(personOneFilePath, personTwoFilePath);
            }
        });
    }

    private void writeToFile(String data)
    {
        final String path = Environment.getExternalStorageDirectory() + File.separator  + "havewemetoutput";

        // Create the folder.
        File folder = new File(path);
        if(!folder.exists()) {
            // Make it, if it doesn't exit
            folder.mkdirs();
        }

        // Create the file.
        File file = new File(folder, "results.txt");

        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private void runHaveWeMetBefore(final String personOneFilePath, final String personTwoFilePath) {

        mViewResultsButton.setText("RUNNING!");

        new Thread(new Runnable() {
            public void run() {
                HashMap<LatLon, List<Long>> haveWeMetBeforeResults = new HashMap<LatLon, List<Long>>();

                Log.i(TAG, "running " + personOneFilePath);
                LocationComparer locationComparer = new LocationComparer(personOneFilePath, personTwoFilePath);
                try {
                    locationComparer.compareLocations();
                } catch (IOException e) {
                    Log.i(TAG, "ERROR");
                    e.printStackTrace();
                }
                Gson gson = new Gson();

                Log.i(TAG, "Making Large String");
                String resultsString = locationComparer.serialize();
                Log.i(TAG, resultsString);
                Log.i(TAG, "END PRINT Large String");

                writeToFile(resultsString);

                Log.i(TAG, "BACKGROUND THREAD end");

                mViewResultsButton.post(new Runnable() {
                    public void run() {
                        mViewResultsButton.setText("Finished! View Results...");
                    }
                });
            }
        }).start();
    }

    /**
     * Checks if the app has permission to write to device storage
     *
     * If the app does not has permission then the user will be prompted to grant permissions
     *
     * @param activity
     */
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case PROGRESS_BAR_TYPE: // we set this to 0
                pDialog = new ProgressDialog(this);
                pDialog.setMessage("Downloading file. Please wait...");
                pDialog.setIndeterminate(false);
                pDialog.setMax(100);
                pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pDialog.setCancelable(true);
                pDialog.show();
                return pDialog;
            default:
                return null;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gather_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    /**
     * Background Async Task to download file
     * */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog(PROGRESS_BAR_TYPE);
        }

        /**
         * Downloading file in background thread
         * */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = conection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                // Output stream
                String filePath = Environment
                        .getExternalStorageDirectory().toString()
                        + "/have-we-met.json";

                OutputStream output = new FileOutputStream(filePath);

                Log.i(TAG, filePath);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         * */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            dismissDialog(PROGRESS_BAR_TYPE);

        }

    }
}
