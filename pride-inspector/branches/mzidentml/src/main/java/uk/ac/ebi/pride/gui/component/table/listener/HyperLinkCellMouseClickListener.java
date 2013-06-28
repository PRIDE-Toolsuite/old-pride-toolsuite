package uk.ac.ebi.pride.gui.component.table.listener;

import uk.ac.ebi.pride.gui.url.HttpUtilities;
import uk.ac.ebi.pride.gui.url.HyperLinkGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mouse listener for clicking hyper link
 * <p/>
 * User: rwang
 * Date: 10-Sep-2010
 * Time: 17:05:09
 */
public class HyperLinkCellMouseClickListener extends MouseAdapter {

    private JTable table;
    private String clickHeader;
    private String linkedHeader;
    private HyperLinkGenerator urlGen;
    private Pattern pattern;

    public HyperLinkCellMouseClickListener(JTable table, String clickHeader,
                                           HyperLinkGenerator generator) {
        this(table, clickHeader, generator, null);
    }

    public HyperLinkCellMouseClickListener(JTable table, String clickHeader,
                                           HyperLinkGenerator generator, Pattern pattern) {
        this(table, clickHeader, clickHeader, generator, pattern);
    }

    public HyperLinkCellMouseClickListener(JTable table, String clickHeader, String linkedHeader,
                                           HyperLinkGenerator generator, Pattern pattern) {
        this.table = table;
        this.clickHeader = clickHeader;
        this.linkedHeader = linkedHeader;
        this.urlGen = generator;
        this.pattern = pattern;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        int col = table.columnAtPoint(new Point(e.getX(), e.getY()));
        String header = table.getColumnName(col);
        if (header.equals(clickHeader)) {
            int row = table.rowAtPoint(new Point(e.getX(), e.getY()));

            Object val = table.getValueAt(table.convertRowIndexToModel(row), col);

            if (val != null && clickHeader.equals(linkedHeader)) {

                Set<Object> urlList = new HashSet<Object>();

                if (pattern != null) {
                    Matcher m = pattern.matcher(val.toString());

                    while (m.find()) {
                        urlList.add(m.group());
                    }
                } else if (val instanceof Collection){
                    for (Object o : (Collection)val) {
                        urlList.add(o);
                    }
                } else {
                    urlList.add(val);
                }

                for (Object url : urlList) {
                    url = urlGen == null ? url : urlGen.generate(url);
                    if (url != null) {
                        HttpUtilities.openURL(url.toString());
                    }
                }
            }
        }
    }
}