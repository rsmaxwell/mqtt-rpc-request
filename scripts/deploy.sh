#!/bin/sh

BASEDIR=$(dirname "$0")
SCRIPT_DIR=$(cd $BASEDIR && pwd)
SUBPROJECT_DIR=$(dirname $SCRIPT_DIR)
PROJECT_DIR=$(dirname $SUBPROJECT_DIR)
BUILD_DIR=${SUBPROJECT_DIR}/build

. ${BUILD_DIR}/buildinfo

ls -al ${SCRIPT_DIR}
ls -al ${SUBPROJECT_DIR}
ls -al ${PROJECT_DIR}

cd ${SUBPROJECT_DIR}


${SUBPROJECT_DIR}/gradlew publish \
    -PrepositoryName=${REPOSITORY} \
    -PprojectVersion=${VERSION}
