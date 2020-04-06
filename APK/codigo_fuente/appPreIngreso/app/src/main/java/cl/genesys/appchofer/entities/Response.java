package cl.genesys.appchofer.entities;


import com.google.gson.Gson;

public class Response {
    public String Resultado;
    public String Token;


    public static Response fromJson(String s) {
        return new Gson().fromJson(s, Response.class);
    }

    public String getResultado() {
        return Resultado;
    }

    public void setResultado(String resultado) {
        Resultado = resultado;
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
