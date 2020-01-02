package com.pawban.communicator_frontend.view.communicator.dialog;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ErrorDialog extends Dialog {

    public ErrorDialog(String message) {
        Icon exclamationIcon = VaadinIcon.EXCLAMATION_CIRCLE.create();
        exclamationIcon.setSize("5em");
        exclamationIcon.setColor("var(--lumo-error-color)");

        H3 title = new H3("Error!");
        title.getStyle()
                .set("color", "var(--lumo-error-color)")
                .set("margin", "auto");

        Label messageLabel = new Label(message);

        Button okButton = new Button("OK");
        okButton.addThemeVariants(ButtonVariant.LUMO_ERROR, ButtonVariant.LUMO_PRIMARY);
        okButton.addClickListener(buttonClickEvent -> this.close());
        okButton.getStyle()
                .set("margin-left", "auto")
                .set("margin-right", "auto");

        VerticalLayout innerLayout = new VerticalLayout(title, messageLabel, okButton);
        innerLayout.setPadding(false);

        HorizontalLayout mainLayout = new HorizontalLayout(exclamationIcon, innerLayout);
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        add(mainLayout);
        setCloseOnEsc(false);
        setCloseOnOutsideClick(false);
        open();
    }

}
