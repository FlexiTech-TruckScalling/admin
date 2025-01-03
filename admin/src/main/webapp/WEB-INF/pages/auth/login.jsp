<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../includes/import-tags.jsp"%>

<div class="container">

	<!-- Outer Row -->
	<div class="row justify-content-center">

		<div class="mt-5">

			<div class="card o-hidden border-0 shadow-lg my-5">
				<div class="card-body p-0">
					<!-- Nested Row within Card Body -->
					<div class="row col-sm-12  login-wrapper">
						<div class="col-lg-12">
							<div class="p-2">
								<div class="text-center mt-2 mb-4">
									<h1 class="h4 text-gray-900 mb-4">Welcome Back!</h1>
								</div>
								<form class="user">
									<div class="form-group">
										<input type="email" class="form-control inputs"
											id="exampleInputEmail" aria-describedby="emailHelp"
											placeholder="Enter Email Address...">
									</div>
									<div class="form-group">
										<input type="password" class="form-control inputs"
											id="exampleInputPassword" placeholder="Password">
									</div>
									<!-- <div class="form-group">
										<div class="custom-control custom-checkbox small">
											<input type="checkbox" class="custom-control-input"
												id="customCheck"> <label
												class="custom-control-label" for="customCheck">Remember
												Me</label>
										</div>
									</div> -->
									<a href="index.html" class="btn btn-primary btn-user rounded-border btn-block">
										Login </a>
								</form>
								<hr>
								<div class="text-center">
									<a class="small" href="forgot-password.html">Forgot
										Password?</a>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</div>

	</div>

</div>