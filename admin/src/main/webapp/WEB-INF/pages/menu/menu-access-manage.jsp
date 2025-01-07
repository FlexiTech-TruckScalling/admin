<%@ include file="../../includes/import-tags.jsp"%>
<link
	href="<%=request.getContextPath()%>/resources/plugins/gijgo/gijgo.min.css"
	rel="stylesheet" type="text/css">

<div class="card">
	<div class="card-header py-3">
		<span class="m-0 font-weight-bold text-secondary">Menu Access</span>
	</div>
	<div class="card-body">
		<form:form action="menu-access-manage.fxt" method="POST"
			modelAttribute="accessTreeDtoList">
			<form:hidden path="accessTreeList" />
			<div class="row fieldset-row">
				<div class="col-md-6">
					<div class="form-group row">
						<div class="col-sm-9">
							<form:select path="roleId" class="form-control select-2"
								id="roleId">
								<form:options items="${roleList}" itemValue="id"
									itemLabel="name" />
							</form:select>
						</div>
					</div>
				</div>
			</div>
			<div class="row fieldset-row px-2">
				<fieldset class="col-md-12 rounded">
					<legend>Menu</legend>
					<div class="row">
						<div class="col-md-12">
							<div class="form-group row">
								<div class="col-sm-12" id="menu-panel">
									<div id="tree"></div>
								</div>
							</div>
						</div>
					</div>
				</fieldset>
			</div>
			<div class="row fieldset-row mt-2">
				<div class="col-md-12" style="text-align: left;">
					<a href="menu-access-manage.fxt" class="btn btn-secondary"
						type="reset" style="margin-right: 15px;">Clear</a>
					<button class="btn btn-primary" id="btnSubmit" type="submit">Save</button>
				</div>
			</div>
		</form:form>
	</div>
</div>

<script>var contextPath = "<%=request.getContextPath()%>"
</script>
<script src="<c:url value='/resources/plugins/gijgo/gijgo.min.js' />"
	type="text/javascript" defer></script>
<script src="<%=request.getContextPath()%>/resources/js/menu-access.js"
	defer></script>
