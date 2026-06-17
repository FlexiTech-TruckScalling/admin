<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
	<div class="card shadow mb-4">
		<!-- <h6 class="m-0 font-weight-bold text-primary">Role Manage</h6>
 -->
		<a href="#collapseCard" class="d-block card-header py-3"
			data-toggle="collapse" role="button" aria-expanded="true"
			aria-controls="collapseCardExample">
			<h6 class="m-0 font-weight-bold text-primary">Subscription Type</h6>
		</a>
		<div class="collapse show" id="collapseCard">
			<div class="card-body">

				<form:form action="subtype-manage.fxt" modelAttribute="subTypeDTO"
					method="post" id="common-form">
					<form:hidden path="id" />
					<div class="row g-3">
						<!-- Code -->
						<div class="col-md-4">
							<form:label path="subscriptionName" class="form-label">Subscription Name <strong
									class="text-danger">*</strong>
							</form:label>
							<form:input path="subscriptionName" type="text"
								class="form-control required" placeholder="Subscription Name" />
							<span class="text-danger input-error-msg"
								data-label="subscriptionName"></span>
						</div>

						<!-- Name -->
						<div class="col-md-4">
							<form:label path="period" class="form-label">Period <strong
									class="text-danger">*</strong>
							</form:label>
							<form:input path="period" type="text" class="form-control required"
								placeholder="Period" />
							<span class="text-danger input-error-msg" data-label="period"></span>
						</div>
					</div>
					<div class="row mt-3">
						<!-- Description -->
						<div class="col-md-4">
							<form:label path="period" class="form-label">Period</form:label>
							<form:textarea path="period" type="text" class="form-control"
								placeholder="Period..." />
							<span class="text-danger input-error-msg" data-label="IP address"></span>
						</div>



					</div>

					<!-- Submit Button -->
					<div class="row mt-4">
						<div class="col text-left">
							<button type="submit" class="btn btn-primary">${empty subTypeDTO.id ? "Save":"Update" }</button>
							<a href="role-manage.fxt" class="btn btn-secondary">Clear</a>
							<c:if test="${not empty subTypeDTO.id }">
								<button type="button" data-action="role-delete.fxt"
									data-id="${subTypeDTO.id }" class="btn btn-danger delete-data">Delete</button>
							</c:if>
						</div>
					</div>
				</form:form>
			</div>
		</div>
	</div>
</div>