package com.breckworld.repository.remote.http.part

import android.webkit.MimeTypeMap
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

fun createPartFromString(dest: String): RequestBody {
    return RequestBody.create(MultipartBody.FORM, dest)
}

fun createPartFromFile(partName: String, file: File?): MultipartBody.Part? {
    if (file == null) return null
    return MultipartBody.Part.createFormData(partName, file.getName(), RequestBody.create(MediaType.parse(getMimeType(file.absolutePath)), file))
}

fun getMimeType(url: String): String {
    var type: String? = null
    var extension: String? = null
    extension = MimeTypeMap.getFileExtensionFromUrl(url)
    if (extension != null) type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
    if(type == null) return "image/*"
    return type
}
