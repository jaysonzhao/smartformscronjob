
package com.gzsolartech.bpmportal.util.email;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="SendMailForEIPResult" type="{http://www.w3.org/2001/XMLSchema}boolean"/>
 *         &lt;element name="strErrorMsg" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "sendMailForEIPResult",
    "strErrorMsg"
})
@XmlRootElement(name = "SendMailForEIPResponse")
public class SendMailForEIPResponse {

    @XmlElement(name = "SendMailForEIPResult")
    protected boolean sendMailForEIPResult;
    protected String strErrorMsg;

    /**
     * Gets the value of the sendMailForEIPResult property.
     * 
     */
    public boolean isSendMailForEIPResult() {
        return sendMailForEIPResult;
    }

    /**
     * Sets the value of the sendMailForEIPResult property.
     * 
     */
    public void setSendMailForEIPResult(boolean value) {
        this.sendMailForEIPResult = value;
    }

    /**
     * Gets the value of the strErrorMsg property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrErrorMsg() {
        return strErrorMsg;
    }

    /**
     * Sets the value of the strErrorMsg property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrErrorMsg(String value) {
        this.strErrorMsg = value;
    }

}
