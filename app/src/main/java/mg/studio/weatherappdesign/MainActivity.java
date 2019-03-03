package mg.studio.weatherappdesign;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.widget.Toast;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private String cityName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void btnClick(View view) {
        new DownloadUpdate().execute();
        Toast.makeText(MainActivity.this, "Information Has Updated", Toast.LENGTH_SHORT).show();
    }


    private class DownloadUpdate extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            cityName = "Chongqing";
            String stringUrl = "http://api.openweathermap.org/data/2.5/forecast?q=";
            stringUrl += cityName + ",cn&mode=json&APPID=56bf9b35eec17e6d1f754768a5bf70eb";

            HttpURLConnection urlConnection = null;
            BufferedReader reader;

            try {
                URL url = new URL(stringUrl);

                // Create the request to get the information from the server, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();

                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Mainly needed for debugging
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                //The temperature
                return buffer.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String temperature) {
            //Update the temperature displayed
            ((TextView) findViewById(R.id.temperature_of_the_day)).setText(temperature);
            //Update the location displayed
            ((TextView) findViewById(R.id.tv_location)).setText(cityName);
            //Update the date displayed
            Calendar.getInstance().setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
            String year = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
            String month = String.valueOf(Calendar.getInstance().get(Calendar.MONTH) + 1);
            String day = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            if (Integer.parseInt(month) < 10) {
                month = "0" + month;
            }
            if (Integer.parseInt(day) < 10) {
                day = "0" + day;
            }
            ((TextView) findViewById(R.id.tv_date)).setText(day + '/' + month + '/' + year);
            //Update the week displayed
            String[] weekValue = {"SUNDAY", "MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY"};
            int week = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
            ((TextView) findViewById(R.id.tv_day0)).setText(weekValue[week]);
            ((TextView) findViewById(R.id.tv_day1)).setText(weekValue[(week + 1) % 7].substring(0, 3));
            ((TextView) findViewById(R.id.tv_day2)).setText(weekValue[(week + 2) % 7].substring(0, 3));
            ((TextView) findViewById(R.id.tv_day3)).setText(weekValue[(week + 3) % 7].substring(0, 3));
            ((TextView) findViewById(R.id.tv_day4)).setText(weekValue[(week + 4) % 7].substring(0, 3));
        }
    }
}