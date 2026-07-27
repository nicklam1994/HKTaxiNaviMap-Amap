package com.nicklam.hktaxinavi.data.toll

/**
 * 香港隧道收費標準 (2024-2025)
 *
 * 資料來源: 運輸署
 * 的士收費標準 (截至 2024 年)
 */
data class TunnelToll(
    val name: String,          // 隧道名稱
    val nameZh: String,        // 中文名
    val fee: Double,           // 的士收費 (HKD)
    val centerLat: Double,     // 隧道中心緯度
    val centerLng: Double,     // 隧道中心經度
    val entryRadiusKm: Double  // 入口偵測半徑 (公里)
)

/**
 * 隧道費計算器
 *
 * 根據 GPS 位置偵測接近/通過隧道，計算累計費用
 */
object TollCalculator {

    /** 香港主要隧道列表 (的士收費) */
    val TUNNELS = listOf(
        // 過海隧道
        TunnelToll("Cross-Harbour Tunnel",     "紅磡海底隧道",   10.0,  22.2935,  114.1785,  0.5),
        TunnelToll("Eastern Harbour Tunnel",    "東區海底隧道",   25.0,  22.2948,  114.2250,  0.5),
        TunnelToll("Western Harbour Tunnel",    "西區海底隧道",   25.0,  22.3000,  114.1550,  0.5),

        // 九龍隧道
        TunnelToll("Tate's Cairn Tunnel",       "大老山隧道",     20.0,  22.3540,  114.2100,  0.5),
        TunnelToll("Lion Rock Tunnel",          "獅子山隧道",      8.0,  22.3480,  114.1750,  0.5),
        TunnelToll("Shing Mun Tunnels",         "城門隧道",        5.0,  22.3780,  114.1550,  0.5),
        TunnelToll("Eagle's Nest Tunnel",       "尖山隧道",        8.0,  22.3510,  114.1550,  0.5),
        TunnelToll("Sha Tin Heights Tunnel",    "沙田嶺隧道",      8.0,  22.3600,  114.1550,  0.5),

        // 新界隧道
        TunnelToll("Tsing Ma Bridge",           "青馬大橋",       0.0,   22.3510,  114.0780,  0.5),
        TunnelToll("Tai Lam Tunnel",            "大欖隧道",       48.0,  22.4000,  114.0580,  0.5),
        TunnelToll("Cheung Tsing Tunnel",       "長青隧道",       0.0,   22.3480,  114.1030,  0.5),
        TunnelToll("Shing Mun Tunnels (Tai Wai)","城門隧道(大圍)",  5.0,  22.3780,  114.1550,  0.5),

        // 香港島隧道
        TunnelToll("Aberdeen Tunnel",           "香港仔隧道",      5.0,   22.2530,  114.1770,  0.5),
    )

    /** 根據 GPS 座標判斷最近隧道 */
    fun findNearestTunnel(lat: Double, lng: Double): TunnelToll? {
        return TUNNELS.minByOrNull { tunnel ->
            distanceKm(lat, lng, tunnel.centerLat, tunnel.centerLng)
        }
    }

    /** 檢查是否在隧道入口範圍內 */
    fun isNearTunnel(lat: Double, lng: Double): TunnelToll? {
        return TUNNELS.firstOrNull { tunnel ->
            distanceKm(lat, lng, tunnel.centerLat, tunnel.centerLng) <= tunnel.entryRadiusKm
        }
    }

    /** Haversine 公式計算兩點距離 (公里) */
    private fun distanceKm(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {
        val R = 6371.0
        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLng / 2) * Math.sin(dLng / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }
}
