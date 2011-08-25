package uk.ac.ebi.pride.gui.component;

import org.jdesktop.swingx.border.DropShadowBorder;
import uk.ac.ebi.pride.gui.GUIUtilities;
import uk.ac.ebi.pride.gui.PrideInspectorContext;
import uk.ac.ebi.pride.gui.utils.GraphicsUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.ImageObserver;
import java.awt.image.Kernel;

/**
 * A panel displays pride's logo and showing a loading status
 * <p/>
 * User: rwang
 * Date: 14/03/11
 * Time: 16:41
 */
public class PrideInspectorLoadingPanel extends PrideInspectorPanel {

    public PrideInspectorLoadingPanel() {
        initComponents();
    }

    private void initComponents() {
        PrideInspectorContext context = (PrideInspectorContext) uk.ac.ebi.pride.gui.desktop.Desktop.getInstance().getDesktopContext();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.fill = GridBagConstraints.NONE;

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.white);
        panel.setPreferredSize(new Dimension(350, 150));

        ImageIcon loadingIcon = (ImageIcon) GUIUtilities.loadIcon(context.getProperty("loading.large.icon"));
        JLabel label = new JLabel();
        label.setOpaque(false);
        label.setIcon(loadingIcon);
        label.setText(context.getProperty("loading.title"));
        label.setFont(label.getFont().deriveFont(18f));
        panel.add(label, c);
        this.add(panel, c);
        this.setBackground(Color.white);
    }
}
