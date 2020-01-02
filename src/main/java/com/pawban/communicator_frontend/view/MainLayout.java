package com.pawban.communicator_frontend.view;

import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;

import java.util.Objects;

@Push
@Theme(value = Lumo.class)
public class MainLayout extends VerticalLayout implements RouterLayout {

    private final HorizontalLayout content;

    public MainLayout() {
        H2 title = new H2("communicator v0.1");
        title.getStyle().set("margin-top", "var(--lumo-space-m)");
        title.setWidthFull();
        VerticalLayout header = new VerticalLayout();
        header.getThemeList().add(Lumo.DARK);
        header.add(title);

        content = new HorizontalLayout();
        content.setSizeFull();
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        content.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);

        setJustifyContentMode(JustifyContentMode.CENTER);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);

        add(header, content);
        setSizeFull();
        expand(content);
    }

    @Override
    public void showRouterLayoutContent(HasElement hasElement) {
        Objects.requireNonNull(hasElement);
        Objects.requireNonNull(hasElement.getElement());
        content.removeAll();
        content.getElement().appendChild(hasElement.getElement());
    }

}
