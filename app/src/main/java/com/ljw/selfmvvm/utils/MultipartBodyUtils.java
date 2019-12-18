package com.ljw.selfmvvm.utils;

import com.ljw.selfmvvm.net.upload.UploadFileRequestBody;

import java.io.File;
import java.util.Map;

import androidx.lifecycle.MutableLiveData;
import okhttp3.MultipartBody;

/**
 * Create by Ljw on 2019/12/14 14:45
 */
public class MultipartBodyUtils {
    public static MultipartBody.Part getBody(MutableLiveData liveData, Map<String, File> files) {
        UploadFileRequestBody uploadFileRequestBody = new UploadFileRequestBody(files, liveData);
        MultipartBody.Part body = MultipartBody.Part.create(uploadFileRequestBody);
        return body;
    }
}
