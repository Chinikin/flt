package com.depression.model;

import java.math.BigDecimal;

public class CapitalShortcutRecharge extends Page {
    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_shortcut_recharge.sr_id
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    /* @Comment() */
    private Long srId;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_shortcut_recharge.cash_money
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    /* @Comment() */
    private BigDecimal cashMoney;

    /**
     *
     * This field was generated by MyBatis Generator.
     * This field corresponds to the database column capital_shortcut_recharge.is_delete
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    /* @Comment() */
    private Byte isDelete;

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_shortcut_recharge.sr_id
     *
     * @return the value of capital_shortcut_recharge.sr_id
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public Long getSrId() {
        return srId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_shortcut_recharge.sr_id
     *
     * @param srId the value for capital_shortcut_recharge.sr_id
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public void setSrId(Long srId) {
        this.srId = srId;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_shortcut_recharge.cash_money
     *
     * @return the value of capital_shortcut_recharge.cash_money
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public BigDecimal getCashMoney() {
        return cashMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_shortcut_recharge.cash_money
     *
     * @param cashMoney the value for capital_shortcut_recharge.cash_money
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public void setCashMoney(BigDecimal cashMoney) {
        this.cashMoney = cashMoney;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method returns the value of the database column capital_shortcut_recharge.is_delete
     *
     * @return the value of capital_shortcut_recharge.is_delete
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public Byte getIsDelete() {
        return isDelete;
    }

    /**
     * This method was generated by MyBatis Generator.
     * This method sets the value of the database column capital_shortcut_recharge.is_delete
     *
     * @param isDelete the value for capital_shortcut_recharge.is_delete
     *
     * @mbggenerated Sat Aug 27 14:35:49 CST 2016
     */
    public void setIsDelete(Byte isDelete) {
        this.isDelete = isDelete;
    }
}