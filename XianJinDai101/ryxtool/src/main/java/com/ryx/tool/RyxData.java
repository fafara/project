package com.ryx.tool;

/**
 * Created by laomao on 16/8/1.
 */
public class RyxData {
    static
    {
        System.loadLibrary("ryxtool");
    }
    public static native String getRHXkey();
    public static native String getRSkey();
    public static native String getRS20Key();
    public static native String getRYXkey();
    public static native String getBankkey();
    public static native String getKeystore();
    public static native String getKeystoreModulus();
    public static native String getKeystoreSignNumber();
    public static native String getKeystoreSubjectDN();

}
