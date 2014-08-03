package net.kencochrane.raven.connection;

import mockit.*;
import net.kencochrane.raven.event.Event;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.concurrent.locks.ReentrantLock;

import static mockit.Deencapsulation.getField;
import static mockit.Deencapsulation.setField;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;

public class AbstractConnectionTest {
    @Injectable
    private final String publicKey = "9bcf4a8c-f353-4f25-9dda-76a873fff905";
    @Injectable
    private final String secretKey = "56a9d05e-9032-4fdd-8f67-867d526422f9";
    @Tested
    private AbstractConnection abstractConnection = null;
    //Spying with mockito as jMockit doesn't support mocks of ReentrantLock
    @Spy
    private ReentrantLock reentrantLock = new ReentrantLock();
    //Disable thread sleep during the tests
    @Mocked("sleep")
    private Thread mockThread = null;

    @BeforeMethod
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testAuthHeader() throws Exception {
        String authHeader = abstractConnection.getAuthHeader();

        assertThat(authHeader, is("Sentry sentry_version=5,"
                + "sentry_client=Raven-Java/Test,"
                + "sentry_key=" + publicKey + ","
                + "sentry_secret=" + secretKey));
    }

    @Test
    public void testSuccessfulSendCallsDoSend(@Injectable final Event mockEvent) throws Exception {
        setField(abstractConnection, "lock", reentrantLock);

        abstractConnection.send(mockEvent);

        new Verifications() {{
            abstractConnection.doSend(mockEvent);
        }};
    }

    @Test
    public void testExceptionOnSendStartLockDown(@Injectable final Event mockEvent) throws Exception {
        setField(abstractConnection, "lock", reentrantLock);
        new NonStrictExpectations() {{
            abstractConnection.doSend((Event) any);
            result = new ConnectionException();
        }};

        abstractConnection.send(mockEvent);

        verify(reentrantLock).tryLock();
        verify(reentrantLock).unlock();
    }

    @Test
    public void testLockDownDoublesTheWaitingTime(@Injectable final Event mockEvent) throws Exception {
        new NonStrictExpectations() {{
            abstractConnection.doSend((Event) any);
            result = new ConnectionException();
        }};

        abstractConnection.send(mockEvent);

        long waitingTimeAfter = getField(abstractConnection, "waitingTime");
        assertThat(waitingTimeAfter, is(AbstractConnection.DEFAULT_BASE_WAITING_TIME * 2));
        new Verifications() {{
            Thread.sleep(AbstractConnection.DEFAULT_BASE_WAITING_TIME);
        }};
    }

    @Test
    public void testLockDownDoesntDoubleItAtMax(@Injectable final Event mockEvent) throws Exception {
        setField(abstractConnection, "waitingTime", AbstractConnection.DEFAULT_MAX_WAITING_TIME);
        new NonStrictExpectations() {{
            abstractConnection.doSend((Event) any);
            result = new ConnectionException();
        }};

        abstractConnection.send(mockEvent);

        long waitingTimeAfter = getField(abstractConnection, "waitingTime");
        assertThat(waitingTimeAfter, is(AbstractConnection.DEFAULT_MAX_WAITING_TIME));
        new Verifications() {{
            Thread.sleep(AbstractConnection.DEFAULT_MAX_WAITING_TIME);
        }};
    }
}
