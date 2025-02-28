package org.flexitech.projects.embedded.truckscale.services.shift;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.flexitech.projects.embedded.truckscale.common.CommonValidators;
import org.flexitech.projects.embedded.truckscale.common.enums.InOutBounds;
import org.flexitech.projects.embedded.truckscale.common.enums.ShiftStatus;
import org.flexitech.projects.embedded.truckscale.dao.shift.UserShiftDAO;
import org.flexitech.projects.embedded.truckscale.dao.shift.UserShiftSummaryDAO;
import org.flexitech.projects.embedded.truckscale.dao.transaction.TransactionDAO;
import org.flexitech.projects.embedded.truckscale.dto.counter.CounterDTO;
import org.flexitech.projects.embedded.truckscale.dto.shift.CurrentShiftSummaryDTO;
import org.flexitech.projects.embedded.truckscale.dto.shift.UserShiftDTO;
import org.flexitech.projects.embedded.truckscale.dto.transaction.TransactionSearchDTO;
import org.flexitech.projects.embedded.truckscale.dto.user.UserDTO;
import org.flexitech.projects.embedded.truckscale.dto.user_shift.UserShiftSearchDTO;
import org.flexitech.projects.embedded.truckscale.dto.user_shift.UserShiftSummaryDTO;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShift;
import org.flexitech.projects.embedded.truckscale.entities.shift.UserShiftSummary;
import org.flexitech.projects.embedded.truckscale.entities.transaction.Transaction;
import org.flexitech.projects.embedded.truckscale.util.CommonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserShiftReportServiceImpl implements UserShiftReportService {

	@Autowired
	UserShiftDAO userShiftDAO;
	
	@Autowired
	UserShiftSummaryDAO shiftSummaryDAO;
	
	@Autowired
	UserShiftService userShiftService;
	
	@Autowired
	TransactionDAO transactionDAO;

	@Override
	public List<UserShiftDTO> searchUserShift(UserShiftSearchDTO searchDTO, boolean export) {
		List<UserShift> shifts = this.userShiftDAO.searchUserShift(searchDTO, export);

		if (CommonValidators.validList(shifts)) {
			return shifts.stream().map(UserShiftDTO::new).collect(Collectors.toList());
		}

		return Collections.emptyList();
	}

	@Override
	public Integer countUserShift(UserShiftSearchDTO searchDTO) {
		return this.userShiftDAO.countUserShift(searchDTO);
	}

	@Override
	public UserShiftSummaryDTO getShiftDetailByShiftId(Long shiftId) {

		UserShift shift = this.userShiftDAO.get(shiftId);
		
		if(!CommonValidators.isValidObject(shift)) {
			return null;
		}
		
		if(ShiftStatus.OPEN.getCode().equals(shift.getShiftStatus())) {
			
			UserShiftSummaryDTO summaryDTO = new UserShiftSummaryDTO();
			summaryDTO.setCounter(new CounterDTO(shift.getUser().getCounter()));
			summaryDTO.setUserShift(new UserShiftDTO(shift));
			summaryDTO.setUser(new UserDTO(shift.getUser()));
			
			TransactionSearchDTO search = new TransactionSearchDTO();
			search.setSessionCode(shift.getCode());
			
			List<Transaction> transactions = this.transactionDAO.searchTransactions(search, true);
			
			int totalTrn = 0;
			int totalInTrn = 0;
			int totalOutTrn = 0;
			BigDecimal totalAmount = new BigDecimal(0);
			if(CommonValidators.validList(transactions)) {
				for(Transaction t: transactions) {
					totalTrn ++;
					if(InOutBounds.IN.getCode().equals(t.getInOutStatus())) {
						totalInTrn ++;
					}else if (InOutBounds.OUT.getCode().equals(t.getInOutStatus())) {
						totalOutTrn ++;
					}
					totalAmount = totalAmount.add(t.getCost());
				}
			}
			
			summaryDTO.setTotalAmount(totalAmount);
			summaryDTO.setTotalAmountDesc(CommonUtil.formatNumber(totalAmount));
			summaryDTO.setTotalTransaction(totalTrn);
			summaryDTO.setTotalInTransaction(totalInTrn);
			summaryDTO.setTotalOutTransaction(totalOutTrn);
			
			return summaryDTO;
			
		}else {
			UserShiftSummary summary = this.shiftSummaryDAO.getUserShiftDetailByShiftId(shiftId);
			if(CommonValidators.isValidObject(summary)) {
				return new UserShiftSummaryDTO(summary);
			}
		}
		return new UserShiftSummaryDTO();
	}

}
