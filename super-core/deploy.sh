#!/usr/bin/env bash

# 变量定义
IMAGE=`cat IMAGE_NAME`
#DEPLOYMENT="foundation-message-deployment"
DEPLOYMENT=$1
#MODULE="foundation-message"
MODULE=$2

# 开始部署
echo "部署名：${DEPLOYMENT}，模块名：${MODULE}，更新最新镜像: ${IMAGE}"
#kubectl set image deployments/${DEPLOYMENT} ${MODULE}=${IMAGE}
