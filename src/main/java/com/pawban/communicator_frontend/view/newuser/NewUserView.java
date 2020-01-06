package com.pawban.communicator_frontend.view.newuser;

import com.pawban.communicator_frontend.domain.Country;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.service.CountryService;
import com.pawban.communicator_frontend.service.UserService;
import com.pawban.communicator_frontend.session.CommunicatorSession;
import com.pawban.communicator_frontend.session.SessionData;
import com.pawban.communicator_frontend.view.MainLayout;
import com.pawban.communicator_frontend.view.communicator.CommunicatorView;
import com.pawban.communicator_frontend.view.newuser.component.CountriesComboBox;
import com.pawban.communicator_frontend.view.newuser.component.EnterButton;
import com.pawban.communicator_frontend.view.newuser.component.UsernameField;
import com.pawban.communicator_frontend.view.newuser.component.VisibilityComboBox;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

@Route(
        value = "",
        layout = MainLayout.class
)
public class NewUserView extends VerticalLayout {

    private final UserService userService;
    private final CountryService countryService;

    private final CommunicatorSession session;

    private final TextField username = new UsernameField("Username:", "Enter the username you want to use");
    private final ComboBox<Country> country = new CountriesComboBox("Countries:", "Select country");
    private final ComboBox<Boolean> visibility = new VisibilityComboBox("Status:", "Select your visibility");
    private final Button enter = new EnterButton("Open the communicator");

    public NewUserView(@Autowired final UserService userService,
                       @Autowired final CountryService countryService,
                       @Autowired final CommunicatorSession session) {
        this.userService = userService;
        this.countryService = countryService;
        this.session = session;

        buildForm();
        bindFormToData();
    }

    private void buildForm() {
        H3 formHeader = new H3("Create new user");
        country.setItems(countryService.getCountries());

        VerticalLayout userForm = new VerticalLayout(formHeader, username, country, visibility, enter);
        userForm.getThemeList().add(Lumo.DARK);
        add(userForm);

        setWidth("350px");
        setPadding(false);
        setSpacing(false);
        setMargin(false);
    }

    private void bindFormToData() {
        Binder<User> binder = new Binder<>(User.class);
        User user = new User();
        binder.setBean(user);

        binder.forField(username)
                .asRequired("Username can't be empty.")
                .withValidator(
                        name -> name.matches("\\w{3,30}"),
                        "Username length must be between 3 and 30. Username can contain only lowercase " +
                                "letters, uppercase letters, digits and underscores."
                )
                .withValidator(
                        this::isUsernameAvailable,
                        "This username is already taken. Choose another one."
                )
                .bind("username");

        binder.forField(country)
                .asRequired("You must choose the country you come from.")
                .bind("country");

        binder.forField(visibility)
                .asRequired("You must choose your visibility.")
                .bind("visible");

        enter.addClickListener(event -> {
            if (binder.validate().isOk()) {
                createUser(user);
            }
        });
    }

    private boolean isUsernameAvailable(final String username) {
        return userService.isUsernameAvailable(username);
    }

    private void createUser(final User user) {
        SessionData sessionData = userService.createUser(user);
        this.session.setSession(sessionData);
        RouteConfiguration configuration = RouteConfiguration.forSessionScope();
        configuration.removeRoute("");
        configuration.setRoute("", CommunicatorView.class, Collections.singletonList(MainLayout.class));
        UI.getCurrent().getPage().reload();
    }

}
