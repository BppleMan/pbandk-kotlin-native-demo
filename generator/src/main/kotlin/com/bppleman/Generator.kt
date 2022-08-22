package com.bppleman

import pbandk.gen.ServiceGenerator
import java.nio.file.Paths

class Generator : ServiceGenerator {
    override fun generate(service: ServiceGenerator.Service): List<ServiceGenerator.Result> {
        service.debug { "Generating code for service ${service.name}" }
        val interfaceMethods = mutableListOf<String>()
        val clientMethods = mutableListOf<String>()
        val serverMethods = mutableListOf<String>()
        service.methods.forEach { method ->
            val reqType = service.kotlinTypeMappings[method.inputType!!]!!
            val respType = service.kotlinTypeMappings[method.outputType!!]!!
            val nameLit = "\"${method.name}\""
            interfaceMethods.add("suspend fun ${method.name}(req: $reqType): $respType")
            clientMethods.add("override suspend fun ${method.name}(req: $reqType): $respType { " +
                "return client.callUnary($nameLit, req) }")
            serverMethods.add("$nameLit -> ${method.name}(req as $reqType) as Resp")
        }
        return listOf(ServiceGenerator.Result(
            otherFilePath = Paths.get(service.filePath).resolveSibling(service.name + ".kt").toString().replace("main.", ""),
            code = """
                package ${service.file.kotlinPackageName}
                
                interface ${service.name} {
                    ${interfaceMethods.joinToString("\n    ")}
                    class Client(val client: Rpc.Client) : ${service.name} {
                        ${clientMethods.joinToString("\n        ")}
                    }
                    abstract class Server : Rpc.Server(), ${service.name} {
                        override suspend fun <Req : pbandk.Message, Resp : pbandk.Message> onUnaryCall(name: String, req: Req): Resp {
                            return when (name) {
                                ${serverMethods.joinToString("\n                ")}
                                else -> error("404")
                            }
                        }
                    }
                }
            """.trimIndent()
        ))
    }
}
