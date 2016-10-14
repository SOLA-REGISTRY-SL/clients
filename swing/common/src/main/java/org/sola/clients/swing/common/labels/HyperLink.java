package org.sola.clients.swing.common.labels;

import java.awt.Color;
import java.awt.Font;
import java.awt.font.TextAttribute;
import java.util.Map;
import javax.swing.JLabel;

/**
 * Extends JLabel component to make hyperlink
 */
public class HyperLink extends JLabel {

    private Color linkColor = new Color(0, 102, 255);
    private Color clickedColor = Color.red;
    private boolean selected = false;
    
    public Color getLinkColor() {
        return linkColor;
    }

    public void setLinkColor(Color linkColor) {
        this.linkColor = linkColor;
        setSelected(getSelected());
    }

    public Color getClickedColor() {
        return clickedColor;
    }

    public void setClickedColor(Color clickedColor) {
        this.clickedColor = clickedColor;
        setSelected(getSelected());
    }

    public HyperLink() {
        setSelected(false);
        setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                setSelected(true);
            }
        });
        this.setFont(getFont());
    }

    public boolean getSelected(){
        return selected;
    }
    
    public void setSelected(boolean selected){
        this.selected = selected;
        if(selected)
            super.setForeground(getClickedColor());
        else
            super.setForeground(getLinkColor());
    }
    
    @Override
    public void setForeground(Color color){
        // Do nothing
    }
    
    @Override
    public void setFont(Font font) {
        Map attributes = font.getAttributes();
        attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
        super.setFont(font.deriveFont(attributes));
    }
}
