/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.ArrayList;

/**
 *
 * @author ruano
 */
public class OperacionDto {

    /**
     * @return the login
     */
    public String getLogin() {
        return login;
    }

    /**
     * @param login the login to set
     */
    public void setLogin(String login) {
        this.login = login;
    }

    /**
     * @return the listaRolesOID
     */
    public ArrayList<String> getListaRolesOID() {
        return listaRolesOID;
    }

    /**
     * @param listaRolesOID the listaRolesOID to set
     */
    public void setListaRolesOID(ArrayList<String> listaRolesOID) {
        this.listaRolesOID = listaRolesOID;
    }

    private String login;
    private ArrayList<String> listaRolesOID;

  
}
