package cl.genesys.appchofer.entities;

/*
 * Created by jorge.padilla on 09-09-2015.
 */

public class ListaValores {

    private String listId;
    private String listDesc;
    private int listTipo;

    public ListaValores() {

    }

    public ListaValores(String listId, String listDesc, int listTipo) {
        this.listId = listId;
        this.listDesc = listDesc;
        this.listTipo = listTipo;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getListDesc() {
        return listDesc;
    }

    public void setListDesc(String listDesc) {
        this.listDesc = listDesc;
    }

    public int getListTipo() {
        return listTipo;
    }

    public void setListTipo(int listTipo) {
        this.listTipo = listTipo;
    }
}




