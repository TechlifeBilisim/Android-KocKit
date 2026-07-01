package com.techlife.kockit.feature.placementtest

import androidx.annotation.DrawableRes
import com.techlife.kockit.R
import com.techlife.kockit.feature.placementtest.PlacementTestFakeData.QUESTION_IMAGE_RES

data class PlacementQuestion(
    @DrawableRes val imageResId: Int = QUESTION_IMAGE_RES,
    val prompt: String = "",
    val detail: String = "",
    val subject: String = "Matematik"
)

object PlacementTestFakeData {
    @DrawableRes
    val QUESTION_IMAGE_RES: Int = R.drawable.soru

    fun questionsFor(section: PlacementTestSection): List<PlacementQuestion> {
        if (section == PlacementTestSection.GENERAL_CULTURE) {
            return listOf(
                PlacementQuestion(
                    prompt = "Soru 1",
                    detail = "Aşağıdaki görsele göre doğru cevabı işaretleyin.",
                    subject = "Tarih"
                ),
                PlacementQuestion(
                    prompt = "Soru 2",
                    detail = "Haritada işaretli bölge için doğru seçeneği işaretleyin.",
                    subject = "Coğrafya"
                ),
                PlacementQuestion(
                    prompt = "Soru 3",
                    detail = "Deney düzeneğine göre doğru cevabı seçin.",
                    subject = "Fen Bilimleri"
                )
            )
        }
        return listOf(
            PlacementQuestion(
                prompt = "Soru 1",
                detail = "Grafikte verilen fonksiyon için doğru seçeneği işaretleyin.",
                subject = "Matematik"
            ),
            PlacementQuestion(
                prompt = "Soru 2",
                detail = "Sayı dizisine göre doğru cevabı işaretleyin.",
                subject = "Sayısal Yetenek"
            ),
            PlacementQuestion(
                prompt = "Soru 3",
                detail = "Paragrafın ana düşüncesine uygun seçeneği işaretleyin.",
                subject = "Sözel Yetenek"
            )
        )
    }
}
