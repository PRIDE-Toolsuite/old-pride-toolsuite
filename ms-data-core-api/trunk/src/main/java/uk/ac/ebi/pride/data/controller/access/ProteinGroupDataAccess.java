package uk.ac.ebi.pride.data.controller.access;

import uk.ac.ebi.pride.data.core.ProteinGroup;

import java.util.Collection;

/**
 * ProteinAmbiguityGroupDataAccess defines the interface for accessing protein grouping information.
 *
 * @author Rui Wang
 * @version $Id$
 */
public interface ProteinGroupDataAccess {

    /**
     * Whether this controller contains protein groups information
     *
     * @return boolean  return true if identifications exist
     */
    public boolean hasProteinAmbiguityGroup();

    /**
     * Get a collection of identification group ids
     *
     * @return Collection   a string collection of identification ids
     */
    public Collection<Comparable> getProteinAmbiguityGroupIds();

    /**
     * Get the total number of protein ambiguity groups
     *
     * @return  number of protein ambiguity groups
     */
    public int getNumberOfProteinAmbiguityGroups();

    /**
     * Get protein ambiguity group using protein group id
     *
     * @param proteinGroupId protein group id
     * @return  protein group
     */
    public ProteinGroup getProteinAmbiguityGroupById(Comparable proteinGroupId);


}
