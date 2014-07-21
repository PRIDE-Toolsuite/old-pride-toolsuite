package uk.ac.ebi.pride.gui.component.table.sorttreetable;

import org.jdesktop.swingx.JXTableHeader;


import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * Class to enable tables to display and interact with components in their headers.<br>
 * To be used in tandem with {@link uk.ac.ebi.pride.gui.component.table.sorttreetable.ComponentHeaderRenderer}.
 *
 * @author yperez
 *
 */
public class ComponentTableHeader extends JXTableHeader {

	private ComponentHeaderRenderer chr;
	private String[] columnToolTips;
	private boolean[] reorderingAllowedColumns;
	
    /**
     * Constructs a <code>JTableHeader</code> which is initialized with
     * <code>columnModel</code> as the column model.
     * 
     * @param columnModel the column model for the table
     */
	public ComponentTableHeader(TableColumnModel columnModel) {
		this(columnModel, null);
	}
	
	/**
	 * Constructs a <code>JTableHeader</code> which is initialized with
	 * <code>columnModel</code> as the column model and a string array
	 * containing column-specific tooltips.
	 * 
	 * @param columnModel the column model for the table
	 */
	public ComponentTableHeader(TableColumnModel columnModel, String[] columnToolTips) {
		super(columnModel);
		this.columnToolTips = columnToolTips;
		reorderingAllowedColumns = new boolean[columnModel.getColumnCount()];
	}
	
	@Override
	public String getToolTipText(MouseEvent me) {
		if (columnToolTips != null) {
			return columnToolTips[table.convertColumnIndexToModel(
					columnModel.getColumnIndexAtX(me.getX()))];
		}
		return super.getToolTipText(me);
	}

	// hacky mouse event interception to avoid row sorting when hitting table header component
	@Override
	protected void processMouseEvent(MouseEvent me) {
		if (me.getID() == MouseEvent.MOUSE_PRESSED ||
				me.getID() == MouseEvent.MOUSE_CLICKED) {
			int col = columnModel.getColumnIndexAtX(me.getX());
			if (col != -1) {
				if (columnModel.getColumn(col).getHeaderRenderer() instanceof ComponentHeaderRenderer) {
					chr = (ComponentHeaderRenderer) columnModel.getColumn(col).getHeaderRenderer();
					Rectangle headerRect = this.getHeaderRect(col);
					Rectangle compRect = chr.getComponent().getBounds();
					compRect.x += headerRect.x;
					if ((compRect.contains(me.getPoint()))) {
						
						chr.dispatchEvent(new MouseEvent(
								me.getComponent(), me.getID(), me.getWhen(), me.getModifiers(),
								me.getX()-headerRect.x, me.getY(), me.getXOnScreen(), me.getYOnScreen(),
								me.getClickCount(), me.isPopupTrigger(), me.getButton()));
						this.repaint(headerRect);
						// fool other listeners by changing click-type events into release-type ones
						if (me.getID() == MouseEvent.MOUSE_CLICKED) {
							me = new MouseEvent(
									me.getComponent(), MouseEvent.MOUSE_RELEASED, me.getWhen(), me.getModifiers(),
									me.getX()-headerRect.x, me.getY(), me.getXOnScreen(), me.getYOnScreen(),
									me.getClickCount(), me.isPopupTrigger(), me.getButton());
						}
					}
				}
			}
		} else if (me.getID() == MouseEvent.MOUSE_RELEASED) {
			if (chr != null) {
				chr.dispatchEvent(me);
				chr = null;
				this.repaint();
			}
		}
		super.processMouseEvent(me);
	}

	/**
	 * Sets whether the user can drag the specified column's header to reorder columns. 
	 * 
	 * @param reorderingAllowed <code>true</code> if the table view should allow reordering; otherwise <code>false</code>.
	 * @param column the column to be affected.
	 */
	public void setReorderingAllowed(boolean reorderingAllowed, int column) {
		reorderingAllowedColumns[column] = reorderingAllowed;
	}
	
}
