# Changelog

All notable changes to the **Easy Good Eats** project will be documented in this file.

## [1.0.0] - 2026-03-25

### Added
- Created initial `README.md` with project description, functionality, and platform details.
- Added `CHANGELOG.md` to track project evolution.
- Added `ENVIRONMENT_SETUP.md` (Wiki) with instructions for setting up the development environment and VM.

### Fixed
- **SettingsScreen:** Fixed "Unresolved reference 'clickable'" error by adding the missing `androidx.compose.foundation.clickable` import.
- **SearchScreen:** Fixed a critical app crash (`NoSuchMethodError: FlowRow`) by replacing the unstable `FlowRow` component with `LazyRow`.

### Changed
- **UI Layout:** Moved the filter section in the `SearchScreen` to appear below the "Find Recipes" button for a more intuitive user flow.
- **Filter UI:** Updated filter sections to be horizontally scrollable chips using `LazyRow`.
