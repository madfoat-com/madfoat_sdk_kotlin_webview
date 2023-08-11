package com.madfoat.madfoatsample_androidx;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.madfoat.madfoatsdklib.activity.WebviewActivity;
import com.madfoat.madfoatsdklib.entity.request.payment.Address;
import com.madfoat.madfoatsdklib.entity.request.payment.App;
import com.madfoat.madfoatsdklib.entity.request.payment.Billing;
import com.madfoat.madfoatsdklib.entity.request.payment.MobileRequest;
import com.madfoat.madfoatsdklib.entity.request.payment.MobileRequestSeamless;
import com.madfoat.madfoatsdklib.entity.request.payment.Name;
import com.madfoat.madfoatsdklib.entity.request.payment.Paymethod;
import com.madfoat.madfoatsdklib.entity.request.payment.Repeat;
import com.madfoat.madfoatsdklib.entity.request.payment.Tran;
import com.madfoat.madfoatsdklib.entity.response.payment.MobileResponse;
import com.madfoat.madfoatsdklib.entity.response.status.StatusResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.Random;

@Keep
public class MainActivity extends AppCompatActivity {

    EditText text_language, text_currency;
    EditText phone, email;
    private String amount = "1000"; // Just for testing
    WebView webView;

    private static final int REQ_PERMISSION = 1233;
    public static final String KEY = "pQ6nP-7rHt@5WRFv";// "pQ6nP-7rHt@5WRFv";//"pcL58-59R5k~4pjn";//"pQ6nP-7rHt@5WRFv";//"kCB5C#cg9MK@Njqp";//"pQ6nP-7rHt@5WRFv";//"s66Mx#BMN5-djBWj"; //"BwxtF~dq9L#xgWZb";//pQ6nP-7rHt@5WRFv        // TODO: Insert your Key here
    public static final String STORE_ID = "15996";//"15996";//"15164";//"15996"; //"20880";// "15996";//"18503";//"21941";   //15996           // TODO: Insert your Store ID here
    public static final boolean isSecurityEnabled = true;      // Mark false to test on simulator, True to test on actual device and Production
    public static final int REQUEST_CODE = 100;

    public static String token = "";
    public static String tokenFlag="true";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQ_PERMISSION);
        }
//        FL.init(new FLConfig.Builder(this)
//                .minLevel(FLConst.Level.V)
//                .logToFile(true)
//                .dir(new File(Environment.getExternalStorageDirectory(), "file_logger_demo"))
//                .retentionPolicy(FLConst.RetentionPolicy.FILE_COUNT)
//                .build());
//        FL.setEnabled(true);
        text_language = findViewById(R.id.text_language);
        text_currency = findViewById(R.id.text_currency);
        phone = findViewById(R.id.phone);
        email = findViewById(R.id.email);
        webView = findViewById(R.id.webview);
        webView.setWebViewClient(new MyWebViewClient());
        getSavedCardDetails();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            if (grantResults.length > 0 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                finish();
            }
        }
    }
    private void getSavedCardDetails() {
        RequestQueue queue = SingletonRequestQueue.getInstance(getApplicationContext()).getRequestQueue();
        VolleyLog.DEBUG = true;
        String uri = appurl.listlinkurl;

        JSONObject jsonObject = new JSONObject();
        JSONObject loginObj = new JSONObject();
        try {
            jsonObject.put("storeid", "15996"); //15996
            jsonObject.put("authkey", "pQ6nP-7rHt@5WRFv");//pQ6nP-7rHt@5WRFv
            jsonObject.put("custref", "556"); //789
            jsonObject.put("testmode", "1"); //1
            loginObj.put("SavedCardListRequest", jsonObject);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(uri, loginObj, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                VolleyLog.wtf(response.toString());
                //   Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                JSONObject jsonloginresponse = null;
                try {

                    jsonloginresponse = new JSONObject(response.toString());

                    JSONObject MerchantLoginResponse = jsonloginresponse.getJSONObject("SavedCardListResponse");
                    String status = MerchantLoginResponse.getString("Status");
                    if (status.equalsIgnoreCase("Success")) {
                        String data = MerchantLoginResponse.getString("data").toString();
                        Log.e("Success", "-" + data);
//                        FL.v(data);
                        String url = null;
//                        try {
                        url = "https://secure.telr.com/jssdk/v2/token_frame.html?sdk=ios&store_id="+STORE_ID+"&currency="+text_currency.getText().toString()+"&test_mode=1&saved_cards=" + URLEncoder.encode(data);
                        String strNew = url.replace("+", " ");
//                        } catch (UnsupportedEncodingException e) {
//                            e.printStackTrace();
//                            Log.e("Encode Error:",e.toString());
//                        }
                        Log.e("URL:", strNew);
//                        FL.v("tokenurl:"+strNew);
                        WebSettings webSettings = webView.getSettings();
                        webSettings.setJavaScriptEnabled(true);
                        webSettings.setUseWideViewPort(true);
                        webSettings.setLoadWithOverviewMode(true);
                        webSettings.setDefaultTextEncodingName("utf-8");
                        webView.loadUrl(strNew);

                        webView.requestFocus();

                        webView.setWebChromeClient(new WebChromeClient() {
                            public boolean onConsoleMessage(ConsoleMessage cmsg) {

                                /* process JSON */
                                cmsg.message();
                                Log.e("Message:", cmsg.message().toString());
//                                FL.e(cmsg.message());

                                return true;

                            }
                        });
                    }
//                } ////////
                    else {
                        token="null";
                        tokenFlag="false";
                        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
//                    FL.e(e);
                    writeLogToFile(MainActivity.this);
                } catch (NullPointerException ne) {
                    ne.printStackTrace();
//                    FL.e(ne);
                }

            }
        }, errorListener) {

        };

        queue.add(jsonObjectRequest);

    }

    Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            if (error instanceof NetworkError) {
                Toast.makeText(getApplicationContext(), "No network available", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }
    };


    public void sendMessage(View view) {


//        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
//         intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        EditText editText = (EditText) findViewById(R.id.text_amount);
        amount = editText.getText().toString();
        Log.e("tokenflag",tokenFlag);
        if(tokenFlag=="false"){
            Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
            intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequest());
            intent.putExtra("tokenFlag",tokenFlag);

            intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.telr.SuccessTransationActivity");
            intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.telr.FailedTransationActivity");
            intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled);
