package com.csa.cashsasa.api;

public class ApiData
{
    /*private static final String BASE_URL = "https://www.futuristicdevextranet.com/digipay/mecash/";
    private static final String SAVE_MESSAGE = "index.php/web/add_message";
    private static final String REGISTER_USER = "index.php/web/add_user_details";
    private static final String VERIFY_PHONE = "index.php/web/verify_phone";
    private static final String LOGIN_USER = "index.php/web/login_user";
    private static final String SEND_CODE = "index.php/web/send_message";
    private static final String APPLY_LOAN = "index.php/web/calculate_loan_amount";
    private static final String LOAN_HISTORY = "index.php/web/loan_history";
    private static final String PAY_LOAN = "index.php/web/pay_loan";
    private static final String ROLL_BACK = "index.php/web/roll_back_changes";
    private static final String LOAN_DETAILS = "index.php/web/userLoanDetails";
    private static final String RESET_PIN = "index.php/web/reset_pin";*/


    private static final String BASE_URL = "http://192.168.43.104/Api/Android/";
    private static final String SAVE_MESSAGE = "web/add_message";
    private static final String REGISTER_USER = "web/add_user_details";
    private static final String VERIFY_PHONE = "web/verify_phone";
    private static final String LOGIN_USER = "web/login_user";
    private static final String SEND_CODE = "web/send_message";
    private static final String APPLY_LOAN = "web/calculate_loan_amount";
    private static final String LOAN_HISTORY = "web/loan_history";
    private static final String PAY_LOAN = "web/pay_loan";
    private static final String ROLL_BACK = "web/roll_back_changes";
    private static final String LOAN_DETAILS = "web/userLoanDetails";
    private static final String RESET_PIN = "web/reset_pin";

    public static String ProcessMessage()
    {
        return BASE_URL + SAVE_MESSAGE;
    }

    public static String ProcessUserData()
    {
        return BASE_URL + REGISTER_USER;
    }

    public static String VerifyUserPhone()
    {
        return BASE_URL + VERIFY_PHONE;
    }

    public static String LoginUser()
    {
        return BASE_URL + LOGIN_USER;
    }

    public static String SendCode()
    {
        return BASE_URL + SEND_CODE;
    }

    public static String applyLoan()
    {
        return BASE_URL + APPLY_LOAN;
    }

    public static String loanHistory()
    {
        return BASE_URL + LOAN_HISTORY;
    }

    public static String payLoan()
    {
        return BASE_URL + PAY_LOAN;
    }

    public static String rollBack()
    {
        return BASE_URL + ROLL_BACK;
    }

    public static String loanDetails()
    {
        return BASE_URL + LOAN_DETAILS;
    }

    public static String resetPin()
    {
        return BASE_URL + RESET_PIN;
    }
}
