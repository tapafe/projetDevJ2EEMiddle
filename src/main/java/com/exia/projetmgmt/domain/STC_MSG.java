/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exia.projetmgmt.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import javax.xml.bind.annotation.*;

/**
 *
 * @author Arnaud RIGAUT
 */

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class STC_MSG implements Serializable{
    
    @XmlElement
    public Boolean op_statut;
    @XmlElement
    public String op_name;
    @XmlElement
    public String op_info;
    @XmlElement
    public String app_name;
    @XmlElement
    public String app_version;
    @XmlElement
    public String app_token;
    @XmlElement
    public String user_login;
    @XmlElement
    public String user_psw;
    @XmlElement
    public String user_token;
    @XmlElement
    public List<String> data;
    
    
}
