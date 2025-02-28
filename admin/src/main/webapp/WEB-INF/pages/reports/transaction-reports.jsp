<%@ include file="../../includes/import-tags.jsp"%>
<style>
</style>
<div class="">
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">Transaction Search</h6>
		</div>
		<div class="card-body">
			<form:form action="transaction-report.fxt" modelAttribute="searchDTO"
				method="post" id="common-form">
				<form:hidden path="pageNo" />
				<div class="row g-3">
					<!-- Customer Name -->
					<div class="col-md-3">
						<form:label path="customerName" class="form-label">Customer Name</form:label>
						<form:input path="customerName" type="text" class="form-control"
							placeholder="Customer name" />
					</div>

					<!-- Goods -->
					<div class="col-md-3">
						<form:label path="goodId" class="form-label">Goods</form:label>
						<form:select path="goodId"
							class="form-control border bg-white selectpicker">
							<option value="-1">-- Select Goods --</option>
							<form:options items="${goodList}" itemValue="id" itemLabel="name" />
						</form:select>
					</div>

					<!-- Product -->
					<div class="col-md-3">
						<form:label path="productId" class="form-label">Product</form:label>
						<form:select path="productId"
							class="form-control border bg-white selectpicker">
							<option value="-1">-- Select Product --</option>
							<form:options items="${productList}" itemValue="id"
								itemLabel="name" />
						</form:select>
					</div>

					<!-- Product -->
					<div class="col-md-3">
						<form:label path="paymentTypeId" class="form-label">Payment Type</form:label>
						<form:select path="paymentTypeId"
							class="form-control border bg-white selectpicker">
							<option value="-1">-- Select Payment Type --</option>
							<form:options items="${paymentTypeList}" itemValue="id"
								itemLabel="name" />
						</form:select>
					</div>
				</div>

				<div class="row g-3 mt-3">
					<!-- Vehicle -->
					<div class="col-md-3">
						<form:label path="vehiclePrefix" class="form-label">Vehicle Prefix</form:label>
						<form:input path="vehiclePrefix" type="text" class="form-control"
							placeholder="Vehicle prefix" />
					</div>

					<div class="col-md-3">
						<form:label path="vehicleNumber" class="form-label">Vehicle Number</form:label>
						<form:input path="vehicleNumber" type="text" class="form-control"
							placeholder="Vehicle number" />
					</div>

					<!-- Driver Name -->
					<div class="col-md-3">
						<form:label path="driverName" class="form-label">Driver Name</form:label>
						<form:input path="driverName" type="text" class="form-control"
							placeholder="Driver name" />
					</div>

					<!-- Weight Range -->
					<div class="col-md-3">
						<form:label path="fromWeight" class="form-label">Weight Range</form:label>
						<div class="input-group">
							<form:input path="fromWeight" type="number" class="form-control"
								placeholder="From" />
							<div class="input-group-append">
								<form:select path="mathSign">
									<form:options items="${mathSignList }" itemValue="code"
										itemLabel="desc" />
								</form:select>
							</div>
						</div>
					</div>
				</div>

				<div class="row g-3 mt-3">
					<!-- Dates -->
					<div class="col-md-3">
						<form:label path="createdFromDate" class="form-label">From Date</form:label>
						<form:input path="createdFromDate" type="date"
							class="form-control" />
					</div>

					<div class="col-md-3">
						<form:label path="createdToDate" class="form-label">To Date</form:label>
						<form:input path="createdToDate" type="date" class="form-control" />
					</div>

					<!-- Status -->
					<div class="col-md-3">
						<form:label path="inOutStatus" class="form-label">In/Out Status</form:label>
						<form:select path="inOutStatus"
							class="form-control border bg-white selectpicker">
							<option value="-1">-- Select Status --</option>
							<form:options items="${inOutStatusList}" itemLabel="desc" itemValue="code" />
						</form:select>
					</div>

					<!-- Transaction Code -->
					<div class="col-md-3">
						<form:label path="transctionCode" class="form-label">Transaction Code</form:label>
						<form:input path="transctionCode" type="text" class="form-control"
							placeholder="Transaction code" />
					</div>
				</div>

				<!-- Submit Button -->
				<div class="row mt-4">
					<div class="col text-left">
						<button type="submit" id="search-btn" class="btn btn-primary">Search</button>
						<a href="transaction-report.fxt" class="btn btn-secondary">Clear</a>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="card mb-4">
		<div class="card-header py-3">
			<span class="m-0 font-weight-bold text-secondary">Transaction
				List</span>
		</div>
		<div class="card-body">
			<table class="table table-striped">
				<thead class="bg-gradient-primary text-white">
					<tr>
						<th class="py-1">#</th>
						<th class="py-1">Transaction Code</th>
						<th class="py-1">Customer</th>
						<th class="py-1">Goods</th>
						<th class="py-1">Product</th>
						<th class="py-1">Vehicle</th>
						<th class="py-1">Driver</th>
						<th class="py-1">Weight</th>
						<th class="py-1" style="width: 120px;">Car + Cargo Weight</th>
						<th class="py-1">Status</th>
						<th class="py-1">Date/Time</th>
						<th class="py-1">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty transactionList}">
						<tr>
							<td colspan="11" class="text-center">No transactions found.</td>
						</tr>
					</c:if>
					<c:if test="${not empty transactionList}">
						<c:forEach items="${transactionList}" var="transaction"
							varStatus="loop">
							<tr>
								<td>${(searchDTO.pageNo - 1) * searchDTO.limit + loop.index + 1}</td>
								<td>${transaction.transactionCode}</td>
								<td>${transaction.customerDTO.name}</td>
								<td>${transaction.goodDTO.name}</td>
								<td>${transaction.productDTO.name}</td>
								<td>${transaction.vehicleDTO.prefix}
									${transaction.vehicleDTO.number}</td>
								<td>${transaction.driverName}</td>
								<td>${transaction.weight}</td>
								<td>${transaction.cargoWeight}</td>
								<td><span
									class="badge ${transaction.inOutStatus == 1 ? 'badge-success' : 'badge-warning'}">
										${transaction.inOutStatus == 1 ? 'IN' : 'OUT'} </span></td>
								<td>${transaction.createdTimeDesc }</td>
								<td><a href="transaction-detail.fxt?id=${transaction.id}"
									class="btn btn-sm btn-info"> <i class="fas fa-eye"></i>
								</a> <%-- <a href="transaction-edit.fxt?id=${transaction.id}"
									class="btn btn-sm btn-primary"> <i class="fas fa-edit"></i>
								</a> --%></td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>

			<div class="row mt-3">
				<div class="pagination mr-3 ml-auto">
					<!-- Pagination content -->
				</div>
			</div>
		</div>
	</div>
</div>

<%@ include file="../../includes/pagination-template.jsp"%>
<script>
	var pageCount = parseInt("${searchDTO.pageCount}") || 1;
	var page = parseInt("${searchDTO.pageNo}") || 1;
	
	console.log(pageCount, page)
	
	if (page < 1 || page > pageCount) {
	    page = 1;
	}
	
	console.log("Total Pages: ", pageCount);
	console.log("Start Page: ", page);
	
	$('.pagination').twbsPagination({
	    totalPages: pageCount,
	    visiblePages: 5,
	    next: 'Next',
	    prev: 'Prev',
	    startPage: page,
	    initiateStartPageClick: false,
	    onPageClick: function(event, page) {
	        $('#pageNo').val(page);
	        $('#common-form').submit();
	    }
	});


	$("#search-btn").on("click", function() {
		$('#pageNo').val(1);
	});
</script>