package org.vudroid.core.utils

import android.content.Context
import android.text.TextUtils
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import org.vudroid.core.entity.APage
import java.io.File

/**
 * @author: archko 2020/1/1 :9:04 下午
 */
object APageSizeLoader {

    val PAGE_COUNT = 20

    fun loadPageSizeFromFile(
        context: Context,
        pageCount: Int,
        file: File
    ): PageSizeBean? {
        var pageSizeBean: PageSizeBean? = null
        try {
            val size = file.length()
            var saveFile = File(
                context.externalCacheDir?.path + "/amupdf"
                        + File.separator + "page" + File.separator + file.nameWithoutExtension + ".json"
            )
            val content = StreamUtils.readStringFromFile(saveFile)
            if (!TextUtils.isEmpty(content)) {
                pageSizeBean = fromJson(pageCount, size, JSONObject(content))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pageSizeBean
    }

    fun loadPageSizeFromFile(
        context: Context,
        pageCount: Int,
        path: String
    ): PageSizeBean? {
        var pageSizeBean: PageSizeBean? = null
        try {
            val file = File(path)
            val size = file.length()
            var saveFile = File(
                context.externalCacheDir?.path + "/amupdf"
                        + File.separator + "page" + File.separator + file.nameWithoutExtension + ".json"
            )
            val content = StreamUtils.readStringFromFile(saveFile)
            if (!TextUtils.isEmpty(content)) {
                pageSizeBean = fromJson(pageCount, size, JSONObject(content))
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return pageSizeBean
    }

    fun savePageSizeToFile(
        context: Context,
        crop: Boolean,
        file: File,
        list: List<APage>,
    ) {
        var saveFile = File(
            //FileUtils.getExternalCacheDir(App.instance).path
            context.externalCacheDir?.path + "/amupdf"
                    + File.separator + "page" + File.separator + file.nameWithoutExtension + ".json"
        )
        val content = toJson(crop, file.length(), list)
        StreamUtils.saveStringToFile(content, saveFile)
    }

    fun savePageSizeToFile(
        context: Context,
        crop: Boolean,
        path: String,
        list: List<APage>,
    ) {
        val file = File(path)
        var saveFile = File(
            context.externalCacheDir?.path + "/amupdf"
                    + File.separator + "page" + File.separator + file.nameWithoutExtension + ".json"
        )
        val content = toJson(crop, file.length(), list)
        StreamUtils.saveStringToFile(content, saveFile)
    }

    private fun fromJson(pageCount: Int, fileSize: Long, jo: JSONObject): PageSizeBean? {
        val ja = jo.optJSONArray("pagesize")
        if (null == ja || ja.length() != pageCount) {
            Log.d("TAG", "new pagecount:$pageCount")
            return null
        }
        if (fileSize != jo.optLong("filesize")) {
            Log.d("TAG", "new filesize:$fileSize")
            return null
        }
        val pageSizeBean = PageSizeBean()
        val list = fromJson(ja)
        pageSizeBean.List = list
        pageSizeBean.crop = jo.optBoolean("crop")
        return pageSizeBean
    }

    private fun fromJson(ja: JSONArray): List<APage> {
        val list = mutableListOf<APage>()
        for (i in 0 until ja.length()) {
            list.add(APage.fromJson(ja.optJSONObject(i)))
        }
        return list
    }

    private fun toJson(crop: Boolean, fileSize: Long, list: List<APage>): String {
        val jo = JSONObject()
        try {
            jo.put("crop", crop)
            jo.put("filesize", fileSize)
            jo.put("pagesize", toJsons(list))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return jo.toString()
    }

    private fun toJsons(list: List<APage>): JSONArray {
        val jsonArray = JSONArray()
        for (aPage in list) {
            jsonArray.put(APage.toJson(aPage))
        }
        return jsonArray
    }

    fun deletePageSizeFromFile(context: Context, path: String) {
        val file = File(path)
        val saveFile = File(
            context.externalCacheDir?.path + "/amupdf"
                    + File.separator + "page" + File.separator + file.nameWithoutExtension + ".json"
        )
        if (saveFile.exists()) {
            saveFile.delete()
        }
    }

    class PageSizeBean {
        var List: List<APage>? = null
        var crop = false
        var fileSize = 0L
    }
}