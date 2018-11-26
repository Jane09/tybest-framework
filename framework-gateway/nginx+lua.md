### 统一接入层
    
#### OpenResty
    
   [openresty](https://openresty.org/cn/)
    
    - 一个基于 Nginx 与 Lua 的高性能 Web 平台，其内部集成了大量精良的 Lua 库、第三方模块以及大多数的依赖项。用于方便地搭建能够处理超高并发、扩展性极高的动态 Web 应用、Web 服务和动态网关。
   
    sudo yum install yum-utils
    sudo yum-config-manager --add-repo https://openresty.org/package/centos/openresty.repo
    sudo yum install openresty
    sudo yum install openresty-resty
    sudo yum --disablerepo="*" --enablerepo="openresty" list available        