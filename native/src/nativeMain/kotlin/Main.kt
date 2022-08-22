import generate.pb.Greeter
import generate.pb.HelloReply
import generate.pb.HelloRequest
import kotlinx.coroutines.runBlocking

fun main(args: Array<String>) {
    // Just a fake little mock sample
    val server = object : Greeter.Server() {
        override suspend fun SayHello(req: HelloRequest): HelloReply {
            return HelloReply("Hello, ${req.name}!")
        }
    }

    val client = Greeter.Client(Rpc.Client.Mock(server))

    runBlocking {
        val reply = client.SayHello(HelloRequest("World"))
        println("Result: ${reply.message}")
    }
}
