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

      # 网络通信 socketio 协议实现
      socketio:
        enabled: true
        # 监听的 hostname
        hostname: 0.0.0.0
        # 端口
        port: 20041
```