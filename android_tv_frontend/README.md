# Android TV Frontend

Modern TV UI with Ocean Professional theme:
- Left navigation: Home, Search, Favorites, Settings
- Top search bar with voice trigger placeholder
- Central content grid with focus scaling/outline
- TV-safe margins and DPAD navigation
- Landscape-only, overscan-safe padding (48dp each side)

Architecture:
- Activity: FragmentActivity for Leanback compatibility (MainActivity.kt)
- UI: ConstraintLayout with left navigation rail, top search bar, and RecyclerView-based grid
- Cards: Custom CardItemView with focus outline and scale animation
- Data: Sample data per section (Home/Search/Favorites/Settings)

Theme:
- Ocean Professional colors:
  - Primary: #2563EB
  - Secondary/Success: #F59E0B
  - Error: #EF4444
  - Background: #f9fafb
  - Surface: #ffffff
  - Text: #111827
- Retro note: clean aesthetic, subtle shadows, rounded corners, minimalist design

Run instructions:
1) Ensure JDK 17 is installed and Android SDK is configured (ANDROID_HOME etc.).
2) From the android_tv_frontend folder:
   - Build: ./gradlew assembleDebug
   - Install on a TV/emulator: ./gradlew installDebug
3) Launch on device from Android TV launcher (app is registered with LEANBACK_LAUNCHER).

Navigation and DPAD:
- Focus moves left-nav -> search -> content grid
- All interactive elements are focusable and provide visual feedback
- Grid cards scale up and show a focus outline when focused

Voice search:
- The mic button is a placeholder; integrate SpeechRecognizer or Assistant for a full voice experience.
- If implementing, request RECORD_AUDIO permission (TV-safe).

Future improvements:
- Add Fragments for each top-level destination if deeper navigation is required
- Consider migrating to Jetpack Compose for TV (androidx.tv) when available in this project
