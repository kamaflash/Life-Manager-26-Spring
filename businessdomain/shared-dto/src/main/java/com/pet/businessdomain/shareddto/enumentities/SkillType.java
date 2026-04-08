package com.pet.businessdomain.shareddto.enumentities;

public enum SkillType {
    // TECHNOLOGY
    BASIC_PROGRAMMING("basic_programming"),
    LOGIC_THINKING("logic_thinking"),
    WEB_DESIGN("web_design"),
    FRONTEND_DEVELOPMENT("frontend_development"),
    DATA_ANALYSIS("data_analysis"),
    STATISTICS("statistics"),
    PYTHON_BASICS("python_basics"),
    CYBERSECURITY_BASICS("cybersecurity_basics"),
    NETWORK_SAFETY("network_safety"),
    REACT("react"),
    ANGULAR("angular"),
    DEVOPS_BASICS("devops_basics"),
    CI_CD("ci_cd"),
    AUTOMATION("automation"),
    CLOUD_BASICS("cloud_basics"),
    AWS("aws"),
    AZURE("azure"),
    GOOGLE_CLOUD("google_cloud"),
    AI_BASICS("ai_basics"),
    MACHINE_LEARNING("machine_learning"),
    PYTHON_ADVANCED("python_advanced"),
    JAVA_ADVANCED("java_advanced"),
    OBJECT_ORIENTED_DESIGN("object_oriented_design"),
    WEB_SECURITY("web_security"),
    BIG_DATA("big_data"),
    SQL("sql"),
    MOBILE_DEVELOPMENT("mobile_development"),
    REACT_NATIVE("react_native"),
    FLUTTER("flutter"),
    ROBOTICS("robotics"),
    PHYSICS_APPLIED("physics_applied"),

    // HEALTH
    PUBLIC_HEALTH_BASICS("public_health_basics"),
    HEALTH_AWARENESS("health_awareness"),
    NUTRITION_BASICS("nutrition_basics"),
    DIET_PLANNING("diet_planning"),
    FIRST_AID("first_aid"),
    EMERGENCY_RESPONSE("emergency_response"),
    HUMAN_ANATOMY("human_anatomy"),
    PHYSIOLOGY_BASICS("physiology_basics"),
    PHYSIOTHERAPY_BASICS("physiotherapy_basics"),
    REHABILITATION("rehabilitation"),
    MENTAL_HEALTH_AWARENESS("mental_health_awareness"),
    STRESS_MANAGEMENT("stress_management"),
    PHARMACOLOGY_BASICS("pharmacology_basics"),
    MEDICATION_MANAGEMENT("medication_management"),
    MEDICAL_DIAGNOSTICS("medical_diagnostics"),
    CLINICAL_PROCEDURES("clinical_procedures"),
    NURSING_BASICS("nursing_basics"),
    PATIENT_CARE("patient_care"),
    BIOMEDICINE("biomedicine"),
    LAB_TECHNIQUES("lab_techniques"),
    MEDICAL_RESEARCH("medical_research"),

    // CONSTRUCTION
    CONSTRUCTION_BASICS("construction_basics"),
    MATERIAL_HANDLING("material_handling"),
    MASONRY("masonry"),
    STRUCTURAL_BASICS("structural_basics"),
    PLUMBING("plumbing"),
    WATER_SYSTEMS("water_systems"),
    ELECTRICAL_INSTALLATION("electrical_installation"),
    WIRING("wiring"),
    CARPENTRY_BASICS("carpentry_basics"),
    WOODWORK("woodwork"),
    CONSTRUCTION_SAFETY("construction_safety"),
    RISK_MANAGEMENT("risk_management"),
    BLUEPRINT_READING("blueprint_reading"),
    TECHNICAL_DRAWING("technical_drawing"),
    CONCRETE_TECHNOLOGY("concrete_technology"),
    STRUCTURAL_ANALYSIS("structural_analysis"),
    HVAC_INSTALLATION("hvac_installation"),
    VENTILATION_SYSTEMS("ventilation_systems"),
    PROJECT_MANAGEMENT("project_management"),
    CONSTRUCTION_COORDINATION("construction_coordination"),

