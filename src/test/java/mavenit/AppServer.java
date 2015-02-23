package mavenit;

import org.junit.rules.ExternalResource;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Properties;

public class AppServer extends ExternalResource {

    private static final Properties props;
    private Context context;

    static {
        props = new Properties();
        props.setProperty("remote.connections", "default");
        props.setProperty("remote.connection.default.port", System.getProperty("app-server.port", "8080"));
        props.setProperty("remote.connection.default.host", System.getProperty("app-server.host", "localhost"));
        props.setProperty("remote.connectionprovider.create.options.org.xnio.Options.SSL_ENABLED", "false");
        props.setProperty(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
        props.setProperty("org.jboss.ejb.client.scoped.context", "true");
    }

    @Override
    protected void before() throws Throwable {
        context = new InitialContext(props);
    }

    public <T> T lookup(String moduleName, Class<?> impl, Class<T> intf) {
        return lookup("", moduleName, impl, intf);
    }

    public <T> T lookup(String appName, String moduleName, Class<?> impl, Class<T> intf) {
        String name = "ejb:" + appName + "/" + moduleName + "//" + impl.getSimpleName() + "!" + intf.getName();
        return lookup(name);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T lookup(String name) {
        try {
            return (T) context.lookup(name);
        } catch (NamingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void after() {
        if (context != null) {
            try {
                context.close();
            } catch (NamingException e) {
                // nop
            }
        }
    }
}
