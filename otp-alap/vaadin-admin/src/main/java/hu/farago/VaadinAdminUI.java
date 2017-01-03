package hu.farago;

import java.util.ArrayList;

import javax.servlet.annotation.WebServlet;

import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.server.VaadinSession;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalSplitPanel;
import com.vaadin.ui.renderers.HtmlRenderer;

import hu.farago.dto.response.Response;

@SpringUI
@Theme("mytheme")
@Title("OTP Admin")
public class VaadinAdminUI extends UI {
	private static final long serialVersionUID = -634741018001365762L;

	private TabSheet tabSheet;
	private VerticalSplitPanel vsp;
	private Grid responseGrid;
	private BeanItemContainer<Response> responses;
	
	@Override
    protected void init(VaadinRequest vaadinRequest) {
		VaadinSession.getCurrent().getSession().setMaxInactiveInterval(-1);
		tabSheet = new TabSheet();

		buildTabs();
		setupResponseGrid();

		vsp = new VerticalSplitPanel(tabSheet, responseGrid);
		vsp.setSplitPosition(70, Unit.PERCENTAGE);

		setContent(vsp);
    }

	private void buildTabs() {
		//tabSheet.addTab(openedOrders, "Services", new ThemeResource("img/planets/01.png"));
		
	}
	
	private void setupResponseGrid() {
		responses = new BeanItemContainer<Response>(Response.class, new ArrayList<>());
		responseGrid = new Grid();
		responseGrid.setSizeFull();
		responseGrid.setContainerDataSource(responses);
		responseGrid.setColumnReorderingAllowed(true);
		responseGrid.setDescription("Responses sent by the IB client");
		Grid.Column htmlColumn = responseGrid.getColumn("htmlResponse");
		Grid.Column cdtColumn = responseGrid.getColumn("clientDateTime");
		htmlColumn.setRenderer(new HtmlRenderer(""));
		htmlColumn.setEditable(true);
		htmlColumn.setWidthUndefined();
		// expand ratios
		htmlColumn.setExpandRatio(6);
		cdtColumn.setExpandRatio(1);
		// show content on hover
		responseGrid.setColumns(cdtColumn.getPropertyId(), htmlColumn.getPropertyId());
		responseGrid.setCellDescriptionGenerator((cell) -> cell.getValue().toString());
	}
	
    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = VaadinAdminUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
		private static final long serialVersionUID = -2851891616838878655L;
    }
}
