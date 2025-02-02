<%@ include file="../../includes/import-tags.jsp"%>
<style>
table td {
	padding: 5px 3px !important;
}

.small-width {
	width: 60px !important;
}
</style>
<link
	href="<%=request.getContextPath()%>/resources/plugins/gijgo/gijgo.min.css"
	rel="stylesheet" type="text/css">

<div class="card">
	<div class="card-header py-3">
		<span class="m-0 font-weight-bold text-secondary">System
			Settings</span>
	</div>
	<div class="card-body">
		<form:form action="system-settings.fxt" method="POST" id="common-form"
			modelAttribute="systemSettingListDTO">
			<div class="overflow-auto">
				<table class="table table-striped border">
					<thead class="bg-gradient-primary text-white">
						<tr>
							<th class="py-1 text-center" scope="col" style="width: 50px;">#</th>
							<th class="py-1" scope="col">Code</th>
							<th class="py-1" scope="col">Description</th>
							<th class="py-1" scope="col">Value</th>
							<th class="py-1" scope="col">Sequence</th>
							<th class="py-1" scope="col">Status</th>
							<th class="py-1" scope="col">Updated Time</th>
						</tr>
					</thead>
					<tbody>
						<c:if test="${empty systemSettingListDTO.settings }">
							<tr>
								<td colspan="6" class="text-center">No setting.</td>
							</tr>
						</c:if>
						<c:if test="${not empty systemSettingListDTO.settings }">
							<c:forEach items="${systemSettingListDTO.settings }"
								var="counter" varStatus="loop">
								<form:hidden path="settings[${loop.index }].id" />
								<form:hidden path="settings[${loop.index }].code"/>
								<tr>
									<td class="align-middle text-center">${loop.index + 1 }</td>
									<td class="align-middle">${counter.code }</td>
									<td class="align-middle">${counter.description }</td>
									<td class="align-middle"><c:choose>
											<c:when test="${counter.inputType == 2 }">
												<form:input type="password"
													class="form-control required py-1"
													path="settings[${loop.index }].value"
													placeholder="${counter.description }" />
											</c:when>
											<c:when test="${counter.inputType == 3 }">
												<form:input type="number" min="0"
													class="form-control required py-1"
													path="settings[${loop.index }].value"
													placeholder="${counter.description }" />
											</c:when>
											<c:otherwise>
												<form:input class="form-control required py-1"
													path="settings[${loop.index }].value"
													placeholder="${counter.description }" />
											</c:otherwise>
										</c:choose></td>
									<td class="align-middle"><form:input type="number"
											class="form-control small-width mx-auto required py-1 text-center"
											min="1" path="settings[${loop.index }].sequence" /></td>
									<td class="align-middle"><form:select
											path="settings[${loop.index }].status"
											class="form-control border bg-white selectpicker">
											<form:options items="${statusList }" itemValue="code"
												itemLabel="desc" />
										</form:select></td>
									<td class="align-middle">${counter.updatedTimeDesc }</td>
								</tr>
							</c:forEach>
						</c:if>
					</tbody>
				</table>
			</div>
			<div class="row fieldset-row mt-2">
				<div class="col-md-12" style="text-align: left;">
					<a href="system-settings.fxt" class="btn btn-secondary"
						type="reset" style="margin-right: 10px;">Clear</a>
					<button class="btn btn-primary" id="btnSubmit" type="submit">Update</button>
				</div>
			</div>
		</form:form>
	</div>
</div>