    // BUSINESS
    BASIC_MANAGEMENT("BASIC_management"),
    DIGITAL_MARKETING("digital_marketing"),
    FINANCIAL_OTHER("financial_other"),
    TEAM_LEADERSHIP("team_leadership"),
    BUSINESS_PLANNING("business_planning"),
    STARTUP_OTHER("startup_other"),
    ECONOMIC_FUNDAMENTALS("economic_fundamentals"),
    PROJECT_OTHER("project_other"),
    SALES_SKILLS("sales_skills"),
    NEGOTIATION("negotiation"),
    CONFLICT_RESOLUTION("conflict_resolution"),
    BUSINESS_STRATEGY("business_strategy"),

    // CREATIVE
    BASIC_GRAPHIC_DESIGN("basic_graphic_design"),
    BASIC_PHOTOGRAPHY("basic_photography"),
    DIGITAL_ILLUSTRATION("digital_illustration"),
    UIUX_DESIGN("uiux_design"),
    BASIC_MUSIC_THEORY("basic_music_theory"),
    ADVANCED_PHOTOGRAPHY("advanced_photography"),
    VIDEO_EDITING("video_editing"),
    CREATIVE_WRITING("creative_writing"),
    DIGITAL_ANIMATION("digital_animation"),
    MUSIC_PRODUCTION("music_production"),
    SOCIAL_MEDIA_MANAGEMENT("social_media_management"),
    INTERIOR_DESIGN("interior_design"),
    FASHION_DESIGN("fashion_design"),
    GAME_DESIGN("game_design"),
    GAME_MECHANICS("game_mechanics"),

    // SOCIAL
    BASIC_TEACHING("basic_teaching"),
    CHILD_PSYCHOLOGY_BASIC("child_psychology_basic"),
    COMMUNITY_WORK_BASIC("community_work_basic"),
    ADVANCED_PSYCHOLOGY("advanced_psychology"),
    EDTECH_TOOLS("edtech_tools"),
    COUNSELING_SKILLS("counseling_skills"),
    SPECIAL_EDUCATION("special_education"),
    SOCIAL_POLICY_ANALYSIS("social_policy_analysis"),
    ADVANCED_COUNSELING("advanced_counseling"),
    YOUTH_PROGRAMS("youth_programs"),
    COMMUNITY_DEVELOPMENT("community_development"),
    EDUCATION_POLICY_ANALYSIS("education_policy_analysis"),
    COMMUNITY_PSYCHOLOGY("community_psychology"),

    // SCIENCE
    BASIC_BIOLOGY("basic_biology"),
    BASIC_CHEMISTRY("basic_chemistry"),
    BASIC_PHYSICS("basic_physics"),
    GENETICS_BASICS("genetics_basics"),
    MICROBIOLOGY("microbiology"),
    ORGANIC_CHEMISTRY("organic_chemistry"),
    BIOCHEMISTRY("biochemistry"),
    ENVIRONMENTAL_SCIENCE("environmental_science"),
    RESEARCH_METHODS("research_methods"),
    GENETIC_ENGINEERING("genetic_engineering"),
    CHEMICAL_ANALYSIS("chemical_analysis"),
    ECOLOGY_BASICS("ecology_basics"),
    LAB_SAFETY("lab_safety"),

    // HOSPITALITY
    BASIC_COOKING("basic_cooking"),
    BASIC_PASTRY("basic_pastry"),
    INTERMEDIATE_COOKING("intermediate_cooking"),
    FOOD_SAFETY("food_safety"),
    INTERNATIONAL_CUISINE("international_cuisine"),
    RESTAURANT_MANAGEMENT("restaurant_management"),
    BARISTA_SKILLS("barista_skills"),
    EVENT_PLANNING("event_planning"),
    TOURISM_MANAGEMENT("tourism_management"),
    FOOD_PRESENTATION("food_presentation"),
    HOTEL_OPERATIONS("hotel_operations"),
    FOOD_COST_CONTROL("food_cost_control"),
    CUSTOMER_SERVICE("customer_service"),
    COCKTAIL_PREPARATION("cocktail_preparation"),

    // EDUCATION
    BASIC_SKILLS("basic_skills"),
    READING("reading"),
    WRITING("writing"),
    BASIC_MATH("basic_math"),
    COMMUNICATION("communication");

    private final String value;

    SkillType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SkillType fromValue(String value) {
        for (SkillType skill : SkillType.values()) {
            if (skill.value.equals(value)) {
                return skill;
            }
        }
        throw new IllegalArgumentException("Unknown skill: " + value);
    }
}