<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
	<div class="card shadow mb-4">
		<a href="#collapseCard" class="d-block card-header py-3"
			data-toggle="collapse" role="button" aria-expanded="true"
			aria-controls="collapseCardExample">
			<h6 class="m-0 font-weight-bold text-primary">User Manage</h6>
		</a>
		<div class="collapse ${empty userDTO.id?'':'show' }" id="collapseCard">
			<div class="card-body">

				<form:form action="user-manage.fxt" modelAttribute="userDTO"
					method="post" id="common-form">
					<form:hidden path="id" />
					<div class="row g-3">
						<!-- Login Name -->
						<div class="col-md-4">
							<form:label path="loginName" class="form-label">Login Name <strong
									class="text-danger">*</strong>
							</form:label>
							<form:input type="text" class="form-control required"
								path="loginName" />
							<span class="text-danger input-error-msg" data-label="Login Name"></span>
						</div>

						<!-- Name -->
						<div class="col-md-4">
							<form:label path="name" class="form-label">Name <strong
									class="text-danger">*</strong>
							</form:label>
							<form:input type="text" class="form-control required" id="name"
								path="name" />
							<span class="text-danger input-error-msg" data-label="Name"></span>
						</div>

						<!-- Phone Number -->
						<div class="col-md-4">
							<form:label path="phoneNo" class="form-label">Phone Number <strong
									class="text-danger">*</strong>
							</form:label>
							<form:input type="text" class="form-control required"
								id="phoneNo" path="phoneNo" />
							<span class="text-danger input-error-msg"
								data-label="Phone Number"></span>
						</div>

						<!-- Password -->
						<div class="col-md-4">
							<form:label path="password" class="form-label">Password <strong
									class="text-danger">*</strong>
							</form:label>
							<form:input type="password" class="form-control required"
								id="password" path="password" />
							<span class="text-danger input-error-msg" data-label="Password"></span>
						</div>

						<!-- User Role -->
						<div class="col-md-4">
							<form:label path="userRoleId" class="form-label">User Role <strong
									class="text-danger">*</strong>
							</form:label>
							<form:select
								class="form-control border bg-white selectpicker required"
								id="userRole" path="userRoleId">
								<option value="-1" disabled selected>Select User Role</option>
								<form:options items="${userRoleList }" itemLabel="name"
									itemValue="id" />
							</form:select>
							<span class="text-danger input-error-msg" data-label="User Role"></span>
						</div>

						<!-- Counter -->
						<div class="col-md-4">
							<form:label path="counterId" class="form-label">Counter</form:label>
							<form:select class="form-control border bg-white selectpicker"
								id="counterId" path="counterId">
								<option value="-1" selected>Select Counter</option>
								<!-- Populate with counters from backend -->
								<form:options items="${counterList }" itemLabel="name"
									itemValue="id" />
							</form:select>
						</div>

						<div class="col-md-4">
							<form:label path="status" class="form-label">Status <strong
									class="text-danger">*</strong>
							</form:label>
							<form:select
								class="form-control border bg-white selectpicker required"
								id="status" path="status">
								<option value="-1" disabled selected>Select Status</option>
								<form:options items="${statusList }" itemLabel="desc"
									itemValue="code" />
							</form:select>
							<span class="text-danger input-error-msg" data-label="Status"></span>
						</div>
					</div>

					<!-- Submit Button -->
					<div class="row mt-4">
						<div class="col text-left">
							<button type="submit" class="btn btn-primary">${empty userDTO.id ? "Save":"Update"}</button>
							<a href="user-manage.fxt" class="btn btn-secondary">Clear</a>
							<c:if test="${not empty userDTO.id }">
								<button type="button" data-action="user-delete.fxt"
									data-id="${userDTO.id }" class="btn btn-danger delete-data">Delete</button>
							</c:if>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>

	<div class="card mb-4">
		<div class="card-header py-3">
			<span class="m-0 font-weight-bold text-secondary">Total Users</span>
		</div>
		<div class="card-body ">
			<div class=" table-responsive">
				<table class="table table-striped">
					<thead class="bg-gradient-primary text-white">
						<tr>
							<th class="py-1" scope="col" style="width: 50px;">#</th>
							<th class="py-1" scope="col">Login Name</th>
							<th class="py-1" scope="col">Name</th>
							<th class="py-1" scope="col">Phone Number</th>
							<th class="py-1" scope="col">Role</th>
							<th class="py-1" scope="col">Counter</th>
							<th class="py-1" scope="col">Status</th>
							<th class="py-1" scope="col">Created Time</th>
							<th class="py-1" scope="col" style="width: 140px;">Actions</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${empty userList }">
							<tr>
								<td colspan="8" class="text-center">No user role.</td>
							</tr>
						</c:if>
						<c:if test="${not empty userList }">
							<c:forEach items="${userList }" var="counter" varStatus="loop">
								<tr
									class="${not empty new_id && new_id == counter.id || roleDTO.id == counter.id ? 'font-weight-bold text-info':'' }">
									<td>${loop.index + 1 }</td>
									<td>${counter.loginName }</td>
									<td>${counter.name }</td>
									<td>${counter.phoneNo }</td>
									<td>${counter.userRoleDTO.name }</td>
									<td>${counter.counterDTO.name }</td>
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

</div>
