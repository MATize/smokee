package at.mount.matize.jsf;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

@SessionScoped
public class UrlParamStore implements Serializable {

    private static final long serialVersionUID = -6693409597297370772L;

    private Map<String, List<String>> paramsToIgnoreMap;

    private Boolean disableUrlParamsSwitch;

    @PostConstruct
    private void init() {
        paramsToIgnoreMap = new HashMap<>();
        disableUrlParamsSwitch = false;
    }

    public List<String> pullParamsToIgnore(FacesContext context) {
        String viewState = context.getExternalContext()
                .getRequestParameterMap().get("javax.faces.ViewState");
        List<String> ret = paramsToIgnoreMap.get(viewState);
        paramsToIgnoreMap.remove(viewState);
        return ret != null ? ret : new ArrayList<String>();
    }

    public void pushParamsToIgnore(FacesContext context, String paramToIgnore) {
        List<String> paramsToIgnore = new ArrayList<>();
        paramsToIgnore.add(paramToIgnore);
        pushParamsToIgnore(context, paramsToIgnore);
    }

    public void pushParamsToIgnore(FacesContext context,
            List<String> paramsToIgnore) {
        String viewSate = context.getExternalContext().getRequestParameterMap()
                .get("javax.faces.ViewState");
        List<String> toIgnore = new ArrayList<>();
        if (paramsToIgnoreMap.containsKey(viewSate)) {
            toIgnore = paramsToIgnoreMap.get(viewSate);
            toIgnore.addAll(paramsToIgnore);
        } else {
            toIgnore = paramsToIgnore;
        }
        paramsToIgnoreMap.put(viewSate, toIgnore);
    }

    public Boolean pullDisableUrlParamsSwitch() {
        Boolean ret = disableUrlParamsSwitch;
        disableUrlParamsSwitch = false;
        return ret;
    }

    public void disableUrlParams() {
        this.disableUrlParamsSwitch = true;
    }
}
