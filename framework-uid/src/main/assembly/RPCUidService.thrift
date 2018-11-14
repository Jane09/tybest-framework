namespace java com.tybest.framework.interface
service RPCUidService{

    i64 getTimestampe(1:i64 myTimestamp)

    string getUid(1:string param)
}