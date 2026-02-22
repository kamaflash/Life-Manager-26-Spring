package com.pet.businessdomain.personservice.entities.enumentities;

public class SEnumAccount {

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
