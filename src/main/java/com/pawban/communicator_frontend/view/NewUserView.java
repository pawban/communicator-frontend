package com.pawban.communicator_frontend.view;

import com.pawban.communicator_frontend.domain.Country;
import com.pawban.communicator_frontend.domain.Session;
import com.pawban.communicator_frontend.domain.User;
import com.pawban.communicator_frontend.domain.UserStatus;
import com.pawban.communicator_frontend.service.CountryService;
import com.pawban.communicator_frontend.service.UserService;
import com.pawban.communicator_frontend.session.CommunicatorSession;
import com.pawban.communicator_frontend.view.component.UsernameTextField;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteConfiguration;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(
        value = "",
        layout = MainLayout.class
)
public class NewUserView extends VerticalLayout {

    private final CommunicatorSession session;

    private final UserService userService;
    private final CountryService countryService;

    private final UsernameTextField username = new UsernameTextField("Username:");
    private final ComboBox<Country> country = new ComboBox<>("Countries:");
    private final ComboBox<UserStatus> status = new ComboBox<>("Status:");
    private final Button enter = new Button("Open the communicator");

    public NewUserView(@Autowired final UserService userService, @Autowired final CountryService countryService,
                       @Autowired final CommunicatorSession session) {
        this.userService = userService;
        this.countryService = countryService;
        this.session = session;

        User newUser = new User();

        buildForm();
        bindFormToData(newUser);
    }

    private void buildForm() {
        setWidth("350px");
        setHeight("100%");
        setPadding(false);
        setSpacing(false);
        setMargin(false);

        Div upperPlaceHolder = new Div();
        add(upperPlaceHolder);
        upperPlaceHolder.setHeight("50%");

        VerticalLayout userForm = new VerticalLayout();
        add(userForm);
        userForm.getThemeList().add(Lumo.DARK);

        H3 formHeader = new H3("Create new user");

        username.setWidthFull();
        username.setPlaceholder("Enter the username you want to use");
        username.focus();

        List<Country> countries = countryService.getCountries();
        country.setItemLabelGenerator(Country::getName);
        country.setItems(countries);
        country.setPlaceholder("Select country");
        country.setRequired(true);
        country.setAllowCustomValue(false);
        country.setWidthFull();

        List<UserStatus> statuses = userService.getUserStatuses();
        status.setItemLabelGenerator(UserStatus::getStatus);
        status.setItems(statuses);
        status.setPlaceholder("Select your initial status");
        status.setRequired(true);
        status.setWidthFull();

        enter.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        enter.setWidthFull();

        userForm.add(formHeader, username, country, status, enter);

        Div lowerPlaceHolder = new Div();
        add(lowerPlaceHolder);
        lowerPlaceHolder.setHeight("50%");
    }

    private void bindFormToData(final User user) {
        Binder<User> binder = new Binder<>(User.class);
        binder.setBean(user);

        Binder.Binding<User, String> usernameBinding = binder.forField(username)
                .asRequired("Username can't be empty.")
                .withValidator(
                        name -> name.length() > 2,
                        "Username must have at least 3 characters.")
                .withValidator(
                        this::isUsernameAvailable,
                        "This username is already taken. Choose another one.")
                .bind("username");
        username.addMyInputListener(event -> {
            usernameBinding.validate();
        });

        binder.forField(country)
                .asRequired("You must choose the country you come from.")
                .bind("country");

        binder.forField(status)
                .asRequired("You must choose your initial status.")
                .bind("status");

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
        Session session = userService.createUser(user);
        this.session.setSession(session);
        RouteConfiguration configuration = RouteConfiguration.forSessionScope();
        configuration.setRoute("", CommunicatorView.class, MainLayout.class);
        UI.getCurrent().getPage().reload();
    }

}
