 # kernel-net-core
 
``` yaml
isass:
  core:
    net:
      # 网络模块公共配置
      # 网络通信模块总开关
      enabled: true
`     # 默认通信协议：tcp(开发中)、websocket(开发中)、socketio(已实现)、mqtt(规划中)
      protocol: socketio
      # 是否使用通信代理
      proxy: true
      totalNodeAbove: 1000
      virtualNodeCount: -1
      
      # 网络通信 socketio 协议实现
      socketio:
        enabled: true
        # 监听的 hostname
        hostname: 0.0.0.0
        # 端口
        port: 20041
        # 不使用代理时，暴露给客户端连接的 url。如果不填，则自动获取生成为：http://{网卡ip}:port
        exposeUrl: http://127.0.0.1:20041
```