package com.techlife.kockit.data.remote.mapper

import com.techlife.kockit.data.remote.dto.ogrenci.OgrenciDto
import com.techlife.kockit.data.remote.dto.yo.YoBilimDto
import com.techlife.kockit.data.remote.dto.yo.YoBolumDto
import com.techlife.kockit.data.remote.dto.yo.YoFakulteDto
import com.techlife.kockit.data.remote.dto.yo.YoUniversiteDto
import com.techlife.kockit.domain.ogrenci.model.Ogrenci
import com.techlife.kockit.domain.yo.model.YoBilim
import com.techlife.kockit.domain.yo.model.YoBolum
import com.techlife.kockit.domain.yo.model.YoFakulte
import com.techlife.kockit.domain.yo.model.YoUniversite

fun YoBilimDto.toDomain(): YoBilim = YoBilim(
    id = yoBilimId,
    name = ad
)

fun YoUniversiteDto.toDomain(): YoUniversite = YoUniversite(
    id = yoUniversiteId,
    name = ad
)

fun YoFakulteDto.toDomain(): YoFakulte = YoFakulte(
    id = yoFakulteId,
    universityId = yoUniversiteId,
    name = ad
)

fun YoBolumDto.toDomain(): YoBolum = YoBolum(
    id = yoBolumId,
    bilimId = yoBilimId,
    name = ad
)

fun OgrenciDto.toDomain(): Ogrenci = Ogrenci(
    ogrenciId = ogrenciId,
    kullaniciId = kullaniciId,
    sinifDuzeyId = sinifDuzeyId,
    hazirlikSinavTurId = hazirlikSinavTurId,
    hazirlikPuanTurId = hazirlikPuanTurId,
    ogrenciDuzeyId = ogrenciDuzeyId,
    ogrenciHedefId = ogrenciHedefId,
    ogrenciHedef = ogrenciHedef,
    okulTurId = okulTurId,
    okulId = okulId,
    notOrtalama = notOrtalama,
    okulAd = okulAd,
    haftalikCalismaSure = haftalikCalismaSure,
    gunlukOturumSure = gunlukOturumSure,
    genelTekrarGun = genelTekrarGun,
    gunlukParagrafSeans = gunlukParagrafSeans,
    gunlukProblemSeans = gunlukProblemSeans,
    musaitOlmadigiGun = musaitOlmadigiGun,
    izinGunler = izinGunler
)

fun List<YoBilimDto>.toYoBilimDomain(): List<YoBilim> = map { it.toDomain() }

fun List<YoUniversiteDto>.toYoUniversiteDomain(): List<YoUniversite> = map { it.toDomain() }

fun List<YoFakulteDto>.toYoFakulteDomain(): List<YoFakulte> = map { it.toDomain() }

fun List<YoBolumDto>.toYoBolumDomain(): List<YoBolum> = map { it.toDomain() }
