<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">${customerDTO.name } Vehicle Manage</h6>
		</div>
		<div class="card-body">

			<form:form action="customer-vehicle-manage.fxt" modelAttribute="customerVehicleDTO"
				method="post" id="common-form">
				<form:hidden path="id" />
				<div class="row g-3">
					<!-- Name -->
					<div class="col-md-4">
						<form:label path="number" class="form-label">Number <strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="number" type="text" class="form-control required"
							placeholder="Vehicle Number" />
						<span class="text-danger input-error-msg" data-label="Vehicle Number "></span>
					</div>

					<!-- IP -->
					<div class="col-md-4">
						<form:label path="weight" class="form-label">Weight <strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="weight" type="number" min="0.00" step="any"
							class="form-control required" placeholder="Weight" />
						<span class="text-danger input-error-msg" data-label="Weight"></span>
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
				<div class="row mt-3">
					<div class="col-md-4">
						<form:label path="driverId" class="form-label">Driver
						</form:label>
						<form:select path="driverId"
							class="form-control border bg-white selectpicker">
							<option value="-1" selected>-- Select One --</option>
							<form:options items="${driverList }" itemValue="id"
								itemLabel="name" />
						</form:select>
						<span class="text-danger input-error-msg" data-label="Driver"></span>
					</div>
				</div>

				<!-- Submit Button -->
				<div class="row mt-4">
					<div class="col text-left">
						<button type="submit" class="btn btn-primary">${empty customerVehicleDTO.id ? "Save":"Update" }</button>
						<a href="customer-vehicle-manage.fxt" class="btn btn-secondary">Clear</a>
						<c:if test="${not empty customerVehicleDTO.id }">
								<button type="button" data-action="customer-vehicle-delete.fxt"
									data-id="${customerVehicleDTO.id }" class="btn btn-danger delete-data">Delete</button>
							</c:if>
						<a href='<c:url value="/customer-manage.fxt?id=${customerId }"/>' class="btn btn-secondary">Back</a>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="card mb-4">
		<div class="card-header py-3">
			<span class="m-0 font-weight-bold text-secondary">Total
				Vehicles</span>
		</div>
		<div class="card-body">
			<table class="table table-striped">
				<thead class="bg-gradient-primary text-white">
					<tr>
						<th class="py-1" scope="col" style="width: 50px;">#</th>
						<th class="py-1" scope="col">Number</th>
						<th class="py-1" scope="col">Weight</th>
						<th class="py-1" scope="col">Driver</th>
						<th class="py-1" scope="col">Status</th>
						<th class="py-1" scope="col">Created Time</th>
						<th class="py-1" scope="col" style="width: 140px;">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty customerVehicleList }">
						<tr>
							<td colspan="7" class="text-center">No vehicles.</td>
						</tr>
					</c:if>
					<c:if test="${not empty customerVehicleList }">
						<c:forEach items="${customerVehicleList }" var="counter" varStatus="loop">
							<tr
								class="${not empty new_id && new_id == counter.id || customerVehicleDTO.id == counter.id ? 'font-weight-bold text-info':'' }">
								<td>${loop.index + 1 }</td>
								<td>${counter.number }</td>
								<td>${counter.weight }</td>
								<td>${counter.driver.name }</td>
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