package org.sola.clients.swing.gis.mapaction;

import javax.swing.JOptionPane;
import org.geotools.swing.extended.Map;
import org.geotools.swing.mapaction.extended.ExtendedAction;
import org.sola.clients.beans.referencedata.LandTypeBean;
import org.sola.clients.reports.ReportManager;
import org.sola.clients.swing.gis.beans.CadastreObjectBean;
import org.sola.clients.swing.gis.imagegenerator.MapImageGeneratorForSelectedParcel;
import org.sola.clients.swing.gis.imagegenerator.MapImageInformation;
import org.sola.clients.swing.gis.layer.CadastreChangeNewCadastreObjectLayer;
import org.sola.clients.swing.ui.reports.ReportViewerForm;
import org.sola.clients.swing.ui.reports.SaveFormat;
import org.sola.common.StringUtility;
import org.sola.common.messaging.GisMessage;
import org.sola.common.messaging.MessageUtility;

/**
 *
 * This map action allows to print cadastre object
 */
public class SurveyPlanPrint extends ExtendedAction {

    /**
     * The name of the map action
     */
    public final static String MAPACTION_NAME = "survey-plan-print";
    private CadastreChangeNewCadastreObjectLayer newCadastreObjectLayer = null;
    private final Map map;

    /**
     * Constructor of the class
     *
     * @param map The map control that will be interacting with the map action
     * @param newCadastreObjectLayer New cadastre objects layer
     */
    public SurveyPlanPrint(Map map, CadastreChangeNewCadastreObjectLayer newCadastreObjectLayer) {
        super(map, MAPACTION_NAME,
                MessageUtility.getLocalizedMessage(
                        GisMessage.CADASTRE_CHANGE_PRINT_SURVEY_PLAN).getMessage(),
                "resources/map.png");
        this.map = map;
        this.newCadastreObjectLayer = newCadastreObjectLayer;
    }

    @Override
    public void onClick() {
        if (newCadastreObjectLayer.getBeanList() != null && newCadastreObjectLayer.getBeanList().size() > 0) {
            try {
                CadastreObjectBean co = newCadastreObjectLayer.getBeanList().get(0);
                
                if(StringUtility.isEmpty(co.getNameFirstpart()) || co.getNameFirstpart().equalsIgnoreCase("tmp")){
                    MessageUtility.displayMessage(GisMessage.CADASTRE_CHANGE_FILLIN_SURVEY_DETAILS);
                    return;
                }
                
                MapImageGeneratorForSelectedParcel gen
                        = new MapImageGeneratorForSelectedParcel(
                                co, 665, 423, 200, 200, true, 192, 40);
                MapImageInformation info = gen.getInformation();
                
                ReportViewerForm form = new ReportViewerForm(
                        ReportManager.getSurveyPlanReport(
                                co.getLandTypeCode().equalsIgnoreCase(LandTypeBean.TYPE_PRIVATE_LAND),
                                co, info.getScale(), 
                                info.getMapImageLocation(), info.getScalebarImageLocation()));
                form.setSaveFormats(SaveFormat.Pdf, SaveFormat.Docx, SaveFormat.Odt, SaveFormat.Html);
                form.setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }
    }
}
