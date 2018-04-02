namespace java com.vanilla.rpc.thrift

service EchoService{
	string echo(
		1:string str
	)
}