package com.example.choo.jframe_android;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

public class Base64EnDe {
    public static String fileToString(File file) {
        String fileString = new String();
        FileInputStream inputStream = null;
        ByteArrayOutputStream byteOutStream = null;

        try {
            inputStream = new FileInputStream(file);
            byteOutStream = new ByteArrayOutputStream();

            int len = 0;
            byte[] buf = new byte[1024];
            while ((len = inputStream.read(buf)) != -1) {
                byteOutStream.write(buf, 0, len);
            }
            byte[] fileArray = byteOutStream.toByteArray();
            fileString = Base64.encodeToString(fileArray, 2);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                byteOutStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

//        for (int j = 0; j < fileString.length(); j += 200) {
//            if (j + 200 >= fileString.length()) {
////				System.out.println(fileString.substring(j, fileString.length()));
//                Log.d("choo", "last encoded = " + fileString.substring(j, fileString.length()));
//            } else {
////				System.out.println(fileString.substring(j, j+199));
//                Log.d("choo", "en = " + fileString.substring(j, j + 199));
//            }
//        }
        return fileString;
    }

    public static String getFileToByte(String path){
        Bitmap bm = null;
        ByteArrayOutputStream baos = null;
        byte[] b = null;
        String encodeString = null;
        try{
            bm = BitmapFactory.decodeFile(path);
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            b = baos.toByteArray();
            encodeString = Base64.encodeToString(b, 2);
        }catch (Exception e){
            e.printStackTrace();
        }
        return encodeString;
    }

    public byte[] uriToBytes(Context context, Uri uri) throws IOException {
        Log.d("choo", "uri = " + uri);
        File file = new File(uri.getPath());
        InputStream iStream = context.getContentResolver().openInputStream(uri);
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = iStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public String bytesToString(byte[] bytes) {
        return Base64.encodeToString(bytes, 2);
    }

    public static byte[] stringToBytes(String str) {
        byte[] decodedString = Base64.decode(str, 0);
        return decodedString;
    }
}
