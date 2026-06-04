package hu.matenikula.foxmanager.web;

import hu.matenikula.foxmanager.domain.Fox;
import hu.matenikula.foxmanager.domain.Gender;
import hu.matenikula.foxmanager.service.FoxService;
import lombok.Getter;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@ViewScoped
public class FoxView implements Serializable {

    @Inject
    private transient FoxService foxService;

    @Getter
    private List<Fox> foxes;

    @Getter
    private Fox newFox = new Fox();

    @PostConstruct
    public void init() {
        loadFoxes();
    }

    private void loadFoxes() {
        foxes = foxService.getAllFoxes();
    }

    public void save() {
        foxService.createFox(newFox);
        newFox = new Fox();
        loadFoxes();
        addMessage("Sikeres mentés");
    }

    public void delete(Fox fox) {
        foxService.deleteFox(fox.getId());
        loadFoxes();
        addMessage("Róka törölve");
    }

    private void addMessage(String text) {
        FacesContext.getCurrentInstance().addMessage(null,
                new FacesMessage(FacesMessage.SEVERITY_INFO, text, null));
    }

    public Gender[] getGenders() {
        return Gender.values();
    }
}