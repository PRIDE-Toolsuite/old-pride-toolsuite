package uk.ac.ebi.pride.data.core;

/**
 * Contact Role is a class to define the role of an Specific Contact (Organization or Person)in the context
 * of the Experiment a role is defined as CvParams ()
 * The role that a Contact plays in an organization or with respect to the associating class.
 * A Contact may have several Roles within scope, and as such, associations to ContactRole allow
 * the use of a Contact in a certain manner. Examples might include a provider, or a data analyst.
 * <p/>
 * Created by IntelliJ IDEA.
 * User: yperez
 * Date: 19/08/11
 * Time: 15:40
 */

public class ContactRole {

    /**
     * Could be an Organization or a Person
     */
    AbstractContact contact = null;

    /**
     * Role of an specific Contact
     */
    CvParam role = null;

    /**
     * Constructor for a Contact Role
     * @param contact An AbstractContact (Person or Organization)
     * @param role The role of the AbstractContact (CvTerms)
     */
    public ContactRole(AbstractContact contact, CvParam role) {
        this.contact = contact;
        this.role    = role;
    }

    /**
     * Get an AbstractContact
     * @return AbstractContact
     */
    public AbstractContact getContact() {
        return contact;
    }

    /**
     * Set and AbstractContact
     * @param contact AbstractContact
     */
    public void setContact(AbstractContact contact) {
        this.contact = contact;
    }

    /**
     * Get the role of the AbstractContact
     * @return CvParam
     */
    public CvParam getRole() {
        return role;
    }

    /**
     * Set a role for an AbstractContact
     * @param role CvParam
     */
    public void setRole(CvParam role) {
        this.role = role;
    }
}

