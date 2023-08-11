package com.madfoat.madfoatsdklib.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import androidx.annotation.Keep;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.BuildConfig;
import com.orhanobut.logger.DiskLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.telr.mobile.sdk.R;
import com.telr.mobile.sdk.entity.request.payment.MobileRequest;
import com.telr.mobile.sdk.entity.request.payment.MobileRequestSeamless;
import com.telr.mobile.sdk.entity.request.status.StatusRequest;
import com.telr.mobile.sdk.entity.response.payment.MobileResponse;
import com.telr.mobile.sdk.entity.response.status.StatusResponse;
import com.telr.mobile.sdk.service.InitiatePaymentListener;
import com.telr.mobile.sdk.service.PaymentService;
import com.telr.mobile.sdk.service.PaymentServiceSeamless;
import com.telr.mobile.sdk.service.StatusListener;
import com.telr.mobile.sdk.service.TelrSharedPreference;
import com.telr.mobile.sdk.utils.LogUtils;
import com.telr.mobile.sdk.webservices.PaymentTask;
import com.telr.mobile.sdk.webservices.PaymentTaskSeamless;
import com.telr.mobile.sdk.webservices.StatusTask;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@Keep
public class WebviewActivity extends AppCompatActivity implements InitiatePaymentListener, StatusListener {
    SharedPreferences sharedpreferences;
    public static final String EXTRA_MESSAGE = "com.telr.mobile.sdk.MESSAGE";
    private static final String url = "https://secure.telr.com/gateway/mobile.xml";
    private static final String completeUrl = "https://secure.telr.com/gateway/mobile_complete.xml";
//    private static final String url = "https://uat-secure.telrdev.com/gateway/mobile.xml";
//    private static final String completeUrl = "https://uat-secure.telr.com/gateway/mobile_complete.xml";
    public static final String SUCCESS_ACTIVTY_CLASS_NAME = "successClass";
    public static final String FAILED_ACTIVTY_CLASS_NAME = "failedClass";
    public static final String IS_SECURITY_ENABLED = "securityEnabled";
    public static final String PAYMENT_RESPONSE = "paymentResponse";

    public static final String MOBILE_REQUEST = "mobileRequest";

    public static final String SUCCESS_ACTIVTY_NAME = "successActivtyName";
    public static final String FAILED_ACTIVITY_NAME = "failedActivityName";
    public static final String IS_SECURITY_ENABLED1 = "isSecurityEnabled";
    public static final String TOKENFLAG="tokenFlag";
    private static final String TAGG = WebviewActivity.class.getName();

    private String successActivtyName;
    private String failedActivityName;
    private boolean isSecurityEnabled;
    private MobileRequest mobileRequest;
    private MobileRequestSeamless mobileRequestSeamless;
    private PaymentService paymentService;
    private PaymentServiceSeamless paymentServiceSeamless;
    private TelrSharedPreference telrSharedPreference;
    private PaymentTask paymentTask;
    private PaymentTaskSeamless paymentTaskSeamless;
    private WebView webView=null;
    private StatusTask statusTask;
    private boolean isErrorOcuur;
    ProgressBar progressBar;
    String token;

    String url1;
    private  ValueCallback<Uri[]> uploadMessageCallback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
      //  Log.e(TAGG,"onCreate");

