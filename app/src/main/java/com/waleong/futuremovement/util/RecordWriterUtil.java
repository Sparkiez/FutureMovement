package com.waleong.futuremovement.util;

import android.text.TextUtils;

import com.waleong.futuremovement.model.ColumnDefinition;

import java.io.BufferedReader;
import java.util.List;
import java.util.Map;

/**
 * A writer that writes the output of a daily summary CSV file
 * by extracting the relevant information from a list of parsed transactional
 * record.
 * Created by raymondleong on 03,July,2019
 */
public class RecordWriterUtil {

    public static String generateOutput(List<Map<String, String>> transactions) {
        StringBuilder stringBuilder = new StringBuilder();

        // Creating the header.
        stringBuilder.append(TextUtils.join(",", new String[]{"Client_Information", "Product_Information", "Total_Transaction_Amount"}));

        for (int i = 0; i < transactions.size(); i++) {
            // Append carriage return before writing the next line.
            stringBuilder.append("\n");

            Map<String, String> transaction = transactions.get(i);
            String clientInfo = generateClientInfo(transaction);
            String productInfo = generateProductInfo(transaction);
            String totalTransactionAmount = generateTotalTransactionAmount(transaction) + "";
            String newLine = TextUtils.join(",", new String[]{clientInfo, productInfo, totalTransactionAmount});

            stringBuilder.append(newLine);
        }

        return stringBuilder.toString();
    }

    private static String generateClientInfo(Map<String, String> transaction) {
        String clientType = transaction.get(ColumnDefinition.KEY_CLIENT_TYPE);
        String clientNumber = transaction.get(ColumnDefinition.KEY_CLIENT_NUMBER);
        String clientAccountNumber = transaction.get(ColumnDefinition.KEY_ACCOUNT_NUMBER);
        String clientSubAccountNumber = transaction.get(ColumnDefinition.KEY_SUBACCOUNT_NUMBER);

        return clientType + clientNumber + clientAccountNumber + clientSubAccountNumber;
    }

    private static String generateProductInfo(Map<String, String> transaction) {
        String exchangeCode = transaction.get(ColumnDefinition.KEY_EXCHANGE_CODE);
        String productGroupCode = transaction.get(ColumnDefinition.KEY_PRODUCT_GROUP_CODE);
        String symbol = transaction.get(ColumnDefinition.KEY_SYMBOL);
        String expirationDate = transaction.get(ColumnDefinition.KEY_EXPIRATION_DATE);

        return exchangeCode + productGroupCode + symbol + expirationDate;
    }

    private static Integer generateTotalTransactionAmount(Map<String, String> transaction) {
        String quantityLong = transaction.get(ColumnDefinition.KEY_QUANTITY_LONG);
        String quantityShort = transaction.get(ColumnDefinition.KEY_QUANTITY_SHORT);

        // Pad it with 0 until it is 10 number.
        Integer quantityLongAsInt = NumberUtil.parseInteger(quantityLong);
        Integer quantityShortAsInt = NumberUtil.parseInteger(quantityShort);

        if (quantityLongAsInt == null || quantityLongAsInt == null) {
            return null;
        }

        return quantityLongAsInt - quantityShortAsInt;
    }
}
