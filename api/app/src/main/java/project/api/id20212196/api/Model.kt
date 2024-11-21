package project.api.id20212196.api

import android.content.ClipData.Item
import retrofit2.http.Body
import retrofit2.http.Header

data class ApiResponse(
    val header: Header,
    val body: Body
)

data class Header(
    val resultCode: String,     //결과 코드
    val resultMsg : String      //결과 메시지
)

data class Body(
    val pageNo: Int,            //페이지 번호
    val totalCount: Int,        //전체 결과 수
    val numOfRows: Int,         //한 페이지 결과 수
    val items: List<Item>?      //목록
)

data class Item(
    val entpName: String,       //업체명
    val itemName: String,       //제품명
    val itemSeq: String,        //품목기준코드
    val efcyQesitm: String?,    //효능
    val useMethodQesitm: String?,   //사용법
    val atpnWarnQesitm: String?,    //경고
    val atpnQesitm: String?,        //주의사항
    val intrcQesitm: String?,       //상호작용
    val seQesitm:String?,           //부작용
    val depositMethodQesitm:String?,    //보관법
    val openDe: String,         //공개일자
    val updateDe:String,        //수정일자
    val itemImage:String?,      //낱알이미지
    val bizrno: String?
)
