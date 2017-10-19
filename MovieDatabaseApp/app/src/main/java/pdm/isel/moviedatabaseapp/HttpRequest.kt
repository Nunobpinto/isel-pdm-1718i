package pdm.isel.moviedatabaseapp
import com.android.volley.*
import com.android.volley.toolbox.JsonRequest
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import com.android.volley.toolbox.HttpHeaderParser
import java.nio.charset.Charset


class HttpRequest <T>(url : String, private val klass: Class<T>, successListener : (T)-> Unit, errorListener: (VolleyError)->Unit) :
        JsonRequest<T>(Request.Method.GET,url,"",successListener,errorListener) {

    private val mapper : ObjectMapper = ObjectMapper()

    override fun parseNetworkResponse(response: NetworkResponse?): Response<T> {
        return try {
            val json = String(
                    response?.data!!,
                    Charset.forName(HttpHeaderParser.parseCharset(response.headers))
            )
            Response.success(
                    mapper.readValue(json, klass),
                    HttpHeaderParser.parseCacheHeaders(response))
        }catch (e : IOException){
            Response.error(ParseError(e))
        }

    }

}