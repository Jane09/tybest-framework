### reference
    http://www.yunweipai.com/archives/23653.html
    
### gateway class
    
    client gateway:
        receive the client request as a server
    
    open gateway:
        the entrypoint of the third party enterprise
        
        
### technology requirement
    
    Unified Access
        High Performance
        High Concurrency
        High Availablility
        Load Balance
    Security Protection
        Brush Control
        Blacklist
        
    Flow Control
        limit flow
        demotion
        broker
    protocol adapter
        http
        jsf
        rpc
        
        
### self-innovate
    
    access layer
        nginx+lua
        long connection
        ratelimiter
        blacklist/white list
        router
        load balance
        broker
        
    distribute layer
        
        nio + servlet3
        check
        restful converter
        
        
    backend business layer
        
        
    gateway registry center
    
    OA authentication center
    
        
        