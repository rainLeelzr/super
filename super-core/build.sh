#!/usr/bin/env bash

# 变量定义
#MODULE="foundation-message"
MODULE=$1
HARBOR="manager-server-01:30030"
PROJECT="ipas"
TIME=`date "+%Y%m%d%H%M"`
GIT_VERSION=`git log -1 --pretty="%h"`
IMAGE_NAME=${HARBOR}/${PROJECT}/${MODULE}:${TIME}_${GIT_VERSION}

# 开始构建
docker build --build-arg MODULE=${MODULE} --build-arg PROJECT=${PROJECT} --build-arg HARBOR=${HARBOR} -t ${IMAGE_NAME} .
docker push ${IMAGE_NAME}

echo "${IMAGE_NAME}" > IMAGE_NAME