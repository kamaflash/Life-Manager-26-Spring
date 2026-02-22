package com.pet.businessdomain.financeservice.entities.enumentities;

public class Enum {

    public enum OwnerType {
        CHARACTER,
        COMPANY,
        NPC
    }
    public enum Frequency {
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY,
        OTHER
    }
    public enum TransactionType {
        INCOME,
        EXPENSE,
        TRANSFER
    }
    public enum ExpenseCategory {
        FOOD,
        HOUSING,
        TRANSPORT,
        EDUCATION,
        HEALTH,
        LEISURE,
        OTHER
    }
    public enum IncomeCategory {
        SALARY,
        BUSINESS,
        INVESTMENT,
        SCHOLARSHIP,
        OTHER
    }


}
