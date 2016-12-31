package com.buttering.roler.util;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by kinamare on 2016-12-29.
 */

public class FileUtil {
	public static MultipartBody.Part makeMultiPartBody(File file) {
		RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
		return MultipartBody.Part.createFormData("file", file.getName(), requestFile);
	}
}