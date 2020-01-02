package com.pawban.communicator_frontend.view.communicator.component;

import com.vaadin.flow.component.icon.Icon;

public class VisibilityIcon extends Icon {

    private boolean visible;

    public VisibilityIcon(boolean visible) {
        this.visible = visible;
        getStyle().set("cursor", "pointer");
        setSize("18px");
        setVisibility(visible);
    }

    public void setVisibility(boolean visible) {
        this.visible = visible;
        getElement().setAttribute("icon", visible ? "vaadin:eye" : "vaadin:eye-slash");
        getElement().setAttribute("title", visible ?
                "You are visible - all communicator users can see you" :
                "You are invisible - only members of chat rooms you belong to can see you");
    }

    public void toggleVisibility() {
        setVisibility(!visible);
    }

    public boolean isVisible() {
        return visible;
    }

}
