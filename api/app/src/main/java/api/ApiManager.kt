package api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiManager{
    private  val retrofit: Retrofit= Retrofit.Builder()
        .baseUrl("https://apis.data.go.kr/1471000/DrbEasyDrugInfoService/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: ApiService = retrofit.create(ApiService::class.java)
    fun getService(): ApiService{
        return service
    }
}