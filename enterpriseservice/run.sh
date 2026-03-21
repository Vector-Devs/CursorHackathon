#!/usr/bin/env bash
# Run without spring-boot-maven-plugin (avoids loader-tools / buildpack resolution on strict mirrors).
set -euo pipefail
cd "$(dirname "$0")"
mvn -q compile
mvn -q dependency:build-classpath \
	-Dmdep.pathSeparator=: \
	-Dmdep.includeScope=runtime \
	-Dmdep.outputFile=target/cp.txt
exec java -cp "target/classes:$(cat target/cp.txt)" com.enterpriseservice.EnterpriseServiceApplication "$@"
