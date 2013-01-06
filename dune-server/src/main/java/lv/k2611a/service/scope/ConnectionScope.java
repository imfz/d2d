package lv.k2611a.service.scope;

public class ConnectionScope extends AbstractScope {

    private ContextService contextService;

    public void setContextService(ContextService contextService) {
        this.contextService = contextService;
    }

    protected Context getContext() {
        return contextService.getCurrentConnectionContext();
    }
}
