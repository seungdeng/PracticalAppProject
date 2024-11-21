package project.api.id20212196.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("getDrbEasyDrugList")
    fun getDrugList(
        @Query("serviceKey") serviceKey: String = "bFpyWKe0h65drvOygR3vdfvRkiYFkO9+jKipysO97p7Gxwpas5oZcqCgpN2EnSOkjPUJsDFOkZ+VklfajiTB8Q==",
        @Query("pageNo") pageNo: Int=1,
        @Query("numOfRows") numOfRows: Int=10,
        @Query("entpName") entpName: String? = null,
        @Query("itemName") itemName: String? = null,
        @Query("efcyQesitm") efcyQesitm: String? = null,
        @Query("type") type:String = "json"
    ) : Call<ApiResponse>

}