package com.depression.model;

import java.math.BigDecimal;
import java.util.Date;

public class CapitalPlatformUnitFundStatistics extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_platform_unit_fund_statistics.fsid
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    /* @Comment() */
    private Long fsid;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_platform_unit_fund_statistics.top_up_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    /* @Comment(充值统计) */
    private BigDecimal topUpAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_platform_unit_fund_statistics.withdraw_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    /* @Comment(提现统计) */
    private BigDecimal withdrawAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_platform_unit_fund_statistics.income_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    /* @Comment(收入统计) */
    private BigDecimal incomeAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_platform_unit_fund_statistics.expenses_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    /* @Comment(支出统计) */
    private BigDecimal expensesAmount;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_platform_unit_fund_statistics.statistics_date
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    /* @Comment(统计日期) */
    private Date statisticsDate;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_platform_unit_fund_statistics.is_delete
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    /* @Comment() */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_platform_unit_fund_statistics.fsid
     *
     * @return the value of capital_platform_unit_fund_statistics.fsid
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public Long getFsid() {
        return fsid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_platform_unit_fund_statistics.fsid
     *
     * @param fsid the value for capital_platform_unit_fund_statistics.fsid
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public void setFsid(Long fsid) {
        this.fsid = fsid;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_platform_unit_fund_statistics.top_up_amount
     *
     * @return the value of capital_platform_unit_fund_statistics.top_up_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public BigDecimal getTopUpAmount() {
        return topUpAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_platform_unit_fund_statistics.top_up_amount
     *
     * @param topUpAmount the value for capital_platform_unit_fund_statistics.top_up_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public void setTopUpAmount(BigDecimal topUpAmount) {
        this.topUpAmount = topUpAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_platform_unit_fund_statistics.withdraw_amount
     *
     * @return the value of capital_platform_unit_fund_statistics.withdraw_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public BigDecimal getWithdrawAmount() {
        return withdrawAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_platform_unit_fund_statistics.withdraw_amount
     *
     * @param withdrawAmount the value for capital_platform_unit_fund_statistics.withdraw_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public void setWithdrawAmount(BigDecimal withdrawAmount) {
        this.withdrawAmount = withdrawAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_platform_unit_fund_statistics.income_amount
     *
     * @return the value of capital_platform_unit_fund_statistics.income_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public BigDecimal getIncomeAmount() {
        return incomeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_platform_unit_fund_statistics.income_amount
     *
     * @param incomeAmount the value for capital_platform_unit_fund_statistics.income_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public void setIncomeAmount(BigDecimal incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_platform_unit_fund_statistics.expenses_amount
     *
     * @return the value of capital_platform_unit_fund_statistics.expenses_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public BigDecimal getExpensesAmount() {
        return expensesAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_platform_unit_fund_statistics.expenses_amount
     *
     * @param expensesAmount the value for capital_platform_unit_fund_statistics.expenses_amount
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public void setExpensesAmount(BigDecimal expensesAmount) {
        this.expensesAmount = expensesAmount;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_platform_unit_fund_statistics.statistics_date
     *
     * @return the value of capital_platform_unit_fund_statistics.statistics_date
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public Date getStatisticsDate() {
        return statisticsDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_platform_unit_fund_statistics.statistics_date
     *
     * @param statisticsDate the value for capital_platform_unit_fund_statistics.statistics_date
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public void setStatisticsDate(Date statisticsDate) {
        this.statisticsDate = statisticsDate;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_platform_unit_fund_statistics.is_delete
     *
     * @return the value of capital_platform_unit_fund_statistics.is_delete
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_platform_unit_fund_statistics.is_delete
     *
     * @param isDelete the value for capital_platform_unit_fund_statistics.is_delete
     *
     * @mbggenerated Tue Aug 30 14:19:50 CST 2016
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}