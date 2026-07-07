package com.techlife.kockit.feature.auth.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.techlife.kockit.core.designsystem.component.KocKitBoldText
import com.techlife.kockit.core.designsystem.component.KocKitPrimaryButton
import com.techlife.kockit.core.designsystem.component.KocKitSemiText
import com.techlife.kockit.core.designsystem.component.KocKitText
import com.techlife.kockit.core.designsystem.layout.AuthFormMetrics
import com.techlife.kockit.core.designsystem.theme.KocKitTheme
import com.techlife.kockit.core.designsystem.theme.TextPrimary

@Composable
fun LegalAgreementDialog(
    title: String,
    body: String,
    metrics: AuthFormMetrics,
    onDismiss: () -> Unit,
    onAccepted: () -> Unit
) {
    val colors = KocKitTheme.extraColors
    val scrollState = rememberScrollState()
    val hasReadToEnd by remember {
        derivedStateOf {
            scrollState.maxValue == 0 || scrollState.value >= scrollState.maxValue - 32
        }
    }
    val canScrollMore by remember {
        derivedStateOf {
            scrollState.maxValue > 0 && scrollState.value < scrollState.maxValue - 32
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            KocKitBoldText(
                text = title,
                color = TextPrimary,
                fontSize = metrics.subheadFontSize,
                lineHeight = metrics.subheadLineHeight
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = if (metrics.isExpanded) 520.dp else 360.dp)
                    .verticalScroll(scrollState)
            ) {
                KocKitText(
                    text = body,
                    color = TextPrimary,
                    fontSize = metrics.subheadFontSize,
                    lineHeight = metrics.subheadLineHeight
                )
            }
        },
        confirmButton = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                AnimatedVisibility(
                    visible = canScrollMore,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        KocKitSemiText(
                            text = "Devam etmek için aşağı kaydır",
                            color = colors.pastelGreen,
                            fontSize = metrics.subheadFontSize,
                            lineHeight = metrics.subheadLineHeight
                        )
                        Icon(
                            imageVector = Icons.Filled.KeyboardArrowDown,
                            contentDescription = null,
                            tint = colors.pastelGreen,
                            modifier = Modifier.padding(start = 4.dp)
                        )
                    }
                }
                KocKitPrimaryButton(
                    text = "Tamam",
                    onClick = onAccepted,
                    enabled = hasReadToEnd,
                    containerColor = colors.pastelGreen,
                    height = metrics.buttonHeight,
                    fontSize = metrics.buttonFontSize,
                    modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                )
            }
        }
    )
}

internal val TERMS_AGREEMENT_TEXT = """
Kullanım Koşulları

1. Genel Hükümler
KoçKit uygulamasını kullanarak bu koşulları kabul etmiş sayılırsınız. Uygulama, öğrencilerin sınav hazırlık süreçlerini planlamalarına ve takip etmelerine yardımcı olmak amacıyla sunulmaktadır.

2. Hesap Güvenliği
Hesabınızın güvenliğinden siz sorumlusunuz. Şifrenizi üçüncü kişilerle paylaşmamalı ve hesabınızda gerçekleşen işlemleri takip etmelisiniz.

3. Kullanım Kuralları
Uygulamayı yalnızca yasal amaçlarla kullanabilirsiniz. Diğer kullanıcıların haklarını ihlal eden, yanıltıcı veya zararlı içerik paylaşmak yasaktır.

4. Hizmet Değişiklikleri
KoçKit, hizmet kapsamını önceden bildirimde bulunarak veya bulunmaksızın güncelleyebilir. Önemli değişiklikler uygulama içi bildirimlerle duyurulur.

5. Fikri Mülkiyet
Uygulama içeriği, tasarımı ve yazılımı KoçKit'e aittir. İzinsiz kopyalama, dağıtma veya ticari kullanım yasaktır.

6. Sorumluluk Sınırı
KoçKit, uygulamanın kesintisiz veya hatasız çalışacağını garanti etmez. Kullanıcıların uygulamayı kullanımından doğabilecek dolaylı zararlardan sorumlu tutulamaz.

7. Hesabın Sonlandırılması
Koşullara aykırı kullanım tespit edilmesi halinde hesabınız askıya alınabilir veya sonlandırılabilir.

8. Uygulanacak Hukuk
Bu sözleşme Türkiye Cumhuriyeti kanunlarına tabidir. Uyuşmazlıklarda İstanbul mahkemeleri yetkilidir.

Devam etmek için metnin sonuna kadar okuyup "Tamam" butonuna basmanız gerekmektedir.
""".trimIndent()

internal val KVKK_AGREEMENT_TEXT = """
KVKK Aydınlatma Metni

1. Veri Sorumlusu
KoçKit olarak kişisel verileriniz 6698 sayılı Kişisel Verilerin Korunması Kanunu kapsamında işlenmektedir.

2. İşlenen Veriler
Kimlik bilgileri, iletişim bilgileri, rumuz, e-posta adresi, telefon numarası, şifre (şifrelenmiş), kullanım ve performans verileri işlenebilir.

3. İşleme Amaçları
Hesap oluşturma, kimlik doğrulama, hizmet sunumu, güvenlik, destek, yasal yükümlülüklerin yerine getirilmesi ve kullanıcı deneyiminin iyileştirilmesi.

4. Aktarım
Verileriniz yalnızca kanuni zorunluluklar veya hizmet sağlayıcılarımızla sınırlı ve gerekli ölçüde paylaşılabilir.

5. Haklarınız
KVKK madde 11 kapsamında verilerinize erişme, düzeltme, silme, işlemeyi kısıtlama ve itiraz etme haklarına sahipsiniz.

6. Saklama Süresi
Verileriniz hizmet ilişkisi süresince ve ilgili mevzuatta öngörülen süreler boyunca saklanır.

7. İletişim
KVKK talepleriniz için uygulama içi destek kanallarından bize ulaşabilirsiniz.

Devam etmek için metnin sonuna kadar okuyup "Tamam" butonuna basmanız gerekmektedir.
""".trimIndent()
