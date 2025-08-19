# Shop Demo + Applitools Visual Testing

A simple Jetpack Compose demo app with **Applitools Eyes** integration for Android (Espresso).  
Includes toggleable **visual bugs** to create testing challenges.

---

## Setup

1. Clone repo and open in Android Studio.
2. Make sure to include Applitools dependency in `app/build.gradle`:
   ```gradle
   androidTestImplementation 'com.applitools:eyes-android-espresso:latest.release'
3. Set your API key in BaseTest.kt:
   ```kotlin
   eyes.apiKey = "YOUR_API_KEY"

### Visual Challenges
```markdown
## Visual Challenges

In the Product List screen, tap the 🐞 icon to toggle intentional UI glitches:

- Off-by-one cart badge  
- Missing currency symbol  
- Randomized padding  
- Wrong button color  
- Truncated product names  
- Faint divider  
```

## Usage Flow

1. Run the tests once → establishes **baseline screenshots**.
2. Toggle some glitches with the 🐞 icon.
3. Run tests again → Applitools will highlight mismatches in the dashboard.

