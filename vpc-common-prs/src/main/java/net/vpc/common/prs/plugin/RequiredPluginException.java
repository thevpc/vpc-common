package net.vpc.common.prs.plugin;

/**
 * Created by IntelliJ IDEA.
 * User: vpc
 * Date: 26 févr. 2009
 * Time: 17:51:46
 * To change this template use File | Settings | File Templates.
 */
public class RequiredPluginException extends PluginException{
    private String requiredPlugin;

    public RequiredPluginException(String plugin,String requiredPlugin) {
        super(plugin, "Required Plugin "+requiredPlugin+" not found");
        this.requiredPlugin=requiredPlugin;
    }

    public String getRequiredPlugin() {
        return requiredPlugin;
    }

}
