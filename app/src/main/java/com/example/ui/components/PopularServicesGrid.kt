package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Engineering
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceCategory
import com.example.ui.theme.PastelBankBg
import com.example.ui.theme.PastelBankIcon
import com.example.ui.theme.PastelCarBg
import com.example.ui.theme.PastelCarIcon
import com.example.ui.theme.PastelCourierBg
import com.example.ui.theme.PastelCourierIcon
import com.example.ui.theme.PastelEduBg
import com.example.ui.theme.PastelEduIcon
import com.example.ui.theme.PastelGovBg
import com.example.ui.theme.PastelGovIcon
import com.example.ui.theme.PastelHealthBg
import com.example.ui.theme.PastelHealthIcon
import com.example.ui.theme.PastelProfBg
import com.example.ui.theme.PastelProfIcon
import com.example.ui.theme.PastelTransportBg
import com.example.ui.theme.PastelTransportIcon
import com.example.ui.theme.RajshahiGreenPrimary
import com.example.ui.theme.NeumorphicCard
import com.example.ui.theme.neumorphicRaised
import com.example.ui.theme.rememberNeumorphColors

data class PopularCategoryMeta(
    val category: ServiceCategory,
    val titleBn: String,
    val icon: ImageVector,
    val iconTint: Color,
    val badgeBg: Color
)

@Composable
fun PopularServicesGrid(
    onCategoryClick: (ServiceCategory) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        PopularCategoryMeta(
            category = ServiceCategory.GOVT_ADMIN,
            titleBn = "সরকারি ও\nপ্রশাসনিক সেবা",
            icon = Icons.Default.AccountBalance,
            iconTint = PastelGovIcon,
            badgeBg = PastelGovBg
        ),
        PopularCategoryMeta(
            category = ServiceCategory.HEALTH,
            titleBn = "স্বাস্থ্য সেবা",
            icon = Icons.Default.LocalHospital,
            iconTint = PastelHealthIcon,
            badgeBg = PastelHealthBg
        ),
        PopularCategoryMeta(
            category = ServiceCategory.TRANSPORT,
            titleBn = "পরিবহন সেবা",
            icon = Icons.Default.LocalShipping,
            iconTint = PastelTransportIcon,
            badgeBg = PastelTransportBg
        ),
        PopularCategoryMeta(
            category = ServiceCategory.PROFESSIONAL,
            titleBn = "পেশাজীবী সেবা",
            icon = Icons.Default.Engineering,
            iconTint = PastelProfIcon,
            badgeBg = PastelProfBg
        ),
        PopularCategoryMeta(
            category = ServiceCategory.BANKING,
            titleBn = "ব্যাংকিং সেবা",
            icon = Icons.Default.AccountBalance,
            iconTint = PastelBankIcon,
            badgeBg = PastelBankBg
        ),
        PopularCategoryMeta(
            category = ServiceCategory.COURIER,
            titleBn = "কুরিয়ার সেবা",
            icon = Icons.Default.Inventory2,
            iconTint = PastelCourierIcon,
            badgeBg = PastelCourierBg
        ),
        PopularCategoryMeta(
            category = ServiceCategory.CAR_SERVICE,
            titleBn = "কার সার্ভিস",
            icon = Icons.Default.DirectionsCar,
            iconTint = PastelCarIcon,
            badgeBg = PastelCarBg
        ),
        PopularCategoryMeta(
            category = ServiceCategory.EDUCATION,
            titleBn = "শিক্ষা সেবা",
            icon = Icons.Default.School,
            iconTint = PastelEduIcon,
            badgeBg = PastelEduBg
        )
    )

    Column(modifier = modifier.fillMaxWidth()) {
        // Section Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Whatshot,
                    contentDescription = null,
                    tint = Color(0xFFEA580C),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "জনপ্রিয় সেবা",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onViewAllClick() }
                    .testTag("view_all_popular_button")
            ) {
                Text(
                    text = "সব সেবা দেখুন",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TangailGreenPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View all",
                    tint = TangailGreenPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // 2-Column Grid
        val rows = items.chunked(2)
        rows.forEach { pair ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                pair.forEach { item ->
                    Box(modifier = Modifier.weight(1f)) {
                        PopularServiceCard(
                            meta = item,
                            onClick = { onCategoryClick(item.category) }
                        )
                    }
                }
                if (pair.size == 1) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun PopularServiceCard(
    meta: PopularCategoryMeta,
    onClick: () -> Unit
) {
    val nc = rememberNeumorphColors()

    NeumorphicCard(
        shape = RoundedCornerShape(18.dp),
        cornerRadius = 18.dp,
        elevation = 5.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(122.dp)
            .clickable { onClick() }
            .testTag("popular_card_${meta.category.name}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Neumorphic Embossed Icon Pill
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .neumorphicRaised(
                        shape = RoundedCornerShape(12.dp),
                        cornerRadius = 12.dp,
                        elevation = 2.5.dp,
                        surfaceColor = meta.badgeBg,
                        highlightColor = nc.highlight.copy(alpha = 0.8f),
                        shadowColor = nc.shadow.copy(alpha = 0.6f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = meta.icon,
                    contentDescription = meta.titleBn,
                    tint = meta.iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Label and Arrow Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                Text(
                    text = meta.titleBn,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 16.sp,
                    modifier = Modifier.weight(1f)
                )

                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Go",
                    tint = RajshahiGreenPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}
