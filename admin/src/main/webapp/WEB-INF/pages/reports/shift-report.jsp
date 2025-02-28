<%@ include file="../../includes/import-tags.jsp"%>
<style>
</style>
<div class="">
    <div class="card shadow mb-4">
        <div class="card-header py-3">
            <h6 class="m-0 font-weight-bold text-primary">User Shift Search</h6>
        </div>
        <div class="card-body">
            <form:form action="shift-report.fxt" modelAttribute="searchDTO" 
                method="post" id="common-form">
                <form:hidden path="pageNo" />
                <div class="row g-3">
                    <!-- Username -->
                    <div class="col-md-3">
                        <form:label path="username" class="form-label">Username</form:label>
                        <form:input path="username" type="text" class="form-control" 
                            placeholder="Enter username" />
                    </div>

                    <!-- Start Date -->
                    <div class="col-md-3">
                        <form:label path="startTime" class="form-label">Start Date</form:label>
                        <form:input path="startTime" type="date" class="form-control" />
                    </div>

                    <!-- End Date -->
                    <div class="col-md-3">
                        <form:label path="endTime" class="form-label">End Date</form:label>
                        <form:input path="endTime" type="date" class="form-control" />
                    </div>
                </div>

                <!-- Submit Button -->
                <div class="row mt-4">
                    <div class="col text-left">
                        <button type="submit" id="search-btn" class="btn btn-primary">Search</button>
                        <a href="shift-report.fxt" class="btn btn-secondary">Clear</a>
                    </div>
                </div>
            </form:form>
        </div>
    </div>

    <div class="card mb-4">
        <div class="card-header py-3">
            <span class="m-0 font-weight-bold text-secondary">User Shift List</span>
        </div>
        <div class="card-body">
            <table class="table table-striped">
                <thead class="bg-gradient-primary text-white">
                    <tr>
                        <th class="py-1">#</th>
                        <th class="py-1">Shift Code</th>
                        <th class="py-1">User</th>
                        <th class="py-1">Start Time</th>
                        <th class="py-1">End Time</th>
                        <th class="py-1">Status</th>
                        <th class="py-1">Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${empty shiftList}">
                        <tr>
                            <td colspan="7" class="text-center">No shifts found.</td>
                        </tr>
                    </c:if>
                    <c:if test="${not empty shiftList}">
                        <c:forEach items="${shiftList}" var="shift" 
                            varStatus="loop">
                            <tr>
                                <td>${(searchDTO.pageNo - 1) * searchDTO.limit + loop.index + 1}</td>
                                <td>${shift.code}</td>
                                <td>${shift.user.name}</td>
                                <td>${shift.startTimeDesc}</td>
                                <td>${shift.endTimeDesc}</td>
                                <td>
                                    <span class="badge ${shift.shiftStatus == 1 ? 'badge-success' : 'badge-secondary'}">
                                        ${shift.shiftStatus == 1 ? 'Active' : 'Completed'}
                                    </span>
                                </td>
                                <td>
                                    <a href="shift-detail.fxt?shiftId=${shift.id}" 
                                        class="btn btn-sm btn-info">
                                        <i class="fas fa-eye"></i>
                                    </a>
                                </td>
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