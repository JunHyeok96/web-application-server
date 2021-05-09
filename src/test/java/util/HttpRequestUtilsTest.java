package util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Map;

import org.junit.Test;

import util.HttpRequestUtils.Pair;
import webserver.HttpMethod;

public class HttpRequestUtilsTest {
    @Test
    public void parseQueryString() {
        String queryString = "userId=javajigi";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));

        queryString = "userId=javajigi&password=password2";
        parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is("password2"));
    }

    @Test
    public void parseQueryString_null() {
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(null);
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString("");
        assertThat(parameters.isEmpty(), is(true));

        parameters = HttpRequestUtils.parseQueryString(" ");
        assertThat(parameters.isEmpty(), is(true));
    }

    @Test
    public void parseQueryString_invalid() {
        String queryString = "userId=javajigi&password";
        Map<String, String> parameters = HttpRequestUtils.parseQueryString(queryString);
        assertThat(parameters.get("userId"), is("javajigi"));
        assertThat(parameters.get("password"), is(nullValue()));
    }

    @Test
    public void parseCookies() {
        String cookies = "logined=true; JSessionId=1234";
        Map<String, String> parameters = HttpRequestUtils.parseCookies(cookies);
        assertThat(parameters.get("logined"), is("true"));
        assertThat(parameters.get("JSessionId"), is("1234"));
        assertThat(parameters.get("session"), is(nullValue()));
    }

    @Test
    public void getKeyValue() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId=javajigi", "=");
        assertThat(pair, is(new Pair("userId", "javajigi")));
    }

    @Test
    public void getKeyValue_invalid() throws Exception {
        Pair pair = HttpRequestUtils.getKeyValue("userId", "=");
        assertThat(pair, is(nullValue()));
    }

    @Test
    public void parseHeader() throws Exception {
        String header = "Content-Length: 59";
        Pair pair = HttpRequestUtils.parseHeader(header);
        assertThat(pair, is(new Pair("Content-Length", "59")));
    }

    @Test
    public void parseRequestPath() {
        String url = "GET /index.html HTTP/1.1";
        assertEquals(HttpRequestUtils.parseRequestPath(url), "/index.html");
    }

    @Test
    public void parseRequestPath_파라미터있음(){
        String url = "GET /user/create?userId=junhyeok96&password=1234&name=jerry&email=abcd@gmail.com HTTP/1.1";
        assertEquals(HttpRequestUtils.parseRequestPath(url), "/user/create");
    }


    @Test
    public void parseHttpMethod(){
        String url = "GET /index.html HTTP/1.1";
        assertEquals(HttpRequestUtils.parseMethod(url), HttpMethod.GET);
    }

    @Test
    public void parseParams(){
        String url = "GET /user/create?userId=junhyeok96&password=1234&name=jerry&email=abcd@gmail.com HTTP/1.1";
        Map<String, String> paramMap = HttpRequestUtils.parseParameter(url);

        assertEquals(paramMap.get("userId"), "junhyeok96");
        assertEquals(paramMap.get("password"), "1234");
        assertEquals(paramMap.get("name"),"jerry");
        assertEquals(paramMap.get("email"), "abcd@gmail.com");
    }

    @Test
    public void parseParams_파라미터_없음(){
        String url = "GET /user/create HTTP/1.1";
        Map<String, String> paramMap = HttpRequestUtils.parseParameter(url);

        assertEquals(paramMap.size(), 0);
    }

}
