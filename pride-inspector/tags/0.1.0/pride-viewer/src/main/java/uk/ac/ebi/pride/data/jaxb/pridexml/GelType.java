//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2010.03.16 at 10:28:41 AM GMT 
//


package uk.ac.ebi.pride.data.jaxb.pridexml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * The GelType (abstract) element provides a basis for describing the kind of gel used for this identication.
 * 
 * <p>Java class for GelType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="GelType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="GelLink" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="additional" type="{}paramType" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GelType", propOrder = {
    "gelLink",
    "additional"
})
@XmlSeeAlso({
    SimpleGel.class
})
public abstract class GelType {

    @XmlElement(name = "GelLink", required = true)
    protected String gelLink;
    protected ParamType additional;

    /**
     * Gets the value of the gelLink property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getGelLink() {
        return gelLink;
    }

    /**
     * Sets the value of the gelLink property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setGelLink(String value) {
        this.gelLink = value;
    }

    /**
     * Gets the value of the additional property.
     * 
     * @return
     *     possible object is
     *     {@link ParamType }
     *     
     */
    public ParamType getAdditional() {
        return additional;
    }

    /**
     * Sets the value of the additional property.
     * 
     * @param value
     *     allowed object is
     *     {@link ParamType }
     *     
     */
    public void setAdditional(ParamType value) {
        this.additional = value;
    }

}
