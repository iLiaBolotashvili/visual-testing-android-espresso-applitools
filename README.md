# Shop Demo + Applitools Visual Testing

Jetpack Compose demo app wired to **Applitools Eyes** (Espresso).  
Includes a toggleable **🐞 Visual Bugs** button to create real‑world visual diffs.

---

## Quick Start

1) Add the Eyes Espresso SDK to **`app/build.gradle`**:
```gradle
androidTestImplementation "com.applitools:eyes-android-espresso:latest.release"
```

2) Set your API key in **`androidTest/.../util/BaseTest.kt`**:
```kotlin
eyes.apiKey = "YOUR_API_KEY"
```

3) Run instrumentation tests: **Run ▶ VisualTests** (or `./gradlew connectedAndroidTest`).  
   First run creates a **baseline**; later runs compare & show diffs in the Eyes dashboard.

---

## Visual Bugs (on-demand)

- **Login** screen: top **Extended FAB** — “**🐞 Visual Bugs: ON/OFF**”
- When ON, a few subtle, realistic glitches appear (e.g., reduced elevation, slightly wrong width/color).
- **Products** screen reads the same flag for tiny inconsistencies (e.g., altered elevation/corners/price color).  
  Search the code for comments like **`// BUG #1`**, **`// BUG #2`**, etc.

---

## Test Project Layout (POM)

```
app/src/androidTest/java/com/example/shop
├─ data/Constants.kt
├─ pages/                 # Page Objects
│  ├─ LoginPage.kt
│  ├─ ProductsListPage.kt
│  ├─ CartPage.kt
│  ├─ CheckoutPage.kt
│  └─ ProfilePage.kt
├─ tests/VisualTests.kt   # example journeys
└─ util/
   ├─ BaseTest.kt         # Eyes config + snapshot helper
   ├─ HelperFunctions.kt  # waits/taps/type (Compose-safe)
   └─ TestConfig.kt       # timeouts
```

**Helpers** avoid raw touches and flaky waits (e.g., focusing text fields via semantics instead of taps).

---

## Compose‑safe Snapshots (stable)

Use the provided `snapshot(tag, waitForText)` helper before each checkpoint:
```kotlin
// waits for a stable cue, syncs Compose/Espresso, tiny settle, then:
eyes.check(Target.window().withName(tag))
```
Pick stable cues like **"Shop"**, **"Your Cart"**, **"Shipping & Payment"** (not transient snackbars).

---

## Coil & Screenshots (avoid hardware bitmap crash)

`DemoApplication` installs a software‑bitmap ImageLoader in debug/tests:
```kotlin
class DemoApplication : Application(), ImageLoaderFactory {
  override fun newImageLoader() = ImageLoader.Builder(this)
    .allowHardware(false) // hardware enabled in release if you prefer
    .build()
}
```
Manifest set with: `android:name=".DemoApplication"`.

---

## Fresh Baselines (start clean)

Change any baseline key for a “new app” run:
```kotlin
config.appName = "Shop Demo – ${System.currentTimeMillis()}"
// or: config.branchName = "workshop-${System.currentTimeMillis()}"
```

---

## Dashboard Tips

- Accept/Reject changes to update baselines.
- Keep diffs from failing tests.
```kotlin
try { eyes.close() } catch (_: com.applitools.eyes.android.core.DiffsFoundException) {}
```

---

## Troubleshooting

- **Failed to inject touch input** → target a node with **`hasClickAction()`**; use page helpers.
- **Background‑only screenshots** → use `snapshot(...)` with a real, stable `waitForText` + short settle.
- **Software rendering doesn't support hardware bitmaps** → ensure debug/tests use software bitmaps (see `DemoApplication`).

Happy testing! 🎉
