package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.html.Image;

public class FlagImage extends Image {

    public FlagImage(final String flagUrl,
                     final int width) {
        super(flagUrl, "flag");
        setWidth(width + "px");
        setHeight(width * 3 / 4 + "px");
    }

}
