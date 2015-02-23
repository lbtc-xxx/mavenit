package mavenit;

import org.junit.*;

public class HelloImplIT {
    
    @ClassRule
    public static AppServer appServer = new AppServer();
    private static Hello sut;

    @BeforeClass
    public static void beforeClass() {
        sut = appServer.lookup("mavenit", HelloImpl.class, Hello.class);
    }

    @Test
    public void test() {
        Assert.assertEquals("Hello world!", sut.hello());
    }
}
