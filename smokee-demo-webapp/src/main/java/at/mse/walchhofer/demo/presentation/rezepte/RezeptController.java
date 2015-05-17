package at.mse.walchhofer.demo.presentation.rezepte;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Collection;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.Part;

import at.mse.walchhofer.demo.business.bilder.entity.Bild;
import at.mse.walchhofer.demo.business.rezepte.boundary.RezeptService;
import at.mse.walchhofer.demo.business.rezepte.entity.Rezept;
import at.mse.walchhofer.demo.presentation.navigation.NavigationController;
import at.mse.walchhofer.smokee.api.SmokeTest;
import at.mse.walchhofer.smokee.api.SmokeValue;
import at.mse.walchhofer.smokee.api.SmokeValue.SmokeValueType;

@Named("rezeptCtrl")
@RequestScoped
public class RezeptController {

    @Inject
    RezeptViewModel viewModel;

    @Inject
    NavigationController navigation;

    @Inject
    FacesContext facesContext;

    @EJB
    RezeptService rezeptService;

    @SmokeTest(rollback = true, expectedResult = @SmokeValue(type = SmokeValueType.BOOLEAN, value = "true"))
    boolean testCreate() {
        try {
            viewModel.setAutor("Matthias Walchhofer");
            viewModel.setBeschreibung("Testbeschreibung lange und so");
            viewModel.setName("Testrezept");
            InputStream resourceAsStream = this.getClass().getResourceAsStream("/smokee/rezepte/images/yaki_tori.jpg");
            byte[] file = new byte[4000];
            int actualSize = resourceAsStream.read(file);

            viewModel.setFile(new Part() {

                @Override
                public void write(String fileName) throws IOException {
                }

                @Override
                public String getSubmittedFileName() {
                    return "yaki_tori.jpg";
                }

                @Override
                public long getSize() {
                    return actualSize;
                }

                @Override
                public String getName() {
                    return "yaki_tori.jpg";
                }

                @Override
                public InputStream getInputStream() throws IOException {
                    return resourceAsStream;
                }

                @Override
                public Collection<String> getHeaders(String name) {
                    return new ArrayList<String>();
                }

                @Override
                public Collection<String> getHeaderNames() {
                    return new ArrayList<String>();
                }

                @Override
                public String getHeader(String name) {
                    return "";
                }

                @Override
                public String getContentType() {
                    return "image/jpeg";
                }

                @Override
                public void delete() throws IOException {
                }
            });

            return !create().equals("");

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public String create() {
        Calendar now = Calendar.getInstance();

        Rezept rezept2create = new Rezept();
        rezept2create.setTitel(viewModel.getName());
        rezept2create.setAnleitung(viewModel.getBeschreibung());
        rezept2create.setAutor(viewModel.getAutor());
        rezept2create.setErstelltAm(now);

        Bild bild2create = new Bild();
        String fileName = Base64.getEncoder().encodeToString(viewModel.getFile().getName().getBytes());
        bild2create.setFileName(fileName);
        bild2create.setMimeType(viewModel.getFile().getContentType());
        bild2create.setSize(viewModel.getFile().getSize());
        bild2create.setFile(new byte[(int) viewModel.getFile().getSize()]);
        try {
            // not the best idea, should first check the size and read buffered
            new DataInputStream(viewModel.getFile().getInputStream()).readFully(bild2create.getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        bild2create.setErstelltAm(now);

        Rezept created = rezeptService.create(rezept2create, bild2create);

        if (created != null) {
            return navigation.getUebersicht();
        } else {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Rezept konnte nicht gespeichert werden!", ""));
            return "";
        }
    }

}