        try {
            webView =findViewById(R.id.webview);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.getSettings().setAllowContentAccess(true);
            webView.getSettings().setAllowFileAccess(true);
            webView.getSettings().setDomStorageEnabled(true);
            webView.getSettings().setSupportMultipleWindows(true);
            progressBar = findViewById(R.id.progressBar);
            paymentService = new PaymentService();
            paymentServiceSeamless=new PaymentServiceSeamless();
            telrSharedPreference=new TelrSharedPreference(WebviewActivity.this);
            Intent intent = getIntent();


            token = intent.getStringExtra(TOKENFLAG);
            if(token.equalsIgnoreCase("true")){

                mobileRequestSeamless=(MobileRequestSeamless)intent.getParcelableExtra(EXTRA_MESSAGE);
                successActivtyName = intent.getStringExtra(SUCCESS_ACTIVTY_CLASS_NAME);
                failedActivityName = intent.getStringExtra(FAILED_ACTIVTY_CLASS_NAME);
                isSecurityEnabled = intent.getBooleanExtra(IS_SECURITY_ENABLED, true);
                paymentServiceSeamless.isValidRequestSeamless(this,mobileRequestSeamless, successActivtyName, failedActivityName, isSecurityEnabled);
                mobileRequestSeamless.setDevice(paymentService.getDeviceDetails(WebviewActivity.this));
                paymentServiceSeamless.updateSDKVersion(mobileRequestSeamless);
            }
            else
            {
                mobileRequest = (MobileRequest) intent.getParcelableExtra(EXTRA_MESSAGE);
                successActivtyName = intent.getStringExtra(SUCCESS_ACTIVTY_CLASS_NAME);
                failedActivityName = intent.getStringExtra(FAILED_ACTIVTY_CLASS_NAME);
                isSecurityEnabled = intent.getBooleanExtra(IS_SECURITY_ENABLED, true);
                paymentService.isValidRequest(this,mobileRequest, successActivtyName, failedActivityName, isSecurityEnabled);
                mobileRequest.setDevice(paymentService.getDeviceDetails(WebviewActivity.this));
                paymentService.updateSDKVersion(mobileRequest);
            }


        } catch (final Exception e) {
            // isErrorOcuur=true;
            showAlertDialog(e.getMessage());
        }
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("message");

        Logger.clearLogAdapters();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
//        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("My custom tag")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        Logger.addLogAdapter(new DiskLogAdapter());


        Logger.w("no thread info and only 1 method");

        Logger.clearLogAdapters();
        formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
        Logger.i("no thread info and method info");

        Logger.t("tag").e("Custom tag for only one use");


