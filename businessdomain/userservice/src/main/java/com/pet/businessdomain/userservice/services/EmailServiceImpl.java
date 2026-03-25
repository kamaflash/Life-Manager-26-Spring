package com.pet.businessdomain.userservice.services;

import com.pet.businessdomain.shareddto.dto.UserDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class EmailServiceImpl implements IEmailService {

    private String appBaseUrl;
    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendConfirmationEmail(UserDto userDto) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        String baseUrl = "http://localhost:4200/confirm-registration";
        String queryParams = String.format(
                "?uid=%s",
                URLEncoder.encode(String.valueOf(userDto.getId()), StandardCharsets.UTF_8)
        );

        String confirmationUrl = baseUrl + queryParams;
        String htmlContent = "<div style=\"font-family: 'Segoe UI', Arial, sans-serif; max-width: 600px; margin: 0 auto; padding: 25px; background: #f8fafc; border-radius: 12px; box-shadow: 0 5px 15px rgba(0,0,0,0.05);\">" +

                "<div style=\"text-align: center; margin-bottom: 35px; padding-bottom: 20px; border-bottom: 2px solid #e2e8f0;\">" +
                "<div style=\"font-size: 40px; color: #3b82f6; margin-bottom: 10px;\">📊🧑‍💼</div>" +
                "<h1 style=\"color: #0f172a; margin-bottom: 8px; font-size: 26px;\">¡Bienvenido a Life Manager 26!</h1>" +
                "<p style=\"color: #475569; font-weight: 600; font-size: 16px;\">Tu asistente personal para organizar tu vida social, laboral y financiera</p>" +
                "</div>" +

                "<p style=\"font-size: 16px; line-height: 1.7; color: #475569; margin-bottom: 20px;\">" +
                "Estimado/a usuario/a,<br><br>" +
                "Hemos recibido tu solicitud de registro en <strong style=\"color: #3b82f6;\">Life Manager 26</strong>, la herramienta definitiva para llevar un control completo de tu día a día. Con nuestra app podrás gestionar tareas, finanzas, eventos sociales y mucho más, todo en un solo lugar." +
                "</p>" +

                "<div style=\"background: #ffffff; border-radius: 10px; padding: 20px; margin: 25px 0; border: 1px solid #e2e8f0;\">" +
                "<p style=\"margin: 0 0 15px 0; font-size: 16px; color: #0f172a; font-weight: 600;\">" +
                "✨ Lo que podrás hacer con Life Manager 26:" +
                "</p>" +
                "<ul style=\"margin: 0; padding-left: 20px; color: #475569;\">" +
                "<li style=\"margin-bottom: 8px;\"><span style=\"color: #22c55e; font-weight: bold;\">✓</span> <strong>Finanzas personales:</strong> Registra ingresos y gastos, visualiza tu balance y establece presupuestos</li>" +
                "<li style=\"margin-bottom: 8px;\"><span style=\"color: #22c55e; font-weight: bold;\">✓</span> <strong>Agenda social:</strong> Organiza eventos, cumpleaños y reuniones con recordatorios inteligentes</li>" +
                "<li style=\"margin-bottom: 8px;\"><span style=\"color: #22c55e; font-weight: bold;\">✓</span> <strong>Metas laborales:</strong> Define objetivos profesionales, haz seguimiento de proyectos y tareas pendientes</li>" +
                "<li style=\"margin-bottom: 8px;\"><span style=\"color: #22c55e; font-weight: bold;\">✓</span> <strong>Estadísticas claras:</strong> Gráficos y reportes para entender tus hábitos y mejorar tu productividad</li>" +
                "</ul>" +
                "</div>" +

                "<div style=\"text-align: center; margin: 40px 0; padding: 25px; background: linear-gradient(90deg, #eef2ff, #e9d5ff, #fee2e2); border-radius: 12px;\">" +
                "<p style=\"color: #0f172a; font-weight: 600; margin-bottom: 15px; font-size: 17px;\">" +
                "🚀 Activa tu cuenta y empieza a organizar tu vida ahora mismo" +
                "</p>" +
                "<a href=\"" + confirmationUrl + "\" style=\"" +
                "display: inline-block; padding: 16px 40px; " +
                "background: linear-gradient(135deg, #3b82f6, #8b5cf6); " +
                "color: white; font-size: 18px; font-weight: bold; " +
                "text-decoration: none; border-radius: 8px; " +
                "box-shadow: 0 6px 20px rgba(59, 130, 246, 0.4); " +
                "border: none; cursor: pointer; " +
                "transition: all 0.3s ease; transform: translateY(0);\" " +
                "onmouseover=\"this.style.transform='translateY(-2px)'; this.style.boxShadow='0 8px 25px rgba(59, 130, 246, 0.5)'\" " +
                "onmouseout=\"this.style.transform='translateY(0)'; this.style.boxShadow='0 6px 20px rgba(59, 130, 246, 0.4)'\">" +
                "✅ Activar mi cuenta" +
                "</a>" +
                "</div>" +

                "<div style=\"background: #f1f5f9; border-radius: 10px; padding: 20px; margin: 25px 0; border-left: 4px solid #3b82f6;\">" +
                "<p style=\"margin: 0 0 10px 0; font-size: 14px; color: #0f172a; font-weight: 600;\">" +
                "ℹ️ Información importante:" +
                "</p>" +
                "<p style=\"margin: 0; font-size: 14px; color: #475569; line-height: 1.6;\">" +
                "• Este enlace de confirmación expira en <strong>48 horas</strong><br>" +
                "• Una vez activada, podrás personalizar tu perfil y preferencias<br>" +
                "• La app sincroniza tus datos en la nube para que no pierdas nada<br>" +
                "• Si tienes dudas, consulta nuestra guía de inicio rápido en la web" +
                "</p>" +
                "</div>" +

                "<p style=\"font-size: 14px; color: #64748b; line-height: 1.6; margin-top: 25px;\">" +
                "<strong>Enlace alternativo:</strong> Si el botón no funciona, copia y pega esta URL en tu navegador:<br>" +
                "<span style=\"color: #3b82f6; background: #ffffff; padding: 8px 12px; border-radius: 6px; font-size: 13px; display: inline-block; margin-top: 8px; word-break: break-all;\">" + confirmationUrl + "</span>" +
                "</p>" +

                "<p style=\"font-size: 13px; color: #94a3b8; line-height: 1.5; margin-top: 30px; padding-top: 20px; border-top: 1px solid #e2e8f0;\">" +
                "<strong>«Organizar tu vida no es una opción, es la clave para alcanzar tus metas.»</strong><br><br>" +
                "Si no has solicitado registrarte en Life Manager 26, por favor ignora este mensaje.<br>" +
                "Gracias por confiar en nosotros para mejorar tu día a día." +
                "</p>" +

                "<div style=\"text-align: center; margin-top: 30px; padding-top: 20px; border-top: 1px solid #e2e8f0;\">" +
                "<p style=\"font-size: 12px; color: #94a3b8;\">" +
                "© " + java.time.Year.now().getValue() + " Life Manager 26 — Organiza tu vida social, laboral y financiera<br>" +
                "Email generado automáticamente. Por favor no responder." +
                "</p>" +
                "</div>" +
                "</div>";

        helper.setFrom("careerlifemanager@gmail.com");
        helper.setTo(userDto.getEmail());
        helper.setSubject("Confirma tu registro");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }
}
