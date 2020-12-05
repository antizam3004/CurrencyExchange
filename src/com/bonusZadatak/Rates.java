package com.bonusZadatak;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.*;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;

//klasa služi da dobije url, pokupi json file sa tog url-a, očita ga i vrati listu vrijednosti valuta

public class Rates{

    ObservableList<String> currencyNameList= FXCollections.observableArrayList();

    public ObservableList<Currency> getRates(String url)
    {
        ObservableList<Currency> exchangeList= FXCollections.observableArrayList();

        try {
            JSONObject jsonObject=(JSONObject) getJsonFromUrl(url).get("rates");
            Iterator<String> keys=jsonObject.keys();
            while(keys.hasNext()) {
                String key = keys.next();
                exchangeList.add(new Currency(key, (Double) jsonObject.get(key)));
                currencyNameList.add(key);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return exchangeList;
    }

    public static JSONObject getJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }

    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public ObservableList<String> getCurrencyNames(){
        return currencyNameList;
    }
}
