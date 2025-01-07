<div class="">
	<div class="card shadow mb-4">
		<div class="card-header py-3">
			<h6 class="m-0 font-weight-bold text-primary">User Manage</h6>
		</div>
		<div class="card-body">

			<form action="" method="post" id="common-form">
				<div class="row g-3">
					<!-- Login Name -->
					<div class="col-md-4">
						<label for="loginName" class="form-label">Login Name <strong
							class="text-danger">*</strong></label> <input type="text"
							class="form-control required" id="loginName" name="loginName">
						<span class="text-danger input-error-msg" data-label="Login Name"></span>
					</div>

					<!-- Name -->
					<div class="col-md-4">
						<label for="name" class="form-label">Name <strong
							class="text-danger">*</strong></label> <input type="text"
							class="form-control required" id="name" name="name"> <span
							class="text-danger input-error-msg" data-label="Name"></span>
					</div>

					<!-- Phone Number -->
					<div class="col-md-4">
						<label for="phoneNo" class="form-label">Phone Number <strong
							class="text-danger">*</strong></label> <input type="text"
							class="form-control required" id="phoneNo" name="phoneNo">
						<span class="text-danger input-error-msg"  data-label="Phone Number"></span>
					</div>

					<!-- Password -->
					<div class="col-md-4">
						<label for="password" class="form-label">Password <strong
							class="text-danger">*</strong></label> <input type="password"
							class="form-control required" id="password" name="password">
						<span class="text-danger input-error-msg"  data-label="Password"></span>
					</div>

					<!-- User Role -->
					<div class="col-md-4">
						<label for="userRole" class="form-label">User Role <strong
							class="text-danger">*</strong></label> <select
							class="form-control border bg-white selectpicker required"
							id="userRole" name="userRole">
							<option value="-1" disabled selected>Select User Role</option>
							<!-- Populate with roles from backend -->
							<option value="1">Admin</option>
							<option value="2">Operator</option>
						</select>
						<span class="text-danger input-error-msg"  data-label="User Role"></span>
					</div>

					<!-- Counter -->
					<div class="col-md-4">
						<label for="counter" class="form-label">Counter</label> <select
							class="form-control border bg-white selectpicker" id="counter"
							name="counter">
							<option value="-1" disabled selected>Select Counter</option>
							<!-- Populate with counters from backend -->
							<option value="1">Counter 1</option>
							<option value="2">Counter 2</option>
						</select>
					</div>

					<div class="col-md-4">
						<label for="status" class="form-label">Status <strong
							class="text-danger">*</strong></label> <select
							class="form-control border bg-white selectpicker required"
							id="status" name="status">
							<option value="-1" disabled selected>Select Status</option>
							<!-- Populate with counters from backend -->
							<option value="1">Active</option>
							<option value="2">Inactive</option>
						</select>
						<span class="text-danger input-error-msg"  data-label="Status"></span>
					</div>
				</div>

				<!-- Submit Button -->
				<div class="row mt-4">
					<div class="col text-left">
						<button type="submit" class="btn btn-primary">Save User</button>
					</div>
				</div>
			</form>
		</div>
	</div>

</div>
