package com.pawban.communicator_frontend.view;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Objects;

public class MainLayout extends Composite<Div> implements RouterLayout {

    private final Div content = new Div();

    public MainLayout() {
        H2 title = new H2("communicator v0.1");
        title.setClassName("title");
        title.getStyle().set("margin", "auto");
        title.setWidthFull();
        title.setHeight("3vh");
        title.setMaxHeight("3vh");

        VerticalLayout header = new VerticalLayout();
        header.getThemeList().add(Lumo.DARK);
        header.add(title);
        header.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.CENTER);

        content.setWidthFull();
        content.setMaxWidth("100vw");
        content.setHeight("90vh");
        content.setMaxHeight("90vh");
        content.getStyle().set("display", "flex");
        content.getStyle().set("justify-content", "center");

        getContent().add(new VerticalLayout(header, content));
    }

    @Override
    public void showRouterLayoutContent(HasElement hasElement) {
        Objects.requireNonNull(hasElement);
        Objects.requireNonNull(hasElement.getElement());
        content.removeAll();
        content.getElement().appendChild(hasElement.getElement());
    }

}
