package com.yh.kakaotranslator.data.source

import com.yh.kakaomap.util.RetrofitClient
import com.yh.kakaotranslator.data.api.KakaoService
import com.yh.kakaotranslator.data.api.KakaoTranslatorDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class KakaoRemoteDataSourceImpl : KakaoRemoteDataSource{

    private val kakaoServie = RetrofitClient.create<KakaoService>(KakaoService.BASE_URL)

    override fun getText(
        query: String,
        srcLang: String,
        targetLang: String,
        onSuccess: (KakaoTranslatorDto) -> Unit,
        onFailure: (String) -> Unit,
    ) {
        kakaoServie.getLanguage(query = query, srcLang = srcLang, targetLang = targetLang).enqueue(
            object : Callback<KakaoTranslatorDto> {
                override fun onResponse(
                    call: Call<KakaoTranslatorDto>,
                    response: Response<KakaoTranslatorDto>,
                ) {
                    response.body()?.let(onSuccess)
                }

                override fun onFailure(call: Call<KakaoTranslatorDto>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            }
        )
    }
}