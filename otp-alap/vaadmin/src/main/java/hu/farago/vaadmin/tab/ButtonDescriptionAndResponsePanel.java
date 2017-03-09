package hu.farago.vaadmin.tab;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Grid;
import com.vaadin.ui.RichTextArea;
import com.vaadin.ui.VerticalLayout;

public class ButtonDescriptionAndResponsePanel<T> extends VerticalLayout {
	
	private static final long serialVersionUID = 7331887909881138593L;
	
	private Button button;
	private RichTextArea description;
	private Grid response;
	private BeanItemContainer<T> container;
	
	public ButtonDescriptionAndResponsePanel(
			String buttonCaption, 
			ClickListener listener, 
			String descriptionText, 
			Class<T> clazz) {
		this.button = new Button(buttonCaption, listener);
		this.description = new RichTextArea("Description", descriptionText);
		buildGrid(clazz);
		addComponents(button, description, response);
	}

	private void buildGrid(Class<T> clazz) {
		this.response = new Grid("Response");
		this.container = new BeanItemContainer<T>(clazz);
		this.response.setContainerDataSource(container);
		
		this.response.setSizeFull();
		this.response.setResponsive(true);
		this.response.setColumnReorderingAllowed(true);
	}

}