        Logger.clearLogAdapters();
        formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .tag("MyTag")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        Logger.w("my log message with my tag");
        saveLogcatToFile(WebviewActivity.this);
    }

    private void saveLogcatToFile(WebviewActivity webviewActivity) {
        String fileName = "logcat_"+System.currentTimeMillis()+".txt";
        File outputFile = new File(webviewActivity.getExternalCacheDir(),fileName);
        try {
            @SuppressWarnings("unused")
            Process process = Runtime.getRuntime().exec("logcat -f "+outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        return;
////        LogUtils.logError(TAGG,"onBackPressed");
////        Log.e("onBackPressed:",mobileRequest.getTran().getClazz());
//        if(mobileRequest.getTran().getClazz().equalsIgnoreCase("cont")){
//            webView.stopLoading();
//            webView.destroy();
//            statusTask.cancel(true);
//            webView.clearHistory();
//            webView.destroy();
//
//           // this.finish();
//            Log.e("onBackPressed:","Do nothing");
//           // finish();
//        }
//        else
//        {
//            if (webView != null)
//            {
//                webView.clearHistory();
//                webView.destroy();
//                webView=null;
//                closeRunningTasks();
//                super.onBackPressed();
//            }
//        }
//        super.onBackPressed();

    }

    @Override
    public void onResume() {
        LogUtils.logError(TAGG, "onResume");
        if (token.equalsIgnoreCase("true")) {
            if (paymentTaskSeamless == null) {
                LogUtils.logError(TAGG,"onResume paymentTask null");
                paymentTaskSeamless = new PaymentTaskSeamless(this);
                if (mobileRequestSeamless != null) paymentTaskSeamless.execute(url, mobileRequestSeamless);
            }
            else {
                Log.e(TAGG,"onResume notifyAllDiv");
                //  paymentTask.notifyAll();
            }
        } else{
            if (paymentTask == null) {
                LogUtils.logError(TAGG,"onResume paymentTask null");
                paymentTask = new PaymentTask(this);
                if (mobileRequest != null) paymentTask.execute(url, mobileRequest);
            }
            else {
                Log.e(TAGG,"onResume notifyAllDiv");
                //  paymentTask.notifyAll();
            }
    }


        super.onResume();
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        LogUtils.logError(TAGG,"onSaveInstanceState");
        if (token.equalsIgnoreCase("true")) {
            savedInstanceState.putParcelable(MOBILE_REQUEST, mobileRequestSeamless);
            savedInstanceState.putString(SUCCESS_ACTIVTY_NAME, successActivtyName);
            savedInstanceState.putString(FAILED_ACTIVITY_NAME, failedActivityName);
            savedInstanceState.putBoolean(IS_SECURITY_ENABLED1, isSecurityEnabled);
            closeRunningTasks();
        }
        else
        {
            savedInstanceState.putParcelable(MOBILE_REQUEST, mobileRequest);
            savedInstanceState.putString(SUCCESS_ACTIVTY_NAME, successActivtyName);
            savedInstanceState.putString(FAILED_ACTIVITY_NAME, failedActivityName);
            savedInstanceState.putBoolean(IS_SECURITY_ENABLED1, isSecurityEnabled);
            closeRunningTasks();
        }

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        LogUtils.logError(TAGG,"onRestoreInstanceState");
        if (token.equalsIgnoreCase("true")) {
            mobileRequestSeamless = savedInstanceState.getParcelable(MOBILE_REQUEST);
            successActivtyName = savedInstanceState.getString(SUCCESS_ACTIVTY_NAME);
            failedActivityName = savedInstanceState.getString(FAILED_ACTIVITY_NAME);
            isSecurityEnabled = savedInstanceState.getBoolean(IS_SECURITY_ENABLED1);
        }
        else
        {
            mobileRequest = savedInstanceState.getParcelable(MOBILE_REQUEST);
            successActivtyName = savedInstanceState.getString(SUCCESS_ACTIVTY_NAME);
            failedActivityName = savedInstanceState.getString(FAILED_ACTIVITY_NAME);
            isSecurityEnabled = savedInstanceState.getBoolean(IS_SECURITY_ENABLED1);
        }

    }


    private void showAlertDialog(final String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.dismiss();
                        try {
                            Intent intent = new Intent(WebviewActivity.this, Class.forName(failedActivityName));
                            intent.putExtra(PAYMENT_RESPONSE, message);
                    //---------------------------------------------------------------
                            Logger.addLogAdapter(new AndroidLogAdapter());
                            Logger.d("message");

                            Logger.clearLogAdapters();
                            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                                    .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                                    .methodCount(0)         // (Optional) How many method line to show. Default 2
                                    .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
//        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                                    .tag("My custom tag")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                                    .build();

                            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

                            Logger.addLogAdapter(new AndroidLogAdapter() {
                                @Override public boolean isLoggable(int priority, String tag) {
                                    return BuildConfig.DEBUG;
                                }
                            });

                            Logger.addLogAdapter(new DiskLogAdapter());


                            Logger.w("Case 1"+message.toString());

                            Logger.clearLogAdapters();
                            formatStrategy = PrettyFormatStrategy.newBuilder()
                                    .showThreadInfo(false)
                                    .methodCount(0)
                                    .build();

                            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
//                            Logger.i("no thread info and method info");
//
//                            Logger.t("tag").e("Custom tag for only one use");


                            Logger.clearLogAdapters();
                            formatStrategy = PrettyFormatStrategy.newBuilder()
                                    .showThreadInfo(false)
                                    .methodCount(0)
                                    .tag("MyTag")
                                    .build();
                            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

                            Logger.w(message);
                            saveLogcatToFile(WebviewActivity.this);
                    //---------------------------------------------------------------------
                            startActivity(intent);
                            finish();
                        } catch (ClassNotFoundException ex) {
                            ex.printStackTrace();
                        }
                    }
                });
        alertDialog.show();
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onPaymentLoadPageSuccess(final ResponseEntity<?> response) {
        try
        {
            if(webView!=null)
            {
                if (webView.getSettings() != null) {
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.getSettings().setAllowContentAccess(true);
                    webView.getSettings().setAllowFileAccess(true);
                    webView.getSettings().setDomStorageEnabled(true);
                    webView.getSettings().setSupportMultipleWindows(true);
                }
                else
                {
                    showAlertDialog("Web View Not Initialized");
                    return;
                }
                webView.setWebViewClient(new WebViewClient()
                {

                    public void onPageStarted(WebView view, String url, Bitmap favicon)
                    {
                        super.onPageStarted(view, url, favicon);
                        LogUtils.logError(TAGG, "onPageStarted " + url);
                        if (url.equals("https://secure.telr.com/gateway/webview_close.html")
                                || url.equals("https://secure.telr.com/gateway/webview_abort.html"))
                        {
                            StatusRequest statusRequest = new StatusRequest();
                            if (token.equalsIgnoreCase("true")) {
                                statusRequest.setKey(mobileRequestSeamless.getKey());
                                statusRequest.setStore(mobileRequestSeamless.getStore());
                            }
                            else{
                                statusRequest.setKey(mobileRequest.getKey());
                                statusRequest.setStore(mobileRequest.getStore());
                            }

                            statusRequest.setComplete(((MobileResponse) response.getBody()).getWebview().getCode());
                            statusTask = new StatusTask(WebviewActivity.this);
                            statusTask.execute(completeUrl, statusRequest);
                            telrSharedPreference.saveDataToPreference("Code", ((MobileResponse) response.getBody()).getWebview().getCode());
                            progressBar.setVisibility(View.VISIBLE);
                            // Log.e("CODEXXXX",":"+((MobileResponse) response.getBody()).getWebview().getCode());
                        }

                    }
                });

                //--------------------------------------------------------------------------------------
                webView.setWebChromeClient(new WebChromeClient() {

                    //For Android5.0+
                    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                        uploadMessageCallback = filePathCallback;
                        showChooserDialog();
                        return true;
                    }
                });
//                view.loadUrl(url);
                //-------------------------------------------------------------------------------------
                webView.loadUrl(((MobileResponse) response.getBody()).getWebview().getStart());
                progressBar.setVisibility(View.GONE);
            }
            else
            {
                showAlertDialog("WebView Not Initialized");
            }
        }
        catch (Exception e)
        {

        }

    }

    private void showChooserDialog() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,
                "Select Picture"), 1);

    }


    @Override
    public void onPaymentLoadPageFailure(ResponseEntity<?> response) {
        try {
            System.out.println("error:" + response.getStatusCode() + response.getBody());
            MobileResponse resonse = (MobileResponse) response.getBody();
            if(response.getStatusCode().value()==200)
            {
                onStatusSucceedAuth(response);
//                Intent intent = new Intent(WebviewActivity.this, Class.forName(successActivtyName));
////                intent.putExtra(PAYMENT_RESPONSE, (StatusResponse) response.getBody());
////                intent.putExtra("Code", telrSharedPreference.getDataFromPreference("Code"));
////                startActivity(intent);
////                finish();
            }
            else{
                showAlertDialog(resonse.getAuth() != null ? resonse.getAuth().getMessage() : "Error!");
            }

            Log.e("2:","");
        } catch (NullPointerException ne) {
            ne.printStackTrace();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onStatusSucceedAuth(ResponseEntity<?> response) {
        progressBar.setVisibility(View.GONE);
        try {
            //---------------------------------------------------------------
            Logger.addLogAdapter(new AndroidLogAdapter());
            Logger.d("message");

            Logger.clearLogAdapters();
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                    .methodCount(0)         // (Optional) How many method line to show. Default 2
                    .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
//        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                    .tag("My custom tag")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                    .build();

            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

            Logger.addLogAdapter(new AndroidLogAdapter() {
                @Override public boolean isLoggable(int priority, String tag) {
                    return BuildConfig.DEBUG;
                }
            });

            Logger.addLogAdapter(new DiskLogAdapter());


            Logger.w("Case 2"+response.toString());

            Logger.clearLogAdapters();
            formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)
                    .methodCount(0)
                    .build();

            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
//                            Logger.i("no thread info and method info");
//
//                            Logger.t("tag").e("Custom tag for only one use");


            Logger.clearLogAdapters();
            formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)
                    .methodCount(0)
                    .tag("MyTag")
                    .build();
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

            Logger.w(response.toString());
            saveLogcatToFile(WebviewActivity.this);
            //---------------------------------------------------------------------
            Intent intent = new Intent();
            intent.putExtra(PAYMENT_RESPONSE, (MobileResponse) response.getBody());
            intent.putExtra("Code", telrSharedPreference.getDataFromPreference("Code"));
            intent.putExtra("auth","yes");
            setResult(RESULT_OK,intent);
            finish();
        } catch (NullPointerException | ClassCastException ne) {
            ne.printStackTrace();
        }
    }


    @Override
    public void onStatusSucceed(ResponseEntity<?> response) {
//        try {
        progressBar.setVisibility(View.GONE);
            Intent intent = new Intent();
            intent.putExtra(PAYMENT_RESPONSE,(StatusResponse) response.getBody());
        //---------------------------------------------------------------
        Logger.addLogAdapter(new AndroidLogAdapter());
        Logger.d("message");

        Logger.clearLogAdapters();
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                .methodCount(0)         // (Optional) How many method line to show. Default 2
                .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
//        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                .tag("My custom tag")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        Logger.addLogAdapter(new AndroidLogAdapter() {
            @Override public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });

        Logger.addLogAdapter(new DiskLogAdapter());


        Logger.w("Case 3"+response.toString());

        Logger.clearLogAdapters();
        formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .build();

        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
