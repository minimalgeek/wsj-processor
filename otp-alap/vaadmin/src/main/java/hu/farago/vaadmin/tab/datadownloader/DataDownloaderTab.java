package hu.farago.vaadmin.tab.datadownloader;

import org.springframework.beans.factory.annotation.Autowired;

import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.spring.annotation.UIScope;
import com.vaadin.ui.GridLayout;

import hu.farago.data.service.EdgarDownloadService;
import hu.farago.data.service.ForexDataDownloaderService;
import hu.farago.data.service.InsiderTradingDownloadService;
import hu.farago.data.service.MacroManService;
import hu.farago.data.service.NasdaqDownloadService;
import hu.farago.data.service.OilReportService;
import hu.farago.data.service.SAndPIndicesRefreshService;
import hu.farago.data.service.SeekingAlphaDownloadService;
import hu.farago.data.service.ServicesService;
import hu.farago.data.service.ZacksDownloadService;
import hu.farago.vaadmin.tab.ButtonDescriptionAndResponsePanel;

@SpringComponent
@UIScope
public class DataDownloaderTab extends GridLayout {

	private static final long serialVersionUID = -4143512087173627823L;

	@Autowired
	private EdgarDownloadService edgarDownloadService;

	@Autowired
	private ForexDataDownloaderService forexDataDownloaderService;

	@Autowired
	private InsiderTradingDownloadService insiderTradingDownloadService;

	@Autowired
	private MacroManService macroManService;

	@Autowired
	private NasdaqDownloadService nasdaqDownloadService;

	@Autowired
	private OilReportService oilReportService;

	@Autowired
	private SAndPIndicesRefreshService sAndPIndicesRefreshService;

	@Autowired
	private SeekingAlphaDownloadService seekingAlphaDownloadService;

	@Autowired
	private ServicesService servicesService;

	@Autowired
	private ZacksDownloadService zacksDownloadService;

	public DataDownloaderTab() {
		super(2, 10);
		setSizeFull();
		setMargin(true);
		setSpacing(true);

		addComponent(
				new ButtonDescriptionAndResponsePanel<Double>(
					"Lofasz", 
					(e) -> {}, 
					"<h1>Nothing gonna happen here</h1><p>But who cares?</p>", 
					Double.class), 
				0, 0);
		addComponent(
				new ButtonDescriptionAndResponsePanel<Double>(
					"Lofasz", 
					(e) -> {}, 
					"<h1>Nothing gonna happen here</h1><p>But who cares?</p>", 
					Double.class), 
				1, 0);
		addComponent(
				new ButtonDescriptionAndResponsePanel<Double>(
					"Lofasz", 
					(e) -> {}, 
					"<h1>Nothing gonna happen here</h1><p>But who cares?</p>", 
					Double.class), 
				0, 1);
		addComponent(
				new ButtonDescriptionAndResponsePanel<Double>(
					"Lofasz", 
					(e) -> {}, 
					"<h1>Nothing gonna happen here</h1><p>But who cares?</p>", 
					Double.class), 
				0, 2);
	}
}
