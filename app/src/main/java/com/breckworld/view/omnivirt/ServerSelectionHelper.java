package com.breckworld.view.omnivirt;

import android.content.Context;
import android.preference.PreferenceManager;

class ServerSelectionHelper {
    private static String contentProductionUrl = "www.vroptimal-3dx-assets.com";
    private static String contentStagingUrl = "upload-staging.omnivirt.com";
    private static String contentSandboxUrl = "upload-sandbox.omnivirt.com";
    private static String scriptProductionUrl = "remote.vroptimal-3dx-assets.com";
    private static String scriptStagingUrl = "upload-staging.omnivirt.com";
    private static String scriptSandboxUrl = "upload-sandbox.omnivirt.com";
    private static String adProductionUrl = "ads.omnivirt.com";
    private static String adStagingUrl = "upload-staging.omnivirt.com";
    private static String adSandboxUrl = "upload-sandbox.omnivirt.com";

    ServerSelectionHelper() {
    }

    static ServerSelectionHelper.ServerMode getMode(Context context) {
        switch(PreferenceManager.getDefaultSharedPreferences(context).getInt("VRKIT_SERVER_SELECTION", 0)) {
            case 1:
                return ServerSelectionHelper.ServerMode.Staging;
            case 2:
                return ServerSelectionHelper.ServerMode.Sandbox;
            case 3:
                return ServerSelectionHelper.ServerMode.Custom;
            default:
                return ServerSelectionHelper.ServerMode.Production;
        }
    }

    static boolean isProduction(Context context) {
        return getMode(context) == ServerSelectionHelper.ServerMode.Production;
    }

    static String getContentUrl(Context context) {
        switch(getMode(context)) {
            case Staging:
                return contentStagingUrl;
            case Sandbox:
                return contentSandboxUrl;
            case Custom:
                return PreferenceManager.getDefaultSharedPreferences(context).getString("VRKIT_CONTENT_URL", "");
            default:
                return contentProductionUrl;
        }
    }

    static String getScriptUrl(Context context) {
        switch(getMode(context)) {
            case Staging:
                return scriptStagingUrl;
            case Sandbox:
                return scriptSandboxUrl;
            case Custom:
                return PreferenceManager.getDefaultSharedPreferences(context).getString("VRKIT_SCRIPT_URL", "");
            default:
                return scriptProductionUrl;
        }
    }

    static String getAdUrl(Context context) {
        switch(getMode(context)) {
            case Staging:
                return adStagingUrl;
            case Sandbox:
                return adSandboxUrl;
            case Custom:
                return PreferenceManager.getDefaultSharedPreferences(context).getString("VRKIT_AD_URL", "");
            default:
                return adProductionUrl;
        }
    }

    static enum ServerMode {
        Production,
        Staging,
        Sandbox,
        Custom;

        private ServerMode() {
        }
    }
}