//        startActivity(intent);

            startActivityForResult(intent, REQUEST_CODE);
        }
        else
        {
            Intent intent = new Intent(MainActivity.this, WebviewActivity.class);
            intent.putExtra(WebviewActivity.EXTRA_MESSAGE, getMobileRequestSeamless());
            intent.putExtra("tokenFlag",tokenFlag);
            intent.putExtra(WebviewActivity.SUCCESS_ACTIVTY_CLASS_NAME, "com.telr.SuccessTransationActivity");
            intent.putExtra(WebviewActivity.FAILED_ACTIVTY_CLASS_NAME, "com.telr.FailedTransationActivity");
            intent.putExtra(WebviewActivity.IS_SECURITY_ENABLED, isSecurityEnabled);
//        startActivity(intent);

            startActivityForResult(intent, REQUEST_CODE);
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);
        //writeLogToFile(MainActivity.this);
        if (requestCode == REQUEST_CODE &&
                resultCode == RESULT_OK) {
            String paymentMethod = intent.getStringExtra("auth");
            if (paymentMethod.equalsIgnoreCase("yes")) {
                MobileResponse status = (MobileResponse) intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                builder1.setMessage("Thank you! The transaction is " + status.getAuth().getMessage());
                builder1.setCancelable(true);

                builder1.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
//                                webView.clearCache(true);
//                                getSavedCardDetails();
//                                webView.setVisibility(View.VISIBLE);
                            }
                        });


                AlertDialog alert11 = builder1.create();
                alert11.show();
//                TextView textView = (TextView)findViewById(R.id.text_payment_result);
//                TextView txt_code=(TextView)findViewById(R.id.txt_code);
//                txt_code.setText("Code : "+intent.getStringExtra("Code"));
//                textView.setText(textView.getText() +" : " + status.getTrace());
                //  Log.e("CODEZZZ",":"+ TelrSharedPreference.getInstance(this).getDataFromPreference("Code"));

                if (status.getAuth() != null) {

                    AlertDialog.Builder builder2 = new AlertDialog.Builder(MainActivity.this);
                    builder2.setMessage("Thank you! The transaction is " + status.getAuth().getMessage());
                    builder2.setCancelable(true);

                    builder2.setPositiveButton(
                            "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
//                                    webView.clearCache(true);
//                                    getSavedCardDetails();
//                                    webView.setVisibility(View.VISIBLE);
                                }
                            });


                    AlertDialog alert12 = builder2.create();
                    alert12.show();
