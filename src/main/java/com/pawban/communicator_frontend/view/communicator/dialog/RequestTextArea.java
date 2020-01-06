package com.pawban.communicator_frontend.view.communicator.dialog;

import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.binder.Binder;

public class RequestTextArea extends TextArea {

    private Binder<Content> binder = new Binder<>();

    public RequestTextArea() {
        setLabel("Request");
        setPlaceholder("Enter a few words to convince chat room owner to accept your access request...");
        setWidthFull();
        setHeight("200px");
        setMaxLength(2000);
        setRequired(true);
        binder.setBean(new Content());
        binder.forField(this)
                .withValidator(text -> text.length() > 0, "Request can't be empty.")
                .bind(Content::getContent, Content::setContent);
    }

    public boolean isValid() {
        return binder.validate().isOk();
    }

    public void clear() {
        binder.setBean(new Content());
    }

    private class Content {

        String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

    }

}
