<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
	<div class="card shadow mb-4">
		<!-- <h6 class="m-0 font-weight-bold text-primary">Role Manage</h6>
 -->
		<a href="#collapseCard" class="d-block card-header py-3"
			data-toggle="collapse" role="button" aria-expanded="true"
			aria-controls="collapseCardExample">
			<h6 class="m-0 font-weight-bold text-primary">Role Manage</h6>
		</a>
		<div class="collapse show" id="collapseCard">
			<div class="card-body">

				<form:form action="role-manage.fxt" modelAttribute="roleDTO"
					method="post" id="common-form">
					<form:hidden path="id" />
					<div class="row g-3">
						<!-- Code -->
						<div class="col-md-4">
							<form:label path="name" class="form-label">Code <strong
									class="text-danger">*</strong>
							</form:label>
							<form:input path="code" type="text" class="form-control required"
								placeholder="Role code" />
							<span class="text-danger input-error-msg" data-label="Code"></span>
						</div>

						<!-- Name -->
						<div class="col-md-4">
							<form:label path="name" class="form-label">Name <strong
									class="text-danger">*</strong>
							</form:label>
							<form:input path="name" type="text" class="form-control required"
								placeholder="Role name" />
							<span class="text-danger input-error-msg" data-label="Name"></span>
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
						<!-- Description -->
						<div class="col-md-4">
							<form:label path="description" class="form-label">Description</form:label>
							<form:textarea path="description" type="text"
								class="form-control" placeholder="Description..." />
							<span class="text-danger input-error-msg" data-label="IP address"></span>
						</div>

						<div class="col-md-4 mt-3 d-flex align-items-end">
							<div class="d-flex align-items-center">
								<form:checkbox class="" path="useApp" value="1" />
								<label for="useApp1" class="m-0">Use Desktop Application</label>

							</div>
						</div>

					</div>

					<!-- Submit Button -->
					<div class="row mt-4">
						<div class="col text-left">
							<button type="submit" class="btn btn-primary">${empty roleDTO.id ? "Save":"Update" }</button>
							<a href="role-manage.fxt" class="btn btn-secondary">Clear</a>
							<c:if test="${not empty roleDTO.id }">
								<button type="button" data-action="role-delete.fxt"
									data-id="${roleDTO.id }" class="btn btn-danger delete-data">Delete</button>
							</c:if>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>

	<div class="card mb-4">
		<div class="card-header py-3">
			<span class="m-0 font-weight-bold text-secondary">Total User
				Roles</span>
		</div>
		<div class="card-body">
			<table class="table table-striped">
				<thead class="bg-gradient-primary text-white">
					<tr>
						<th class="py-1" scope="col" style="width: 50px;">#</th>
						<th class="py-1" scope="col">Code</th>
						<th class="py-1" scope="col">Name</th>
						<th class="py-1" scope="col">Description</th>
						<th class="py-1" scope="col">Status</th>
						<th class="py-1" scope="col">Created Time</th>
						<th class="py-1" scope="col" style="width: 140px;">Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:if test="${empty roleList }">
						<tr>
							<td colspan="6" class="text-center">No user role.</td>
						</tr>
					</c:if>
					<c:if test="${not empty roleList }">
						<c:forEach items="${roleList }" var="counter" varStatus="loop">
							<tr
								class="${not empty new_id && new_id == counter.id || roleDTO.id == counter.id ? 'font-weight-bold text-info':'' }">
								<td>${loop.index + 1 }</td>
								<td>${counter.code }</td>
								<td>${counter.name }</td>
								<td>${counter.description }</td>
								<td><span
									class="badge ${counter.status == 1 ? 'badge-info':'badge-danger' }">
										${counter.statusDesc } </span></td>
								<td>${counter.createdTimeDesc }</td>
								<td><a href="?id=${counter.id }"><i
										class="fas fa-fw fa-edit"></i>Edit</a></td>
							</tr>
						</c:forEach>
					</c:if>
				</tbody>
			</table>
		</div>
	</div>
</div>