/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.exia.projetmgmt.facade;

import com.exia.projetmgmt.domain.STC_MSG;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Scanner;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.*;
import javax.jms.*;
import javax.jws.*;
import javax.xml.bind.*;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.BindingType;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.w3c.dom.*;
import javax.xml.parsers.*;

/**
 *
 * @author Arnaud RIGAUT
 */
@Stateless
@WebService(
  endpointInterface = "com.exia.projetmgmt.facade.ProjetServiceEndpointInterface",
  portName = "ProjetInterface",
  serviceName = "ProjetService"
 )
@BindingType("http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class ProjetServiceBean implements ProjetServiceEndpointInterface {

    @Inject
    private JMSContext context;
    
    @Resource(lookup="jms/textQueue")
    private Queue textQueue;
    
    
    @Override
    public String decodage(String name) {

        Document doc = convertStringToXMLDocument(name);
        NodeList nList = doc.getElementsByTagName("data");
        Node text = nList.item(0);
        Element elem = (Element) text;
        
        STC_MSG msg = new STC_MSG();
        
        List<String> strList = new ArrayList<String>();
        strList.add(elem.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue());
        
        msg.data = strList;
        
        sendText(msg);
        return String.valueOf(elem.getElementsByTagName("name").item(0).getChildNodes().item(0).getNodeValue());
        //return(String.valueOf(doc));
    }
    
    private void sendText(STC_MSG msg){
        JAXBContext jaxbContext;
        try{            
            jaxbContext = JAXBContext.newInstance(STC_MSG.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            StringWriter writer = new StringWriter();
            //transformation de l'objet en flux XML stocké dans un Writer
            jaxbMarshaller.marshal(msg, writer);
            String xmlMessage = writer.toString();
            //affichage du XML dans la console de sortie
            System.out.println(xmlMessage);
            
            //encapsulation du paiement au format XML dans un objet javax.jms.TextMessage
            TextMessage msgContext = context.createTextMessage(xmlMessage);
            
            //envoi du message dans la queue textQueue
            context.createProducer().send(textQueue, msgContext);


        }catch(JAXBException ex){
            Logger.getLogger(ProjetServiceBean.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static Document convertStringToXMLDocument(String xmlString) 
    {
        //Parser that produces DOM object trees from XML content
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         
        //API to obtain DOM Document instance
        DocumentBuilder builder = null;
        try
        {
            //Create DocumentBuilder with default configuration
            builder = factory.newDocumentBuilder();
             
            //Parse the content to Document object
            Document doc = builder.parse(new InputSource(new StringReader(xmlString)));
            return doc;
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
        return null;
    }
    
    private static double confiance(String text){
        
        ArrayList<String> word = new ArrayList<String>();
        ArrayList<String> dico = new ArrayList<String>();

        Scanner scanner = new Scanner(text);
        while (scanner.hasNext() == true) {
            String s = scanner.next();
            word.add(s);
            dico.add(s);
        }

        word = new ArrayList<>(new HashSet<>(word));
        dico = new ArrayList<>(new HashSet<>(dico));
            
        return calc(word, dico);
    }
    
    public static double calc(ArrayList<String> text, ArrayList<String> dico){

            int conf = 0;

            for (String word:text) {
                if (dico.contains(word)){
                    conf += 1;
                }
            }

            int size = text.size();
            double temp = (double) conf/size;
            
            System.out.println(conf);
            System.out.println(size);
            System.out.println(temp);       
            
            return (temp * 100);
        }

        private static String deleteAll(String strValue, String charToRemove) {
            return strValue.replaceAll(charToRemove, "");

        }
    

}
