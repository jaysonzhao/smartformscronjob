
package com.gzsolartech.bpmportal.util.email;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="strMailFrom" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strMailTo" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strMailSubject" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strMailBody" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strCredentialAccount" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strCredentialPwd" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strMailCC" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="strMailBcc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
    "strMailFrom",
    "strMailTo",
    "strMailSubject",
    "strMailBody",
    "strCredentialAccount",
    "strCredentialPwd",
    "strMailCC",
    "strMailBcc"
})
@XmlRootElement(name = "SendMailForEIP")
public class SendMailForEIP {

    protected String strMailFrom;
    protected String strMailTo;
    protected String strMailSubject;
    protected String strMailBody;
    protected String strCredentialAccount;
    protected String strCredentialPwd;
    protected String strMailCC;
    protected String strMailBcc;

    /**
     * Gets the value of the strMailFrom property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrMailFrom() {
        return strMailFrom;
    }

    /**
     * Sets the value of the strMailFrom property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrMailFrom(String value) {
        this.strMailFrom = value;
    }

    /**
     * Gets the value of the strMailTo property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrMailTo() {
        return strMailTo;
    }

    /**
     * Sets the value of the strMailTo property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrMailTo(String value) {
        this.strMailTo = value;
    }

    /**
     * Gets the value of the strMailSubject property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrMailSubject() {
        return strMailSubject;
    }

    /**
     * Sets the value of the strMailSubject property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrMailSubject(String value) {
        this.strMailSubject = value;
    }

    /**
     * Gets the value of the strMailBody property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrMailBody() {
        return strMailBody;
    }

    /**
     * Sets the value of the strMailBody property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrMailBody(String value) {
        this.strMailBody = value;
    }

    /**
     * Gets the value of the strCredentialAccount property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrCredentialAccount() {
        return strCredentialAccount;
    }

    /**
     * Sets the value of the strCredentialAccount property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrCredentialAccount(String value) {
        this.strCredentialAccount = value;
    }

    /**
     * Gets the value of the strCredentialPwd property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrCredentialPwd() {
        return strCredentialPwd;
    }

    /**
     * Sets the value of the strCredentialPwd property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrCredentialPwd(String value) {
        this.strCredentialPwd = value;
    }

    /**
     * Gets the value of the strMailCC property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrMailCC() {
        return strMailCC;
    }

    /**
     * Sets the value of the strMailCC property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrMailCC(String value) {
        this.strMailCC = value;
    }

    /**
     * Gets the value of the strMailBcc property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStrMailBcc() {
        return strMailBcc;
    }

    /**
     * Sets the value of the strMailBcc property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStrMailBcc(String value) {
        this.strMailBcc = value;
    }

}
