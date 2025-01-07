<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">Role Manage</h6>
		</div>
		<div class="card-body">

			<form:form action="counter-manage.fxt" modelAttribute="counterDTO"
				method="post" id="common-form">
				<form:hidden path="id" />
				<div class="row g-3">
					<!-- Name -->
					<div class="col-md-4">
						<form:label path="name" class="form-label">Name <strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="name" type="text" class="form-control required"
							placeholder="Counter name" />
						<span class="text-danger input-error-msg" data-label="Name"></span>
					</div>

					<!-- IP -->
					<div class="col-md-4">
						<form:label path="counterIp" class="form-label">IP Address <strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="counterIp" type="text"
							class="form-control required" placeholder="0.0.0.0" />
						<span class="text-danger input-error-msg" data-label="IP address"></span>
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
						<button type="submit" class="btn btn-primary">${empty counterDTO.id ? "Save":"Update" }</button>
						<a href="counter-manage.fxt" class="btn btn-secondary">Clear</a>
					</div>
				</div>
			</form:form>
		</div>
	</div>

	<div class="card mb-4">
		<div class="card-header py-3">
			<span class="m-0 font-weight-bold text-secondary">Total
				Counters</span>
		</div>
		<div class="card-body">
			<table class="table table-striped">
				<thead class="bg-gradient-primary text-white">
					<tr>
						<th class="py-1" scope="col" style="width: 50px;">#</th>
						<th class="py-1" scope="col">Name</th>
						<th class="py-1" scope="col">IP Address</th>
						<th class="py-1" scope="col">Status</th>
						<th class="py-1" scope="col" style="width: 140px;">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty counterList }">
						<tr>
							<td colspan="5" class="text-center">No counter.</td>
						</tr>
					</c:if>
					<c:if test="${not empty counterList }">
						<c:forEach items="${counterList }" var="counter" varStatus="loop">
							<tr
								class="${not empty new_id && new_id == counter.id || counterDTO.id == counter.id ? 'font-weight-bold text-info':'' }">
								<td>${loop.index + 1 }</td>
								<td>${counter.name }</td>
								<td>${counter.counterIp }</td>
								<td><span
									class="badge ${counter.status == 1 ? 'badge-info':'badge-danger' }">
										${counter.statusDesc } </span></td>
								<td><a href=""><i class="fas fa-fw fa-cog"></i> Setting</a>
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