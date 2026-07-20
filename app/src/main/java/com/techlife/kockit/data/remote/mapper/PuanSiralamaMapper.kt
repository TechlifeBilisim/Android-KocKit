package com.techlife.kockit.data.remote.mapper

import com.techlife.kockit.data.remote.dto.puansiralama.PuandanSiralamaDto
import com.techlife.kockit.data.remote.dto.puansiralama.SiralamadanPuanDto
import com.techlife.kockit.domain.puansiralama.model.PuandanSiralamaResult
import com.techlife.kockit.domain.puansiralama.model.SiralamadanPuanResult

fun PuandanSiralamaDto.toDomain(): PuandanSiralamaResult =
    PuandanSiralamaResult(siralama = siralama)

fun SiralamadanPuanDto.toDomain(): SiralamadanPuanResult =
    SiralamadanPuanResult(
        puanYerlestirmeTurId = puanYerlestirmeTurId,
        puan = puan,
        siralama = siralama
    )
