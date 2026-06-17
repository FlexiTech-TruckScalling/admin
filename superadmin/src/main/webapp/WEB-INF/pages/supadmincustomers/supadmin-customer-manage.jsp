<%@ include file="../../includes/import-tags.jsp"%>

<div class="">
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">Super Admin Customer Manage</h6>
		</div>
		<div class="card-body">
			<form:form action="customer-manage.fxt" modelAttribute="supAdminCustDTO"
				method="post" id="common-form">
				<form:hidden path="id" />
				<div class="row g-3">

					<!-- Contact Name -->
					<div class="col-md-4">
						<form:label path="contactName" class="form-label">Contact Name<strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="contactName" type="text"
							class="form-control required" placeholder="Contact Name" />
						<span class="text-danger input-error-msg"
							data-label="Contact Name"></span>
					</div>

					<!-- Phone number -->
					<div class="col-md-4">
						<form:label path="contactPhone" class="form-label">Contact Phone<strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="contactPhone" type="text"
							class="form-control required" placeholder="Phone No." />
						<span class="text-danger input-error-msg"
							data-label="Contact Phone No."></span>
					</div>

					<!-- Contact Email -->
					<div class="col-md-4">
						<form:label path="contactEmail" class="form-label">Contact Email<strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="contactEmail" type="text"
							class="form-control required" placeholder="Email" />
						<span class="text-danger input-error-msg"
							data-label="Contact Email"></span>
					</div>
					
					<!-- DOB -->
					<div class="col-md-4">
						<form:label path="dob" class="form-label">DOB<strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="dob" type="text"
							class="form-control required" placeholder="dob" />
						<span class="text-danger input-error-msg"
							data-label="dob"></span>
					</div>
					
					<div class="col-md-4">
						<form:label path="superAdminLicenseKey" class="form-label">Lincense Key Status <strong
								class="text-danger">*</strong>
						</form:label>
						<form:select
							class="form-control border bg-white selectpicker required"
							id="status" path="superAdminLicenseKey">
							<option value="-1" disabled selected>Select Lincense Key Status</option>
							<form:options items="${licenseKeyStsList }" itemLabel="desc"
								itemValue="code" />
						</form:select>
						<span class="text-danger input-error-msg" data-label="Status"></span>
					</div>
					
					<!-- Expiration -->
					<div class="col-md-4">
						<form:label path="expiration" class="form-label">Expiration<strong
								class="text-danger">*</strong>
						</form:label>
						<form:input path="expiration" type="text"
							class="form-control required" placeholder="dob" />
						<span class="text-danger input-error-msg"
							data-label="dob"></span>
					</div>
					
					<form:hidden path="superAdminLicenseKey" />
					<form:hidden path="key" />
					<form:hidden path="subscriptionTypeId" />

				</div>
				
				<!-- Submit Button -->
				<div class="row mt-4">
					<div class="col text-left">
						<button type="submit" class="btn btn-primary">${empty supAdminCustDTO.id ? "Save":"Update" }</button>
						<a href="customer-manage.fxt" class="btn btn-secondary">Clear</a>
						<c:if test="${not empty supAdminCustDTO.id }">
							<button type="button" data-action="customer-delete.fxt"
								data-id="${supAdminCustDTO.id }" class="btn btn-danger delete-data">Delete</button>
						</c:if>
						<%-- <c:if test="${not empty supAdminCustDTO.id }">
							<a class="btn btn-info" href="${supAdminCustDTO.id }/customer-vehicle-manage.fxt">Add
								Vehicles</a>
						</c:if> --%>
					</div>
				</div>
			</form:form>
		</div>
	</div>
</div>