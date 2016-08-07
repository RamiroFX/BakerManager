/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import javax.swing.AbstractButton;
import javax.swing.ButtonModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.text.View;

/**
 *
 * @author Ramiro
 */
public class RadioButtonUI extends BasicRadioButtonUI {

    public Icon getDefaultIcon() {
        return null;
    }

    private static Dimension size = new Dimension();
    private static Rectangle rec1 = new Rectangle();
    private static Rectangle rec2 = new Rectangle();
    private static Rectangle rec3 = new Rectangle();

    public synchronized void paint(Graphics g, JComponent c) {
        AbstractButton b = (AbstractButton) c;
        ButtonModel model = b.getModel();
        Font f = c.getFont();
        g.setFont(f);
        FontMetrics fm = c.getFontMetrics(f);

        Insets i = c.getInsets();
        size = b.getSize(size);
        rec1.x = i.left;
        rec1.y = i.top;
        rec1.width = size.width - (i.right + rec1.x);
        rec1.height = size.height - (i.bottom + rec1.y);
        rec2.x = rec2.y = rec2.width = rec2.height = 0;
        rec3.x = rec3.y = rec3.width = rec3.height = 0;

        String text = SwingUtilities.layoutCompoundLabel(c, fm, b.getText(),
                null, b.getVerticalAlignment(), b.getHorizontalAlignment(), b
                .getVerticalTextPosition(), b
                .getHorizontalTextPosition(), rec1, rec2, rec3, 0);

        if (c.isOpaque()) {
            g.setColor(b.getBackground());
            g.fillRect(0, 0, size.width, size.height);
        }
        if (text == null) {
            return;
        }
        g.setColor(b.getForeground());
        if (!model.isSelected() && !model.isPressed() && !model.isArmed()
                && b.isRolloverEnabled() && model.isRollover()) {
            g.drawLine(rec1.x, rec1.y + rec1.height, rec1.x + rec1.width,
                    rec1.y + rec1.height);
        }
        View v = (View) c.getClientProperty(BasicHTML.propertyKey);
        if (v != null) {
            v.paint(g, rec3);
        } else {
            paintText(g, b, rec3, text);
        }
    }
}
