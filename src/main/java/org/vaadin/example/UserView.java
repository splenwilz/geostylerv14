package org.vaadin.example;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
// import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.server.VaadinSession;
// import org.vaadin.flow.router.PageTitle;

@Route("user/:username")
// @PageTitle("User Page")
public class UserView extends VerticalLayout implements BeforeEnterObserver {

    private H1 welcomeMessage;

    public UserView() {
        addClassName("main-view"); // Reuse the background style from MainView
        injectCustomCss();

        welcomeMessage = new H1("Welcome, User");  // Placeholder text
        welcomeMessage.addClassName("user-welcome");
        add(welcomeMessage);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        String username = event.getRouteParameters().get("username").orElse("Unknown User");
        welcomeMessage.setText("Welcome, " + username);
    }

    private void injectCustomCss() {
        String customCss =
                ".main-view { text-align: center; width: 500px !important; border-radius: 8px; margin-top: 40px !important; background-color: white; " +
                "margin: 0 auto; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); }" +
                ".user-welcome { text-align: center; font-size: 2rem; color: #007bff; margin-top: 20px; }" + // Style for the welcome message
                "html, body { height: 100%; margin: 0; background: linear-gradient(45deg, #dbecfc, #dcfaea); }";

        // Store the custom CSS in VaadinSession for access in other views
        VaadinSession.getCurrent().setAttribute("customCss", customCss);

        // Inject the fetched CSS into the document
        String style = "<style>" + customCss + "</style>";
        getElement().executeJs("this.insertAdjacentHTML('beforeend', $0)", style);
    }
}
