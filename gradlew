#!/usr/bin/env sh

DIR="$(cd "$(dirname "$0")" && pwd)"
GRADLE_USER_HOME="$DIR/.gradle"

exec "$DIR/gradle/wrapper/gradle-wrapper.jar" "$@"
