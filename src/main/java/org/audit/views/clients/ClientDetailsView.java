package org.audit.views.clients;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.audit.dto.ClientDTO;
import org.audit.services.IClientService;
import org.springframework.beans.factory.annotation.Autowired;

@Route("clients/details")
@PageTitle("Client Details")
public class ClientDetailsView extends VerticalLayout implements HasUrlParameter<Integer> {

    private final IClientService clientService;
    private ClientDTO client;

    @Autowired
    public ClientDetailsView(IClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public void setParameter(BeforeEvent event, Integer clientId) {
        client = clientService.getClientById(clientId);
        initializeView();
    }

    private void initializeView() {
        removeAll(); // Curățăm layout-ul

        if (client != null) {
            add(new H1("Client Details"));
            add(new TextField("Name", client.getName()));
            add(new TextField("Contact Person", client.getContactPerson()));
            add(new TextField("Email", client.getContactEmail()));
            add(new TextArea("Description", client.getDescription()));
        } else {
            add(new H1("Client not found."));
        }
    }
}
