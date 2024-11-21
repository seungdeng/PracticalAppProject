package project.api.id20212196.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiManager{
    private  val retrofit: Retrofit= Retrofit.Builder()
        .baseUrl("http://apis.data.go.kr/1471000/DrbEasyDrugInfoService/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val service: ApiService = retrofit.create(ApiService::class.java)
    fun getService(): ApiService{
        return service
    }
}