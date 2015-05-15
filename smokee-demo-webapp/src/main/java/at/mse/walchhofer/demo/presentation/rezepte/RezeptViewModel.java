package at.mse.walchhofer.demo.presentation.rezepte;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.Part;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import at.mse.walchhofer.demo.business.bilder.entity.Bild;


@Named("rezept")
@RequestScoped
public class RezeptViewModel implements Serializable {

    private static final long serialVersionUID = 680322268132733780L;

    private Long id;
    
    @NotNull
    @Size(min=3,max=200)
    private String name;
    
    @NotNull
    @Size(min=10,max=1024)
    private String beschreibung;
    
    @NotNull
    @Size(min=1,max=30)
    private String autor;

    private String erstelltAm;
    
    private Part file;
    
    private Bild randomBild;

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Part getFile() {
        return file;
    }

    public void setFile(Part file) {
        this.file = file;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getErstelltAm() {
        return erstelltAm;
    }

    public void setErstelltAm(String erstelltAm) {
        this.erstelltAm = erstelltAm;
    }

    public Bild getRandomBild() {
        return randomBild;
    }

    public void setRandomBild(Bild randomBild) {
        this.randomBild = randomBild;
    }
    
}
