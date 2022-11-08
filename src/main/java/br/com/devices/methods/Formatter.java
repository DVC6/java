
package br.com.devices.methods;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class Formatter {
    
    public BigDecimal format(Long value) {
        return new BigDecimal(value.doubleValue() / 1073741824).setScale(2, RoundingMode.HALF_EVEN);
    }
    
}
