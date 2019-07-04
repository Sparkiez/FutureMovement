package com.waleong.futuremovement;

import com.waleong.futuremovement.util.NumberUtil;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by raymondleong on 03,July,2019
 */
public class NumberUtilTest {
    @Test
    public void testInteger() {
        Integer number = NumberUtil.parseInteger("1");
        assert(number == 1);
    }

    @Test
    public void testNegativeInteger() {
        Integer number = NumberUtil.parseInteger("-1");
        assert(number == -1);
    }

    @Test
    public void testInvalidInteger() {
        Integer number = NumberUtil.parseInteger("askjnsd");
        assertNull(number);
    }

    @Test
    public void testEmptyInteger() {
        Integer number = NumberUtil.parseInteger("");
        assertNull(number);
    }
}
