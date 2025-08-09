<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../../includes/import-tags.jsp"%>

<div class="container">

	<!-- Outer Row -->
	<div class="row justify-content-center">

		<div class="mt-5">

			<div class="card o-hidden border-0 shadow-lg my-5">
				<div class="card-body p-0  login-wrapper">
					<!-- Nested Row within Card Body -->
					<div class="row col-sm-12 mx-auto">
						<div class="col-lg-12">
							<div class="p-2">
								<div class="text-center mt-2 mb-4">
									<h1 class="h4 text-gray-900 mb-4">Truck Scale Management System</h1>
								</div>
								<form:form method="POST" class="user" id="common-form" action="login.fxt"
									modelAttribute="loginDTO">
									<div class="form-group">
										<form:input path="loginName" type="text"
											class="form-control inputs required" id="exampleInputEmail"
											placeholder="Login name" />
										<span class="text-danger input-error-msg"></span>
									</div>
									<div class="form-group">
										<form:input path="password" type="password"
											class="form-control inputs required"
											id="exampleInputPassword" placeholder="Password" />
										<span class="text-danger input-error-msg"></span>
									</div>

									<button type="submit"
										class="submit btn btn-primary btn-user rounded-border btn-block">
										Login</button>
								</form:form>
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