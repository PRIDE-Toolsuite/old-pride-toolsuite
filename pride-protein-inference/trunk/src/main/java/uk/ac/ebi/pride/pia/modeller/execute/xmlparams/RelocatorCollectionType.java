//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2013.08.27 at 05:05:16 PM CEST 
//


package uk.ac.ebi.pride.pia.modeller.execute.xmlparams;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for relocatorCollectionType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="relocatorCollectionType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="relocator" type="{}relocatorType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "relocatorCollectionType", propOrder = {
    "relocator"
})
public class RelocatorCollectionType {

    @XmlElement(required = true)
    protected RelocatorType relocator;

    /**
     * Gets the value of the relocator property.
     * 
     * @return
     *     possible object is
     *     {@link RelocatorType }
     *     
     */
    public RelocatorType getRelocator() {
        return relocator;
    }

    /**
     * Sets the value of the relocator property.
     * 
     * @param value
     *     allowed object is
     *     {@link RelocatorType }
     *     
     */
    public void setRelocator(RelocatorType value) {
        this.relocator = value;
    }

}