//
//                    FL.d(status.getAuth().getStatus());
                    status.getAuth().getStatus();   // Authorisation status. A indicates an authorised transaction. H also indicates an authorised transaction, but where the transaction has been placed on hold. Any other value indicates that the request could not be processed.
                    status.getAuth().getAvs();      /* Result of the AVS check:
                                            Y = AVS matched OK
                                            P = Partial match (for example, post-code only)
                                            N = AVS not matched
                                            X = AVS not checked
                                            E = Error, unable to check AVS */
                    status.getAuth().getCode();     // If the transaction was authorised, this contains the authorisation code from the card issuer. Otherwise it contains a code indicating why the transaction could not be processed.
                    status.getAuth().getMessage();  // The authorisation or processing error message.
                    status.getAuth().getCa_valid();
                    status.getAuth().getCardcode(); // Code to indicate the card type used in the transaction. See the code list at the end of the document for a list of card codes.
                    status.getAuth().getCardlast4();// The last 4 digits of the card number used in the transaction. This is supplied for all payment types (including the Hosted Payment Page method) except for PayPal.
                    status.getAuth().getCvv();      /* Result of the CVV check:
                                           Y = CVV matched OK
                                           N = CVV not matched
                                           X = CVV not checked
                                           E = Error, unable to check CVV */
                    status.getAuth().getTranref(); //The payment gateway transaction reference allocated to this request.
                    status.getAuth().getCard().getFirst6(); // The first 6 digits of the card number used in the transaction, only for version 2 is submitted in Tran -> Version
                    status.getAuth().getCard().getCountry();

                    status.getAuth().getCard().getExpiry().getMonth();
                    status.getAuth().getCard().getExpiry().getYear();
                }

            } else {
                StatusResponse status = (StatusResponse) intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);

                AlertDialog.Builder builder11 = new AlertDialog.Builder(MainActivity.this);
                builder11.setMessage("Thank you! The transaction is " + status.getAuth().getMessage());
                builder11.setCancelable(true);

                builder11.setPositiveButton(
                        "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
//                                webView.clearCache(true);
//                                getSavedCardDetails();
//                                webView.setVisibility(View.VISIBLE);
                            }
                        });


                AlertDialog alert111 = builder11.create();
                alert111.show();
//
//                TextView textView = (TextView) findViewById(R.id.text_payment_result);
//                TextView txt_code = (TextView) findViewById(R.id.txt_code);
//                txt_code.setText("Code : " + intent.getStringExtra("Code"));
//                textView.setText(textView.getText() + " : " + status.getTrace());
                //  Log.e("CODEZZZ",":"+ TelrSharedPreference.getInstance(this).getDataFromPreference("Code"));

                if (status.getAuth() != null) {
//                    FL.d(status.getAuth().getStatus());
                    status.getAuth().getStatus();   // Authorisation status. A indicates an authorised transaction. H also indicates an authorised transaction, but where the transaction has been placed on hold. Any other value indicates that the request could not be processed.
                    status.getAuth().getAvs();      /* Result of the AVS check:
                                            Y = AVS matched OK
                                            P = Partial match (for example, post-code only)
                                            N = AVS not matched
                                            X = AVS not checked
                                            E = Error, unable to check AVS */
                    status.getAuth().getCode();     // If the transaction was authorised, this contains the authorisation code from the card issuer. Otherwise it contains a code indicating why the transaction could not be processed.
                    status.getAuth().getMessage();  // The authorisation or processing error message.
                    status.getAuth().getCa_valid();
                    status.getAuth().getCardcode(); // Code to indicate the card type used in the transaction. See the code list at the end of the document for a list of card codes.
                    status.getAuth().getCardlast4();// The last 4 digits of the card number used in the transaction. This is supplied for all payment types (including the Hosted Payment Page method) except for PayPal.
                    status.getAuth().getCvv();      /* Result of the CVV check:
                                           Y = CVV matched OK
                                           N = CVV not matched
                                           X = CVV not checked
                                           E = Error, unable to check CVV */
                    status.getAuth().getTranref(); //The payment gateway transaction reference allocated to this request.
                    status.getAuth().getCard().getFirst6(); // The first 6 digits of the card number used in the transaction, only for version 2 is submitted in Tran -> Version
                    status.getAuth().getCard().getCountry();

                    status.getAuth().getCard().getExpiry().getMonth();
                    status.getAuth().getCard().getExpiry().getYear();
                }
            }
