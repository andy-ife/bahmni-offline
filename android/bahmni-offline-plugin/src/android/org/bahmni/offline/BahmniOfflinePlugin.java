package org.bahmni.offline;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import android.webkit.WebView;
import android.util.Log;

import org.bahmni.offline.dbServices.dao.FormDbService;
import org.bahmni.offline.dbServices.dao.ConceptDbService;
import org.bahmni.offline.dbServices.dao.ConfigDbService;
import org.bahmni.offline.dbServices.dao.DbHelper;
import org.bahmni.offline.dbServices.dao.LocationDbService;
import org.bahmni.offline.dbServices.dao.ReferenceDataDbService;
import org.bahmni.offline.services.AppUpdateService;
import org.bahmni.offline.services.DbService;
import static org.bahmni.offline.Constants.METADATA_DB_VERSION;

public class BahmniOfflinePlugin extends CordovaPlugin {
    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        Log.i("BahmniOfflinePlugin", "Initializing BahmniOfflinePlugin and injecting AndroidOfflineService");
        
        try {
            WebView sysWebView = (WebView) webView.getEngine().getView();
            
            String databaseName = "/metaData.db";
            String dbPath = cordova.getActivity().getExternalFilesDir(null) + databaseName;
            DbHelper metaDataDbHelper = new DbHelper(cordova.getActivity(), dbPath, METADATA_DB_VERSION);

            sysWebView.addJavascriptInterface(new DbService(cordova.getActivity(), metaDataDbHelper), "AndroidOfflineService");
            sysWebView.addJavascriptInterface(new ConfigDbService(metaDataDbHelper), "AndroidConfigDbService");
            sysWebView.addJavascriptInterface(new LocationDbService(metaDataDbHelper), "AndroidLocationDbService");
            sysWebView.addJavascriptInterface(new ReferenceDataDbService(metaDataDbHelper), "AndroidReferenceDataDbService");
            sysWebView.addJavascriptInterface(new ConceptDbService(metaDataDbHelper), "AndroidConceptDbService");
            sysWebView.addJavascriptInterface(new FormDbService(metaDataDbHelper), "AndroidFormDbService");
            sysWebView.addJavascriptInterface(new AppUpdateService(cordova.getActivity()), "AppUpdateService");
        } catch (Exception e) {
            Log.e("BahmniOfflinePlugin", "Error injecting JS interfaces: ", e);
        }
    }
}
