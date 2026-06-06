#!/bin/bash
# Health monitor for spring-ddd-new
# Runs every ~6 minutes, logs to .monitor-health.log

PROJECT_DIR="/mnt/c/projects/opensources/spring-ddd-project/spring-ddd-new"
LOG_FILE="$PROJECT_DIR/.monitor-health.log"
cd "$PROJECT_DIR" || exit 1

# Build classpath for JaCoCo once
JARS=$(find ~/.m2/repository/org/jacoco -name "*.jar" | grep -v sources | tr '\n' ':')
JARS="${JARS}$(find ~/.m2/repository/org/ow2/asm -name "asm-*.jar" | grep -v sources | tr '\n' ':')"

while true; do
    TIMESTAMP=$(date '+%Y-%m-%d %H:%M:%S')
    {
        echo "===== $TIMESTAMP ====="

        # Single-step: compile + test together, so jacoco.exec is only written after completion
        TEST_LOG=$(mktemp)
        mvn compile test -pl spring-ddd-tests -Dsurefire.failIfNoSpecifiedTests=false > "$TEST_LOG" 2>&1
        MVN_EXIT=$?
        TEST_SUMMARY=$(grep -E "^\[INFO\] Tests run:" "$TEST_LOG" | tail -1)
        BUILD_RESULT=$(grep -E "^\[INFO\] BUILD (SUCCESS|FAILURE)" "$TEST_LOG" | tail -1)

        if [ $MVN_EXIT -ne 0 ]; then
            FAILURES=$(echo "$TEST_SUMMARY" | grep -oP 'Failures: \K[0-9]+' || echo "0")
            ERRORS=$(echo "$TEST_SUMMARY" | grep -oP 'Errors: \K[0-9]+' || echo "0")
            if [ "${FAILURES:-0}" -gt 0 ] || [ "${ERRORS:-0}" -gt 0 ]; then
                echo "[TEST FAIL] $TEST_SUMMARY | $BUILD_RESULT"
            else
                echo "[BUILD FAIL] $BUILD_RESULT"
            fi
        else
            echo "[TEST OK] $TEST_SUMMARY | $BUILD_RESULT"
        fi
        rm -f "$TEST_LOG"

        # Coverage check - safe to read now because mvn has finished
        if [ -f spring-ddd-tests/target/jacoco.exec ]; then
            COV_OUT=$(java -cp "$JARS:/tmp" GenReport2 spring-ddd-tests/target/jacoco.exec \
              spring-ddd-domain/target/classes \
              spring-ddd-application-service/target/classes \
              spring-ddd-interface-web/target/classes \
              spring-ddd-infrastructure-persistence/target/classes \
              spring-ddd-infrastructure-cache/target/classes \
              spring-ddd-launcher/target/classes \
              spring-ddd-tests/target/classes 2>&1)
            TOTAL_LINE=$(echo "$COV_OUT" | grep "^TOTAL:")
            PARTIAL_LINE=$(echo "$COV_OUT" | grep "^Partial coverage classes")
            if [ -n "$TOTAL_LINE" ]; then
                echo "$TOTAL_LINE"
                PARTIAL_COUNT=$(echo "$PARTIAL_LINE" | grep -oP '\(\K[0-9]+' || echo "?")
                echo "Partial coverage classes count: $PARTIAL_COUNT"
            else
                echo "[COVERAGE ERROR]"
                echo "$COV_OUT" | head -5
            fi
        else
            echo "[COVERAGE SKIP] jacoco.exec not found"
        fi

        echo ""
    } >> "$LOG_FILE"

    sleep 360
done
