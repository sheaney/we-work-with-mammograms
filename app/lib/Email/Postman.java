package lib.Email;

import com.typesafe.plugin.MailerAPI;
import com.typesafe.plugin.MailerPlugin;
import play.Logger;
import play.Play;

/**
 * Created by fernando on 5/15/14.
 */
public class Postman {

    private static MailerAPI mail = play.Play.application().plugin(MailerPlugin.class).email();
    private static String host = Play.application().configuration().getString("host");

    public static void welcomeEmail(String name, String userId, String password){
        if (play.Play.application().isProd() || play.Play.application().isDev()) {
            Logger.info(String.format("Sending email to \"%s\"", userId));
            mail.setSubject("Bienvenido a WWWM!");
            mail.addRecipient(userId);
            mail.addFrom("no-reply@wwwm.com");
            mail.sendHtml("<html><div>Estimad@ " + name + ", </div>" +
                    "<div>A partir de este momento puede acceder al sistema con los siguientes datos:</div>" +
                    "<div>Usuario: <b>" + userId + "</b></div>" +
                    "<div>Contraseña: <b>" + password + "</b></div><br/>" +
                    "<div>Desde <a href=\"" + host + "\" >aqui</a> puedes acceder al sistema.</div>" +
                    "<div>Equipo WWWM.</div></html>");
        }
    }
}
