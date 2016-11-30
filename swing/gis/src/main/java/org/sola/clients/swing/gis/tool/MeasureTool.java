package org.sola.clients.swing.gis.tool;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.text.DecimalFormat;
import org.geotools.geometry.jts.Geometries;
import org.geotools.swing.extended.util.Messaging;
import org.geotools.swing.tool.extended.ExtendedDrawTool;
import org.sola.common.messaging.GisMessage;
import org.sola.common.messaging.MessageUtility;

/**
 * Map Tool used to measure distance on the map. Pan and Zoom can also be used
 * while using this tool.
 *
 * @author soladev
 */
public class MeasureTool extends ExtendedDrawTool {

    private String toolTip = MessageUtility.getLocalizedMessage(
            GisMessage.MEASURE_TOOLTIP).getMessage();

    public MeasureTool() {
        super();
        this.setGeometryType(Geometries.LINESTRING);
        this.setToolName("measure");
        this.setToolTip(toolTip);
        this.setIconImage("resources/ruler.png");
    }

    /**
     * Triggered when the user double clicks to complete the measure line.
     * Formats the display value as meters or kilometers depending on the length
     * of the measure line and displays a message to inform the user.
     *
     * @param geometry Geometry representing the measure line
     */
    @Override
    protected void treatFinalizedGeometry(Geometry geometry) {
        javax.swing.JOptionPane.showMessageDialog(null, "Total distance is " + getDistance(geometry));
    }

    private String getDistance(Geometry geometry) {
        Double length = geometry.getLength();
        DecimalFormat formatter = new DecimalFormat("#,###,###.0");
        //String messageId = GisMessage.MEASURE_DISTANCE_METERS;
        if (length > new Double(5000)) {
            length = length / 1000;
            //messageId = GisMessage.MEASURE_DISTANCE_KILOMETERS;
            return formatter.format(length) + "km";
        }
        return formatter.format(length) + "m";
        //return Messaging.getInstance().getMessageText(messageId, formatter.format(length));
    }

    /**
     * Draws the temporary line while the mouse is moving.
     *
     * @param g2D
     */
    @Override
    protected void drawTemporaryLines(Graphics2D g2D) {

        String label = "";
        int startX = 0;
        int startY = 0;
        int endX = 0;
        int endY = 0;

        if (getStartPos1() != null && getMovingPos() != null) {
            startX = (int) getStartPos1().getX();
            startY = (int) getStartPos1().getY();
            endX = (int) getMovingPos().getX();
            endY = (int) getMovingPos().getY();

            Point2D startPoint = getMapControl().getPointInMap(getStartPos1());
            Point2D endPoint = getMapControl().getPointInMap(getMovingPos());

            Geometry geom = this.geometryFactory.createLineString(
                    new Coordinate[]{
                        new Coordinate(startPoint.getX(), startPoint.getY()),
                        new Coordinate(endPoint.getX(), endPoint.getY())
                    });
            label = getDistance(geom);
        }

        Line2D line;
        if (getStartPos1() != null && getMovingPos() != null) {
            line = new Line2D.Double(getStartPos1(), getMovingPos());
            g2D.draw(line);
        }
        if (getStartPos2() != null && getStartPos1() != getStartPos2()) {
            line = new Line2D.Double(getStartPos2(), getMovingPos());
            g2D.draw(line);
        }

        // Draw distance (avoid drawing 0 values by checking coordinates
        if (!label.equals("") && (startX != endX || startY != endY)) {
            int x = (int) (getStartPos1().getX() + getMovingPos().getX()) / 2;
            int y = (int) (getStartPos1().getY() + getMovingPos().getY()) / 2;
            //FontMetrics fm = g2D.getFontMetrics();
            //Rectangle2D rect = fm.getStringBounds(label, g2D);

            Color lineColor = getStyleTemporaryLinesColor();
//            g2D.setColor(Color.WHITE);
//            g2D.fillRect(x,
//                    y - fm.getAscent(),
//                    (int) rect.getWidth(),
//                    (int) rect.getHeight());

            g2D.setXORMode(Color.BLACK);
            g2D.drawString(label, x+1, y-2);
            g2D.setXORMode(lineColor);
        }
    }

    /**
     * Triggered when the user selects/activates the Measure Tool in the Map
     * toolbar.
     *
     * @param selected true if tool selected/activated, false otherwise.
     */
    @Override
    public void onSelectionChanged(boolean selected) {
        if (selected) {
            // If the MeasureTool is selected, force the redraw of 
            // any existing measure line.
            afterRendering();
        }
    }
}