//                            Logger.i("no thread info and method info");
//
//                            Logger.t("tag").e("Custom tag for only one use");


        Logger.clearLogAdapters();
        formatStrategy = PrettyFormatStrategy.newBuilder()
                .showThreadInfo(false)
                .methodCount(0)
                .tag("MyTag")
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

        Logger.w(response.toString());
        saveLogcatToFile(WebviewActivity.this);
        //---------------------------------------------------------------------
           intent.putExtra("Code",telrSharedPreference.getDataFromPreference("Code"));
             intent.putExtra("auth","no");
          //  intent.putExtra("Code","Test"); //telrSharedPreference.getDataFromPreference("Code")
            setResult(RESULT_OK,intent);
            finish();

//        }
//        catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//        catch (NullPointerException ne){
//            ne.printStackTrace();
//        }
    }

    @Override
    public void onStatusFailed(ResponseEntity<?> response) {
        try {
            progressBar.setVisibility(View.GONE);
            //---------------------------------------------------------------
            Logger.addLogAdapter(new AndroidLogAdapter());
            Logger.d("message");

            Logger.clearLogAdapters();
            FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)  // (Optional) Whether to show thread info or not. Default true
                    .methodCount(0)         // (Optional) How many method line to show. Default 2
                    .methodOffset(3)        // (Optional) Skips some method invokes in stack trace. Default 5
//        .logStrategy(customLog) // (Optional) Changes the log strategy to print out. Default LogCat
                    .tag("My custom tag")   // (Optional) Custom tag for each log. Default PRETTY_LOGGER
                    .build();

            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

            Logger.addLogAdapter(new AndroidLogAdapter() {
                @Override public boolean isLoggable(int priority, String tag) {
                    return BuildConfig.DEBUG;
                }
            });

            Logger.addLogAdapter(new DiskLogAdapter());


            Logger.w("Case 4");

            Logger.clearLogAdapters();
            formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)
                    .methodCount(0)
                    .build();

            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
