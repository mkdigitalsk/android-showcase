#!/bin/bash
# Local reflection of .github/workflows/pull_request.yml — same commands, same order.
set -e

./gradlew detekt
./gradlew :app:testDebugUnitTest
TZ=UTC ./gradlew :app:verifyPaparazziDebug -PscreenshotOnly
