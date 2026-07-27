# HKTaxiNaviMap-Amap 🚕

**香港的士司機專用導航 App** — 基於高德地圖 Android SDK

> 整合 [taxi-ledger](https://github.com/nicklam1994/taxi-ledger) 記帳系統，實現導航→行程記錄→記帳一條龍

## ✨ 功能

- 🗺️ **高德車道級導航** — 車道引導、路口放大圖、即時路況
- 🚇 **隧道費自動計算** — 經過隧道自動記錄費用，累計顯示
- 📍 **的士站快捷導航** — 一鍵導航到最近的士站 (Phase 2)
- 🚫 **禁區提醒** — GPS geofencing 限制區警告 (Phase 2)
- 📊 **taxi-ledger 整合** — 行程結束自動生成收入記錄 (Phase 3)
- 🎤 **粵語語音播報** — 港式廣東話導航語音

## 🏗️ 技術棧

| 層級 | 技術 |
|------|------|
| 語言 | Kotlin |
| UI | Jetpack Compose + Material 3 |
| 導航引擎 | 高德 Android 導航 SDK 9.x |
| 地圖 | 高德 3D 地圖 SDK 10.x |
| 定位 | 高德定位 SDK 6.x |
| 網絡 | Retrofit + OkHttp + Coroutines |
| 本地儲存 | Room + DataStore |
| 後端整合 | FastAPI (taxi-ledger) |

## 🚀 快速開始

### 前置條件

- Android Studio Hedgehog (2023.1+) 或更新
- JDK 17
- Android SDK 34
- 高德地圖 API Key ([申請](https://console.amap.com/dev/key/app))

### 1. Clone 專案

```bash
git clone https://github.com/nicklam1994/HKTaxiNaviMap-Amap.git
cd HKTaxiNaviMap-Amap
```

### 2. 配置 API Key

在 `gradle.properties` 中加入你的高德 API Key：

```properties
AMAP_API_KEY=你的高德Key
```

> ⚠️ **不要將 Key 提交到 Git！** `gradle.properties` 已在 `.gitignore` 中

### 3. 用 Android Studio 打開專案

```bash
# 或在 WSL 中:
studio .
```

### 4. 同步 Gradle + 運行

- File → Sync Project with Gradle Files
- Run → Run 'app'

## 📁 專案結構

```
app/src/main/java/com/nicklam/hktaxinavi/
├── HKTaxiNaviApp.kt          # Application (高德 SDK 初始化)
├── MainActivity.kt            # 主頁 (Compose)
├── NaviActivity.kt            # 導航頁 (AMapNaviView)
├── overlay/
│   └── TollOverlayView.kt     # 隧道費浮動 Overlay
├── data/
│   ├── toll/
│   │   └── TollCalculator.kt  # 隧道費計算 + 收費表
│   └── api/                   # taxi-ledger API 集成 (Phase 3)
└── ui/
    ├── theme/                 # Compose 主題
    └── screen/
        └── HomeScreen.kt      # 主頁畫面
```

## 📋 開發路線圖

| Phase | 內容 | 狀態 |
|-------|------|:---:|
| 0 | 專案骨架 + SDK 集成 | ✅ |
| 1 | 核心導航 (搜尋→路線→導航) | 🚧 |
| 2 | 的士專屬功能 (隧道費/的士站/禁區) | 📅 |
| 3 | taxi-ledger 整合 (行程→記帳) | 📅 |

## 📄 授權

MIT License — 詳見 [LICENSE](LICENSE)
