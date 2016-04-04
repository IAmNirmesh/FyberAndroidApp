package nirmesh.fyberandroidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import nirmesh.fyberandroidapp.Tasks.OffersPullAsyncTask;
import nirmesh.fyberandroidapp.adapter.OfferAdapater;
import nirmesh.fyberandroidapp.utils.Offer;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OffersPullAsyncTask.OffersLoadedCallback {

    public static final String FYBER_OFFERS_URL = "http://api.fyber.com/feed/v1/offers.json";

    public static final String UID_PARAM = "uid";
    public static final String API_KEY_PARAM = "apikey";
    public static final String APPID_PARAM = "appid";
    public static final String PUB0_PARAM = "pub0";
    public static final String HASH_KEY_PARAM = "hashkey";

    public static final String DEVICE_ID_PARAM = "device_id";
    public static final String IP_PARAM = "ip";
    public static final String PAGE_PARAM = "page";
    public static final String PS_TIME_PARAM = "ps_time";
    public static final String TIMESTAMP_PARAM = "timestamp";

    public static final String LOCALE_PARAM = "locale";
    public static final String LOCALE_PARAM_VALUE = "DE";

    public static final String API_KEY_PARAM_TEST = "1c915e3b5d42d05136185030892fbb846c278927";
    public static final String UID_PARAM_TEST = "Rahul";
    public static final String APPID_PARAM_TEST = "2070";

    public static final String DEVICE_ID_PARAM_TEST = "2b6f0cc904d137be2e1730235f5664094b831186";
    public static final String IP_PARAM_TEST = "109.235.143.113";
    public static final String PAGE_PARAM_TEST = "1";
    public static final String PS_TIME_PARAM_TEST = "1312211903";
    public static final String PUB0_PARAM_TEST = "campaign2";
    public static final String TIMESTAMP_PARAM_TEST = "1312553361";


    public static final String PARAMETERS_SEPARATOR = "&";
    public static final String PARAMETER_VALUES_SEPARATOR = "=";

    private EditText uidET;
    private EditText apiKeyET;
    private EditText appIdET;
    private EditText pubOET;
    private Button getOfferButton;
    private ListView offersLV;

    private Map<String, Object> params = new TreeMap<>();

    private List<Offer> offers = new ArrayList<>();

    public OfferAdapater offerAdapater;

    public OffersPullAsyncTask offersPullAsyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uidET = (EditText) findViewById(R.id.uidET);
        apiKeyET = (EditText) findViewById(R.id.apiKeyET);
        appIdET = (EditText) findViewById(R.id.appIdET);
        pubOET = (EditText) findViewById(R.id.pub0ET);

        getOfferButton = (Button) findViewById(R.id.load_offers_button);

        offersLV = (ListView) findViewById(R.id.offers_listview);

        offersLV.setEmptyView(findViewById(R.id.empty_list));

        getOfferButton.setOnClickListener(this);

        offerAdapater = new OfferAdapater(this, R.layout.list_item, offers);

        offersLV.setAdapter(offerAdapater);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == getOfferButton.getId()) {

            params = new TreeMap<>();
            params.put(UID_PARAM, uidET.getText().toString());
            params.put(APPID_PARAM, appIdET.getText().toString());
            params.put(PUB0_PARAM, pubOET.getText());
            params.put(LOCALE_PARAM, LOCALE_PARAM_VALUE);

            params.put(IP_PARAM, IP_PARAM_TEST);
            params.put(PAGE_PARAM, PAGE_PARAM_TEST);
            params.put(TIMESTAMP_PARAM, System.currentTimeMillis() / 1000L);

            buildHashKey();

            offersPullAsyncTask = new OffersPullAsyncTask(params, FYBER_OFFERS_URL);
            offersPullAsyncTask.execute();

            offersPullAsyncTask.setOffersLoadedCallback(this);
        }
    }

    /**
     * build hashkey using sha1 algorithm
     * using params and apikey
     */
    private void buildHashKey() {

        StringBuilder sb = new StringBuilder();

        for(String param : params.keySet()) {
            if(sb.length() > 0) sb.append(PARAMETERS_SEPARATOR);
            sb.append(param).append(PARAMETER_VALUES_SEPARATOR).append(params.get(param));
        }

        sb.append(PARAMETERS_SEPARATOR).append(API_KEY_PARAM_TEST);
        String hashValue = hashSha1(sb.toString());
        params.put(HASH_KEY_PARAM, hashValue);
    }

    /**
     * create sha1
     * @param hashValue
     * @return
     */
    private static String hashSha1(String hashValue) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(hashValue.getBytes("UTF-8"));
            sha1 = byteToHex(crypt.digest());
        } catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch(UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    /**
     * convert byte array to hex string
     * @param hash
     * @return
     */
    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    @Override
    public void setOffers(List<Offer> offers) {
        this.offers.clear();
        this.offers.addAll(offers);

        offerAdapater.notifyDataSetChanged();
    }
}
