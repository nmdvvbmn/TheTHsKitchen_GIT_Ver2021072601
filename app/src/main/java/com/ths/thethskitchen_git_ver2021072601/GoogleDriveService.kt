package com.ths.a20210713_sqllitehelper

import android.app.Application
import android.content.Context
import android.content.res.AssetManager
import android.os.AsyncTask
import android.os.NetworkOnMainThreadException
import android.util.Log
import androidx.loader.content.AsyncTaskLoader
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.googleapis.json.GoogleJsonResponseException
import com.google.api.client.json.gson.GsonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.SheetsScopes
import com.google.api.services.sheets.v4.model.Sheet
import com.google.api.services.sheets.v4.model.ValueRange
import com.google.auth.http.HttpCredentialsAdapter
import com.google.auth.oauth2.ServiceAccountCredentials
import java.io.FileInputStream
import java.io.InputStream

class GoogleDriveService: Application() {

    fun getDlist(sheetId: String, inputStream: InputStream?, range: String):
            Pair<List<List<DList>>?, List<List<IList>>?> {

        // 서비스 계정 인증과 권한 부여 후 SheetService 객체 획득
        val service = Sheets.Builder(
            GoogleNetHttpTransport.newTrustedTransport(),
            GsonFactory.getDefaultInstance(),
            HttpCredentialsAdapter(
                ServiceAccountCredentials.fromStream(
                    inputStream
                )
                    .createScoped(listOf(SheetsScopes.SPREADSHEETS_READONLY))
                    .createDelegated("test-googlesheet@thethskitchen.iam.gserviceaccount.com")
            )
        ).setApplicationName("20210713_SQLliteHelper").build()
        Log.d("GOOGLESHEET1", "AUTH")

        // RList Update Ranges
        val setRange: ValueRange = try { //
            service
                .spreadsheets()
                .values()
                .get(sheetId, "UpdateList!A2")
                .execute()
        } catch (ex: GoogleJsonResponseException) {
            Log.d("GoogleSheet", "${ex.statusCode}") // 403
            Log.d("GoogleSheet", "${ex.statusMessage}") // Forbidden
            Log.d("GoogleSheet", "${ex.details.message}") // The caller does not have permission
            return Pair(null, null)
        } catch (e: NetworkOnMainThreadException) {
            Log.d("GoogleSheet", "${e.stackTraceToString()}")
            return Pair(null, null)
        }
        @Suppress("UNCHECKED_CAST") val rangeMax = setRange.getValues()[0][0] as String

        // Update 판단
        if (range.toLong() < rangeMax.toLong() + 3) {
//            GetDList
            val setRanges = "UpdateList!A${range}:M${rangeMax.toLong() + 3}"
            val valueRange: ValueRange = try {
                service
                    .spreadsheets()
                    .values()
                    .get(sheetId, setRanges)
                    .execute()
            } catch (ex: GoogleJsonResponseException) {
                Log.d("GoogleSheet", "${ex.statusCode}") // 403
                Log.d("GoogleSheet", "${ex.statusMessage}") // Forbidden
                Log.d("GoogleSheet", "${ex.details.message}") // The caller does not have permission
                return Pair(null, null)
            } catch (e: NetworkOnMainThreadException) {
                Log.d("GoogleSheet", "${e.stackTraceToString()}")
                return Pair(null, null)
            }
            @Suppress("UNCHECKED_CAST") val values = valueRange.getValues() as List<DList>

            var data: List<List<DList>> = valueRange.getValues() as List<List<DList>>
            if (data != null) {
//                GetIList
                var ranges = getIlistRanges(data)
                val valueIRange: ValueRange = try {
                    service
                        .spreadsheets()
                        .values()
                        .get(sheetId, ranges)
                        .execute()
                } catch (ex: GoogleJsonResponseException) {
                    Log.d("GoogleSheet", "${ex.statusCode}") // 403
                    Log.d("GoogleSheet", "${ex.statusMessage}") // Forbidden
                    Log.d("GoogleSheet", "${ex.details.message}") // The caller does not have permission
                    return Pair(data, null)
                } catch (e: NetworkOnMainThreadException) {
                    Log.d("GoogleSheet", "${e.stackTraceToString()}")
                    return Pair(data, null)
                }
                @Suppress("UNCHECKED_CAST") val values = valueIRange.getValues() as List<IList>

                var iData: List<List<IList>> = valueIRange.getValues() as List<List<IList>>
                return Pair(data, iData)
            }else{
                return Pair(null, null)
            }
        }else{
            return  Pair(null, null)
        }
    }

    private fun getIlistRanges(dList: List<List<DList>>): String? {
        var item = dList.get(0)
        var start = "${item[11]}"
        item = dList.get(dList.size - 1)
        var end = "${item[12]}"
        if (start != null && end != null) {
            var ranges = "재료List!A${start}:F${end}"
            return ranges
        } else {
            return  null
        }

    }


}
