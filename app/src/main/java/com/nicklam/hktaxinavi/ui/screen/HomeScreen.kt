package com.nicklam.hktaxinavi.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nicklam.hktaxinavi.ui.theme.TaxiGold
import com.nicklam.hktaxinavi.ui.theme.TaxiGreen

/**
 * 主頁畫面 — 導航啟動頁
 *
 * 包含:
 * - App 標題
 * - 目的地搜尋欄 (Phase 1)
 * - 常用地點快捷鍵 (Phase 2)
 * - 開始導航按鈕
 */
@Composable
fun HomeScreen(
    onStartNavi: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        // === App 標題 ===
        Text(
            text = "🚕",
            fontSize = 48.sp
        )
        Text(
            text = "HK Taxi Navi",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "香港的士司機專用導航",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // === 快速開始導航 (Demo 用) ===
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(16.dp)),
            colors = CardDefaults.cardColors(
                containerColor = TaxiGreen
            ),
            onClick = onStartNavi
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🗺️ 開始導航",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Text(
                    text = "使用高德車道級導航 SDK",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // === 功能預告 ===
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "📋 開發路線圖",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                FeatureItem("✅", "高德導航 SDK 集成", done = true)
                FeatureItem("✅", "車道引導 + 路口放大圖", done = true)
                FeatureItem("🚧", "目的地搜尋 + 路線規劃", done = false)
                FeatureItem("🚧", "的士站快捷鍵", done = false)
                FeatureItem("📅", "隧道費自動計算", done = false)
                FeatureItem("📅", "taxi-ledger 行程整合", done = false)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        // === 底部資訊 ===
        Text(
            text = "Powered by 高德地圖 AMap SDK",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f),
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Composable
private fun FeatureItem(icon: String, text: String, done: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = icon,
            fontSize = 14.sp,
            modifier = Modifier.width(28.dp)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            color = if (done) {
                MaterialTheme.colorScheme.onSurface
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            }
        )
    }
}
