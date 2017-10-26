package pdm.isel.moviedatabaseapp.comms

import com.android.volley.*
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import com.android.volley.toolbox.HttpHeaderParser
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.nio.charset.Charset

data class HttpRequest <T>(val urlPath: String, private val klass: Class<T>, val successListener : (T)-> Unit, val errorListener: (VolleyError)->Unit) :
        JsonRequest<T>(Request.Method.GET, urlPath,"",successListener,errorListener) {

    private val mapper : ObjectMapper = jacksonObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                    response?.data!!,
                    Charset.forName(HttpHeaderParser.parseCharset(response.headers))
            )
            return Response.success(mapper.readValue(json, klass),HttpHeaderParser.parseCacheHeaders(response))

        }catch (e : IOException){
            return Response.error(ParseError(e))
        }
    }

    override fun deliverResponse(response: T) {
        successListener(response)
    }

    override fun deliverError(error: VolleyError?) {
        errorListener(error!!)
    }
}