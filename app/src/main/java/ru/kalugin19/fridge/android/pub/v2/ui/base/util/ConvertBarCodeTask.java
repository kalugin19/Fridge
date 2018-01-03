package ru.kalugin19.fridge.android.pub.v2.ui.base.util;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * распознание наименования товара по штрих-коду
 */
public class ConvertBarCodeTask extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        try {
            StringBuilder chaine = new StringBuilder("");
            URL url = new URL("http://www.barcode-list.ru/barcode/DE/%D0%9F%D0%BE%D0%B8%D1%81%D0%BA.htm?barcode=" + params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.connect();
            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = rd.readLine()) != null) {
                chaine.append(line);
            }
            return chaine.toString();
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();
        }
        return null;
    }
}