//                            Logger.i("no thread info and method info");
//
//                            Logger.t("tag").e("Custom tag for only one use");


            Logger.clearLogAdapters();
            formatStrategy = PrettyFormatStrategy.newBuilder()
                    .showThreadInfo(false)
                    .methodCount(0)
                    .tag("MyTag")
                    .build();
            Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));

            Logger.w(response.toString());
            saveLogcatToFile(WebviewActivity.this);
            //---------------------------------------------------------------------
            Intent intent = new Intent();
            intent.putExtra(PAYMENT_RESPONSE,(StatusResponse) response.getBody());
            intent.putExtra("Code",telrSharedPreference.getDataFromPreference("Code"));
            intent.putExtra("auth","no");
            setResult(Activity.RESULT_OK,intent);

//            startActivity(intent);
            finish();


//            Intent intent = new Intent(WebviewActivity.this, Class.forName(failedActivityName));
//            intent.putExtra(PAYMENT_RESPONSE, (StatusResponse) response.getBody());
//            intent.putExtra("Code",telrSharedPreference.getDataFromPreference("Code"));
//            startActivity(intent);
//            finish();
        } catch (NullPointerException ne){
            ne.printStackTrace();
        }
//        catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void onStatusPending(ResponseEntity<?> response) {
        try {
            Intent intent = new Intent();
            intent.putExtra(PAYMENT_RESPONSE,(StatusResponse) response.getBody());
            setResult(Activity.RESULT_OK,intent);
//            startActivity(intent);
            finish();

//            Intent intent = new Intent(WebviewActivity.this, Class.forName(failedActivityName));
//            intent.putExtra(PAYMENT_RESPONSE, (StatusResponse) response.getBody());
//            startActivity(intent);
//            finish();

        } catch (NullPointerException ne){
            ne.printStackTrace();
        }
