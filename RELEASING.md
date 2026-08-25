# Releasing NameMask

GitHub Actions builds and publishes a release whenever a semantic version tag beginning with `v` is pushed.

## Prepare the release

1. Confirm `main` is clean and the build passes with `./gradlew clean build`.
2. Move the entries in the `Unreleased` section of `CHANGELOG.md` into a new dated version section.
3. Commit the release documentation.

## Publish the release

For version `1.1.0`:

```bash
git tag -a v1.1.0 -m "NameMask 1.1.0"
git push origin v1.1.0
```

The release workflow will:

1. Validate the tag format.
2. Build `NameMask-1.1.0.jar` with the version embedded in `plugin.yml`.
3. Generate a SHA-256 checksum.
4. Create the GitHub release and attach both files.

Pre-release tags such as `v1.2.0-beta.1` are accepted and automatically marked as pre-releases. After the workflow completes, verify the generated notes. Upload the same jar to Modrinth and select every tested Paper/Minecraft version.

If a release workflow fails, fix the cause before re-running it. Do not move an already published tag to a different commit; publish a new patch version instead.
