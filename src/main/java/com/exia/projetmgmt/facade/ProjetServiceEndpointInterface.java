/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exia.projetmgmt.facade;

import com.exia.projetmgmt.domain.STC_MSG;
import javax.jws.*;

/**
 *
 * @author Arnaud RIGAUT
 */
@WebService(name = "ProjetEndpoint")
public interface ProjetServiceEndpointInterface {
    
    @WebMethod(operationName = "decodage")
    @WebResult(name = "text")
    String decodage(@WebParam(name="STC_MSG") String receiveMsg);
    
}
