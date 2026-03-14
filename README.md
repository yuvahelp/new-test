# Yuva Help Android App

Modern Android app for https://yuva.help using Kotlin + Jetpack Compose.

## Highlights
- Auto-fetches WordPress posts from `https://yuva.help/wp-json/wp/v2/posts?_embed`.
- Home feed with modern card UI (thumbnail, title, excerpt).
- Category sections: Latest Jobs, Results, Admit Cards, Government Schemes, Education News.
- Article reader screen with featured image, date, full content, share button, and open-original-link.
- Bottom navigation: Home, Categories, Latest Updates, Search, About.
- Offline cache with Room.
- Periodic background sync via WorkManager (hourly).
- Local notification on fresh updates (when notification permission is granted on Android 13+).

## Tech Stack
- Kotlin
- Jetpack Compose + Material 3
- Retrofit + Moshi (WordPress REST API)
- Room (offline cache)
- WorkManager (background refresh)

## Notes
- Content is managed only on the website. Publishing a post on WordPress makes it available in app sync automatically.
- For production push notifications from server-side, integrate FCM + WordPress trigger/webhook.
