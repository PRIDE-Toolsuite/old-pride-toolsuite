package uk.ac.ebi.pride.gui.component;

import javax.swing.*;

/**
 * <p>Interface used for manage the icon in the panes added to the tabpane</p>
 *
 * @user: Antonio Fabregat
 * Date: 27-ago-2010
 * Time: 14:04:58
 */
public interface Iconable {

    public void setIcon(Icon icon);
    public Icon getLoadingIcon();
    public Icon getIcon();

}
