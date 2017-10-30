package com.ryx.payment.ruishua.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;

import com.ryx.tool.RyxData;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

public class Utils {

    // 这个是缓存目录，需要自行设定
//	public static File cacheDir = new File(Environment.getExternalStorageDirectory()
//			+File.separator+"imobpay"+File.separator+"cache");

    public static File createCacheDir() {
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = Environment.getExternalStorageDirectory() + File.separator + "imobpay" + File.separator + "cache";
                final File file = new File(path);
                if (!file.exists()) {
                    // 按照指定的路径创建文件夹
                    if (file.mkdirs()) {
                        return file;
                    }
                } else {
                    return file;
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return new File(Environment.getExternalStorageDirectory() + File.separator);
    }

    // 通过Url来获得图片，如果从本地缓存中找到就不进行网络获得
    // 否则进行网络获取，并写入缓存目录
    public static Bitmap getBitmapByUrl(String url) {
        String fileName = getFileNameFromUrl(url);
        Bitmap bitmap = null;
        // 先尝试本地获取
        bitmap = getBitmapFromCache(fileName);
        // 如果为空表明本地不存在
        if (bitmap == null) {
            URL imgUrl = null;
            InputStream is = null;
            try {
                imgUrl = new URL(url);
                is = imgUrl.openStream();
                // 图片的编码
                bitmap = BitmapFactory.decodeStream(is);

//				// 写下缓存目录		保存之前 先改变一下大小
//				saveBitmapToCache(changeBitmapSize(bitmap), fileName);
                saveBitmapToCache(bitmap, fileName);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if (is != null) {
                        is.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return bitmap;
    }

    // 从缓存目录中获取，如果不存在，返回null
    public static Bitmap getBitmapFromCache(String fileName) {
        Bitmap result = null;
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(new File(createCacheDir(), fileName));
            result = BitmapFactory.decodeStream(fis);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    // 将图片保存到缓存目录中
    public static void saveBitmapToCache(Bitmap bitmap, String fileName) {
        if (bitmap == null) {
            return;
        }
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(new File(createCacheDir(), fileName));
            // 这个是重点方法
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // 从Url中解析出文件名
    public static String getFileNameFromUrl(String src) {
        int i = src.lastIndexOf("/");
        return src.substring(i + 1, src.length());
    }

    public static Bitmap changeBitmapSize(Bitmap bitmap) {
        // 得到原图宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
//		int screenWidth = myactivity.getWindowManager().getDefaultDisplay().getWidth(); // // 得到屏幕的宽高（像素)
//		System.out.println("获得的宽是 "+screenWidth+" 宽 " +width+" 高 "+height);
        // 得到宽高的缩放比例
//		float wScale = (float) (screenWidth /4 - 40 )/ width;
        float wScale = (float) 120 / width;
        if (wScale > 1) {
            wScale = 1;
        }
//		System.out.println("求出的缩放比例 "+wScale);

        // 通过Matrix矩阵去设置变化
        Matrix matrix = new Matrix();
        matrix.postScale(wScale, wScale);

        // 根据matix创建新图片
        /*
         * 1.原文件，即要根据哪张图创建新图片 2.起始点的x坐标 3.起始点的y坐标
		 *  4.从起始点到多宽 5.从起始点到多高 6.矩阵对象  7.抗锯齿
		 */

        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static boolean isRebuild(Context context) {

        PackageInfo packageInfo = null;
        try {
            packageInfo = context.getPackageManager().getPackageInfo("com.ryx.payment.ruishua", PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            CertificateFactory certFactory = null;
            try {
                certFactory = CertificateFactory.getInstance("X.509");
                X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(sign.toByteArray()));
                String pubKey = cert.getPublicKey().toString();
                String signNumber = cert.getSerialNumber().toString();
                String subjectDN = cert.getSubjectDN().toString();
                if (pubKey.equals(RyxData.getKeystoreModulus()) && signNumber.equals(RyxData.getKeystoreSignNumber()) && subjectDN.equals(RyxData.getKeystoreSubjectDN())) {
                    Log.e("laomao","pass");
                    return false;
                }
                else {

                    Log.e("laomao","notpass");
                    return true;
                }
            } catch (CertificateException e) {
                e.printStackTrace();
                return true;
            }


        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return true;
        }


    }

    public static String getSingInfo(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo("com.ryx.payment.ruishua", PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            return parseSignature(sign.toByteArray());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String parseSignature(byte[] signature) {
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
            X509Certificate cert = (X509Certificate) certFactory.generateCertificate(new ByteArrayInputStream(signature));
            String pubKey = cert.getPublicKey().toString();
            String signNumber = cert.getSerialNumber().toString();
            LogUtil.showLog("signName:" + cert.getSigAlgName());
            LogUtil.showLog("pubKey:" + pubKey);
            LogUtil.showLog("signNumber:" + signNumber);
            LogUtil.showLog("subjectDN:" + cert.getSubjectDN().toString());
            return "signName:" + cert.getSigAlgName() + ";pubKey:" + pubKey + ";signNumber:" + signNumber + ";subjectDN:" + cert.getSubjectDN().toString();
        } catch (CertificateException e) {
            e.printStackTrace();
            return null;
        }

    }
}