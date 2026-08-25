# Changelog

Notable changes to NameMask are documented here. This project follows [Semantic Versioning](https://semver.org/).

## [Unreleased]

## [1.1.0] - 2026-08-25

### Changed

- Expanded support from Paper 1.21.11 to Paper 1.16.1 and newer with one jar.
- Compiled the plugin to Java 8 bytecode while leaving the server's Java requirement to Paper.
- Verified source compatibility with Paper 26.2 build 119, the latest stable release at publication time.
- Updated the Gradle wrapper and GitHub Actions dependencies to their latest stable releases.
- Added automated build checks and tag-driven GitHub releases with SHA-256 checksums.
- Expanded the installation, compatibility, command, build, and release documentation.

### Fixed

- Repeated `/nickname reset` calls now correctly report that no nickname is set.

## [1.0.1-alpha.1] - 2026-07-09

- Added compatibility notes for the Paper 26.x alpha line.

## [1.0.0] - 2026-07-07

- Added `/nickname <name>`, `/nickname reset`, and the `/nick` alias.
- Added UUID-based nickname persistence and automatic re-application on join.
- Added nickname validation and collision checks.

[Unreleased]: https://github.com/tiraaamisuuu/paper-nickname-plugin/compare/v1.1.0...HEAD
[1.1.0]: https://github.com/tiraaamisuuu/paper-nickname-plugin/compare/v1.0.1-alpha.1...v1.1.0
[1.0.1-alpha.1]: https://github.com/tiraaamisuuu/paper-nickname-plugin/compare/V1.0.0...v1.0.1-alpha.1
[1.0.0]: https://github.com/tiraaamisuuu/paper-nickname-plugin/releases/tag/V1.0.0
