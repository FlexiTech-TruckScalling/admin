package org.flexitech.projects.embedded.truckscale.dto.user_shift;

import java.math.BigDecimal;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.dto.CommonDTO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShiftSummary;
import org.flexitech.projects.embedded.truckscale.util.CommonUtil;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserShiftSummaryDTO extends CommonDTO {
    private UserDTO user;
    private CounterDTO counter;
    private UserShiftDTO userShift;
    private Integer totalTransaction;
    private Integer totalInTransaction;
    private Integer totalOutTransaction;
    private BigDecimal totalAmount;
    private String totalAmountDesc;
    private UserDTO endBy;

    public UserShiftSummaryDTO(UserShiftSummary u) {
        if (!CommonValidators.isValidObject(u)) return;
        
        if (CommonValidators.isValidObject(u.getUser())) {
            this.user = new UserDTO(u.getUser());
        }
        if (CommonValidators.isValidObject(u.getCounter())) {
            this.counter = new CounterDTO(u.getCounter());
        }
        if (CommonValidators.isValidObject(u.getUserShift())) {
            this.userShift = new UserShiftDTO(u.getUserShift());
        }
        this.totalTransaction = u.getTotalTransaction();
        this.totalInTransaction = u.getTotalInTransaction();
        this.totalOutTransaction = u.getTotalOutTransaction();
        this.totalAmount = u.getTotalAmount();
        this.totalAmountDesc = CommonUtil.formatNumber(totalAmount);
        
        if (CommonValidators.isValidObject(u.getEndByUser())) {
            this.endBy = new UserDTO(u.getEndByUser());
        }
        
        setField(u);
    }
}
