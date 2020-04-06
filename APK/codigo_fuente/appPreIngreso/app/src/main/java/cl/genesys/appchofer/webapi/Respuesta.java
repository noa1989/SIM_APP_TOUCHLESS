package cl.genesys.appchofer.webapi;

public class Respuesta {
    public boolean success = true;
    public String message = "";
    public Object resultado = null;
    public Object vTabla = null;
    public Session SESSION = new Session();
}
