#!/bin/bash
set -e

./gradlew :app:verifyPaparazziDebug -PscreenshotOnly
