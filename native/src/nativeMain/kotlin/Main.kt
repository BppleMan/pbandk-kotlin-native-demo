import generate.pb.Greeter
import generate.pb.HelloReply
import generate.pb.HelloRequest
import io.ktor.server.application.call
import io.ktor.server.cio.CIO
import io.ktor.server.engine.embeddedServer
import io.ktor.server.request.receiveChannel
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import io.ktor.util.Identity.decode
import kotlinx.cinterop.allocArrayOf
import kotlinx.cinterop.memScoped
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import pbandk.decodeFromByteArray
import pbandk.encodeToByteArray
import platform.Foundation.NSData
import platform.Foundation.NSFileHandle
import platform.Foundation.NSLog
import platform.Foundation.create
import platform.Foundation.fileHandleForWritingAtPath
import platform.Foundation.writeData

fun main(args: Array<String>) {
    // Just a fake little mock sample
    val server = object : Greeter.Server() {
        override suspend fun SayHello(req: HelloRequest): HelloReply {
            return HelloReply("Hello, ${req.name}!")
        }
    }

    val client = Greeter.Client(Rpc.Client.Mock(server))

    runBlocking {
        launch {
            val reply = client.SayHello(HelloRequest("World"))
            println("Result: ${reply.message}")
        }
    }

    val request = HelloRequest("Hello World")
    val byteArray = request.encodeToByteArray()
    val nsdata = memScoped {
        NSData.create(bytes = allocArrayOf(byteArray), length = byteArray.size.toULong())
    }
    NSLog("%@", nsdata);
    val fileHandle = NSFileHandle.fileHandleForWritingAtPath("/Users/bppleman/floater/pbandk-kotlin-native-demo/bin")!!
    println(fileHandle.hash)
    fileHandle.writeData(nsdata)

    embeddedServer(CIO, port = 9090) {
        routing {
            post("/") {
                val channel = call.receiveChannel()
                val packet = channel.readRemaining()
                val byteArray = mutableListOf<Byte>()
                while (packet.canRead()) {
                    byteArray.add(packet.readByte())
                }
                val request = HelloRequest.decodeFromByteArray(byteArray.toByteArray())
                println(request.name)
                call.respondText("Hello, world!")
            }
        }
    }.start(wait = true)
}
