package com.pawban.communicator_frontend.view.communicator.component;

import com.pawban.communicator_frontend.domain.Message;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

import java.time.format.DateTimeFormatter;

public class MessageComponent extends VerticalLayout {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public MessageComponent(final Message message) {
        setPadding(false);
        setSpacing(false);

        Label senderLabel = new Label(message.getSender());
        senderLabel.getStyle()
                .set("font-weight", "bold");

        Label timeLabel = new Label(formatter.format(message.getCreationTime()));
        timeLabel.getStyle()
                .set("margin-left", "auto")
                .set("color", "var(--lumo-shade-50pct)");

        Label textLabel = new Label(message.getText());
        textLabel.getStyle()
                .set("text-align", "justify")
                .set("line-height", "var(--lumo-line-height-xs)");
        textLabel.setWidthFull();

        HorizontalLayout firstRow = new HorizontalLayout(senderLabel, timeLabel);
        firstRow.setAlignItems(Alignment.CENTER);
        firstRow.setWidthFull();
        firstRow.setPadding(false);
        firstRow.getStyle().set("background-color", "var(--lumo-shade-10pct)");

        add(firstRow, textLabel);
    }

}
