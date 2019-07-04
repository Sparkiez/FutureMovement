package com.waleong.futuremovement;

import com.google.gson.reflect.TypeToken;
import com.waleong.futuremovement.data.TransactionData;
import com.waleong.futuremovement.helper.GsonHelper;
import com.waleong.futuremovement.model.ColumnDefinition;
import com.waleong.futuremovement.util.NumberUtil;
import com.waleong.futuremovement.util.RecordParserUtil;
import com.waleong.futuremovement.util.RecordWriterUtil;
import com.waleong.futuremovement.utils.FileReader;

import org.junit.Before;
import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;


/**
 * Created by raymondleong on 03,July,2019
 */
public class RecordParserUtilTest {

    private List<ColumnDefinition> mColumnDefinitions;

    @Before
    public void setup() {
        String text = FileReader.readFile("col_definition.json");
        Type columnDefinitionsType = new TypeToken<ArrayList<ColumnDefinition>>() {
        }.getType();
        mColumnDefinitions = GsonHelper.parse(text, columnDefinitionsType);
    }

    @Test
    public void testEmptyLineParse() {
        Map<String, String> transactionMap = RecordParserUtil.parse(mColumnDefinitions, "");
        assert (transactionMap.isEmpty());
    }

    @Test
    public void testExistingKeyInPartialLineParse() {
        String partialTransactionStr = TransactionData.TEST_TRANSACTION_1.substring(0, 50);
        Map<String, String> transactionMap = RecordParserUtil.parse(mColumnDefinitions, partialTransactionStr);

        String value = transactionMap.get(ColumnDefinition.KEY_CLIENT_TYPE);
        assert (value.equals("CL  "));
    }

    @Test
    public void testNonExistingKeyInPartialLineParse() {
        String partialTransactionStr = TransactionData.TEST_TRANSACTION_1.substring(0, 55);
        Map<String, String> transactionMap = RecordParserUtil.parse(mColumnDefinitions, partialTransactionStr);

        String value = transactionMap.get(ColumnDefinition.KEY_QUANTITY_SHORT);
        assert (value == null);
    }

    @Test
    public void testPartlyExistingKeyInPartialLineParse() {
        String partialTransactionStr = TransactionData.TEST_TRANSACTION_1.substring(0, 55);
        Map<String, String> transactionMap = RecordParserUtil.parse(mColumnDefinitions, partialTransactionStr);

        String value = transactionMap.get(ColumnDefinition.KEY_QUANTITY_LONG);
        assert (value == null);
    }
}