//        }
//        catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onDestroy() {
        webView.destroy();
        super.onDestroy();
        LogUtils.logError(TAGG,"onDestroy");
        if (webView != null)
        {
            webView.clearHistory();
            webView.destroy();
            webView=null;
        }
        closeRunningTasks();

    }

    @Override
    protected void onPause() {
        super.onPause();
//        statusTask.cancel(true);
//        webView.destroy();
        LogUtils.logError(TAGG,"onPause");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ValueCallback<Uri[]> callback = uploadMessageCallback;
        if (requestCode != 1 || callback == null) {return;}
        ArrayList<Uri> results = new ArrayList<Uri>();
        if (resultCode == RESULT_OK) {
            if (data != null) {
                String dataString = data.getDataString();
                ClipData clipData = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    clipData = data.getClipData();
                }
                if (clipData != null) {
                    results.clear();
                    for (int i = 0; i< clipData.getItemCount(); i++) {
                        ClipData.Item item = clipData.getItemAt(i);
                        results.set(i, item.getUri());
                    }
                }

                if (dataString != null) {
                    results.clear();
                    results.add(Uri.parse(dataString));
                }
            }
        }
        callback.onReceiveValue(results.toArray(new Uri[0]));
        uploadMessageCallback = null;

    }
    private void closeRunningTasks() {
        LogUtils.logError(TAGG,"closeRunningTasks");
        if (paymentTask != null) {
            Log.e("Inside closerunning","PP");
            paymentTask.cancel(true);

        }

        if (statusTask != null) {
            Log.e("Inside closerunning","SS");
            statusTask.cancel(true);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if(mobileRequest.getTran().getClazz().equalsIgnoreCase("cont")){
            if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                    && keyCode == KeyEvent.KEYCODE_BACK
                    && event.getRepeatCount() == 0) {
                Log.e("CDA", "onKeyDown Called");
                onBackPressed();
                return true;
            }
        }
    else{
    super.onBackPressed();
        }
        return super.onKeyDown(keyCode, event);
    }
}
