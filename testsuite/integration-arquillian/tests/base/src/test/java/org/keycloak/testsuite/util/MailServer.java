package org.keycloak.testsuite.util;

import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetup;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.net.SocketException;
import javax.mail.MessagingException;

import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeMultipart;
import org.jboss.logging.Logger;
import static org.keycloak.testsuite.util.MailServerConfiguration.*;

public class MailServer {

    private static final Logger log = Logger.getLogger(MailServer.class);
    
    private static GreenMail greenMail;

    public static void main(String[] args) throws Exception {
        MailServer.start();
        MailServer.createEmailAccount("test@email.test", "password");
        
        try {
            while (true) {
                int c = greenMail.getReceivedMessages().length;

                if (greenMail.waitForIncomingEmail(Long.MAX_VALUE, c + 1)) {
                    MimeMessage m = greenMail.getReceivedMessages()[c++];
                    log.info("-------------------------------------------------------");
                    log.info("Received mail to " + m.getRecipients(RecipientType.TO)[0]);
                    if (m.getContent() instanceof MimeMultipart) {
                        MimeMultipart mimeMultipart = (MimeMultipart) m.getContent();
                        for (int i = 0; i < mimeMultipart.getCount(); i++) {
                            log.info("----");
                            log.info(mimeMultipart.getBodyPart(i).getContentType() + ":\n");
                            log.info(mimeMultipart.getBodyPart(i).getContent());
                        }
                    } else {
                        log.info("\n" + m.getContent());
                    }
                    log.info("-------------------------------------------------------");
                }
            }
        } catch (IOException | InterruptedException | MessagingException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static void start() {
        ServerSetup setup = new ServerSetup(Integer.parseInt(PORT), HOST, "smtp");

        greenMail = new GreenMail(setup);
        greenMail.start();

        log.info("Started mail server (" + HOST + ":" + PORT + ")");
    }

    public static void stop() {
        if (greenMail != null) {
            log.info("Stopping mail server (localhost:3025)");
            // Suppress error from GreenMail on shutdown
            Thread.setDefaultUncaughtExceptionHandler(new UncaughtExceptionHandler() {
                @Override
                public void uncaughtException(Thread t, Throwable e) {
                    if (!(e.getCause() instanceof SocketException && e.getStackTrace()[0].getClassName()
                            .equals("com.icegreen.greenmail.smtp.SmtpHandler"))) {
                        log.error("Exception in thread \"" + t.getName() + "\" ");
                        log.error(e.getMessage(), e);
                    }
                }
            });
            greenMail.stop();
        }
    }

    public static void createEmailAccount(String email, String password) {
        log.debug("Creating email account " + email);
        greenMail.setUser(email, password);
    }
    
    public static MimeMessage getLastReceivedMessage() throws InterruptedException {
        if (greenMail.waitForIncomingEmail(1)) {
            return greenMail.getReceivedMessages()[greenMail.getReceivedMessages().length - 1];
        }
        return null;
    }
}
