package com.example.edisonoffice.crm_integration;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/* Fragment used as page 1 */
public class Page1Fragment extends Fragment {
    ArrayList aa,aa1,aa2,aa3,aa4;
    String name,name1,name2,name3,name4;
    ListView listview;
    private JSONArray Roomes;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tasks, container, false);
        listview= (ListView) rootView.findViewById(R.id.listview);

       //  googleMap = ((MapView) rootView.findViewById(R.id.YOURMAPID)).getMap();



        Thread t = new Thread() {
            public void run() {
                try {
                    postt();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        return rootView;
    }

    //for HTTP POST
    void postt() throws JSONException {
        // Creating HTTP client
        HttpClient httpClient = new DefaultHttpClient();
        // Creating HTTP Post
        HttpPost httpPost = new HttpPost("http://edisonbro.in/crm.php");


        // Building post parameters
        // key and value pair
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        nameValuePair.add(new BasicNameValuePair("sid", "1"));
        nameValuePair.add(new BasicNameValuePair("status", "sd"));
      /*  nameValuePair.add(new BasicNameValuePair("data",
                "Hi, trying Android HTTP post!"));*/

        // Url Encoding the POST parameters
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        // Making HTTP Request
        try {
            final HttpResponse response = httpClient.execute(httpPost);
            String responseBody = null;

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
            //   JSONArray jsonarray = new JSONArray(responseBody);
                JSONObject jsonObj = new JSONObject(responseBody);
                Roomes = jsonObj.getJSONArray("clients");
                aa = new ArrayList();
                aa1 = new ArrayList();
                aa2 = new ArrayList();
                aa3=new ArrayList();
                aa4=new ArrayList();
                for (int i = 0; i < Roomes.length(); i++) {
                    JSONObject jsonobject = Roomes.getJSONObject(i);
                    
                    name = jsonobject.getString("fname");
                    name1 = jsonobject.getString("contact");
                    name2 = jsonobject.getString("house_no");
                    name3 = jsonobject.getString("house_type");
                    name4 = jsonobject.getString("street_name");
                    aa.add(name);
                    aa1.add(name1);
                    aa2.add(name2);
                    aa3.add(name3);
                    aa4.add(name4);
                    //   Log.d("TAG", aa);
                    //  Toast.makeText(MainActivity.this,"Res"+aa,Toast.LENGTH_LONG).show();
                    //  String url = jsonobject.getString("url");
                }








                Log.d("TAG", "Http post Response: " + responseBody);
            }
            Log.d("TAG", responseBody);

            final String finalResponseBody = responseBody;

            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    //   tv.setText(finalResponseBody);
                    Customadapter disadpt = new Customadapter(getActivity().getApplicationContext(), aa,aa1,aa2,aa3,aa4);
                    listview.setAdapter(disadpt);
                    ArrayList stringArray=aa;
                    Log.d("TAG", String.valueOf(stringArray));
/*

                    for(int i =0;i<stringArray.size();i++) {
                        Toast.makeText(getActivity(), "Text!" + stringArray.get(i), Toast.LENGTH_SHORT).show();
                    }
*/


                }
            });
        } catch (ClientProtocolException e) {
            // writing exception to log
            e.printStackTrace();
        } catch (IOException e) {
            // writing exception to log
            e.printStackTrace();

        } /*catch (JSONException e) {
            e.printStackTrace();
        }*/
    }






    class Customadapter extends BaseAdapter {
    ArrayList result;
    Page1Fragment context;
    private ArrayList<String> fname;
    private ArrayList<String> phno;
    private ArrayList<String> house_no;
    private ArrayList<String> house_type;
    private ArrayList<String> street;
    private Context mContext;
     LayoutInflater layoutInflater;

    double latitute= 21.7679;
    double longitude= 78.8718;

    public Customadapter(Context c, ArrayList<String> fnam, ArrayList<String> phnom, ArrayList<String> house_nom ,ArrayList<String> house_typ, ArrayList<String> stret) {
        // TODO Auto-generated constructor stub
        this.mContext = c;
        this.fname=fnam;
        this.phno = phnom;
        this.house_no = house_nom;
        this.house_type = house_typ;
        this.street = stret;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return fname.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder {

        TextView fname,phnumber,house_no,house_type,stret_nam;
        Button btnaddr;
    }

    public View getView(int pos, View child, ViewGroup parent) {
        Holder mHolder;

        if (child == null) {
            layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            child = layoutInflater.inflate(R.layout.single_list_item_atendee, null);
            mHolder = new Holder();

            //link to TextView
               mHolder.fname = (TextView) child.findViewById(R.id.fname);
               mHolder.phnumber = (TextView) child.findViewById(R.id.phnumber);
            mHolder.house_no = (TextView) child.findViewById(R.id.house_no);
            mHolder.house_type = (TextView) child.findViewById(R.id.house_type);
            mHolder.stret_nam = (TextView) child.findViewById(R.id.stret_nam);
            mHolder.btnaddr= (Button) child.findViewById(R.id.button_Address);

            child.setTag(mHolder);
        } else {
            mHolder = (Holder) child.getTag();
        }


        //transfer to TextView in screen
        //  mHolder.tno.setText(anum.get(pos));
        mHolder.fname.setText(fname.get(pos));
        mHolder.phnumber.setText(phno.get(pos));
        mHolder.house_no.setText(house_no.get(pos));
        mHolder.house_type.setText(house_type.get(pos));
        mHolder.stret_nam.setText(street.get(pos));

        mHolder.btnaddr.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent navigation = new Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://maps.google.com/maps?saddr=START_LAT,START_LON&daddr=END_LAT,END_LON"));
                startActivity(navigation);*/

                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" + "saddr="+ latitute + "," + longitude + "&daddr=" + 79.7400 + "," + 15.9129));
                intent.setClassName("com.google.android.apps.maps","com.google.android.maps.MapsActivity");
                startActivity(intent);

               /* String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitute, longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
               startActivity(intent);*/


            }
        });

        return child;
    }

}








}
