package com.pet.businessdomain.shareddto.enumentities;

public enum NotificationType {
    // 1. Sistema / Administración
    SYSTEM,          // notificaciones generales del sistema
    ALERT,           // alertas importantes (seguridad, fallos, etc.)
    MAINTENANCE,     // avisos de mantenimiento

    // 2. Contenido / Aprendizaje
    NEW_CONTENT,     // nuevo curso, lección, recurso disponible
    UPDATE_CONTENT,  // contenido actualizado
    DEADLINE,        // fechas límite (exámenes, entregas)
    REMINDER,        // recordatorios de actividades pendientes

    // 3. Interacción social
    LIKE,
    COMMENT,
    FOLLOW,
    MENTION,         // cuando te mencionan en un comentario o post
    REPLY,           // respuesta a tu comentario

    // 4. Comunicación directa
    MESSAGE,         // nuevo mensaje directo
    CHAT,            // mensaje en chat grupal

    // 5. Eventos / Ofertas
    EVENT,           // eventos próximos (webinars, meetups)
    PROMOTION,       // promociones, descuentos
    ACHIEVEMENT,      // logros, insignias desbloqueadas
    JOBS
}
