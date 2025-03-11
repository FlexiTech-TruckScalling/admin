<%@ include file="../../includes/import-tags.jsp"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="en_US" />
<div class="container-fluid">
	<!-- Page Heading -->
	<div class="d-sm-flex align-items-center justify-content-between mb-4">
		<h1 class="h3 mb-0 text-gray-800">Transaction Details</h1>
		<a href="transaction-report.fxt"
			class="d-none d-sm-inline-block btn btn-secondary shadow-sm"> <i
			class="fas fa-arrow-left fa-sm"></i> Back to Transactions
		</a>
	</div>

	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">Transaction
				#${transactionDTO.transactionCode}</h6>
		</div>
		<div class="card-body">
			<div class="row">
				<!-- Left Column -->
				<div class="col-md-6">
					<!-- Transaction Details -->
					<div class="card mb-4">
						<div class="card-header">
							<h6 class="m-0 font-weight-bold text-primary">Transaction
								Details</h6>
						</div>
						<div class="card-body">
							<dl class="row">
								<dt class="col-sm-4">Status:</dt>
								<dd class="col-sm-8">
									<span
										class="badge ${transactionDTO.inOutStatus == 1 ? 'badge-success' : 'badge-warning'}">
										${transactionDTO.inOutStatus == 1 ? 'IN' : 'OUT'} </span>
								</dd>

								<dt class="col-sm-4">Transaction Type:</dt>
								<dd class="col-sm-8">${transactionDTO.transactionType}</dd>

								<dt class="col-sm-4">In Time:</dt>
								<dd class="col-sm-8">${transactionDTO.inTimeStr}</dd>

								<dt class="col-sm-4">Out Time:</dt>
								<dd class="col-sm-8">${transactionDTO.outTimeStr}</dd>

								<dt class="col-sm-4">Weight:</dt>
								<dd class="col-sm-8">${transactionDTO.weightDesc}</dd>

								<dt class="col-sm-4">Cargo Weight:</dt>
								<dd class="col-sm-8">${transactionDTO.cargoWeightDesc}</dd>
							</dl>
						</div>
					</div>

					<!-- Customer Information -->
					<div class="card mb-4">
						<div class="card-header">
							<h6 class="m-0 font-weight-bold text-primary">Customer
								Information</h6>
						</div>
						<div class="card-body">
							<dl class="row">
								<dt class="col-sm-4">Customer:</dt>
								<dd class="col-sm-8">${transactionDTO.customerDTO.name}</dd>

								<dt class="col-sm-4">Customer Type:</dt>
								<dd class="col-sm-8">${transactionDTO.customerTypeDTO.name}</dd>

								<dt class="col-sm-4">Payment Type:</dt>
								<dd class="col-sm-8">
									<c:if test="${not empty transactionDTO.paymentTypeDTO}">
                                        ${transactionDTO.paymentTypeDTO.name}
                                    </c:if>
								</dd>
							</dl>
						</div>
					</div>
				</div>

				<!-- Right Column -->
				<div class="col-md-6">
					<!-- Vehicle Details -->
					<div class="card mb-4">
						<div class="card-header">
							<h6 class="m-0 font-weight-bold text-primary">Vehicle
								Details</h6>
						</div>
						<div class="card-body">
							<dl class="row">
								<dt class="col-sm-4">Vehicle:</dt>
								<dd class="col-sm-8">${transactionDTO.vehicleDTO.prefix}
									${transactionDTO.vehicleDTO.number}</dd>

								<dt class="col-sm-4">Driver:</dt>
								<dd class="col-sm-8">${transactionDTO.driverName}</dd>

								<dt class="col-sm-4">Container Number:</dt>
								<dd class="col-sm-8">${transactionDTO.containerNumber}</dd>

								<dt class="col-sm-4">Container Size:</dt>
								<dd class="col-sm-8">${transactionDTO.containerSize}</dd>
							</dl>
						</div>
					</div>

					<!-- Goods and Payment -->
					<div class="card mb-4">
						<div class="card-header">
							<h6 class="m-0 font-weight-bold text-primary">Goods &
								Payment</h6>
						</div>
						<div class="card-body">
							<dl class="row">
								<dt class="col-sm-4">Goods:</dt>
								<dd class="col-sm-8">${transactionDTO.goodDTO.name}</dd>

								<dt class="col-sm-4">Product:</dt>
								<dd class="col-sm-8">
									<c:if test="${not empty transactionDTO.productDTO}">
                                        ${transactionDTO.productDTO.name}
                                    </c:if>
								</dd>

								<dt class="col-sm-4">Quantity:</dt>
								<dd class="col-sm-8">${transactionDTO.quantity}</dd>

								<dt class="col-sm-4">Cost:</dt>
								<dd class="col-sm-8">
									<fmt:formatNumber value="${transactionDTO.cost}" pattern="#,##0 ' Ks'" />
								</dd>
							</dl>
						</div>
					</div>
				</div>
			</div>

			<!-- Vehicle Photos -->
			<div class="card mb-4">
				<div class="card-header">
					<h6 class="m-0 font-weight-bold text-primary">Vehicle Photos</h6>
				</div>
				<div class="card-body">
					<div class="row">
						<div class="col-md-6 text-center">
							<c:choose>
								<c:when test="${not empty transactionDTO.vehiclePhotoOne}">
									<img
										src="/images/${transactionDTO.vehiclePhotoOne}"
										class="img-fluid rounded" alt="Vehicle Photo 1"
										style="max-height: 400px;">
								</c:when>
								<c:otherwise>
									<div class="alert alert-warning">No photo available</div>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="col-md-6 text-center">
							<c:choose>
								<c:when test="${not empty transactionDTO.vehiclePhotoTwo}">
									<img
										src="/images/${transactionDTO.vehiclePhotoTwo}"
										class="img-fluid rounded" alt="Vehicle Photo 2"
										style="max-height: 400px;">
								</c:when>
								<c:otherwise>
									<div class="alert alert-warning">No photo available</div>
								</c:otherwise>
							</c:choose>
						</div>
					</div>
				</div>
			</div>

			<!-- System Information -->
			<div class="card">
				<div class="card-header">
					<h6 class="m-0 font-weight-bold text-primary">System
						Information</h6>
				</div>
				<div class="card-body">
					<dl class="row">
						<dt class="col-sm-4">Created By:</dt>
						<dd class="col-sm-8">${transactionDTO.userDTO.name}</dd>

						<dt class="col-sm-4">Session Code:</dt>
						<dd class="col-sm-8">${transactionDTO.sessionCode}</dd>

						<dt class="col-sm-4">Transaction Status:</dt>
						<dd class="col-sm-8">
							<c:choose>
								<c:when test="${transactionDTO.transactionStatus == 1}">
									<span class="badge badge-success">Completed</span>
								</c:when>
								<c:when test="${transactionDTO.transactionStatus == 2}">
									<span class="badge badge-warning">Incompleted</span>
								</c:when>
								<c:when test="${transactionDTO.transactionStatus == 3}">
									<span class="badge badge-danger">Cancelled</span>
								</c:when>
								<c:otherwise>
									<span class="badge badge-secondary">Unknown Status</span>
								</c:otherwise>
							</c:choose>
						</dd>
					</dl>
				</div>
			</div>
		</div>
	</div>
</div>