//            StatusResponse status = intent.getParcelableExtra(WebviewActivity.PAYMENT_RESPONSE);
//            if(status.getAuth()!= null) {
//                Log.d("DataVal:",  status.getAuth().getCard().getFirst6());
//                Log.d("Code", intent.getStringExtra("Code"));
//
//
//            }

        }
    }


    public void writeLogToFile(Context context) {
        BufferedWriter bufferedWriter = null;

        try {
            final File file = new File("ddddd");
            bufferedWriter = new BufferedWriter(new FileWriter(file,true));
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(process.getInputStream()));

            String oneLine;
            while ((oneLine= bufferedReader.readLine()) != null) {
                bufferedWriter.write(oneLine);
                bufferedWriter.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private MobileRequest getMobileRequest() {
        MobileRequest mobile = new MobileRequest();
        mobile.setStore(STORE_ID);                       // Store ID
        mobile.setKey(KEY);                              // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
        App app = new App();
        app.setId("123456789");                          // Application installation ID
        app.setName("TelrSDK");                          // Application name
        app.setUser("123456");                           // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
        app.setVersion("0.0.1");                         // Application version
        app.setSdk("123");
        mobile.setApp(app);
        Tran tran = new Tran();
        tran.setTest("1");       //1                        // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.setType("auth");                           /* auth Transaction type
                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
                                                        */

        tran.setClazz("paypage");                       // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.setCartid(String.valueOf(new BigInteger(128, new Random()))); //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
        tran.setDescription("Test Mobile API");         // Transaction description
        tran.setLanguage(text_language.getText().toString());
        tran.setCurrency(text_currency.getText().toString());                        // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
        tran.setAmount(amount);                         // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
        //  tran.setRef("030026794329");                                // (Optinal) Previous transaction reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.

        //040023303844  //030023738912
        // tran.setFirstref("030023738912");             // (Optinal) Previous user transaction detail reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.

        mobile.setTran(tran);
        Billing billing = new Billing();
        Address address = new Address();

        address.setCity("Dubai");                       // City : the minimum required details for a transaction to be processed
        address.setCountry("AE");                       // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
        address.setRegion("Dubai");                     // Region
        address.setLine1("SIT GTower");                 // Street address – line 1: the minimum required details for a transaction to be processed
        //address.setLine2("SIT G=Towe");               // (Optinal)
        //address.setLine3("SIT G=Towe");               // (Optinal)
        //address.setZip("SIT G=Towe");                 // (Optinal)
        billing.setAddress(address);
        Name name = new Name();
        name.setFirst("Divya");                          // Forename : .1the minimum required details for a transaction to be processed
        name.setLast("Thampi");                          // Surname : the minimum required details for a transaction to be processed
        name.setTitle("Mrs");                           // Title
        billing.setName(name);
        billing.setEmail("test@telr.com"); //Replace with email id//stackfortytwo@gmail.com : the minimum required details for a transaction to be processed.
        billing.setPhone("1234567890"); //Replace with phone number
        mobile.setBilling(billing);
        mobile.setCustref("556"); // 789new parameter added for saved card feature
//        Paymethod paymethod=new Paymethod();
////        if(isSeamlessEnabled){
//        paymethod.setType("card");
//   if(token.toString().length()>1)
//   {
//       paymethod.setCardtoken(token);
//   }
//   else
//   {
//       paymethod.setCardtoken("null");
//   }
//        mobile.setPaymethod(paymethod);
//        }
        Log.e("REQUEST-----:",mobile.toString());
//        FL.d("Request:"+mobile.toString());
        return mobile;

    }


    private MobileRequestSeamless getMobileRequestSeamless() {
        MobileRequestSeamless mobiles = new MobileRequestSeamless();
        mobiles.setStore(STORE_ID);                       // Store ID
        mobiles.setKey(KEY);                              // Authentication Key : The Authentication Key will be supplied by Telr as part of the Mobile API setup process after you request that this integration type is enabled for your account. This should not be stored permanently within the App.
        App app = new App();
        app.setId("123456789");                          // Application installation ID
        app.setName("TelrSDK");                          // Application name
        app.setUser("123456");                           // Application user ID : Your reference for the customer/user that is running the App. This should relate to their account within your systems.
        app.setVersion("0.0.1");                         // Application version
        app.setSdk("123");
        mobiles.setApp(app);
        Tran tran = new Tran();
        tran.setTest("1");       //1                        // Test mode : Test mode of zero indicates a live transaction. If this is set to any other value the transaction will be treated as a test.
        tran.setType("paypage");                           /* auth Transaction type
                                                            'auth'   : Seek authorisation from the card issuer for the amount specified. If authorised, the funds will be reserved but will not be debited until such time as a corresponding capture command is made. This is sometimes known as pre-authorisation.
                                                            'sale'   : Immediate purchase request. This has the same effect as would be had by performing an auth transaction followed by a capture transaction for the full amount. No additional capture stage is required.
                                                            'verify' : Confirm that the card details given are valid. No funds are reserved or taken from the card.
                                                        */

        tran.setClazz("sale");                       // Transaction class only 'paypage' is allowed on mobile, which means 'use the hosted payment page to capture and process the card details'
        tran.setCartid(String.valueOf(new BigInteger(128, new Random()))); //// Transaction cart ID : An example use of the cart ID field would be your own transaction or order reference.
        tran.setDescription("Test Mobile API Div");         // Transaction description
        tran.setLanguage(text_language.getText().toString());
        tran.setCurrency(text_currency.getText().toString());                        // Transaction currency : Currency must be sent as a 3 character ISO code. A list of currency codes can be found at the end of this document. For voids or refunds, this must match the currency of the original transaction.
        tran.setAmount(amount);                         // Transaction amount : The transaction amount must be sent in major units, for example 9 dollars 50 cents must be sent as 9.50 not 950. There must be no currency symbol, and no thousands separators. Thedecimal part must be separated using a dot.
        //  tran.setRef("030026794329");                                // (Optinal) Previous transaction reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.

        //040023303844  //030023738912
        // tran.setFirstref("030023738912");             // (Optinal) Previous user transaction detail reference : The previous transaction reference is required for any continuous authority transaction. It must contain the reference that was supplied in the response for the original transaction.

        mobiles.setTran(tran);
        Billing billing = new Billing();
        Address address = new Address();

        address.setCity("Dubai");                       // City : the minimum required details for a transaction to be processed
        address.setCountry("AE");                       // Country : Country must be sent as a 2 character ISO code. A list of country codes can be found at the end of this document. the minimum required details for a transaction to be processed
        address.setRegion("Dubai");                     // Region
        address.setLine1("SIT Tower");                 // Street address – line 1: the minimum required details for a transaction to be processed
        //address.setLine2("SIT G=Towe");               // (Optinal)
        //address.setLine3("SIT G=Towe");               // (Optinal)
        //address.setZip("SIT G=Towe");                 // (Optinal)
        billing.setAddress(address);
        Name name = new Name();
        name.setFirst("Divya");                          // Forename : .1the minimum required details for a transaction to be processed
        name.setLast("Thampi");                          // Surname : the minimum required details for a transaction to be processed
        name.setTitle("Mrs");                           // Title
        billing.setName(name);
        billing.setEmail("test@telr.com"); //Replace with email id//stackfortytwo@gmail.com : the minimum required details for a transaction to be processed.
        billing.setPhone("1234567890"); //Replace with phone number
        mobiles.setBilling(billing);
        //---------------------------------------------------
        Repeat repeat=new Repeat();
        repeat.setAmount("1");
        repeat.setInterval("1");
        repeat.setPeriod("m");
        repeat.setTerm("3");
        repeat.setFinals("");
        repeat.setStart("27062023");
        mobiles.setRepeat(repeat);
        //---------------------------------------------------
        mobiles.setCustref("556"); //789 new parameter added for saved card feature
        Paymethod paymethod=new Paymethod();
//        if(isSeamlessEnabled){
        paymethod.setType("card");
        paymethod.setCardtoken(token);

        mobiles.setPaymethod(paymethod);
//        }
        Log.e("REQUEST-----:",mobiles.toString());
//        FL.d("Request:"+mobiles.toString());
        return mobiles;

    }


    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);

            view.setVisibility(View.GONE);
            Log.e("Token url-------:",url);
            String segments[] = url.split("=");
// Grab the last segment
            String document = segments[segments.length - 1];

            token=document.toString();
            Log.e("Token -------:",token);
//            workwithNewFlow(token);
            return true;
        }
    }


}
