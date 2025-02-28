<%@ include file="../../includes/import-tags.jsp"%>
<style>
    .summary-card {
        border-left: 4px solid #4e73df;
        background-color: #f8f9fc;
    }
    .status-badge {
        font-size: 0.9rem;
        padding: 0.5rem 1rem;
    }
</style>

<div class="container-fluid">
    <!-- User Information Card -->
    <div class="card mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">User Information</h6>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <dl class="row">
                        <dt class="col-sm-4">Full Name:</dt>
                        <dd class="col-sm-8">${userShiftSummary.user.name}</dd>

                        <dt class="col-sm-4">Login Name:</dt>
                        <dd class="col-sm-8">${userShiftSummary.user.loginName}</dd>

                        <dt class="col-sm-4">Phone:</dt>
                        <dd class="col-sm-8">${userShiftSummary.user.phoneNo}</dd>
                    </dl>
                </div>
                <div class="col-md-6">
                    <dl class="row">
                        <dt class="col-sm-4">User Role:</dt>
                        <dd class="col-sm-8">
                            <c:if test="${not empty userShiftSummary.user.userRoleDTO}">
                                ${userShiftSummary.user.userRoleDTO.name}
                            </c:if>
                        </dd>

                        <dt class="col-sm-4">Assigned Counter:</dt>
                        <dd class="col-sm-8">
                            <c:choose>
                                <c:when test="${not empty userShiftSummary.counter}">
                                    ${userShiftSummary.counter.name} (IP: ${userShiftSummary.counter.counterIp})
                                </c:when>
                                <c:otherwise>
                                    N/A
                                </c:otherwise>
                            </c:choose>
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>

    <!-- Shift Details Card -->
    <div class="card mb-4">
        <div class="card-header py-3 d-flex justify-content-between align-items-center">
            <h6 class="m-0 font-weight-bold text-primary">Shift Details</h6>
            <span class="status-badge badge 
                  ${userShiftSummary.userShift.shiftStatus == 1 ? 'badge-success' : 
                   userShiftSummary.userShift.shiftStatus == 2 ? 'badge-warning' : 'badge-danger'}">
                ${userShiftSummary.userShift.shiftStatus == 1 ? 'ACTIVE' : 
                 userShiftSummary.userShift.shiftStatus == 2 ? 'CLOSE' : 'CLOSED'}
            </span>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-6">
                    <dl class="row">
                        <dt class="col-sm-4">Shift Code:</dt>
                        <dd class="col-sm-8">${userShiftSummary.userShift.code}</dd>

                        <dt class="col-sm-4">Start Time:</dt>
                        <dd class="col-sm-8">${userShiftSummary.userShift.startTimeDesc}</dd>
                    </dl>
                </div>
                <div class="col-md-6">
                    <dl class="row">
                        <dt class="col-sm-4">End Time:</dt>
                        <dd class="col-sm-8">
                            <c:choose>
                                <c:when test="${not empty userShiftSummary.userShift.endTimeDesc}">
                                    ${userShiftSummary.userShift.endTimeDesc}
                                </c:when>
                                <c:otherwise>
                                    <span class="text-muted">- Ongoing -</span>
                                </c:otherwise>
                            </c:choose>
                        </dd>

                        <dt class="col-sm-4">Handled By:</dt>
                        <dd class="col-sm-8">
                            ${userShiftSummary.user.name} (${userShiftSummary.user.loginName})
                        </dd>
                    </dl>
                </div>
            </div>
        </div>
    </div>

    <!-- Counter Information Card -->
    <c:if test="${not empty userShiftSummary.counter}">
    <div class="card mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Counter Information</h6>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-12">
                    <dl class="row">
                        <dt class="col-sm-4">Counter Name:</dt>
                        <dd class="col-sm-8">${userShiftSummary.counter.name}</dd>

                        <dt class="col-sm-4">IP Address:</dt>
                        <dd class="col-sm-8">${userShiftSummary.counter.counterIp}</dd>
                    </dl>
                </div>
                <%-- <div class="col-md-6">
                    <dl class="row">
                        <dt class="col-sm-4">Settings Categories:</dt>
                        <dd class="col-sm-8">
                            <c:forEach items="${userShiftSummary.counter.settingCategoryDTOs}" 
                                     var="category" varStatus="loop">
                                ${category.name}<c:if test="${!loop.last}">, </c:if>
                            </c:forEach>
                        </dd>
                    </dl>
                </div> --%>
            </div>
        </div>
    </div>
    </c:if>

    <!-- Transaction Summary Card -->
    <div class="card mb-4 summary-card">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">Transaction Summary</h6>
        </div>
        <div class="card-body">
            <div class="row text-center">
                <div class="col-md-3 mb-4">
                    <div class="h5 mb-0 font-weight-bold text-gray-800">
                        ${userShiftSummary.totalTransaction}
                    </div>
                    <span class="small text-uppercase text-muted">Total Transactions</span>
                </div>

                <div class="col-md-3 mb-4 border-left">
                    <div class="h5 mb-0 font-weight-bold text-success">
                        ${userShiftSummary.totalInTransaction}
                    </div>
                    <span class="small text-uppercase text-muted">IN Transactions</span>
                </div>

                <div class="col-md-3 mb-4 border-left">
                    <div class="h5 mb-0 font-weight-bold text-danger">
                        ${userShiftSummary.totalOutTransaction}
                    </div>
                    <span class="small text-uppercase text-muted">OUT Transactions</span>
                </div>

                <div class="col-md-3 mb-4 border-left">
                    <div class="h5 mb-0 font-weight-bold text-primary">
                        ${userShiftSummary.totalAmountDesc}
                    </div>
                    <span class="small text-uppercase text-muted">Total Amount</span>
                </div>
            </div>
        </div>
    </div>

    <!-- Action Buttons -->
    <div class="row mb-4">
        <div class="col">
            <a href="shift-report.fxt" class="btn btn-secondary">
                <i class="fas fa-arrow-left"></i> Back to List
            </a>
        </div>
    </div>
</div>