package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Balance
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceCategory
import com.example.ui.theme.NeumorphicCard
import com.example.ui.theme.RajshahiGreenPrimary
import com.example.ui.theme.neumorphicRaised
import com.example.ui.theme.rememberNeumorphColors

data class SubCategoryMeta(
    val category: ServiceCategory,
    val titleBn: String,
    val icon: ImageVector,
    val iconTint: Color,
    val bgTint: Color
)

@Composable
fun AllCategoriesRow(
    onCategoryClick: (ServiceCategory) -> Unit,
    onViewAllClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        SubCategoryMeta(
            category = ServiceCategory.SOCIAL_ORG,
            titleBn = "সামাজিক\nসংগঠন",
            icon = Icons.Default.Groups,
            iconTint = Color(0xFF6A1B9A),
            bgTint = Color(0xFFEDE7F6)
        ),
        SubCategoryMeta(
            category = ServiceCategory.AGRICULTURE,
            titleBn = "কৃষি\nসেবা",
            icon = Icons.Default.Eco,
            iconTint = Color(0xFF2E7D32),
            bgTint = Color(0xFFE8F5E9)
        ),
        SubCategoryMeta(
            category = ServiceCategory.TRADE_COMMERCE,
            titleBn = "ট্রেড/\nবাণিজ্য",
            icon = Icons.Default.Storefront,
            iconTint = Color(0xFFD84315),
            bgTint = Color(0xFFFBE9E7)
        ),
        SubCategoryMeta(
            category = ServiceCategory.LAW_LEGAL,
            titleBn = "আইন ও\nআইনজীবী",
            icon = Icons.Default.Balance,
            iconTint = Color(0xFF1565C0),
            bgTint = Color(0xFFE3F2FD)
        ),
        SubCategoryMeta(
            category = ServiceCategory.LOCAL_GOVT,
            titleBn = "স্থানীয়\nসেবা",
            icon = Icons.Default.AccountBalance,
            iconTint = Color(0xFF00897B),
            bgTint = Color(0xFFE0F2F1)
        ),
        SubCategoryMeta(
            category = ServiceCategory.TOURISM_OTHER,
            titleBn = "অন্যান্য\nসেবা",
            icon = Icons.Default.MoreHoriz,
            iconTint = Color(0xFF546E7A),
            bgTint = Color(0xFFECEFF1)
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
                    imageVector = Icons.Default.GridView,
                    contentDescription = null,
                    tint = RajshahiGreenPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "সকল বিভাগ",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { onViewAllClick() }
                    .testTag("view_all_categories_button")
            ) {
                Text(
                    text = "বিভাগগুলো দেখুন",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = RajshahiGreenPrimary
                )
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "View all",
                    tint = RajshahiGreenPrimary,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        val nc = rememberNeumorphColors()

        // Horizontal scrolling row of Neumorphic category cards
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(vertical = 6.dp, horizontal = 2.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items.forEach { item ->
                NeumorphicCard(
                    shape = RoundedCornerShape(18.dp),
                    cornerRadius = 18.dp,
                    elevation = 4.5.dp,
                    modifier = Modifier
                        .width(96.dp)
                        .height(112.dp)
                        .clickable { onCategoryClick(item.category) }
                        .testTag("category_pill_${item.category.name}")
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .neumorphicRaised(
                                    shape = CircleShape,
                                    cornerRadius = 22.dp,
                                    elevation = 2.5.dp,
                                    surfaceColor = item.bgTint,
                                    highlightColor = nc.highlight.copy(alpha = 0.8f),
                                    shadowColor = nc.shadow.copy(alpha = 0.6f)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.titleBn,
                                tint = item.iconTint,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = item.titleBn,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            lineHeight = 13.sp
                        )
                    }
                }
            }
        }
    }
}
