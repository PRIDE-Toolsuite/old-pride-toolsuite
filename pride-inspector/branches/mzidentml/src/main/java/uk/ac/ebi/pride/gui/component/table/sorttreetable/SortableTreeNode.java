package uk.ac.ebi.pride.gui.component.table.sorttreetable;

import org.jdesktop.swingx.treetable.TreeTableNode;

import javax.swing.*;
import javax.swing.RowSorter.SortKey;
import javax.swing.table.TableModel;
import java.util.List;

/**
 * Common interface for sortable tree table nodes.
 * 
 * @author yperez
 */
public interface SortableTreeNode extends TreeTableNode {
	
	/**
	 * Sorts this node's list of children in accordance with the provided sort
	 * keys and filter.
	 * 
	 * @param sortKeys the list of sort keys
	 * @param filter the row filter
	 * @param hideEmpty <code>true</code> if non-leaf nodes with no visible children shall be hidden,
	 *  <code>false</code> otherwise
	 */
	public void sort(List<? extends SortKey> sortKeys, RowFilter<? super TableModel, ? super Integer> filter, boolean hideEmpty);
	
	/**
	 * Returns the model index of the node's child at the specified view index.
	 * @param viewIndex
	 * @return the child's model index
	 */
	public int convertRowIndexToModel(int viewIndex);

	/**
	 * Returns the view index of the node's child at the specified model index.
	 * @param modelIndex
	 * @return the child's view index
	 */
	public int convertRowIndexToView(int modelIndex);
	
	/**
	 * Returns whether this node is capable of sorting its children.
	 * @return <code>true</code> if this node can sort its children,
	 *  <code>false</code> otherwise
	 */
	public boolean canSort();
	
	/**
	 * Returns whether this node is capable of sorting specific columns of its children.
	 * @param sortKeys the list of sort keys specifying the columns that are to be sorted
	 * @return <code>true</code> if this node can sort its children,
	 *  <code>false</code> otherwise
	 */
	public boolean canSort(List<? extends SortKey> sortKeys);
	
	/**
	 * Returns whether this node's children are sorted.
	 * @return <code>true</code> if this node's children are sorted,
	 *  <code>false</code> otherwise
	 */
	public boolean isSorted();
	
	/**
	 * Resets this node's children's sort order to their original order.
	 */
	public void reset();
	
}
