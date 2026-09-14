# s8128637Assignment2

Android app for the NIT3213 final assignment. Authenticates against the
`nit3213api`, lists the returned dashboard entities, and shows full details
for a selected entity.

## Screens

1. **Login** — username/password form. Posts to the Footscray auth endpoint
   and navigates to the dashboard on success, or shows an inline error.
2. **Dashboard** — RecyclerView of entities returned for the logged-in
   session's `keypass`, each card summarizing the entity's fields (excluding
   `description`). Supports pull-to-refresh.
3. **Details** — every field of the selected entity, including the full
   `description`.

## API

Base URL: `https://nit3213apinew.onrender.com`

| Endpoint | Method | Purpose |
|---|---|---|
| `/footscray/auth` | POST | `{ "username", "password" }` → `{ "keypass" }` |
| `/dashboard/{keypass}` | GET | → `{ "entities": [...], "entityTotal" }` |

Each topic's entities have a different schema, so entities are modeled as
generic `Map<String, Any>` rather than a fixed data class. `EntityUiMapper`
centralizes the logic for building a card summary (all fields except
`description`) and the full details view (all fields plus `description`).

## Architecture

MVVM with Hilt for dependency injection:

- `data/` — `Nit3213ApiService` (Retrofit), request/response DTOs, and
  repositories that translate HTTP/network failures into `ApiResult`
  states with user-facing messages (e.g. this API returns `404`, not
  `401`, for invalid login credentials).
- `di/` — Hilt modules providing the OkHttp/Retrofit/API singletons and
  binding repository interfaces to their implementations.
- `ui/` — one `Activity` + `HiltViewModel` per screen. Each ViewModel
  exposes a sealed UI state (`Loading` / `Success` / `Empty` / `Error`)
  as a `StateFlow`, collected by the Activity via
  `repeatOnLifecycle(STARTED)`.
- `util/` — `EntityUiMapper` (generic entity field formatting) and
  `Constants` (API base URL, endpoint, intent extra keys).

## Tech stack

- Kotlin, Views + ViewBinding (no Compose)
- Hilt (KSP annotation processing) for DI
- Retrofit + OkHttp + Gson for networking
- Coroutines + `StateFlow` for async state
- MockK + `kotlinx-coroutines-test` for unit tests

## Building and running

```bash
./gradlew assembleDebug   # build the debug APK
./gradlew installDebug    # install to a connected device/emulator
./gradlew testDebugUnitTest  # run unit tests
```

Requires JDK 17+ and the Android SDK (compileSdk/targetSdk 37). If your
Gradle build fails with a Hilt/Kotlin plugin conflict, note that this
project intentionally sets `android.builtInKotlin=false` in
`gradle.properties` — AGP 9.3.2's built-in Kotlin support isn't yet
compatible with Hilt's Gradle plugin, so the project falls back to the
classic Kotlin Gradle plugin + KSP for annotation processing.

## Tests

11 unit tests covering:

- `LoginViewModel` — blank-field validation, successful login, and
  repository error propagation.
- `DashboardViewModel` — success (populated list), empty list, and error
  states.
- `EntityUiMapper` — title/subtitle/description extraction, including the
  fallback when `description` is missing.

## Notes

- Login credentials (student ID as username, first name as password) are
  entered by the user at runtime — they are never hardcoded in the app.
- The project/package name (`s8128637assignment2`) is prefixed with `s`
  because Java/Kotlin package segments cannot start with a digit.
