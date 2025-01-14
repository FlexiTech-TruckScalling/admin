<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">Driver Manage</h6>
		</div>
		<div class="card-body">

			<form:form action="driver-manage.fxt" modelAttribute="driverDTO"
				method="post" id="common-form">
				<form:hidden path="id" />
				<div class="row g-3">
					<!-- Name -->
					<div class="col-md-4">
						<form:label path="name" class="form-label">Name <strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="name" type="text" class="form-control required"
							placeholder="Driver name" />
						<span class="text-danger input-error-msg" data-label="Name"></span>
					</div>

					<!-- IP -->
					<div class="col-md-4">
						<form:label path="phoneNumber" class="form-label">Phone Number <strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="phoneNumber" type="text"
							class="form-control required" placeholder="Phone number" />
						<span class="text-danger input-error-msg" data-label="Phone number"></span>
					</div>

					<div class="col-md-4">
						<form:label path="status" class="form-label">Status <strong
								class="text-danger">*</strong>
						</form:label>
						<form:select path="status"
							class="form-control border bg-white selectpicker required">
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
						<button type="submit" class="btn btn-primary">${empty driverDTO.id ? "Save":"Update" }</button>
						<a href="driver-manage.fxt" class="btn btn-secondary">Clear</a>
						<c:if test="${not empty driverDTO.id }">
								<button type="button" data-action="driver-delete.fxt"
									data-id="${driverDTO.id }" class="btn btn-danger delete-data">Delete</button>
							</c:if>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="card mb-4">
		<div class="card-header py-3">
			<span class="m-0 font-weight-bold text-secondary">Total
				Drivers</span>
		</div>
		<div class="card-body">
			<table class="table table-striped">
				<thead class="bg-gradient-primary text-white">
					<tr>
						<th class="py-1" scope="col" style="width: 50px;">#</th>
						<th class="py-1" scope="col">Name</th>
						<th class="py-1" scope="col">Phone No.</th>
						<th class="py-1" scope="col">Status</th>
						<th class="py-1" scope="col">Created Time</th>
						<th class="py-1" scope="col" style="width: 140px;">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty driverList }">
						<tr>
							<td colspan="6" class="text-center">No drivers.</td>
						</tr>
					</c:if>
					<c:if test="${not empty driverList }">
						<c:forEach items="${driverList }" var="counter" varStatus="loop">
							<tr
								class="${not empty new_id && new_id == counter.id || driverDTO.id == counter.id ? 'font-weight-bold text-info':'' }">
								<td>${loop.index + 1 }</td>
								<td>${counter.name }</td>
								<td>${counter.phoneNumber }</td>
								<td><span
									class="badge ${counter.status == 1 ? 'badge-info':'badge-danger' }">
										${counter.statusDesc } </span></td>
								<td>${counter.createdTimeDesc }</td>
								<td>
									<a href="?id=${counter.id }"><i class="fas fa-fw fa-edit"></i>Edit</a>
								</td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
</div>