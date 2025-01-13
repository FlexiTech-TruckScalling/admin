<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">Customer Search</h6>
		</div>
		<div class="card-body">

			<form:form action="customer-search.fxt" modelAttribute="searchDTO"
				method="post" id="common-form">
				<form:hidden path="pageNo" />
				<div class="row g-3">
					<!-- Name -->
					<div class="col-md-4">
						<form:label path="name" class="form-label">Name 
						</form:label>
						<form:input path="name" type="text" class="form-control"
							placeholder="Customer name" />
						<span class="text-danger input-error-msg"
							data-label="Customer Name"></span>
					</div>

					<!-- Code -->
					<div class="col-md-4">
						<form:label path="code" class="form-label">Code
						</form:label>
						<form:input path="code" type="text" class="form-control"
							placeholder="Customer Code" />
						<span class="text-danger input-error-msg"
							data-label="Customer Code"></span>
					</div>

					<!-- Phone number -->
					<div class="col-md-4">
						<form:label path="phoneNumber" class="form-label">Phone Number
						</form:label>
						<form:input path="phoneNumber" type="text" class="form-control"
							placeholder="Phone No." />
						<span class="text-danger input-error-msg"
							data-label="Customer Phone No."></span>
					</div>

				</div>

				<div class="row g-3 mt-3">

					<div class="col-md-4">
						<form:label path="customerType" class="form-label">Customer Type
						</form:label>
						<form:select path="customerType"
							class="form-control border bg-white selectpicker">
							<option value="-1" selected>-- Select One --</option>
							<form:options items="${customerTypeList }" itemValue="id"
								itemLabel="name" />
						</form:select>
					</div>

					<div class="col-md-4">
						<form:label path="status" class="form-label">Status
						</form:label>
						<form:select path="status"
							class="form-control border bg-white selectpicker">
							<option value="-1" disabled selected>-- Select One --</option>
							<form:options items="${statusList }" itemValue="code"
								itemLabel="desc" />
						</form:select>
						<span class="text-danger input-error-msg" data-label="Status"></span>
					</div>
				</div>

				<!-- Submit Button -->
				<div class="row mt-4">
					<div class="col text-left">
						<button type="submit" id="search-btn" class="btn btn-primary">Search</button>
						<a href="customer-search.fxt" class="btn btn-secondary">Clear</a>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="card mb-4">
		<div class="card-header py-3">
			<span class="m-0 font-weight-bold text-secondary">Total
				Customers</span>
		</div>
		<div class="card-body">
			<table class="table table-striped">
				<thead class="bg-gradient-primary text-white">
					<tr>
						<th class="py-1" scope="col" style="width: 50px;">#</th>
						<th class="py-1" scope="col">Name</th>
						<th class="py-1" scope="col">Code</th>
						<th class="py-1" scope="col">Phone Number</th>
						<th class="py-1" scope="col">Customer Type</th>
						<th class="py-1" scope="col">Address</th>
						<th class="py-1" scope="col">Status</th>
						<th class="py-1" scope="col">Created Time</th>
						<th class="py-1" scope="col" style="width: 140px;">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty customerList }">
						<tr>
							<td colspan="9" class="text-center">No customers.</td>
						</tr>
					</c:if>
					<c:if test="${not empty customerList }">
						<c:forEach items="${customerList }" var="counter" varStatus="loop">
							<tr>
								<td>${loop.index + 1 }</td>
								<td>${counter.name }</td>
								<td>${counter.code }</td>
								<td>${counter.phoneNumber }</td>
								<td>${counter.customerTypeNames }</td>
								<td>${counter.address }</td>
								<td><span
									class="badge ${counter.status == 1 ? 'badge-info':'badge-danger' }">
										${counter.statusDesc } </span></td>
								<td>${counter.createdTimeDesc }</td>
								<td><a href="customer-manage.fxt?id=${counter.id }"><i
										class="fas fa-fw fa-edit"></i>Edit</a></td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
			<div class="row mt-3">
				<div class="pagination mr-3 ml-auto">
					<!-- Pagination content goes here -->
				</div>
			</div>

		</div>
	</div>
</div>
<%@ include file="../../includes/pagination-template.jsp"%>
<script>
	var pageCount = "${searchDTO.pageCount}";
	var page = "${searchDTO.pageNo}";
	console.log(pageCount + "<<>>" + page);
	$('.pagination').twbsPagination({
		totalPages : pageCount,
		visiblePages : 5,
		next : 'Next',
		prev : 'Prev',
		startPage : parseInt(page), // active page
		initiateStartPageClick : false,
		onPageClick : function(event, page) {
			var pageNo = document.getElementById('pageNo');
			pageNo.value = page;
			$('#common-form').submit();
		}
	});
	$("#search-btn").on("click", function(){
		var page = document.getElementById("pageNo");
		if(page){
			$('#pageNo').val(1);
		}
	})
</script>