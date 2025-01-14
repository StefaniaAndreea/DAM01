package org.audit.views.layout;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;
import org.audit.views.projects.NavigableGridProjectsView;

public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
    }

    private void createHeader() {
        Image logo = new Image("themes/custom-theme/logo.png", "CYBERMASTER");
        logo.setHeight("50px");

        RouterLink projectsLink = new RouterLink("Projects", NavigableGridProjectsView.class);

        HorizontalLayout navBar = new HorizontalLayout(logo, projectsLink);
        navBar.setDefaultVerticalComponentAlignment(HorizontalLayout.Alignment.CENTER);
        navBar.setSpacing(true);

        addToNavbar(navBar);
    }
}
