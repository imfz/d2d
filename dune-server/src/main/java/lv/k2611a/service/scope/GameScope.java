package lv.k2611a.service.scope;

public class GameScope extends AbstractScope {

    private ContextService contextService;

    public void setContextService(ContextService contextService) {
        this.contextService = contextService;
    }

    @Override
    protected Context getContext() {
        return contextService.getCurrentGameContext();
    }
}
