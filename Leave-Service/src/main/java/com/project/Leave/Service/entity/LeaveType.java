package com.project.Leave.Service.entity;

public enum LeaveType
{
    CASUAL(12),
    SICK(10),
    EARNED(15);

    private final int yearlyQuota;

    LeaveType(int yearlyQuota) {
        this.yearlyQuota = yearlyQuota;
    }

    public int getYearlyQuota() {
        return yearlyQuota;
    }
}
