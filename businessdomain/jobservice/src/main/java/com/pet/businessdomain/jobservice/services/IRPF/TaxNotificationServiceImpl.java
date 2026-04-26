package com.pet.businessdomain.jobservice.services.IRPF;

import com.pet.businessdomain.jobservice.entities.IRPF.TaxFilingEntity;
import com.pet.businessdomain.jobservice.entities.IRPF.TaxNotificationEntity;
import com.pet.businessdomain.jobservice.repository.IRPF.TaxFilingRepository;
import com.pet.businessdomain.jobservice.repository.IRPF.TaxNotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaxNotificationServiceImpl implements TaxNotificationService {

    private final TaxNotificationRepository notificationRepository;
    private final TaxFilingRepository filingRepository;

    @Override
    @Transactional
    public TaxNotificationEntity createFilingResultNotification(Long characterId, Long taxFilingId, BigDecimal amount, String resultType) {
        log.info("Creating filing result notification for characterId: {}", characterId);

        String formattedAmount = NumberFormat.getCurrencyInstance(new Locale("es", "ES")).format(amount.abs());

        TaxNotificationEntity notification = new TaxNotificationEntity();
        notification.setCharacterId(characterId);
        notification.setTaxFilingId(taxFilingId);
        notification.setNotificationType("RESULT_READY");
        notification.setAmount(amount);

        if ("TO_PAY".equals(resultType)) {
            notification.setTitle("Declaración de la Renta - Resultado a Pagar");
            notification.setMessage("El resultado de tu declaración es de " + formattedAmount + " a pagar. Tienes hasta el " +
                    getPaymentDeadline(taxFilingId) + " para realizar el pago.");
        } else if ("TO_RECEIVE".equals(resultType)) {
            notification.setTitle("Declaración de la Renta - Resultado a Devolver");
            notification.setMessage("¡Buenas noticias! El resultado de tu declaración es de " + formattedAmount +
                    " a devolver. El ingreso se realizará en los próximos días.");
        } else {
            notification.setTitle("Declaración de la Renta - Resultado Neutro");
            notification.setMessage("Tu declaración está ajustada. No hay ni pago ni devolución.");
        }

        notification.setStatus("PENDING");
        notification.setScheduledAt(LocalDateTime.now());
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public TaxNotificationEntity createPaymentReminder(Long characterId, Long taxFilingId, BigDecimal amount, LocalDateTime deadline) {
        log.info("Creating payment reminder for characterId: {}", characterId);

        String formattedAmount = NumberFormat.getCurrencyInstance(new Locale("es", "ES")).format(amount);

        TaxNotificationEntity notification = new TaxNotificationEntity();
        notification.setCharacterId(characterId);
        notification.setTaxFilingId(taxFilingId);
        notification.setNotificationType("PAYMENT_REMINDER");
        notification.setTitle("Recordatorio de pago - Declaración de la Renta");
        notification.setMessage("Tienes un pago pendiente de " + formattedAmount +
                " por tu declaración de la renta. La fecha límite es el " + deadline.toLocalDate());
        notification.setAmount(amount);
        notification.setStatus("PENDING");
        notification.setScheduledAt(deadline.minusDays(5));
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Override
    @Transactional
    public TaxNotificationEntity createPeriodOpenedNotification(Long characterId, Integer taxYear, LocalDateTime deadline) {
        log.info("Creating period opened notification for characterId: {}", characterId);

        TaxNotificationEntity notification = new TaxNotificationEntity();
        notification.setCharacterId(characterId);
        notification.setNotificationType("FILING_REMINDER");
        notification.setTitle("Campaña de la Renta " + taxYear + " abierta");
        notification.setMessage("Ya puedes presentar tu declaración de la renta del año " + taxYear +
                ". Tienes hasta el " + deadline.toLocalDate() + " para hacerlo.");
        notification.setStatus("PENDING");
        notification.setScheduledAt(LocalDateTime.now());
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    @Override
    public List<TaxNotificationEntity> getUnreadNotificationsByCharacter(Long characterId) {
        return notificationRepository.findUnreadByCharacter(characterId);
    }

    @Override
    public List<TaxNotificationEntity> getNotificationsByCharacter(Long characterId) {
        return notificationRepository.findByCharacterIdOrderByCreatedAtDesc(characterId);
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, Long characterId) {
        TaxNotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));

        if (!notification.getCharacterId().equals(characterId)) {
            throw new RuntimeException("Notification does not belong to character");
        }

        notificationRepository.markAsRead(notificationId, LocalDateTime.now());
        log.info("Marked notification {} as read", notificationId);
    }

    @Override
    @Transactional
    public void sendPendingNotifications() {
        log.info("Sending pending notifications");

        List<TaxNotificationEntity> pending = notificationRepository.findPendingToSend(LocalDateTime.now());

        if (pending.isEmpty()) {
            log.info("No pending notifications to send");
            return;
        }

        List<Long> ids = pending.stream().map(TaxNotificationEntity::getId).toList();
        notificationRepository.markAsSent(ids, LocalDateTime.now());

        // Aquí iría la lógica real de envío (email, push, etc.)

        log.info("Sent {} pending notifications", pending.size());
    }

    @Override
    @Transactional
    public void createMassiveNotificationsForFiling(Integer taxYear) {
        log.info("Creating massive notifications for tax year: {}", taxYear);

        List<TaxFilingEntity> filings = filingRepository.findByStatus("SUBMITTED");

        int created = 0;
        for (TaxFilingEntity filing : filings) {
            if (filing.getResultType().equals("TO_PAY")) {
                createPaymentReminder(filing.getCharacterId(), filing.getId(),
                        filing.getAmountToPay(), filing.getPaymentDeadline().atStartOfDay());
                created++;
            }
        }

        log.info("Created {} payment reminders for year: {}", created, taxYear);
    }

    private String getPaymentDeadline(Long taxFilingId) {
        return filingRepository.findById(taxFilingId)
                .map(f -> f.getPaymentDeadline().toString())
                .orElse("fecha no disponible");
    }
}
