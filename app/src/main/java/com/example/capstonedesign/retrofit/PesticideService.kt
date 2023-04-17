package com.example.capstonedesign.retrofit

import com.example.capstonedesign.model.openapi.PesticideResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PesticideService {

    @GET("/openApi/service.do")
    suspend fun getPesticideInfo(
        @Query("apiKey") apiKey: String,
        @Query("serviceCode") serviceCode: String,
        @Query("serviceType") serviceType: String,
        @Query("cropName") cropName: String,
        @Query("diseaseWeedName") diseaseWeedName: String,
        @Query("displayCount") displayCount: Int = 50
    ) : Response<PesticideResponse>
}