package com.pawban.communicator_frontend.view.component;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;

public class CustomizedDialog extends Dialog {

    private final Button okButton = new Button("Pass ownership");
    private final Button cancelButton = new Button("Cancel");
    private final VerticalLayout customContent = new VerticalLayout();

    private Registration okButtonRegistration;
    private Registration cancelButtonRegistration;

    public CustomizedDialog(final String titleText) {
        H3 title = new H3(titleText);
        title.getStyle().set("margin", "auto");

        cancelButton.addClickListener(event -> close());
        cancelButton.setTabIndex(101);

        okButton.getStyle().set("margin-left", "auto");
        okButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        okButton.setTabIndex(100);

        HorizontalLayout buttons = new HorizontalLayout(cancelButton, okButton);
        buttons.setWidthFull();

        customContent.setMargin(false);
        customContent.setPadding(false);
        customContent.setWidthFull();

        VerticalLayout mainLayout = new VerticalLayout(title, customContent, buttons);

        super.add(mainLayout);
        setWidth("350px");
        setCloseOnOutsideClick(false);
    }

    public void setOkButtonText(String okButtonText) {
        okButton.setText(okButtonText);
    }

    public void setCancelButtonText(String cancelButtonText) {
        cancelButton.setText(cancelButtonText);
    }

    public void setOkButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        if (okButtonRegistration != null) {
            okButtonRegistration.remove();
        }
        okButtonRegistration = okButton.addClickListener(listener);
    }

    public void setCancelButtonClickListener(ComponentEventListener<ClickEvent<Button>> listener) {
        if (cancelButtonRegistration != null) {
            cancelButtonRegistration.remove();
        }
        cancelButtonRegistration = cancelButton.addClickListener(listener);
    }

    public void setOkButtonThemeVariants(ButtonVariant... buttonVariants) {
        okButton.removeThemeVariants(ButtonVariant.values());
        okButton.addThemeVariants(buttonVariants);
    }

    public void setCancelButtonThemeVariants(ButtonVariant... buttonVariants) {
        cancelButton.removeThemeVariants(ButtonVariant.values());
        cancelButton.addThemeVariants(buttonVariants);
    }

    @Override
    public void add(Component... components) {
        customContent.add(components);
    }

    @Override
    public void add(String text) {
        customContent.add("text");
    }

}
